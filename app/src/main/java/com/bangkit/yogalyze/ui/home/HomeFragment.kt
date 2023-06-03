package com.bangkit.yogalyze.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.adapter.YogaAdapter
import com.bangkit.yogalyze.databinding.FragmentHomeBinding
import com.bangkit.yogalyze.model.Yoga
import com.bangkit.yogalyze.model.YogaData
import com.bangkit.yogalyze.ui.yoga_detail.YogaDetailActivity
import com.bangkit.yogalyze.ui.yogalyze_video.YogalyzeVideoActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val yogaAdapter = YogaAdapter(YogaData.yoga)
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModel.homeViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showYogaOptions()
        setUpViewModel()

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

        binding.continueButton.setOnClickListener(){
            val intent = Intent(requireActivity(), YogalyzeVideoActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun performSearch(query: String) {
        val filteredList = YogaData.yoga.filter { data ->
            data.name.contains(query, ignoreCase = true)
        }

        yogaAdapter.submitList(filteredList as ArrayList<Yoga>)

        binding.resultNotFoundTextView.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setUpViewModel() {
        homeViewModel.isLoading.observe(requireActivity()){
            binding.progressBar.visibility = if (it.equals(true)) View.VISIBLE else View.GONE
        }

        homeViewModel.getToken().observe(requireActivity()){
            homeViewModel.getUser(it.accessToken.toString())
        }

        homeViewModel.userData.observe(requireActivity()){
            binding.userNameTextView.text = it.name
        }
    }

    private fun showYogaOptions(){
        binding.yogaRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.yogaRecyclerView.setHasFixedSize(true)
        binding.yogaRecyclerView.adapter = yogaAdapter
        yogaAdapter.setOnItemClickCallback(object : YogaAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Yoga) {
                showSelectedYoga(data)
            }
        })
    }

    private fun showSelectedYoga(data: Yoga) {
        val intent = Intent(requireContext(), YogaDetailActivity::class.java)
        intent.putExtra(YogaDetailActivity.EXTRA_NAME, data.name)
        intent.putExtra(YogaDetailActivity.EXTRA_DURATION, data.duration)
        intent.putExtra(YogaDetailActivity.EXTRA_IMAGE, data.image)
        intent.putExtra(YogaDetailActivity.EXTRA_DESCRIPTION, data.description)
        intent.putExtra(YogaDetailActivity.EXTRA_POSES, data.pose)
        startActivity(intent)

        Log.d("yogaPose", data.pose.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}