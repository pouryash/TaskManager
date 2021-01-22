package com.example.taskmanager.presentation.dashboard.addTask

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.R
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.presentation.BaseActivity
import com.example.taskmanager.presentation.CustomSnackbar
import com.example.taskmanager.utils.Status
import com.example.taskmanager.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_add_task.*
import kotlinx.android.synthetic.main.fragment_add_task.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTaskActivity : BaseActivity<AddTaskViewModel>(), AdapterView.OnItemSelectedListener {

    private val addTaskViewModel: AddTaskViewModel by viewModel()

    override fun getViewModel(): AddTaskViewModel = addTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_task)

        setup()

    }

    private fun setup() {

        lifecycleScope.launch {
            addTaskViewModel.taskFlow.collect {
                when (it.state) {
                    Status.SUCCESSFUL -> {
                        hideLoading()
                        it.data?.let { data ->
                            finish()
                            return@collect
                        }
                        CustomSnackbar.make(add_task_root, R.string.unknown_error.toString()).show()
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        CustomSnackbar.make(add_task_root, it.error.toString()).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            addTaskViewModel.usersFlow.collect {
                when (it.state) {
                    Status.SUCCESSFUL -> {
                        hideLoading()
                        it.data?.let { data ->
                            spinner_assign_to_add_task.apply {
                                adapter = CustomeSpinnerAdapterUsers(
                                    this@AddTaskActivity,
                                    it.data?.data!!
                                )
                                onItemSelectedListener = this@AddTaskActivity
                            }
                        }
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        CustomSnackbar.make(add_task_root, it.error.toString()).show()
                    }
                }
            }
        }

        fab_add_task.setOnClickListener {
            if (ValidationUtils.areInputsFilled(et_task_name_add_task) && spinner_assign_to_add_task.selectedItem != null
                && spinner_priority_add_task.selectedItem != null
            ) {
                addTaskViewModel.getUserInfo()

                lifecycleScope.launch {
                    val taskModel = TaskModel(
                        taskName = et_task_name_add_task.text.toString(),
                        description = et_description_add_task.text.toString(),
                        reporter = addTaskViewModel.userInfoFlow.first().name,
                        status = "",
                        priority = spinner_priority_add_task.selectedItem.toString(),
                        loggedTime = "",
                        createDate = "",
                        endDate = "",
                        userId = (spinner_assign_to_add_task.adapter.getItem(spinner_assign_to_add_task.selectedItemId.toInt()) as UserModel).userId
                    )
                    addTaskViewModel.createTask(taskModel)
                }
            }
        }

        spinner_assign_to_add_task.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                lifecycleScope.launch {
                    if (addTaskViewModel.usersFlow.first().state == Status.EMPTY || addTaskViewModel.usersFlow.first().state == Status.ERROR)
                        addTaskViewModel.getUser()
                }
                return false
            }

        })

        val customSpinnerAdapterPriority = CustomeSpinnerAdapterPriority(
            this,
            resources.getStringArray(R.array.priority_array)
        )
        spinner_priority_add_task.apply {
            adapter = customSpinnerAdapterPriority
            onItemSelectedListener = this@AddTaskActivity
        }


        iv_cancel_add_task.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}