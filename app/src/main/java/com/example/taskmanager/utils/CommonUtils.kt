package com.example.taskmanager.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import com.example.taskmanager.R
import com.google.gson.Gson


object CommonUtils {

    fun ConverDateToString(year: Int, month: Int, day: Int): String{
      return "${if (day<10) String.format("%02d", day) else day} / ${if (month<10) String.format("%02d", month) else month} / $year"
    }

    fun getScreenHeight(activity: Activity): Int{
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities: NetworkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }

        } else {
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }
        return false
    }

    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    fun <T> convertModelToJsonString(data: T): String{
        val gson = Gson()
        return gson.toJson(data)
    }

   inline fun <reified T> convertJsonStringToModel(data: String): T{
        val gson = Gson()
       Log.d("DDDDDDDDDDDDDD", T::class.java.toString())
        return gson.fromJson(data, T::class.java)
    }

}