package com.callisdairy.UI.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.callisdairy.CalisApp
import com.callisdairy.Interface.Logout
import com.callisdairy.R
import com.callisdairy.UI.Activities.*
import com.callisdairy.Utils.ClearCache
import com.callisdairy.Utils.Home.data
import com.callisdairy.Utils.Home.suggestedUserData
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.ChooseTypeActivity
import com.callisdairy.databinding.FragmentMenuBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.LoginViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment : Fragment(),Logout {


    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    lateinit var back: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView


    //     Bottom Tab

    lateinit var SelectedHome: ImageView
    lateinit var UnSelectedHome: ImageView

    lateinit var unSelectedNotification: ImageView
    lateinit var selectedNotification: ImageView

    lateinit var UnSelectedCart: ImageView
    lateinit var SelectedCart: ImageView

    lateinit var UnSelectedProfile: ImageView
    lateinit var SelectedProfile: ImageView

    lateinit var UnSelectedMenu: ImageView
    lateinit var SelectedMenu: ImageView


    lateinit var SelectedFavorites: ImageView
    lateinit var UnSelectedFavorites: ImageView

    lateinit var Homeview: View
    lateinit var Marketview: View
    lateinit var Favoritesview: View
    lateinit var ProfileView: View
    lateinit var NotificationView: View
    lateinit var MenuView: View


    private val viewModel:LoginViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        allIds()
        toolBarWithBottomTab()
        back.setSafeOnClickListener {
            parentFragmentManager.popBackStack()
        }



        clicks()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLogoutResponse()
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


    private fun clicks() {
        binding.InterestedMenu.setSafeOnClickListener {
            val bundle = Bundle()
            bundle.putString("flag", "Interested")
            val obj = InterestedFragment()
            obj.arguments = bundle

            parentFragmentManager.beginTransaction().replace(R.id.home_container, obj)
                .addToBackStack(null).commit()

        }

        binding.FavoritesMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("flag", "FavouritesMenu")
            val obj = FavoritesFragment()
            obj.arguments = bundle

            parentFragmentManager.beginTransaction().replace(R.id.home_container, obj)
                .addToBackStack(null).commit()
        }

        binding.FriendsMenu.setOnClickListener {


            val bundle = Bundle()
            bundle.putSerializable("from","user")
            val obj = RequestedListFragment()
            obj.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.home_container, obj)
                .addToBackStack(null).commit()
        }

        binding.EventsMenu.setOnClickListener {

            parentFragmentManager?.beginTransaction()
                ?.replace(R.id.home_container, EventListFragment())
                ?.addToBackStack(null)?.commit()
        }

        binding.MissingPetsMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("flag", "MissingPetsMenu")
            val obj = MissingPetFragment()
            obj.arguments = bundle

            parentFragmentManager.beginTransaction().replace(R.id.home_container, obj)
                .addToBackStack(null).commit()
        }

        binding.GamesClick.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.home_container, GamesFragment())
                .addToBackStack(null).commit()

        }

        binding.RewardSideMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("flag", "RewardSideMenu")
            val obj = RewardsFragment()
            obj.arguments = bundle

            parentFragmentManager.beginTransaction().replace(R.id.home_container, obj)
                .addToBackStack(null).commit()
        }

        binding.SettingSideMenu.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.HelpSideMenu.setOnClickListener {
            val intent = Intent(requireContext(), HelpActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {

            androidExtension.showDialogLogout(requireContext(),getString(R.string.are_you_sure_you_want_to_logout),getString(R.string.logout),this)
        }

        binding.TrackingDevicesMenu.setOnClickListener {
            val intent = Intent(requireContext(), TrackingDeviceActivity::class.java)
            startActivity(intent)
        }

        binding.SwitchProfileClick.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivity(intent)
        }


        binding.BlockedUsers.setOnClickListener {

            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Block UnBlock")
            startActivity(intent)

        }


        binding.VetDocotrClick.setOnClickListener {
            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Doctor")
            startActivity(intent)
        }


        binding.AppointmentsHistory.setOnClickListener {

            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Appointment History")
            startActivity(intent)

        }



        binding.ListedPetClick.setOnClickListener {
            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "ListedPet")
            startActivity(intent)
        }


        binding.MarketMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("flag", "MarketFragmentMenu")
            val obj = MarketFragment()
            obj.arguments = bundle


            parentFragmentManager.beginTransaction()
                .replace(R.id.home_container, obj, "MarketFragment")
                .addToBackStack("MarketFragment").commit()
        }


        binding.PetSectionMenu.setOnClickListener {

            binding.imageRotationOfPet.rotation = if(!binding.viewOfPet.isVisible){
                 0f
            }else{
                -90f
            }

            binding.viewOfPet.isVisible = !binding.viewOfPet.isVisible

        }


        binding.FindDocTorMenu.setOnClickListener {

            binding.imageRotationOfDoctor.rotation = if(!binding.viewOfDoctor.isVisible){
                 0f
            }else{
                -90f
            }

            binding.viewOfDoctor.isVisible = !binding.viewOfDoctor.isVisible

        }


        binding.MarketSectionMenu.setOnClickListener {

            binding.imageRotationOfMarket.rotation = if(!binding.viewOfMarket.isVisible){
                 0f
            }else{
                -90f
            }

            binding.viewOfMarket.isVisible = !binding.viewOfMarket.isVisible

        }





    }

    private fun allIds() {
        SelectedHome = activity?.findViewById(R.id.SelectedHome)!!
        UnSelectedHome = activity?.findViewById(R.id.UnSelectedHome)!!


        UnSelectedCart = activity?.findViewById(R.id.UnSelectedCart)!!
        SelectedCart = activity?.findViewById(R.id.SelectedCart)!!

        UnSelectedProfile = activity?.findViewById(R.id.UnSelectedProfile)!!
        SelectedProfile = activity?.findViewById(R.id.SelectedProfile)!!


        UnSelectedMenu = activity?.findViewById(R.id.UnSelectedMarket)!!
        SelectedMenu = activity?.findViewById(R.id.SelectedMarket)!!


        unSelectedNotification = activity?.findViewById(R.id.UnSelectedNotification)!!
        selectedNotification = activity?.findViewById(R.id.SelectedNotification)!!

        SelectedFavorites = activity?.findViewById(R.id.SelectedFavorites)!!
        UnSelectedFavorites = activity?.findViewById(R.id.UnSelectedFavorites)!!


        Homeview = activity?.findViewById(R.id.Homeview)!!
        Marketview = activity?.findViewById(R.id.Marketview)!!
        Favoritesview = activity?.findViewById(R.id.Favoritesview)!!
        ProfileView = activity?.findViewById(R.id.ProfileView)!!
        NotificationView = activity?.findViewById(R.id.NotificationView)!!
        MenuView = activity?.findViewById(R.id.MenuView)!!

        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!

    }

    private fun toolBarWithBottomTab() {


        mainTitle.visibility = View.VISIBLE
        chat.visibility = View.VISIBLE
        back.visibility = View.GONE
        search.visibility = View.VISIBLE


        mainTitle.text = ""
        Username.text = getString(R.string.favorites)



        SelectedHome.visibility = View.GONE
        UnSelectedHome.visibility = View.VISIBLE

        SelectedCart.visibility = View.GONE
        UnSelectedCart.visibility = View.VISIBLE


        unSelectedNotification.visibility = View.VISIBLE
        selectedNotification.visibility = View.GONE

        UnSelectedProfile.visibility = View.VISIBLE
        SelectedProfile.visibility = View.GONE



        UnSelectedMenu.visibility = View.GONE
        SelectedMenu.visibility = View.VISIBLE

        UnSelectedFavorites.visibility = View.VISIBLE
        SelectedFavorites.visibility = View.GONE


        Homeview.isVisible = false
        Marketview.isVisible = false
        Favoritesview.isVisible = false
        ProfileView.isVisible = false
        NotificationView.isVisible = false
        MenuView.isVisible = true

    }

    override fun logoutUser() {
        data.clear()
        suggestedUserData.clear()
        val token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()
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
//                            androidExtension.alertBox(message, requireContext())
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



}