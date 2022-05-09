package android.assist.tdl.ui.fragments.calendar

import android.assist.tdl.classes.viewmodel.TaskViewModel
import android.assist.tdl.databinding.FragmentCalendarBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel

    private var _binding: FragmentCalendarBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val adapter = CalendarAdapter()
        val recyclerView = binding.tasksListRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val calendar = Calendar.getInstance()

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = "MM/dd/yyyy"
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            val date = dateFormat.format(calendar.time)

            mTaskViewModel.searchTask(date).observe(viewLifecycleOwner) { list ->
                list.let {
                    adapter.setData(it)
                }
            }
        }

        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        mTaskViewModel.readAllData.observe(viewLifecycleOwner) { task ->
            adapter.setData(task)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}