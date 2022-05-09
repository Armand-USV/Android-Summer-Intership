package android.assist.tdl.ui.addtask

import android.app.*
import android.assist.tdl.R
import android.assist.tdl.classes.model.*
import android.assist.tdl.classes.model.Notification
import android.assist.tdl.classes.validation.Validation
import android.assist.tdl.classes.viewmodel.TaskViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class AddTask : AppCompatActivity() {

    // Variable declaration

    private lateinit var mTaskViewModel: TaskViewModel

    private lateinit var types: Spinner
    private lateinit var status: Spinner

    private lateinit var typeSelected: String
    private lateinit var statusSelected: String
    private lateinit var taskNameStr: String
    private lateinit var taskDescriptionStr: String

    private lateinit var taskNameTextInputLayout: TextInputLayout
    private lateinit var taskDescriptionTextInputLayout: TextInputLayout
    private lateinit var taskDeadlineEditText: EditText

    private lateinit var taskDeadlineHelperText : TextView

    private lateinit var saveTaskImg: ImageView
    private lateinit var backImg: ImageView

    private val calendar: Calendar = Calendar.getInstance()
    private val calendar2: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        createNotificationChannel()

        // Variable initialization

        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        types = findViewById(R.id.addTaskTypeSpinner)
        status = findViewById(R.id.addTaskStatusSpinner)

        saveTaskImg = findViewById(R.id.add_task_img)
        backImg = findViewById(R.id.add_back_img)

        taskNameTextInputLayout = findViewById(R.id.addTaskNameTextInputLayout)
        taskDescriptionTextInputLayout = findViewById(R.id.addTaskDescriptionTextInputLayout)
        taskDeadlineEditText = findViewById(R.id.addTaskDeadlineEditText)
        taskDeadlineHelperText = findViewById(R.id.addDeadlineHelperText)

        // Variables declaration and initialization for the 'type' and 'status' selection

        val taskTypes = arrayOf("Personal", "Work", "School")
        val taskStatus = arrayOf("To Do", "In progress", "Done")

        types.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskTypes)
        status.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskStatus)

        types.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                typeSelected = taskTypes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                statusSelected = taskStatus[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        // Code for DatePicker when we click on the 'DeadlineEditText'

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setDateInEditTex(calendar)
            }

        taskDeadlineEditText.setOnClickListener {
            DatePickerDialog(
                this@AddTask, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Code for custom app toolbar
        // Contains a textView and 2 ImageView ( one for back to the 'MainTasks' activity and the other one for adding a task to the database )

        saveTaskImg.setOnClickListener {

            taskNameStr = taskNameTextInputLayout.editText?.text.toString()
            taskDescriptionStr = taskDescriptionTextInputLayout.editText?.text.toString()

            // Boolean variables for the validation values
            // I used an abstract class 'Validation' with 2 functions to validate the name , deadline and description

            val taskNameValidationValue = Validation.textValidation(taskNameStr)
            val taskDescriptionValidationValue = Validation.textValidation(taskDescriptionStr)
            val taskDateValidationValue = Validation.dateValidation(taskDeadlineEditText.text.toString())

            if (taskNameValidationValue && taskDescriptionValidationValue && taskDateValidationValue) {

                // Create a task object and added to the database

                val task = Task(0 , taskNameStr , taskDeadlineEditText.text.toString() , taskDescriptionStr , typeSelected , statusSelected )
                mTaskViewModel.addTask(task)

                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()

                scheduleNotification()

                finish()

            } else {

                // Setting the helpertext for the TextInputLayout

                if ( !taskNameValidationValue ) {
                    taskNameTextInputLayout.helperText = "This field must be completed!"
                } else {
                    taskNameTextInputLayout.helperText = null
                }

                if ( !taskDescriptionValidationValue ) {
                    taskDescriptionTextInputLayout.helperText = "This field must be completed!"
                } else {
                    taskDescriptionTextInputLayout.helperText = null
                }

                // For deadline validation i used a textView because i tried with a TextInputLayout but the DatePickerDialog didn't show when i clicked on the TextInputLayout

                if ( !taskDateValidationValue ) {
                    taskDeadlineHelperText.text = "This field must contain 10 characters!"
                } else {
                    taskDeadlineHelperText.text = null
                }
            }
        }

        backImg.setOnClickListener {
            finish()
        }
    }

    // Function for adding the date selected to the EditText

    private fun setDateInEditTex(cal: Calendar) {
        val format = "MM/dd/yyyy"
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        taskDeadlineEditText.setText(dateFormat.format(cal.time))
    }

    private fun scheduleNotification() {

        val intent = Intent(applicationContext , Notification::class.java)
        val title = "---REMINDER---"
        val message = "A mai ramas o zi pana la deadline-ul task-ului $taskNameStr"

        intent.putExtra(titleExtra,title)
        intent.putExtra(messageExtra,message)

        calendar2.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH))

        calendar2.add(Calendar.DAY_OF_MONTH,-1)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun getTime() : Long {
        return calendar2.timeInMillis
    }

    private fun createNotificationChannel() {

        val name = "Notif chanel"
        val description = "Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelID , name , importance )
        channel.description = description

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}