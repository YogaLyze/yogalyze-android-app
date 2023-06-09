package com.bangkit.yogalyze.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yogalyze.adapter.HistoryAdapter
import com.bangkit.yogalyze.api.response.UserHistoryItem
import com.bangkit.yogalyze.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val historyViewModel by viewModels<HistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()

        return root
    }

    private fun setupViewModel() {
        historyViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it.equals(true)) View.VISIBLE else View.GONE
        }

        historyViewModel.getToken().observe(requireActivity()){
            historyViewModel.getHistory(it)
            historyViewModel.historyData.observe(viewLifecycleOwner){ data ->
                if(data != null){
                    setHistoryData(data)
                }
            }
        }
    }

    private fun setHistoryData(data: List<UserHistoryItem>) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager

        val adapter = HistoryAdapter(ArrayList(data.reversed()))
        binding.rvHistory.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}