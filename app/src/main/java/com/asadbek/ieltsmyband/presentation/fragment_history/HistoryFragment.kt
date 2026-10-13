package com.asadbek.ieltsmyband.presentation.fragment_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.asadbek.ieltsmyband.data.local.AppDatabase
import com.asadbek.ieltsmyband.databinding.FragmentHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        val dao = AppDatabase.getDatabase(requireContext()).historyDao()

        viewLifecycleOwner.lifecycleScope.launch {
            dao.getAllHistory().collectLatest { entities ->
                if (entities.isEmpty()) {
                    binding.layoutEmptyHistory.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.GONE
                } else {
                    binding.layoutEmptyHistory.visibility = View.GONE
                    binding.rvHistory.visibility = View.VISIBLE

                    val items = entities.map {
                        HistoryItem(
                            id = it.id.toString(),
                            date = it.date,
                            title = it.title,
                            score = it.score
                        )
                    }
                    binding.rvHistory.adapter = HistoryAdapter(items)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}
