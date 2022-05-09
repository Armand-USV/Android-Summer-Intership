package android.assist.tdl.classes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val name: String,
    val deadLine: String,
    val description: String,
    val type: String,
    val status: String,
) : Parcelable