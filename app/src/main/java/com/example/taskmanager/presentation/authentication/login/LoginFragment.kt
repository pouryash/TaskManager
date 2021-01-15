package com.example.taskmanager.presentation.authentication.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.taskmanager.R
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.presentation.BaseFragment
import com.example.taskmanager.presentation.CustomSnackbar
import com.example.taskmanager.presentation.authentication.AuthenticationViewModel
import com.example.taskmanager.presentation.authentication.MainActivity
import com.example.taskmanager.utils.Status
import com.example.taskmanager.utils.ValidationUtils
import com.example.taskmanager.utils.launchActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class LoginFragment : BaseFragment<AuthenticationViewModel>() {

    private val authenticationViewModel: AuthenticationViewModel by sharedViewModel()

    override fun getViewModel(): AuthenticationViewModel = authenticationViewModel

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttachView(view: View) {

        lifecycleScope.launchWhenStarted {
            getViewModel().registerFlow.collect {
                when(it.state){
                    Status.SUCCESSFUL -> {
                        hideLoading()
                        it.data?.let {  data->
                            authenticationViewModel.saveUserInfo(data.data!!)
                            NavHostFragment.findNavController(this@LoginFragment).navigate(R.id.action_loginFragment_to_mainActivity)
                            activity?.finish()

                        }
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        CustomSnackbar.make(login_root, it.error.toString()).show()
                    }
                }
            }
        }

        view.btn_login.setOnClickListener {
            if (ValidationUtils.areInputsFilled(
                    et_email_login,
                    et_password_login
                ) && ValidationUtils.isValidPassword(et_password_login) &&
                ValidationUtils.isValidEmail(et_email_login)
            ){
                val userModel = UserModel("", et_password_login.text.toString(),
                    et_email_login.text.toString(),"","")
                authenticationViewModel.loginUser(userModel)
            }
        }

        view.btn_link_to_register.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }
}