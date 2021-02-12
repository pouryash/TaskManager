package com.example.taskmanager.presentation.dashboard.home.filter

import android.app.DatePickerDialog
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.R
import com.example.taskmanager.data.models.FilterTaskModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.presentation.BaseFragment
import com.example.taskmanager.presentation.CustomSnackbar
import com.example.taskmanager.presentation.dashboard.addTask.CustomeSpinnerAdapterUsers
import com.example.taskmanager.presentation.dashboard.home.DashboardViewModel
import com.example.taskmanager.utils.CommonUtils
import com.example.taskmanager.utils.Status
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_add_task.*
import kotlinx.android.synthetic.main.fragment_add_task.add_task_root
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class FilterFragment : BaseFragment<DashboardViewModel>(), DatePickerDialog.OnDateSetListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var fromDatePicker: DatePickerDialog
    private lateinit var toDatePicker: DatePickerDialog
    private lateinit var initView: View
    private val dashboardViewModel: DashboardViewModel by sharedViewModel()

    override fun getViewModel(): DashboardViewModel = dashboardViewModel

    override fun getLayoutId(): Int = R.layout.fragment_filter

    override fun onAttachView(view: View) {
        initView = view

        init()
        observe()

        initView.spinner_users_filter_task.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                lifecycleScope.launch {
                    if (getViewModel().usersFlow.first().state == Status.EMPTY || getViewModel().usersFlow.first().state == Status.ERROR)
                        getViewModel().getUser()
                }
                return false
            }

        })

        view.iv_cancel_filter_tasks.setOnClickListener {
            requireActivity().onBackPressed()
        }

        view.et_toDate_filter_task.setOnClickListener {

            toDatePicker.show()
        }

        view.et_fromDate_filter_task.setOnClickListener {
            fromDatePicker.show()
        }

        view.fab_filter_tasks.setOnClickListener {
            val filterTask = FilterTaskModel()
            filterTask.status = initView.chipgroup_status_filter_task?.children
                ?.toList()
                ?.filter { (it as Chip).isChecked }
                ?.joinToString(", ") {
                    (it as Chip).text.toString().replace(" ", "")
                } ?: ""

            filterTask.priority = initView.chipgroup_priority_filter_task?.children
                ?.toList()
                ?.filter { (it as Chip).isChecked }
                ?.joinToString(", ") { (it as Chip).text } ?: ""

            filterTask.user =
                if (initView.spinner_users_filter_task.visibility == View.VISIBLE) (initView.spinner_users_filter_task.selectedItem as UserModel).name
                else getViewModel().userInfoFlow.value.name
            filterTask.fromDate = initView.et_fromDate_filter_task.text.trim().toString()
            filterTask.toDate = initView.et_toDate_filter_task.text.trim().toString()

            if (filterTask.user.isNotEmpty()) {
                getViewModel().filterTasks(filterTask)
                requireActivity().onBackPressed()
            } else
                CustomSnackbar.make(
                    initView.filter_task_root,
                    resources.getString(R.string.unknown_error)
                ).show()
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            getViewModel().usersFlow.collect {
                when (it.state) {
                    Status.SUCCESSFUL -> {
                        hideLoading()
                        it.data?.let { data ->
                            initView.spinner_users_filter_task.apply {
                                adapter = CustomeSpinnerAdapterUsers(
                                    requireContext(),
                                    it.data?.data!!
                                )
                                onItemSelectedListener = this@FilterFragment
                            }
                        }
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        CustomSnackbar.make(initView.filter_task_root, it.error.toString()).show()
                    }
                }
            }
        }
    }

    private fun init() {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        fromDatePicker = DatePickerDialog(requireContext(), this, year, month, day)
        toDatePicker = DatePickerDialog(requireContext(), this, year, month, day)

        if (getViewModel().userInfoFlow.value.role == "admin") {
            initView.tv_user_label_filter_task.visibility = View.VISIBLE
            initView.spinner_users_filter_task.visibility = View.VISIBLE
            getViewModel().getUser()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        when (view) {
            fromDatePicker.datePicker -> {
                initView.et_fromDate_filter_task.text =
                    CommonUtils.ConverDateToString(year, month, day)
            }
            toDatePicker.datePicker -> {
                initView.et_toDate_filter_task.text =
                    CommonUtils.ConverDateToString(year, month, day)
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}