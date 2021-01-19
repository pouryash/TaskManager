package com.example.taskmanager.presentation

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.taskmanager.utils.AdjustFontScale
import com.example.taskmanager.utils.CommonUtils
import com.example.taskmanager.utils.RuntimePermissionsActivity
import io.reactivex.schedulers.Schedulers


abstract class BaseActivity<V : ViewModel> : RuntimePermissionsActivity() {

    var mProgressDialog: ProgressDialog? = null

    private var mViewModel: V? = null

    abstract fun getViewModel(): V


    @SuppressLint("AutoDispose")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = ProgressDialog(this)
        AdjustFontScale()
    }

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun onPermissionsDeny(requestCode: Int) {
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun isNetworkConnected(): Boolean {
        return CommonUtils.isNetworkConnected(applicationContext)
    }

    fun showLoading() {
        mProgressDialog?.let {
            if (!mProgressDialog?.isShowing!!) {
                mProgressDialog = CommonUtils.showLoadingDialog(this)
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

    override fun onDestroy() {
        super.onDestroy()
    }

}