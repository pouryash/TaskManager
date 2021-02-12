package com.example.taskmanager.utils

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.example.taskmanager.R
import com.example.taskmanager.presentation.CustomSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_state_handler.view.*

private const val UNKnOWN_ERROR = "Unknown Error"

/** Class that renders different state in a uniform way across the application.
 * @param rootView rootView that is used for showing snakcbars.
 * @param contentView a framelayout that hosts the main content of page and can be filled with progress view and no content view.
 * @param retryBlock a block that to be executed when user clicks retry button on snackbar
 * @param renderSuccess a block with contentView as reciever and success state data as argument, that renders the success state.
 */
sealed class StateRendererRefactor<T>(
    private val successRenderer: ISuccessRendererRefactor<T>,
    private val errorRenderer: IErrorRendererRefactor<T>,
    private val loadingRenderer: ILoadingRenderRefactor,
    private val noContentRenderer: INoContentRendererRefactor?
) {

    fun render(state: RIM<T>) {
        when (state.state) {
            Status.SUCCESSFUL -> {
                val data = state.data
                if (data == null)
                    noContentRenderer?.show(true)
                else {
                    successRenderer.render(data)
                    noContentRenderer?.hide()
                }
                errorRenderer.hide()
                loadingRenderer.hide()
            }
            Status.ERROR -> {
                errorRenderer.show(state.error)
                loadingRenderer.hide()
                noContentRenderer?.show(false)
            }
            Status.LOADING -> {
                errorRenderer.hide()
                loadingRenderer.show()
            }
        }
    }

    class DefaultStateRenderer<T>(
        rootView: View,
        contentFrame: FrameLayout,
        lifecycle: Lifecycle,
        retryBlock: (() -> Unit),
        successBlock: FrameLayout.(T) -> Unit
    ) : StateRendererRefactor<T>(
        successRenderer = FrameLayoutSuccessRendererRefactor(contentFrame, successBlock),
        loadingRenderer = FrameLayoutProgressRendererRefactor(contentFrame),
        noContentRenderer = FrameLayoutNoContentRendererRefactor(contentFrame, retryBlock),
        errorRenderer = SnackbarErrorRendererRefactor(rootView, retryBlock, lifecycle)
    )
}

interface IErrorRendererRefactor<T> {
    fun show(error: String?)
    fun hide()
}

interface ISuccessRendererRefactor<T> {
    fun render(data: T)
}

interface ILoadingRenderRefactor {
    fun show()
    fun hide()
}

interface INoContentRendererRefactor {
    fun show(hasReTry: Boolean)
    fun hide()
}

class FrameLayoutNoContentRendererRefactor(
    private val contentFrame: FrameLayout,
    private val retry: () -> Unit
) :
    INoContentRendererRefactor {
    private lateinit var noContentView: View

    override fun show(hasReTry: Boolean) {
        renderNoContent(true)
    }

    override fun hide() {
        renderNoContent(false)
    }

    private fun renderNoContent(showNoContent: Boolean) {
        noContentView = LayoutInflater.from(contentFrame.context)
                .inflate(R.layout.layout_state_handler, contentFrame, false).also {
                    if (contentFrame.state_progress == null)
                        contentFrame.addView(it)
                }
        contentFrame.iv_state_empty.visibility = if (showNoContent) View.VISIBLE else View.GONE
        contentFrame.tv_state_empty.visibility = if (showNoContent) View.VISIBLE else View.GONE
    }
}

class FrameLayoutProgressRendererRefactor(private val contentFrame: FrameLayout) :
    ILoadingRenderRefactor {
    private val progressView: View by lazy {
        LayoutInflater
            .from(contentFrame.context)
            .inflate(R.layout.layout_state_handler, contentFrame, false).also {
                if (contentFrame.state_progress == null)
                    contentFrame.addView(it)
            }
    }

    override fun show() {
        renderLoading(true)
    }

    override fun hide() {
        renderLoading(false)
    }

    private fun renderLoading(showProgressbar: Boolean) {
        progressView.state_progress.visibility = if (showProgressbar) View.VISIBLE else View.GONE
        if (showProgressbar){
            progressView.iv_state_empty.visibility = View.GONE
            progressView.tv_state_empty.visibility = View.GONE
        }
    }
}

class FrameLayoutSuccessRendererRefactor<T>(
    private val contentFrame: FrameLayout,
    private val renderSuccess: FrameLayout.(T) -> Unit
) : ISuccessRendererRefactor<T> {
    override fun render(data: T) {
        contentFrame.renderSuccess(data)
    }
}

class SnackbarErrorRendererRefactor<T>(
    private val rootView: View,
    private val actionBlock: (() -> Unit),
    lifecycle: Lifecycle
) : IErrorRendererRefactor<T>, LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    private var snackbar: Snackbar? = null

    override fun show(error: String?) {
        error?.let {
            showRetryErrorMsg(error, actionBlock)
        }
    }

    override fun hide() {
        snackbar?.dismiss()
    }

    private fun showRetryErrorMsg(msg: String? = UNKnOWN_ERROR, retryBlock: (() -> Unit)) {
        snackbar = CustomSnackbar.make(rootView, msg ?: UNKnOWN_ERROR) {
            retryBlock.invoke()
        }
        snackbar?.show()
    }
}