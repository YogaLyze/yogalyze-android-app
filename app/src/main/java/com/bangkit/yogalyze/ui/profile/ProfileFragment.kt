package com.bangkit.yogalyze.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.databinding.FragmentProfileBinding
import com.bangkit.yogalyze.ui.about_us.AboutUsActivity
import com.bangkit.yogalyze.ui.login.LoginActivity
import com.bangkit.yogalyze.ui.personal_information.PersonalInformationActivity


@Suppress("UNREACHABLE_CODE")
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")
    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModel.ProfileViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.personalInformationButton.setOnClickListener(this)
        binding.notificationButton.setOnClickListener(this)
        binding.aboutUsButton.setOnClickListener(this)
        binding.logoutButton.setOnClickListener(this)
        binding.deleteAccountButton.setOnClickListener(this)

        return root
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.personalInformationButton -> {
                val intent = Intent(requireActivity(), PersonalInformationActivity::class.java)
                startActivity(intent)
            }
            R.id.aboutUsButton -> {
                val intent = Intent(requireActivity(), AboutUsActivity::class.java)
                startActivity(intent)
            }
            R.id.logoutButton -> {
                profileViewModel.logout()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.deleteAccountButton -> {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}