package android.assist.tdl.ui.fragments.school

import android.assist.tdl.classes.viewmodel.TaskViewModel
import android.assist.tdl.databinding.FragmentSchoolBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class SchoolFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel

    private var _binding: FragmentSchoolBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchoolBinding.inflate(inflater, container, false)

        val adapter = SchoolAdapter()
        val recyclerView = binding.schoolTasksRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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