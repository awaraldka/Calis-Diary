package com.callisdairy.Vendor.Fragmnets

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.callisdairy.CalisApp
import com.callisdairy.Interface.Logout
import com.callisdairy.R
import com.callisdairy.UI.Activities.HelpActivity
import com.callisdairy.UI.Activities.SettingsActivity
import com.callisdairy.Utils.ClearCache
import com.callisdairy.Utils.Home
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.ChooseTypeActivity
import com.callisdairy.databinding.FragmentMenuFragmentBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.viewModel.LoginViewModel
import com.callisdairy.viewModel.VendorCommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuVendorFragment : Fragment(), Logout {

    private var _binding: FragmentMenuFragmentBinding? = null
    private val binding get() = _binding!!


    lateinit var addVendors: ImageView
    lateinit var homeTv: TextView
    lateinit var productTv: TextView
    lateinit var petTv: TextView
    lateinit var serviceTv: TextView
    lateinit var settingsTv: TextView


    lateinit var settingsView:View
    lateinit var serviceView:View
    lateinit var petsView:View
    lateinit var productView:View
    lateinit var Homeview:View

    private val viewModelCommon: VendorCommonViewModel by viewModels()

    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentMenuFragmentBinding.inflate(layoutInflater, container, false)

        findIdAndHandleTab()


        val token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()
        viewModelCommon.viewProfileApi(token)

        click()
        observeLogoutResponse()
        observeViewProfileResponse()

        return  binding.root

    }

    private fun click() {


        binding.HelpSideMenu.setOnClickListener {
            val intent = Intent(requireContext(), HelpActivity::class.java)
            intent.putExtra("from","vendor")
            startActivity(intent)
        }

        binding.SettingSideMenu.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            intent.putExtra("from","vendor")
            startActivity(intent)
        }

        binding.ProfileSideMenu.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.vendorContainer,VendorProfileFragment()).addToBackStack("VendorProfileFragment").commit()

        }


        binding.EventsMenu.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.vendorContainer, VendorEventsFragment())
                .addToBackStack(null).commit()
        }

        binding.RewardSideMenu.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.vendorContainer, VendorRewardsFragment())
                .addToBackStack(null).commit()
        }



        binding.logout.setOnClickListener {
            androidExtension.showDialogLogout(requireContext(),getString(R.string.are_you_sure_you_want_to_logout),getString(R.string.logout),this)
        }



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                }
            })
    }

    private fun findIdAndHandleTab() {


        addVendors = activity?.findViewById(R.id.addVendors)!!

        homeTv = activity?.findViewById(R.id.homeTv)!!
        productTv = activity?.findViewById(R.id.productTv)!!
        petTv = activity?.findViewById(R.id.petTv)!!
        serviceTv = activity?.findViewById(R.id.serviceTv)!!
        settingsTv = activity?.findViewById(R.id.settingsTv)!!

        settingsView = activity?.findViewById(R.id.settingsView)!!
        serviceView = activity?.findViewById(R.id.serviceView)!!
        petsView = activity?.findViewById(R.id.petsView)!!
        productView = activity?.findViewById(R.id.productView)!!
        Homeview = activity?.findViewById(R.id.Homeview)!!



        homeTv.setTextColor(Color.parseColor("#757575"))
        productTv.setTextColor(Color.parseColor("#757575"))
        petTv.setTextColor(Color.parseColor("#757575"))
        serviceTv.setTextColor(Color.parseColor("#757575"))
        settingsTv.setTextColor(Color.parseColor("#6FCFB9"))

        addVendors.isVisible = false

        Homeview.isVisible = false
        productView.isVisible = false
        petsView.isVisible = false
        serviceView.isVisible = false
        settingsView.isVisible = true

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

                                SavedPrefManager.deleteAllKeys(context)
                                CalisApp.simpleCache?.release()
                                ClearCache.clearApplicationCache(requireContext())


                                val intent = Intent(requireContext(), ChooseTypeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
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


    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelCommon._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {


                            if (response.data?.responseCode == 200) {
                                try {

                                    Glide.with(requireContext()).load(response.data.result.profilePic).placeholder(R.drawable.placeholder_pet).into(binding.vendorImage)

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