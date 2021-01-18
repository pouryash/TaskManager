package com.example.taskmanager.presentation.dashboard.home


import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.presentation.BaseFragment
import com.example.taskmanager.utils.StateRendererRefactor
import com.example.taskmanager.utils.Status
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_back_drop.*
import kotlinx.android.synthetic.main.fragment_back_drop.view.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class BackDropFragment : BaseFragment<DashboardViewModel>() {

    private val dashboardViewModel: DashboardViewModel by sharedViewModel()

    private var adapter: TaskAdapter = TaskAdapter(mutableListOf())

    private val stateRenderer by lazy {
        StateRendererRefactor.DefaultStateRenderer<BaseModel<ArrayList<TaskModel>>>(requireView(),
            frl_content, lifecycle, dashboardViewModel::getUserTasks) {
            it.data?.let { data ->
                adapter.mRecordsItemList = data
                adapter.notifyDataSetChanged()
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
                if (it.data?.data.isNullOrEmpty())
                    it.data = null
                stateRenderer.render(it)
            }
        }

    }

}