package android.assist.tdl.classes.viewmodel

import android.app.Application
import android.assist.tdl.classes.model.Task
import android.assist.tdl.classes.data.TaskDatabase
import android.assist.tdl.classes.repository.TaskRepository
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData : LiveData<List<Task>>
    private val repository : TaskRepository

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        readAllData = repository.readAllData
    }

    fun addTask(task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun searchTask(searchDeadline : String) : LiveData<List<Task>> {
        return repository.searchTask(searchDeadline).asLiveData()
    }
}