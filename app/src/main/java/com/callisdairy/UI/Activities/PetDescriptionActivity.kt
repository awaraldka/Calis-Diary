package com.callisdairy.UI.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.Adapter.ImagePostViewAdaptor
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.databinding.ActivityPetDescriptionBinding
import com.callisdairy.viewModel.ViewPetViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PetDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetDescriptionBinding
    lateinit var imageAdaptor: ImagePostViewAdaptor

    var PetId = ""
    var flag = ""
    var token = ""
    private val viewModelLocation : GoogleLocationApiViewModel by viewModels()
    var startLat = 0.0
    var startLong = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null

    var receiverId = ""
    var petPic = ""
    var profilePic = ""
    var name = ""
    var sendImage = ""


    var isIntrested = false
    private val viewModel: ViewPetViewModel by viewModels()
    lateinit var socketInstance: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }
        getLocation()
        window.attributes.windowAnimations = R.style.Fade
        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }


        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)


        intent.getStringExtra("id")?.let {
            PetId = it
        }

        intent.getStringExtra("flag")?.let {
            flag = it

        }

        intent.getBooleanExtra("type",false).let{
            isIntrested =  it
        }
        token =  SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()

        if (flag == "Market"){

            if (isIntrested){
                binding.BuyNow.isVisible = true
                binding.Intrested.isVisible = false
            }else{
                binding.BuyNow.isVisible = false
                binding.Intrested.isVisible = true
            }

        }else if (flag == "Interested"){

            if (!isIntrested){
                binding.BuyNow.isVisible = true
                binding.Intrested.isVisible = false
            }else{
                binding.BuyNow.isVisible = false
                binding.Intrested.isVisible = true
            }

        }else{
            if (isIntrested){
                binding.BuyNow.isVisible = true
                binding.Intrested.isVisible = false
            }else{
                binding.BuyNow.isVisible = false
                binding.Intrested.isVisible = true
            }
        }


        binding.Intrested.setSafeOnClickListener {
            viewModel.addToInterestedApi(token,"PET",PetId,"","")
            binding.Intrested.isVisible = false
            binding.BuyNow.isVisible = true
        }


        binding.BuyNow.setSafeOnClickListener {
            val intent = Intent(this, OneToOneChatActivity::class.java)
            intent.putExtra("receiverId", receiverId)
            intent.putExtra("petImage", petPic)
            intent.putExtra("userImage", profilePic)
            intent.putExtra("userName", name)
            intent.putExtra("sendImage", sendImage)
            startActivity(intent)
        }



        binding.shareClick.setSafeOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND_MULTIPLE
                putExtra(Intent.EXTRA_TEXT, "Cali's Diary")
                putExtra(Intent.EXTRA_TEXT, sendImage)
                putExtra(Intent.EXTRA_TEXT, "https://calisdiary.com/")
                type = "text/plain"
            }

            val imageUri = Uri.parse(sendImage)
            val webUri = Uri.parse("https://calisdiary.com/")

            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(imageUri, webUri))

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }



        binding.locationClick.setSafeOnClickListener {
            openMap(startLong.toString(),startLat.toString())
        }



        viewModel.petDescriptionApi(token,PetId)


        click()


        observeResponsePetDescriptionList()
        observeAddToInterestedResponse()
        ObserveLatLngResponse()
    }


    fun click(){


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


    private fun observeResponsePetDescriptionList() {

        lifecycleScope.launch {
            viewModel._petDescriptionData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        Progresss.stop()

                        if(response.data?.responseCode == 200) {
                            try{

                                with(response.data.result){

                                    receiverId  = userId._id.toString()
                                    petPic  = userId.petPic.toString()
                                    profilePic  = userId.profilePic.toString()
                                    name  = userId.name.toString()

                                    startLat = lat.toDouble()
                                    startLong = long.toDouble()


                                    setFieldValue(binding.PetName,petName)
                                    setFieldValue(binding.AddressUser,petAddress)
                                    setFieldValue(binding.userName,userId.name)
                                    setFieldValue(binding.petName,petName)
                                    setFieldValue(binding.DOB,dob)
                                    setFieldValue(binding.originName,origin)
                                    setFieldValue(binding.gender,gender)
                                    setFieldValue(binding.DescriptionName,description)
                                    setFieldValue(binding.DescriptionName,breed)
                                    setFieldValue(binding.petBreedName,breed)
                                    setFieldValue(binding.purchasedStore,purchesStore)
                                    setFieldValue(binding.sizePet,petDescription.size)
                                    setFieldValue(binding.petType,petType)
                                    setFieldValue(binding.celebrate,celebrate)

//                              Favourites Details
                                    setFieldValue(binding.favMovie,movie)
                                    setFieldValue(binding.favSong,song)
                                    setFieldValue(binding.favColor,favColour)
                                    setFieldValue(binding.favFood,favFood)
                                    setFieldValue(binding.favPlace,favPlace)

//                              Care Details

                                    setFieldValue(binding.vaccinatedDate,petDescription.lastVaccinate)
                                    setFieldValue(binding.VeterinaryType,veterinary)
                                    setFieldValue(binding.DoctorAppoint,doctorAppoint)

//                                Activities Details

                                    setFieldValue(binding.PetLanguage,language)
                                    setFieldValue(binding.petDiet,dietFreq)
                                    setFieldValue(binding.petTravel,travel)
                                    setFieldValue(binding.placeOfBirth,placeOfBirth)
                                    setFieldValue(binding.bestFriend,bestFriend)
                                    setFieldValue(binding.favToy,toy)
                                    setFieldValue(binding.commonDiseases,commonDiseases)
                                    setFieldValue(binding.insurance,insurance)
                                    setFieldValue(binding.animalShelter,animalShelter)
                                    setFieldValue(binding.medicalReport,medicalReport)
                                    setFieldValue(binding.habits,habits)
                                    setFieldValue(binding.awards,awards)
                                    setFieldValue(binding.favoriteClimate,favoriteClimate)
                                    setFieldValue(binding.etPlaceOfTravel,placeOfTravel)

                                    binding.indicator.isVisible = mediaUrls.size >1
                                    sendImage = response.data.result.mediaUrls.getOrNull(0)?.media?.mediaUrlMobile!!
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
                            androidExtension.alertBox(message,this@PetDescriptionActivity)
                        }

                    }

                    is Resource.Loading -> {
                        Progresss.start(this@PetDescriptionActivity)
                    }

                    is Resource.Empty -> {

                    }

                }

            }
        }
    }
    private fun setImageAdaptor(petImage: ArrayList<MediaUrls>) {
        imageAdaptor = ImagePostViewAdaptor(this,petImage)
        binding.storeViewpager.adapter = imageAdaptor
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTime(date: String): String {
        val zonedDateTime = ZonedDateTime.parse(date)
        val dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        return dtf.format(zonedDateTime)
    }

    private fun observeAddToInterestedResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addToInterestedData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                            }
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

    private fun ObserveLatLngResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelLocation._latLngStateFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {
                                val latitude = response.data?.results?.get(0)?.geometry?.location?.lat!!
                                val longitude = response.data.results[0].geometry.location.lng
                                openMap(longitude.toString(),latitude.toString())


                            } catch (e: Exception) {
                                e.printStackTrace()
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

    private fun openMap(desLong: String, desLat: String) {
            val latitude = desLat// Destination latitude
            val longitude = desLong // Destination longitude
            val label = binding.AddressUser.text.toString() // Destination label

            val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps") // Use Google Maps app
            startActivity(intent)

    }

    private fun getLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->

                if (task.isSuccessful && task.result != null) {
                    try {
                        val lastLocation = task.result
//                        startLat = (lastLocation)!!.latitude
//                        startLong = (lastLocation).longitude

                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setFieldValue(field: TextView, value: String?) {
        field.text = value?.takeIf { it.isNotBlank() } ?: "NA"
    }



}