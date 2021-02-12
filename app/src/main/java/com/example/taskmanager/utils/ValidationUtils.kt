package com.example.taskmanager.utils

import android.util.Patterns
import android.widget.EditText
import com.example.taskmanager.R
import java.util.regex.Matcher
import java.util.regex.Pattern

object ValidationUtils {

    fun isValidEmail(email: EditText): Boolean {
        return if (email.text.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            email.error = email.context.getString(R.string.email_not_valid)
            false
        } else
            return true
    }

    fun isValidPassword(password: EditText): Boolean {
        val pattern: Pattern
        val matcher: Matcher
//        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{8,24}")
        matcher = pattern.matcher(password.text)
        return if (!matcher.matches()) {
            password.error = password.context.getString(R.string.password_not_valid)
            false
        }else
            true
    }

    fun areInputsFilled(vararg inputs: EditText): Boolean {
        var valid = true

        if (inputs.isEmpty()) {
            return valid
        }
        val error = inputs[0].context
            .getString(R.string.this_field_is_required)
        for (input in inputs) {
            if (input.text.toString().trim().isEmpty()) {
                valid = false
                input.error = error
            }
        }
        return valid
    }

}