package com.example.taskmanager.presentation.dashboard.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.R
import com.example.taskmanager.presentation.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class DashboardFragment : BaseFragment<DashboardViewModel>() {

    private val dashboardViewModel: DashboardViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getViewModel(): DashboardViewModel = dashboardViewModel

    override fun getLayoutId(): Int = R.layout.fragment_dashboard

    override fun onAttachView(view: View) {

        dashboardViewModel.getUserInfo()

        lifecycleScope.launchWhenStarted {
            dashboardViewModel.userInfoFlow.collect {
                tv_dashboard_name.text = "${resources.getString(R.string.user_welcome)} ${it.name}"
            }
        }


    }
}