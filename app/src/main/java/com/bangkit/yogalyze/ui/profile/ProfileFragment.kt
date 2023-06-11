package com.bangkit.yogalyze.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.databinding.FragmentProfileBinding
import com.bangkit.yogalyze.ui.about_us.AboutUsActivity
import com.bangkit.yogalyze.ui.alarm.AlarmActivity
import com.bangkit.yogalyze.ui.personal_information.PersonalInformationActivity
import com.bangkit.yogalyze.ui.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

@Suppress("UNREACHABLE_CODE")
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth = FirebaseAuth.getInstance()
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

        binding.nameTextView.text = firebaseAuth.currentUser?.displayName
        binding.emailTextView.text = firebaseAuth.currentUser?.email

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val timestamp = firebaseAuth.currentUser?.metadata?.creationTimestamp ?: 0
        val date = Date(timestamp)
        val formattedDate = sdf.format(date)
        binding.sinceDateTextView.text = formattedDate

        val initial = firebaseAuth.currentUser?.displayName
        val firstLetter = initial?.substring(0, 1)
        binding.initialTextView.text = firstLetter

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
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Logout")
                    setMessage("Are you sure you want to logout?")
                    setPositiveButton("YES") { _, _ ->
                        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        profileViewModel.logout()
                    }
                    setNegativeButton("NO") { dialog, _->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
            R.id.deleteAccountButton -> {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Account")
                    setMessage("Are you sure you want to delete your account?")
                    setPositiveButton("YES") { _, _ ->
                        profileViewModel.delete()
                        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    setNegativeButton("NO") { dialog, _->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
            R.id.notificationButton -> {
                val intent = Intent(requireActivity(), AlarmActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}