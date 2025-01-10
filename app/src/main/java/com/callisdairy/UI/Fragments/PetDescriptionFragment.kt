package com.callisdairy.UI.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.Adapter.ImagePostViewAdaptor
import com.callisdairy.Interface.AddPetInterface
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.Interface.DeleteClick
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddPetActivity
import com.callisdairy.Utils.LocationClass
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.databinding.FragmentPetDescriptionBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.viewModel.ViewPetViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PetDescriptionFragment : Fragment(), CommonDialogInterface, AddPetInterface, DeleteClick {

    private lateinit var binding: FragmentPetDescriptionBinding


    lateinit var imageAdaptor: ImagePostViewAdaptor


    var postId = ""
    lateinit var backTitle: ImageView
    var token = ""

    var flag = ""
    var typeUser = ""
    var currency = ""
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var startLat = 0.0
    var startLong = 0.0


    private val viewModel: ViewPetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentPetDescriptionBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }

        arguments?.getString("id")?.let {
            postId = it
        }

        arguments?.getString("flag")?.let {
            flag = it
        }

        arguments?.getString("typeUser")?.let {
            typeUser = it
        }

        backTitle = activity?.findViewById(R.id.backTitle)!!

        if (typeUser == "vendor"){
            binding.viewInterested.isVisible = true
        }

        binding.viewInterested.setOnClickListener {
            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag","viewInterestedUser")
            intent.putExtra("from","Pet")
            startActivity(intent)
        }

        lifecycleScope.launch {
            currency = LocationClass.getLocationBaseCurrency(fusedLocationClient,requireActivity())

        }
        currency = getBaseCurrency(currency)


        binding.AddressUser.setOnClickListener {
            openMap(startLong.toString(),startLat.toString())
        }


        if (typeUser == "otherUser"){
            binding.editRemoveButton.visibility = View.GONE
            binding.profilePetProgress.visibility = View.GONE
            binding.addToMarketPlaces.visibility = View.GONE
        }else{
            binding.editRemoveButton.visibility = View.VISIBLE
            binding.profilePetProgress.visibility = View.VISIBLE
            binding.addToMarketPlaces.visibility = View.VISIBLE
        }


        backTitle.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        token =  SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()




        click()


        return view
    }


    override fun onStart() {
        super.onStart()
        token =  SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        viewModel.petDescriptionApi(token,postId)
    }


    fun click(){


        binding.editButton.setOnClickListener {
            val intent = Intent(context, AddPetActivity::class.java)
            intent.putExtra("flag", "Edit Pet")
            intent.putExtra("id", postId)
            startActivity(intent)
        }



        binding.addToMarketClick.setOnClickListener {
           androidExtension.addPriceDialog(requireContext(),this,currency)
        }

        binding.removeToMarketClick.setOnClickListener {
            androidExtension.alertBoxCommon("Are you sure you want to remove pet from market?",requireContext(),this)
        }

        binding.removeButton.setOnClickListener {
            androidExtension.alertBoxDelete("Are you sure you want to delete this pet?",requireContext(),this)
        }



        binding.BasicInfoClick.setOnClickListener {
            binding.BasicInfoLL.visibility = View.VISIBLE
            binding.BasicInfoTV.setTextColor(Color.parseColor("#6FCFB9"))


            binding.FavouritesLL.visibility = View.GONE
            binding.FavouritesTV.setTextColor(Color.parseColor("#000000"))

            binding.CareLL.visibility = View.GONE
            binding.CareTV.setTextColor(Color.parseColor("#000000"))

            binding.ActivitiesLL.visibility = View.GONE
            binding.ActivitiesTV.setTextColor(Color.parseColor("#000000"))




        }
        binding.FavouritesClick.setOnClickListener {

            binding.BasicInfoLL.visibility = View.GONE
            binding.BasicInfoTV.setTextColor(Color.parseColor("#000000"))

            binding.FavouritesLL.visibility = View.VISIBLE
            binding.FavouritesTV.setTextColor(Color.parseColor("#6FCFB9"))


            binding.CareLL.visibility = View.GONE
            binding.CareTV.setTextColor(Color.parseColor("#000000"))

            binding.ActivitiesLL.visibility = View.GONE
            binding.ActivitiesTV.setTextColor(Color.parseColor("#000000"))


        }
        binding.CareClick.setOnClickListener {

            binding.BasicInfoLL.visibility = View.GONE
            binding.BasicInfoTV.setTextColor(Color.parseColor("#000000"))

            binding.FavouritesLL.visibility = View.GONE
            binding.FavouritesTV.setTextColor(Color.parseColor("#000000"))


            binding.CareLL.visibility = View.VISIBLE
            binding.CareTV.setTextColor(Color.parseColor("#6FCFB9"))

            binding.ActivitiesLL.visibility = View.GONE
            binding.ActivitiesTV.setTextColor(Color.parseColor("#000000"))


        }
        binding.ActivitiesClick.setOnClickListener {

            binding.BasicInfoLL.visibility = View.GONE
            binding.BasicInfoTV.setTextColor(Color.parseColor("#000000"))

            binding.FavouritesLL.visibility = View.GONE
            binding.FavouritesTV.setTextColor(Color.parseColor("#000000"))


            binding.CareLL.visibility = View.GONE
            binding.CareTV.setTextColor(Color.parseColor("#000000"))

            binding.ActivitiesLL.visibility = View.VISIBLE
            binding.ActivitiesTV.setTextColor(Color.parseColor("#6FCFB9"))


        }


    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponsePetDescriptionList()
        observeAddPetInToMarketResponse()
        observeRemovePetFromMarketResponse()
        observeRemovePetResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponsePetDescriptionList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._petDescriptionData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if(response.data?.responseCode == 200) {
                                try{

                                    with(response.data.result){


                                        if (lat !=  null && long != null){
                                            startLat = lat.toDouble()
                                            startLong = long.toDouble()
                                        }



                                        if (isMarketPlace!!){
                                            binding.addToMarketClick.isVisible = false
                                            binding.removeToMarketClick.isVisible = true

                                        }else{
                                            binding.addToMarketClick.isVisible = true
                                            binding.removeToMarketClick.isVisible = false
                                        }

                                        // Basic Info Details
                                        setFieldValue(binding.PetName, petName)
                                        setFieldValue(binding.AddressUser, petAddress)
                                        setFieldValue(binding.userName, userId.name)
                                        setFieldValue(binding.petName, petName)
                                        setFieldValue(binding.DOB, dob)
                                        setFieldValue(binding.originName, origin)
                                        setFieldValue(binding.gender, gender)
                                        setFieldValue(binding.DescriptionName, description)
                                        setFieldValue(binding.petBreedName, breed)
                                        setFieldValue(binding.purchasedStore, purchesStore)
                                        setFieldValue(binding.sizePet, petDescription.size)
                                        setFieldValue(binding.petType, petCategoryId?.petCategoryName)
                                        setFieldValue(binding.celebrate, celebrate)

                                        // Favourites Details
                                        setFieldValue(binding.favMovie, movie)
                                        setFieldValue(binding.favSong, song)
                                        setFieldValue(binding.favColor, favColour)
                                        setFieldValue(binding.favFood, favFood)
                                        setFieldValue(binding.favPlace, favPlace)

                                        // Care Details
                                        setFieldValue(binding.vaccinatedDate, petDescription.lastVaccinate)
                                        setFieldValue(binding.VeterinaryType, veterinary)
                                        setFieldValue(binding.DoctorAppoint, doctorAppoint)

                                        // Activities Details
                                        setFieldValue(binding.PetLanguage, language)
                                        setFieldValue(binding.petDiet, dietFreq)
                                        setFieldValue(binding.petTravel, travel)
                                        setFieldValue(binding.placeOfBirth, placeOfBirth)
                                        setFieldValue(binding.bestFriend, bestFriend)
                                        setFieldValue(binding.favToy, toy)
                                        setFieldValue(binding.commonDiseases, commonDiseases)
                                        setFieldValue(binding.insurance, insurance)
                                        setFieldValue(binding.animalShelter, animalShelter)
                                        setFieldValue(binding.medicalReport, medicalReport)
                                        setFieldValue(binding.habits, habits)
                                        setFieldValue(binding.awards, awards)
                                        setFieldValue(binding.favoriteClimate, favoriteClimate)



                                        if (percentage != null){
                                            binding.progressBar.progress = percentage
                                            binding.textViewProgress.text = percentage.toString()
                                        }


                                        binding.indicator.isVisible = mediaUrls.size >1

                                        setImageAdaptor(mediaUrls)



                                    }





                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message,requireContext())
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

    private fun setImageAdaptor(dataImage: ArrayList<MediaUrls>) {
        imageAdaptor = ImagePostViewAdaptor(requireContext(), dataImage)
        binding.storeViewpager.adapter = imageAdaptor
        binding.indicator.setViewPager(binding.storeViewpager)

    }



    private fun observeAddPetInToMarketResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._addToMarketData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBox(response.data.responseMessage, requireContext())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
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



    private fun observeRemovePetFromMarketResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._removeToMarketData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBox(response.data.responseMessage, requireContext())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
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

    override fun petPrice(petPriceValue: String) {
            viewModel.addToMarketApi(token,postId,petPriceValue,currency)

    }

    override fun commonWork() {
        viewModel.removeToMarketApi(token,postId)

    }

    override fun deleteItem() {
        viewModel.removePetApi(token,postId)
    }


    private fun observeRemovePetResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._removePetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    activity?.finishAfterTransition()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
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


    private fun getBaseCurrency(currencyCode: String): String {
        val countryBaseCurrency = hashSetOf("GBP", "USD", "EUR", "CNY", "RUB", "EUR", "INR", "VND", "TRY", "PLN", "IDR", "RON", "PHP")

        return if (countryBaseCurrency.contains(currencyCode)) {
            currencyCode
        } else {
            "USD"
        }
    }

    private fun setFieldValue(field: TextView, value: String?) {
        field.text = value?.takeIf { it.isNotBlank() } ?: "NA"
    }

    private fun openMap(desLong: String, desLat: String) {
        val latitude = desLat// Destination latitude
        val longitude = desLong // Destination longitude
        val label = binding.AddressUser.text.toString() // Destination label

        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps") // Use Google Maps app
        startActivity(intent)

    }

}