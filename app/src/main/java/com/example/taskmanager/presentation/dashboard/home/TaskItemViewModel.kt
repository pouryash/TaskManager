package com.example.taskmanager.presentation.dashboard.home

import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import coil.load
import com.example.taskmanager.R
import com.example.taskmanager.data.models.TaskModel
import java.text.SimpleDateFormat

class TaskItemViewModel(var recordsItem: TaskModel) {
    val taskName: ObservableField<String>
    get() {
      return  if(recordsItem.taskName.isBlank()){
          ObservableField("-")
      }else{
          ObservableField(recordsItem.taskName)
      }
    }
    val description: ObservableField<String>
        get() {
            return  if(recordsItem.description.isBlank()){
                ObservableField("-")
            }else{
                ObservableField(recordsItem.description)
            }
        }
    val reporter: ObservableField<String>
        get() {
            return  if(recordsItem.reporter.isBlank()){
                ObservableField("-")
            }else{
                ObservableField(recordsItem.reporter)
            }
        }
    val status: ObservableField<String>
        get() {
            return  if(recordsItem.status.isBlank()){
                ObservableField("-")
            }else{
                ObservableField(recordsItem.status)
            }
        }
    val priority: ObservableField<String>
        get() {
            return  if(recordsItem.priority.isBlank()){
                ObservableField("-")
            }else{
                ObservableField(recordsItem.priority)
            }
        }
    val createDate: ObservableField<String>
        get() {
            return  if(recordsItem.createDate.isBlank()){
                ObservableField("-")
            }else{
                return ObservableField(recordsItem.createDate.substring(0, 10))
            }
        }

}

@BindingAdapter("bind:status")
fun setStatus(view: LinearLayout, status: String) {

    when(status.toLowerCase()){
        "todo" -> {
            view.setBackgroundResource(R.color.gray600)
        }
        "inprogress" -> {
            view.setBackgroundResource(R.color.colorLightBlue)

        }
        "test" -> {
            view.setBackgroundResource(R.color.colorOrange)

        }
        "done" -> {
            view.setBackgroundResource(R.color.colorGreen)

        }
    }
}

@BindingAdapter("bind:priority")
fun setImage(imageView: ImageView, priority: String) {

    when(priority.toLowerCase()){
        "low" -> {
            imageView.load(R.drawable.ic_low) {
                placeholder(R.drawable.ic_low)
            }
        }
        "medium" -> {
            imageView.load(R.drawable.ic_medium) {
                placeholder(R.drawable.ic_medium)
            }
        }
        "high" -> {
            imageView.load(R.drawable.ic_high) {
                placeholder(R.drawable.ic_high)
            }
        }
    }
}