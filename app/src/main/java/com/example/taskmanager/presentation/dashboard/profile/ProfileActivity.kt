package com.example.taskmanager.presentation.dashboard.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.taskmanager.R
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.presentation.BaseActivity
import com.example.taskmanager.presentation.CustomSnackbar
import com.example.taskmanager.presentation.authentication.AuthenticationActivity
import com.example.taskmanager.utils.Status
import com.example.taskmanager.utils.ValidationUtils
import com.example.taskmanager.utils.launchActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.flow.collect
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

        profileViewModel.getUserInfo()

        lifecycleScope.launchWhenStarted {
            profileViewModel.userInfoFlow.collect {
                setUpProfile(it)
            }
        }

        btn_logout.setOnClickListener {
            profileViewModel.logout()
            finishAffinity()
            launchActivity<AuthenticationActivity> {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }

        btn_save_profile.setOnClickListener {
            if(ValidationUtils.areInputsFilled(
                    et_editemail_profile,
                    et_editPassword_profile,
                    et_editName_profile,
                ) && ValidationUtils.isValidPassword(et_editPassword_profile) &&
                ValidationUtils.isValidEmail(et_editemail_profile)
            ){
                val userModel = UserModel(et_editName_profile.text.toString(), et_editPassword_profile.text.toString(),
                    et_editemail_profile.text.toString(),"","")
                profileViewModel.updateUser(userModel)
            }
        }

        lifecycleScope.launchWhenStarted {
            getViewModel().userFlow.collect {
                when(it.state){
                    Status.SUCCESSFUL -> {
                        hideLoading()
                        it.data?.let {data->
                            setUpProfile(data.data!!)
                            profileViewModel.saveUSerINfo(data.data)
                        }
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        CustomSnackbar.make(register_root, it.error.toString()).show()
                    }
                }
            }
        }

    }

    private fun setUpProfile(userModel: UserModel) {
        tv_profile_email.text = userModel.email
        tv_profile_name.text = userModel.name
        et_editName_profile.setText(userModel.name)
        et_editemail_profile.setText(userModel.email)
    }

}