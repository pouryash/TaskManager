package com.example.taskmanager.presentation.dashboard.addTask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import coil.load
import com.example.taskmanager.R
import com.example.taskmanager.data.models.UserModel
import kotlinx.android.synthetic.main.fragment_task_detail.view.*
import kotlinx.android.synthetic.main.spinner_priority.view.*

class CustomeSpinnerAdapterUsers(var context: Context, var users: ArrayList<UserModel>) : BaseAdapter() {

    val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
       return users.size
    }

    override fun getItem(i: Int): Any {
        return users[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {
       val view = layoutInflater.inflate(R.layout.spinner_priority, null)

        view.tv_label_custom_spinner.text = users[i].name

        return view
    }
}