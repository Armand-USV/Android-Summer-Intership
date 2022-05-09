package android.assist.tdl.ui.fragments.updatetask

import android.app.DatePickerDialog
import android.assist.tdl.R
import android.assist.tdl.classes.model.Task
import android.assist.tdl.classes.validation.Validation
import android.assist.tdl.classes.viewmodel.TaskViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class UpdateTaskFragment : Fragment() {

    private val args by navArgs<UpdateTaskFragmentArgs>()

    private lateinit var mTaskViewModel : TaskViewModel

    private lateinit var updateTaskName : TextInputLayout
    private lateinit var updateTaskDeadline : EditText
    private lateinit var updateTaskDescription : TextInputLayout
    private lateinit var updateTaskType : Spinner
    private lateinit var updateTaskStatus : Spinner
    private lateinit var updateButton : MaterialButton
    private lateinit var deadlineHelperText : TextView

    private lateinit var typeSelected : String
    private lateinit var statusSelected : String
    private lateinit var updateStatusSelected : String

    private var typeSelectedPos : Int = 0
    private var statusSelectedPos : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_task, container, false)

        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val taskTypes = arrayOf("Personal", "Work", "School")
        var taskStatus = arrayOf("To Do", "In progress", "Done")

        updateTaskName = view.findViewById(R.id.updateTaskNameTextInputLayout)
        updateTaskDeadline = view.findViewById(R.id.updateTaskDeadlineEditText)
        updateTaskDescription = view.findViewById(R.id.updateTaskDescriptionTextInputLayout)
        updateTaskType = view.findViewById(R.id.updateTaskTypeSpinner)
        updateTaskStatus = view.findViewById(R.id.updateTaskStatusSpinner)
        updateButton = view.findViewById(R.id.updateTaskButton)
        deadlineHelperText = view.findViewById(R.id.updateDeadlineHelperText)

        updateTaskType.isClickable = false
        updateTaskType.isEnabled = false

        typeSelected = args.currentTask.type
        statusSelected = args.currentTask.status

        if ( statusSelected == "In progress") {
            updateTaskName.isClickable = true
            updateTaskName.isEnabled = true
            updateTaskDeadline.isClickable = true
            updateTaskDeadline.isEnabled = true
            updateTaskDescription.isClickable = true
            updateTaskDescription.isEnabled = true
            taskStatus = arrayOf("In progress" , "Done")
        } else {
            updateTaskName.isClickable = false
            updateTaskName.isEnabled = false
            updateTaskDeadline.isClickable = false
            updateTaskDeadline.isEnabled = false
            updateTaskDescription.isClickable = false
            updateTaskDescription.isEnabled = false
            if ( statusSelected == "To Do") {
                taskStatus = arrayOf("To Do" , "In progress")
            }
        }

        updateTaskType.adapter =
            context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1,taskTypes) }
        updateTaskStatus.adapter =
            context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1,taskStatus) }

        updateTaskName.editText?.setText(args.currentTask.name)
        updateTaskDeadline.setText(args.currentTask.deadLine)
        updateTaskDescription.editText?.setText(args.currentTask.description)

        typeSelectedPos = taskTypes.indexOf(typeSelected)
        updateTaskType.setSelection(typeSelectedPos)
        statusSelectedPos = taskStatus.indexOf(statusSelected)
        updateTaskStatus.setSelection(statusSelectedPos)

        updateTaskStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateStatusSelected = taskStatus[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val calendar: Calendar = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setDateInEditTex(calendar)
            }

        updateTaskDeadline.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        updateButton.setOnClickListener {
            val taskNameStr = updateTaskName.editText?.text.toString()
            val taskDescriptionStr = updateTaskDescription.editText?.text.toString()

            val taskNameValidationValue = Validation.textValidation(taskNameStr)
            val taskDescriptionValidationValue = Validation.textValidation(taskDescriptionStr)
            val taskDateValidationValue = Validation.dateValidation(updateTaskDeadline.text.toString())

            if (taskNameValidationValue && taskDescriptionValidationValue && taskDateValidationValue) {

                val updatedTask = Task(args.currentTask.id , taskNameStr , updateTaskDeadline.text.toString() , taskDescriptionStr , typeSelected , updateStatusSelected )
                mTaskViewModel.updateTask(updatedTask)

                Toast.makeText(requireContext(), "Task updated !", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateTaskFragment_to_nav_personal)

            } else {

                // Setting the helpertext for the TextInputLayout

                if ( !taskNameValidationValue ) {
                    updateTaskName.helperText = "This field must be completed!"
                } else {
                    updateTaskName.helperText = null
                }

                if ( !taskDescriptionValidationValue ) {
                    updateTaskDescription.helperText = "This field must be completed!"
                } else {
                    updateTaskDescription.helperText = null
                }

                if ( !taskDateValidationValue ) {
                    deadlineHelperText.text = "This field must contain 10 characters!"
                } else {
                    deadlineHelperText.text = null
                }
            }
        }

        return view
    }
    private fun setDateInEditTex(cal: Calendar) {
        val format = "MM/dd/yyyy"
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        updateTaskDeadline.setText(dateFormat.format(cal.time))
    }
}
