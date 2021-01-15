package com.example.taskmanager.presentation.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.taskmanager.R
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.presentation.BaseFragment
import com.example.taskmanager.presentation.CustomSnackbar
import com.example.taskmanager.presentation.authentication.AuthenticationViewModel
import com.example.taskmanager.presentation.authentication.MainActivity
import com.example.taskmanager.presentation.authentication.login.LoginFragment
import com.example.taskmanager.utils.Status
import com.example.taskmanager.utils.ValidationUtils
import com.example.taskmanager.utils.ValidationUtils.areInputsFilled
import com.example.taskmanager.utils.ValidationUtils.isValidEmail
import com.example.taskmanager.utils.ValidationUtils.isValidPassword
import com.example.taskmanager.utils.launchActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.splash_screeen.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment<AuthenticationViewModel>() {

    private val authenticationViewModel: AuthenticationViewModel by sharedViewModel()

    override fun getViewModel(): AuthenticationViewModel = authenticationViewModel

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttachView(view: View) {

        lifecycleScope.launchWhenStarted {
            getViewModel().registerFlow.collect {
                when(it.state){
                    Status.SUCCESSFUL -> {
                        hideLoading()
                        it.data?.let {  data->
                            authenticationViewModel.saveUserInfo(data.data!!)
                            NavHostFragment.findNavController(this@RegisterFragment).navigate(R.id.action_registerFragment_to_mainActivity)
                            activity?.finish()
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

        view.btn_register.setOnClickListener {
            if (areInputsFilled(et_email_register, et_name_register, et_password_register) && isValidPassword(et_password_register) &&
                isValidEmail(et_email_register)){
                    val userModel = UserModel(et_name_register.text.toString(), et_password_register.text.toString(),
                    et_email_register.text.toString(),"","")
                authenticationViewModel.registerUser(userModel)
            }
        }

        view.btn_link_to_login.setOnClickListener {
            NavHostFragment.findNavController(this@RegisterFragment).navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

}