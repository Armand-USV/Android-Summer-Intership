package android.assist.tdl.ui.fragments.calendar

import android.assist.tdl.R
import android.assist.tdl.classes.model.Task
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter: RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var tasksList = mutableListOf<Task>()

    class CalendarViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        val idTextView : TextView = itemView.findViewById(R.id.taskIdTextView)
        val nameTextView : TextView = itemView.findViewById(R.id.taskNameTextView)
        val deadlineTextView : TextView = itemView.findViewById(R.id.taskDeadlineTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.calendar_custom_row , parent , false ))
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

        val currentItem = tasksList[position]
        holder.idTextView.text = currentItem.id.toString()
        holder.nameTextView.text = currentItem.name
        holder.deadlineTextView.text = currentItem.deadLine
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun setData(tasks: List<Task>) {
        this.tasksList.clear()
        this.tasksList.addAll(tasks)
        notifyDataSetChanged()
    }
}