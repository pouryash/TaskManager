package com.example.taskmanager.presentation.dashboard.addTask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import coil.load
import com.example.taskmanager.R
import kotlinx.android.synthetic.main.fragment_task_detail.view.*
import kotlinx.android.synthetic.main.spinner_priority.view.*

class CustomeSpinnerAdapterPriority(var context: Context, var labels: Array<String>) : BaseAdapter() {

    val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
       return labels.size
    }

    override fun getItem(i: Int): Any {
        return labels[i]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {
       val view = layoutInflater.inflate(R.layout.spinner_priority, null)

        when(labels[i]){
            "low" -> {
                view.iv_priority_custom_spinner.load(R.drawable.ic_low) {
                    placeholder(R.drawable.ic_low)
                }
                view.tv_label_custom_spinner.text = labels[i]
            }
            "medium" -> {
                view.iv_priority_custom_spinner.load(R.drawable.ic_medium) {
                    placeholder(R.drawable.ic_medium)
                }
                view.tv_label_custom_spinner.text = labels[i]
            }
            "high" -> {
                view.iv_priority_custom_spinner.load(R.drawable.ic_high) {
                    placeholder(R.drawable.ic_high)
                }
                view.tv_label_custom_spinner.text = labels[i]
            }
            else -> {}
        }

        return view
    }
}