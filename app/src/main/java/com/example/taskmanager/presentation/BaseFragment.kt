package com.example.taskmanager.presentation

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.taskmanager.utils.CommonUtils
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment<V : ViewModel> : Fragment(){

    var mProgressDialog: ProgressDialog? = null
    lateinit var mViewModel: V

    abstract fun getViewModel(): V
    abstract fun getLayoutId(): Int
    abstract fun onAttachView(view: View): Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = ProgressDialog(context)
        mViewModel = getViewModel()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(getLayoutId(), container, false)
        onAttachView(view)
        return view
    }


    fun showSnackBar(message: String, action: () -> Unit, duration: Int = Snackbar.LENGTH_LONG) {
        CustomSnackbar.make(requireParentFragment().requireView().rootView, message) {
            action()
        }.setDuration(duration).show()
    }

    fun hideKeyboard() {
        activity?.let {
            val view = it.currentFocus
            if (view != null) {
                val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        return CommonUtils.isNetworkConnected(requireContext())
    }

    fun showLoading() {
        mProgressDialog?.let {
            if (!mProgressDialog?.isShowing!!) {
                mProgressDialog = CommonUtils.showLoadingDialog(requireContext())
            }
        }
    }

    fun hideLoading() {
        mProgressDialog?.let {
            if (mProgressDialog?.isShowing!!) {
                mProgressDialog?.cancel()
            }
        }
    }
}