package android.assist.tdl.ui.fragments.work

import android.assist.tdl.R
import android.assist.tdl.classes.model.Task
import android.assist.tdl.ui.fragments.personal.PersonalFragmentDirections
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class WorkAdapter: RecyclerView.Adapter<WorkAdapter.WorkViewHolder>() {

    private var tasksList = emptyList<Task>()
    private var workTasks = arrayListOf<Task>()

    private var id = 1

    class WorkViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        val idTextView : TextView = itemView.findViewById(R.id.taskIdTextView)
        val nameTextView : TextView = itemView.findViewById(R.id.taskNameTextView)
        val deadlineTextView : TextView = itemView.findViewById(R.id.taskDeadlineTextView)
        val statusTextView : TextView = itemView.findViewById(R.id.taskStatusTextView)

        val rowLayout : ConstraintLayout = itemView.findViewById(R.id.rowLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        return WorkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row , parent , false ))
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {

        val currentItem = tasksList[position]
        holder.idTextView.text = currentItem.id.toString()
        holder.nameTextView.text = currentItem.name
        holder.deadlineTextView.text = currentItem.deadLine
        holder.statusTextView.text = currentItem.status

        holder.rowLayout.setOnClickListener {
            val action = WorkFragmentDirections.actionNavWorkToUpdateTaskFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun setData(tasks: List<Task>) {
        for (task in tasks) {
            if(task.type == "Work" && !workTasks.contains(task) ) {
                task.id = id++
                workTasks.add(task)
            }
        }
        this.tasksList = workTasks
        notifyDataSetChanged()
    }
}