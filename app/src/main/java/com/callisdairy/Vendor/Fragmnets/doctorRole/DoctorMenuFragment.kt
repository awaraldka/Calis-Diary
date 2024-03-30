package com.callisdairy.Vendor.Fragmnets.doctorRole

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.CalisApp
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.Interface.Logout
import com.callisdairy.R
import com.callisdairy.UI.Activities.ChangeLanguageActivity
import com.callisdairy.UI.Activities.ChangePasswordActivity
import com.callisdairy.UI.Activities.HelpActivity
import com.callisdairy.Utils.ClearCache
import com.callisdairy.Utils.Home
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.Vendor.ChooseTypeActivity
import com.callisdairy.databinding.FragmentDoctorMenuBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.LoginViewModel
import com.callisdairy.viewModel.VendorCommonViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoctorMenuFragment : Fragment(), Logout, CommonDialogInterface {

    private val viewModelCommon: VendorCommonViewModel by viewModels()

    private val viewModel: LoginViewModel by viewModels()
    var accountType = ""

    private var _binding: FragmentDoctorMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentDoctorMenuBinding.inflate(layoutInflater, container, false)
        click()


        observeLogoutResponse()
        observeResponseAccountDelete()
        observeResponseAccountDeactivate()
        return binding.root

    }



    private fun click() {


        binding.HelpSideMenu.setSafeOnClickListener {
            val intent = Intent(requireContext(), HelpActivity::class.java)
            intent.putExtra("from", "vendor")
            startActivity(intent)
        }

        binding.ChangePassword.setSafeOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.ChangeLanguage.setSafeOnClickListener {
            val intent = Intent(requireContext(), ChangeLanguageActivity::class.java)
            intent.putExtra("from","vendor")
            startActivity(intent)
        }


        binding.FriendsMenu.setOnClickListener {

            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag", "Friend Requests")
            startActivity(intent)

        }


        binding.AllFriends.setOnClickListener {

            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag", "MyFriend List")
            startActivity(intent)

        }



        binding.ProfileSideMenu.setSafeOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.vendorContainer,
                DoctorProfileFragment()
            ).addToBackStack("DoctorProfileFragment").commit()

        }

        binding.BlockedUsers.setSafeOnClickListener {

            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag", "Block User")
            startActivity(intent)

        }

        binding.AppointmentsHistory.setSafeOnClickListener {
            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag", "Appointment History")
            startActivity(intent)

        }

        binding.FeedbackClick.setSafeOnClickListener {
            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag", "Feedback List")
            intent.putExtra("isScreen","Home")
            startActivity(intent)

        }


        binding.DeleteAccount.setSafeOnClickListener{
            accountType = "delete"
            androidExtension.showDialogDeleteAccount(requireContext(),getString(R.string.are_you_sure_you_want_to_delete),getString(R.string.delete),this)
        }

        binding.DeactivateAccount.setSafeOnClickListener{
            accountType = "deactivate"
            androidExtension.showDialogDeleteAccount(requireContext(),getString(R.string.are_you_sure_you_want_to_deactivate),getString(R.string.deactivate),this)
        }


        binding.logout.setSafeOnClickListener {
            androidExtension.showDialogLogout(requireContext(),getString(R.string.are_you_sure_you_want_to_logout),getString(R.string.logout),this)
        }


    }

    override fun logoutUser() {
        Home.data.clear()
        Home.suggestedUserData.clear()
        val token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        val fireToken = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.DeviceToken).toString()
        viewModel.userLogout(token,fireToken)

    }


    private fun observeLogoutResponse() {

        lifecycleScope.launch {
            viewModel._logoutData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if(response.data?.responseCode == 200) {
                            try{
                                logout()
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(requireContext())
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }

    override fun commonWork() {
        val token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()

        if (accountType == "delete"){
            viewModel.deleteAccountApi(token)
        }
        if (accountType == "deactivate"){
            viewModel.deactivateAccountApi(token)
        }


    }


    private fun logout(){
        SavedPrefManager.deleteAllKeys(context)
        CalisApp.simpleCache?.release()
        ClearCache.clearApplicationCache(requireContext())


        val intent = Intent(requireContext(), ChooseTypeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    private fun observeResponseAccountDelete() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deleteAccountData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    logout()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }

        }


    }

    private fun observeResponseAccountDeactivate() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deactivateAccountData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    logout()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }

        }


    }




}