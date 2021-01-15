package com.example.taskmanager.presentation

import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar

const val RETRY = "تلاش مجدد"
object CustomSnackbar {
    /**
     * Creates Definite Snackbar
     * */
    fun make(view: View, message: String, length: Int = Snackbar.LENGTH_SHORT): Snackbar {
        val t = Snackbar.make(view, message, length)
        t.view.layoutDirection = View.LAYOUT_DIRECTION_RTL
        return t
    }

    /**
     * Creates INDEFINITE Snackbar with action
     * */
    fun make(view: View, message: String, actionText: String = RETRY, listener: (View) -> Unit): Snackbar {
        val t = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        t.view.layoutDirection =  View.LAYOUT_DIRECTION_RTL
        t.setAction(actionText, listener)
        return t
    }
}

