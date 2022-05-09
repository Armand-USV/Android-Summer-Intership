package android.assist.tdl.ui.fragments.work

import android.assist.tdl.classes.viewmodel.TaskViewModel
import android.assist.tdl.databinding.FragmentWorkBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class WorkFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel


    private var _binding: FragmentWorkBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWorkBinding.inflate(inflater, container, false)

        val adapter = WorkAdapter()
        val recyclerView = binding.workTasksRecyclerView
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