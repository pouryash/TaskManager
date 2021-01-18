package com.example.taskmanager.presentation.dashboard.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
import com.example.taskmanager.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity<ProfileViewModel>() {

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun getViewModel(): ProfileViewModel = profileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setup()
    }

    private fun setup() {
        iv_profile_back.setOnClickListener {
            onBackPressed()
        }
    }

}