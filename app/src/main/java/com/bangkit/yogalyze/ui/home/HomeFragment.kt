package com.bangkit.yogalyze.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yogalyze.adapter.YogaAdapter
import com.bangkit.yogalyze.databinding.FragmentHomeBinding
import com.bangkit.yogalyze.model.Yoga
import com.bangkit.yogalyze.model.YogaData
import com.bangkit.yogalyze.ui.yogalyze_video.YogalyzeVideoActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val yogaAdapter = YogaAdapter(YogaData.yoga)
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showYogaOptions()

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText)
                return true
            }
        })

        binding.continueButton.setOnClickListener {
            val intent = Intent(requireActivity(), YogalyzeVideoActivity::class.java)
            startActivity(intent)
        }

        binding.userNameTextView.text = firebaseAuth.currentUser?.displayName

        return root
    }

    private fun performSearch(query: String) {
        val filteredList = YogaData.yoga.filter { data ->
            data.name.contains(query, ignoreCase = true)
        }

        yogaAdapter.submitList(filteredList as ArrayList<Yoga>)

        binding.resultNotFoundTextView.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showYogaOptions(){
        binding.yogaRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.yogaRecyclerView.setHasFixedSize(true)
        binding.yogaRecyclerView.adapter = yogaAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}