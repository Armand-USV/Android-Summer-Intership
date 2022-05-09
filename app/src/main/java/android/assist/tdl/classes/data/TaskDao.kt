package android.assist.tdl.classes.data

import android.assist.tdl.classes.model.Task
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Contain the methods used for accessing the database

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask( task : Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun readAllData() : LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE deadLine LIKE :searchDeadline")
    fun searchTask(searchDeadline: String) : Flow<List<Task>>
}