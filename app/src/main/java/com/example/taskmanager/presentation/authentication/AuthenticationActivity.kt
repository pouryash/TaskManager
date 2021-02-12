package com.example.taskmanager.presentation.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
import com.example.taskmanager.presentation.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationActivity : BaseActivity<AuthenticationViewModel>(){

    private val authenticationViewModel: AuthenticationViewModel by viewModel()

    override fun getViewModel(): AuthenticationViewModel = authenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }

}