package com.example.taskmanager.presentation.dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.databinding.ItemTaskBinding
import com.example.taskmanager.utils.BaseViewHolder

class TaskAdapter(var mRecordsItemList: MutableList<TaskModel>) :
    RecyclerView.Adapter<BaseViewHolder>() {

    override fun getItemCount(): Int {
        return mRecordsItemList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val mMajameViewBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MajameViewHolder(mMajameViewBinding)
    }


    inner class MajameViewHolder(val mBinding: ItemTaskBinding) :
        BaseViewHolder(mBinding.root) {
        private lateinit var taskItemViewModel: TaskItemViewModel

        override fun onBind(position: Int) {
            val recordsItem = mRecordsItemList[position]
            taskItemViewModel = TaskItemViewModel(recordsItem)
            mBinding.viewModel = taskItemViewModel
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings()
        }
    }

}