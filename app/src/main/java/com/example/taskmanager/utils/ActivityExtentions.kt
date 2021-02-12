package com.example.taskmanager.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Extensions for simpler launching of Activities
 * ref:https://gist.github.com/wajahatkarim3/ebe01a5fe57c15b83c772a65da301bff
 * usage of these method exist in reference
 *
 */

inline fun <reified T : Activity> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Activity> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


/**
 * application font based on sp
 * by change font size of device to huge, ui seem inappropriate
 * with this function application font won't bigger than specific size
 */
fun AppCompatActivity.AdjustFontScale() {
    if (resources.configuration.fontScale > 1.2) {
        resources.configuration.fontScale = 1.2f
        val metrics = resources.displayMetrics
        val wm = getSystemService(AppCompatActivity.WINDOW_SERVICE) as (WindowManager)
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = resources.configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(resources.configuration, metrics)
    }
}