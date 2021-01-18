package com.example.taskmanager.presentation.dashboard.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskmanager.R
import com.example.taskmanager.presentation.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<ProfileViewModel>() {

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun getViewModel(): ProfileViewModel = profileViewModel

    override fun getLayoutId(): Int = R.layout.profile_fragment

    override fun onAttachView(view: View) {

    }

}