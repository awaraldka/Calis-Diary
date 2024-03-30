package com.callisdairy.UI.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.Adapter.ImageSliderAdaptor
import com.callisdairy.ModalClass.descriptionImage
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.databinding.ActivityProductDescriptionBinding
import com.callisdairy.viewModel.ViewProductViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.FavProductDocs
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDescriptionBinding
    lateinit var imageAdaptor: ImageSliderAdaptor
    var data:ArrayList<descriptionImage>  = ArrayList()
    var demo : ArrayList<FavProductDocs> = ArrayList()
    var ProductId = ""
    var flag = ""
    var isIntrested = false
    lateinit var socketInstance: SocketManager
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val viewModel : ViewProductViewModel by viewModels()
    private val viewModelLocation : GoogleLocationApiViewModel by viewModels()
    var startLat = 0.0
    var startLong = 0.0

    var receiverId = ""
    var petPic = ""
    var profilePic = ""
    var name = ""
    var imageProduct = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        fusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }

        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)
        getLocation()

        intent.getStringExtra("id")?.let {
            ProductId = it
        }
        intent.getStringExtra("flag")?.let {
            flag = it
        }

        intent.getBooleanExtra("type",false).let{
            isIntrested =  it
        }



        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }

        val token =  SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()

        viewModel.productDescriptionApi(token,ProductId)



        if (flag == "Market"){

            if (isIntrested){
                binding.BuyNow.isVisible = true
                binding.interested.isVisible = false
            }else{
                binding.BuyNow.isVisible = false
                binding.interested.isVisible = true
            }

        }else if (flag == "Interested"){
            if (!isIntrested){
                binding.BuyNow.isVisible = true
                binding.interested.isVisible = false
            }else{
                binding.BuyNow.isVisible = false
                binding.interested.isVisible = true
            }

        }else{
            if (isIntrested){
                binding.BuyNow.isVisible = true
                binding.interested.isVisible = false
            }else{
                binding.BuyNow.isVisible = false
                binding.interested.isVisible = true
            }
        }

        binding.interested.setOnClickListener {
            viewModel.addToInterestedApi(token,"PRODUCT","",ProductId,"")
            binding.interested.isVisible = false
            binding.BuyNow.isVisible = true
        }




        binding.BuyNow.setOnClickListener {
            val intent = Intent(this, OneToOneChatActivity::class.java)
            intent.putExtra("receiverId", receiverId)
            intent.putExtra("petImage", petPic)
            intent.putExtra("userImage", profilePic)
            intent.putExtra("userName", name)
            intent.putExtra("sendImage", imageProduct)
            startActivity(intent)
        }


        binding.shareClick.setSafeOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND_MULTIPLE
                putExtra(Intent.EXTRA_TEXT, "$imageProduct \n https://calisdiary.com/")
                type = "text/plain"
            }

            val imageUri = Uri.parse(imageProduct)
            val webUri = Uri.parse("https://calisdiary.com/")

            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(imageUri, webUri))

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }




        binding.locationClick.setOnClickListener {
            openMap(startLong.toString(),startLat.toString())
        }




        observeResponseServiceDetails()
        observeAddToInterestedResponse()

        ObserveLatLngResponse()
    }


    @SuppressLint("SetTextI18n")
    private fun observeResponseServiceDetails() {

        lifecycleScope.launch {
            viewModel._productDescriptionData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        Progresss.stop()

                        if(response.data?.responseCode == 200) {
                            try {
                                binding.MainLayout.visibility = View.VISIBLE
                                binding.NotFound.visibility = View.GONE


                                receiverId  = response.data.result.userId._id.toString()
                                petPic  = response.data.result.userId.petPic.toString()
                                profilePic  = response.data.result.userId.profilePic.toString()
                                name  = response.data.result.userId.name.toString()
                                startLat = response.data.result.lat.toDouble()
                                startLong = response.data.result.long.toDouble()

                                if (response.data.result.productName == null){
                                    binding.ProductName.text =  getString(R.string.na)
                                }else{
                                    binding.ProductName.text = response.data.result.productName
                                }

                                if (response.data.result.userId.country == null && response.data.result.userId.zipCode == null && response.data.result.userId.city == null&& response.data.result.userId.address == null){
                                    binding.Address.text =  getString(R.string.na)
                                }else{
                                    binding.Address.text = "${response.data.result.userId.address} ${response.data.result.userId.city} ${response.data.result.userId.zipCode} ${response.data.result.userId.country}"
                                }

                                if (response.data.result.price == null){
                                    binding.Price.text =  getString(R.string.na)
                                }else{
                                    binding.Price.text ="${getString(R.string.price)}: ${response.data.result.price} ${response.data.result.currency}"
                                }

                                if (response.data.result.weight == null){
                                    binding.weight.text =  getString(R.string.na)
                                }else{
                                    binding.weight.text = "${response.data.result.weight} KG"
                                }

                                if (response.data.result.userId.name == null){
                                    binding.Name.text =  getString(R.string.na)
                                }else{
                                    binding.Name.text = response.data.result.userId.name
                                }

                                if (response.data.result.description == null){
                                    binding.Description.text =  getString(R.string.na)
                                }else{
                                    binding.Description.text = response.data.result.description.toString()
                                }


                                binding.indicator.isVisible = response.data.result.productImage.size >1


                                if(response.data.result.productImage.size > 0){
                                    imageProduct = response.data.result.productImage[0]
                                }


                                setImageAdaptor(response.data.result.productImage)

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        binding.MainLayout.visibility = View.GONE
                        binding.NotFound.visibility = View.VISIBLE
                        response.message?.let { message ->

                            androidExtension.alertBox(message,this@ProductDescriptionActivity)
                        }

                    }

                    is Resource.Loading -> {
                        binding.MainLayout.visibility = View.GONE
                        Progresss.start(this@ProductDescriptionActivity)
                    }

                    is Resource.Empty -> {
                        binding.MainLayout.visibility = View.GONE
                        Progresss.stop()
                    }

                }

            }
        }
    }



    fun setImageAdaptor(productName: ArrayList<String>) {

        imageAdaptor = ImageSliderAdaptor(this,productName)
        binding.storeViewpager.adapter = imageAdaptor
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    private fun observeAddToInterestedResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addToInterestedData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                } catch (e: Exception) {
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
            val label = binding.Address.text.toString() // Destination label

            val uri = "geo:$latitude,$longitude?q=$latitude,$longitude Products Location"
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


}