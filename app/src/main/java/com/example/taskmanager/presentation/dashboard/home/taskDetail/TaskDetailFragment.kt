package com.example.taskmanager.presentation.dashboard.home.taskDetail

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.TooltipCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.taskmanager.R
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.presentation.BaseFragment
import com.example.taskmanager.presentation.CustomSnackbar
import com.example.taskmanager.presentation.dashboard.home.DashboardViewModel
import com.example.taskmanager.utils.RIM
import com.example.taskmanager.utils.Status
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.android.synthetic.main.fragment_task_detail.view.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val TASK_PARAM = "task_param"


class TaskDetailFragment : BaseFragment<DashboardViewModel>(), AdapterView.OnItemSelectedListener {

    private var task: TaskModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            task = it.getParcelable(TASK_PARAM)!!
        }
        requireActivity().fab_add_task_dashboard.visibility = View.GONE
    }

    private val dashboardViewModel: DashboardViewModel by sharedViewModel()

    override fun getViewModel(): DashboardViewModel = dashboardViewModel

    override fun getLayoutId(): Int = R.layout.fragment_task_detail

    override fun onAttachView(view: View) {

        setup(view)
        setupData(view)
        observe()
    }

    override fun onDestroyView() {
        getViewModel().updatedTasksFlow.value = RIM(state = Status.EMPTY)
        super.onDestroyView()
        val result = task
        setFragmentResult("TaskDetailRequestKey", bundleOf("taskDetail" to result))
        requireActivity().fab_add_task_dashboard.visibility = View.VISIBLE

    }

    private fun observe(){
        lifecycleScope.launchWhenStarted {
            getViewModel().updatedTasksFlow.collect {
                when(it.state){
                    Status.SUCCESSFUL -> {
                        task = it.data?.data
                        setupData(requireView())
                        hideLoading()
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        CustomSnackbar.make(task_detail_root, it.error.toString()).show()
                    }
                }
            }
        }
    }

    private fun setupData(view: View){

        task?.let {
            TooltipCompat.setTooltipText(view.iv_task_detail_priority, it.priority+" priority")
            view.tv_task_detail_name.text = if (it.taskName.isNotEmpty()) it.taskName else "-"
            view.tv_task_detail_description.text = if (it.description.isNotEmpty()) it.description else "-"
            view.tv_task_detail_reporter.text = if (it.reporter.isNotEmpty()) it.reporter else "-"
            view.tv_task_detail_created_date.text = if (it.createDate.isNotEmpty()) it.createDate.substring(0, 10) else "-"
            view.tv_task_detail_end_date.text = if (it.endDate.isNotEmpty()) it.endDate.substring(0, 10) else "-"
            view.tv_task_detail_logged_value.text = if (it.loggedTime.isNotEmpty()) it.loggedTime else "-"
            when(it.status.toLowerCase()){
                "todo" -> {
                    view.spinner_status.setSelection(0)
                }
                "inprogress" -> {
                    view.spinner_status.setSelection(1)
                }
                "test" -> {
                    view.spinner_status.setSelection(2)
                }
                "done" -> {
                    view.spinner_status.setSelection(3)
                }

            }
            when(it.priority.toLowerCase()){
                "low" -> {
                    view.iv_task_detail_priority.load(R.drawable.ic_low) {
                        placeholder(R.drawable.ic_low)
                    }
                }
                "medium" -> {
                    view.iv_task_detail_priority.load(R.drawable.ic_medium) {
                        placeholder(R.drawable.ic_medium)
                    }
                }
                "high" -> {
                    view.iv_task_detail_priority.load(R.drawable.ic_high) {
                        placeholder(R.drawable.ic_high)
                    }
                }
                else -> {}
            }
        }

        view.btn_save_task_detail.setOnClickListener {
            task?.let {
                it.loggedTime = view.et_log_task_detail.text.toString()
                it.status = view.spinner_status.selectedItem.toString().replace(" ", "")
                dashboardViewModel.updateTaskTask(it)
            }
        }

    }

    private fun setup(view: View) {

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.status_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            view.spinner_status.adapter = adapter
        }

        view.iv_task_detail_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


    companion object {

        @JvmStatic
        fun newInstance(task: TaskModel) =
            TaskDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TASK_PARAM, task)
                }
            }
    }

}