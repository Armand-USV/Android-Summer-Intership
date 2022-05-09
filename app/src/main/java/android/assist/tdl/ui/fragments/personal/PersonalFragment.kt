package android.assist.tdl.ui.fragments.personal

import android.assist.tdl.classes.viewmodel.TaskViewModel
import android.assist.tdl.databinding.FragmentPersonalBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class PersonalFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel

    private var _binding: FragmentPersonalBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPersonalBinding.inflate(inflater, container, false)

        val adapter = PersonalAdapter()
        val recyclerView = binding.personalTasksRecyclerView
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