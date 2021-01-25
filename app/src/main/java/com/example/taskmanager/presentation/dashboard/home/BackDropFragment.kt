package com.example.taskmanager.presentation.dashboard.home


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.taskmanager.R
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.presentation.BaseFragment
import com.example.taskmanager.presentation.dashboard.taskDetail.TaskDetailFragment
import com.example.taskmanager.utils.StateRendererRefactor
import com.example.taskmanager.utils.Status
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_back_drop.*
import kotlinx.android.synthetic.main.fragment_back_drop.view.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class BackDropFragment : BaseFragment<DashboardViewModel>() {

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val dashboardViewModel: DashboardViewModel by sharedViewModel()

    private var adapter: TaskAdapter = TaskAdapter(mutableListOf()) {

        val taskDetailFragment = TaskDetailFragment.newInstance(it)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frl_dashboard, taskDetailFragment)
            .addToBackStack(null)
            .commit()

    }

    private val stateRenderer by lazy {
        StateRendererRefactor.DefaultStateRenderer<BaseModel<ArrayList<TaskModel>>>(
            requireView(),
            frl_content, lifecycle, dashboardViewModel::getUserTasks
        ) {
            it.data?.let { data ->
                adapter.mRecordsItemList = data
                adapter.notifyDataSetChanged()
                return@DefaultStateRenderer
            }
        }
    }

    private fun configRV(view: View) {
        view.rv_dashboard_tesks.setHasFixedSize(true)
        view.rv_dashboard_tesks.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_dashboard_tesks.adapter = adapter
        view.rv_dashboard_tesks.visibility = View.VISIBLE
    }

    override fun getViewModel(): DashboardViewModel = dashboardViewModel

    override fun getLayoutId(): Int = R.layout.fragment_back_drop

    override fun onAttachView(view: View) {
        configRV(view)

        dashboardViewModel.getUserTasks()

        lifecycleScope.launchWhenStarted {
            dashboardViewModel.userTasksFlow.collect {
                when (it.state) {
                    Status.SUCCESSFUL -> {
                        it.data?.data?.let {
                            if (it.isNotEmpty())
                            rv_dashboard_tesks.visibility = View.VISIBLE
                        }
                    }
                    Status.LOADING -> {
                        rv_dashboard_tesks.visibility = View.INVISIBLE
                    }
                    Status.ERROR -> {
                        rv_dashboard_tesks.visibility = View.INVISIBLE
                    }
                }
                if (it.data?.data.isNullOrEmpty())
                    it.data = null
                stateRenderer.render(it)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRefreshListener(view)
    }

    private fun initializeRefreshListener(view: View) {
        swipeRefreshLayout = view.swipe_layout
        swipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                dashboardViewModel.getUserTasks()
                swipeRefreshLayout.isRefreshing = false
            }

        })
    }

}