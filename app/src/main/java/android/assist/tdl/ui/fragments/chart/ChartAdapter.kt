package android.assist.tdl.ui.fragments.chart

import android.assist.tdl.classes.model.Task

class ChartAdapter {

    private var tasksList = mutableListOf<Task>()

    private var td : Int = 0
    private var ip : Int = 0
    private var d : Int = 0

    fun setData(tasks: List<Task>) {
        this.tasksList.clear()
        this.tasksList.addAll(tasks)
    }

    fun getToDoCount(tasks: List<Task>) : Int {
        for ( task in tasks ) {
            if ( task.status == "To Do") {
                td ++
            }
        }
        return td
    }

    fun getInProgressCount(tasks: List<Task>) : Int {
        for ( task in tasks ) {
            if ( task.status == "In progress") {
                ip ++
            }
        }
        return ip
    }

    fun getDoneCount(tasks: List<Task>) : Int {
        for ( task in tasks ) {
            if ( task.status == "Done") {
                d ++
            }
        }
        return d
    }
}