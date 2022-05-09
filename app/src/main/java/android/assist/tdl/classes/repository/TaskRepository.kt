package android.assist.tdl.classes.repository

import android.assist.tdl.classes.model.Task
import android.assist.tdl.classes.data.TaskDao
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val readAllData: LiveData<List<Task>> = taskDao.readAllData()

    suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    fun searchTask(searchDeadline : String) : Flow<List<Task>> {
        return taskDao.searchTask(searchDeadline)
    }
}