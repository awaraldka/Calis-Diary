package com.callisdairy.UI.Activities.subscription

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.callisdairy.Interface.PaymentDoneListener
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddEventActivity
import com.callisdairy.UI.Activities.AddMissingPetActivity
import com.callisdairy.UI.Activities.AddPetActivity
import com.callisdairy.UI.Activities.AddPetProfileActivity
import com.callisdairy.UI.Fragments.autoPlayVideo.toast
import com.callisdairy.Utils.LocationClass
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.databinding.ActivityCardDetailsBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.callisdairy.viewModel.SubscriptionViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.stripe.model.Token
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class CardDetailsActivity : AppCompatActivity(), PaymentDoneListener {

    private lateinit var binding:ActivityCardDetailsBinding


    var token = ""
    var planId = ""
    var currency = ""
    var screen = ""

    var addressLine1 = ""
    var addressLine2 = ""
    var city = ""
    var state = ""
    var zipCode = ""
    var country = ""
    var totalCost = "9.99"

    private val viewModel: SubscriptionViewModel by viewModels()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    companion object {
        var name: String = ""
        var card_number: String = ""
        var month: String = ""
        var year: String = ""
        var cvc: String = ""
        var tokenData: String = ""
    }

    private val viewModelLocation: GoogleLocationApiViewModel by viewModels()

    private var startLat = 0.0
    private var startLong = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        fusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }


        intent.getStringExtra("id")?.let { planId = it }
        intent.getStringExtra("screen")?.let { screen = it }
        intent.getStringExtra("totalCost")?.let { totalCost = it }

        token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()

        lifecycleScope.launch {
            currency = LocationClass.getLocationBaseCurrency(fusedLocationClient,this@CardDetailsActivity)
            currency = getBaseCurrency(currency)
        }


        LocationClass.getLocation(this, fusedLocationClient!!) { locationData ->
            if (locationData != null) {
                startLat = locationData.latitude
                startLong = locationData.longitude

                val locationName = "$startLat,$startLong"

                toast(locationName)
                getLatLong()
//                viewModelLocation.getLatLngPayment(locationName, SavedPrefManager.getStringPreferences(this@CardDetailsActivity,SavedPrefManager.GMKEY).toString())


            }
        }




        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationProvider: String = LocationManager.NETWORK_PROVIDER

//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return
//        }
//        locationManager.requestSingleUpdate(locationProvider, object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                val geocoder = Geocoder(this@CardDetailsActivity, Locale.getDefault())
//                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//
//                if (addresses!!.isNotEmpty()) {
//                    val address = addresses[0]
//
//                    addressLine1 = address.getAddressLine(0) ?: ""
//                    addressLine2 = address.getAddressLine(1) ?: addressLine1
//                    city = address.locality ?: ""
//                    state = address.adminArea ?: ""
//                    zipCode = address.postalCode ?: ""
//                    val defaultLocale = Locale.getDefault()
//                    country = defaultLocale.displayCountry ?: ""
//
//                    Log.d("Result:- ", addresses.toString())
//                }
//            }
//
//            @Deprecated("Deprecated in Java")
//            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//            override fun onProviderEnabled(provider: String) {}
//            override fun onProviderDisabled(provider: String) {}
//        }, null)

        binding.PaymentButton.setSafeOnClickListener {
            if (checkValidation()) {
                Progresss.start(this@CardDetailsActivity)
                lifecycleScope.launch {
                    lifecycleScope.launch(Dispatchers.IO) {
                        name = binding.etNameCard.text.toString()
                        card_number = binding.etCardN.text.split("-").toString()
                        val month = binding.etMM.text.toString().trim()
                        val year = binding.etYY.text.toString().trim()
                        cvc = binding.etCvc.text.toString().trim()
                        com.stripe.Stripe.apiKey =
                            "pk_test_51JaDndSIyZlIaC71hWySgs0x57n6EGTWFfXyCvgemPNzJuOaQI9siqD0KdFYIIkYEgLaD4T77wMxV3d5JLSzCBiG003H9pQwU7"
                        try {
                            val card: MutableMap<String, Any> = HashMap()
                            card["name"] = name
                            card["number"] = card_number.replace("[", "").replace("]", "").replace(",", "")
                            card["exp_month"] = month
                            card["exp_year"] = year
                            card["cvc"] = cvc
                            card["address_line1"] = addressLine1
                            card["address_line2"] = addressLine2
                            card["address_city"] = city
                            card["address_state"] = state
                            card["address_zip"] = zipCode
                            card["address_country"] = country
                            val params: MutableMap<String, Any> = HashMap()
                            params["card"] = card

                            val stripeToken: Token = Token.create(params)
                            tokenData = stripeToken.id
                            Log.d("Result:- ", stripeToken.toString())


                            viewModel.paymentApi(token = token, stripeToken = tokenData, totalCost = totalCost, curr = currency,plan_id = planId)

                        } catch (e: Exception) {
                            Progresss.stop()
                            e.printStackTrace()
                            runOnUiThread {
                                androidExtension.alertBox(e.message.toString(),this@CardDetailsActivity)

                            }
                        }
                    }
                }
            }
        }


        makePaymentApi()

    }

    private fun checkValidation(): Boolean {
        name = binding.etNameCard.text.toString()
        card_number = binding.etCardN.text.split("-").toString().replace("[", "").replace("]", "")
        val month = binding.etMM.text.toString().trim()
        val year = binding.etYY.text.toString().trim()
        cvc = binding.etCvc.text.toString().trim()
         if (card_number.isEmpty() && card_number == "") {
            binding.etCardN.error = "Please Enter card number"
            return false
        } else if (month.isEmpty()) {
            binding.etMM.error = "Please enter expiry month"
            return false
        } else if (year.isEmpty()) {
            binding.etYY.error = "Please enter expiry year"
            return false
        } else if (cvc.isEmpty()) {
            binding.etCvc.error = "Please enter cvc number"
            return false
        } else if (name.isEmpty()) {
            binding.etNameCard.error = "Please Enter User Name"
            return false
        }
        return true
    }





    private fun makePaymentApi() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._subscriptionData.collect { response ->

                    when (response) {
                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data!!.responseCode == 200) {
                                try{
                                    supportFragmentManager.let { DialogBoxPayment(this@CardDetailsActivity).show(it, "MyCustomFragment") }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
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


    private fun getBaseCurrency(currencyCode: String): String {
        val countryBaseCurrency = hashSetOf("GBP", "USD", "EUR", "CNY", "RUB", "EUR", "INR", "VND", "TRY", "PLN", "IDR", "RON", "PHP")

        return if (countryBaseCurrency.contains(currencyCode)) {
            currencyCode
        } else {
            "USD"
        }
    }

    override fun paymentDone() {
        if (screen =="Event"){
            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)
        }

        if (screen =="Missing"){
            val intent = Intent(this, AddMissingPetActivity::class.java)
            intent.putExtra("flag", "Add")
            startActivity(intent)
        }

        if (screen =="Product"){
            val intent = Intent(this, CommonContainerActivity::class.java)
            intent.putExtra("flag", "addProduct")
            startActivity(intent)
        }

        if (screen =="Service"){
            val intent = Intent(this,CommonContainerActivity::class.java)
            intent.putExtra("flag","addService")
            intent.putExtra("from","add")
            startActivity(intent)
        }

        if (screen =="Pet"){
            val intent = Intent(this, AddPetActivity::class.java)
            intent.putExtra("flag", "Add Pet")
            startActivity(intent)
        }

        if (screen =="Event Vendor"){
            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)
        }

        if (screen =="Pet Profile"){
            val intent = Intent(this, AddPetProfileActivity::class.java)
            intent.putExtra("from","AddPetProfileActivity")
            startActivity(intent)
        }





        finishAfterTransition()
    }

    override fun failedPayment() {

    }

    override fun cancelPayment() {

    }


    private fun observeLocation() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelLocation._latLngStatePaymentFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {
                                Log.d("tag","response.toString() => ${response.toString()}")
                                var address = ArrayList<String>()
                                var countryText = ""
                                var stateText = ""
                                var cityName = ""
                                var zipcodeText = ""

                                var objectData = JSONObject(response.toString())
                                val jsonArrayData: JSONArray = objectData.getJSONArray("results")

                                if (jsonArrayData.length() > 0) {
                                    val data = jsonArrayData[0] as JSONObject
                                    val address_components = data.getJSONArray("address_components")
                                    for (i in 0 until address_components.length()) {
                                        val jsonObject = address_components[i] as JSONObject
                                        val types = jsonObject.getJSONArray("types")
                                        for (j in 0 until types.length()) {
                                            if (types[j] == "route") {
                                                address.add(jsonObject.getString("long_name"))
                                            }
                                            if (types[j] == "sublocality_level_3") {
                                                address.add(jsonObject.getString("long_name"))
                                            }
                                            if (types[j] == "sublocality_level_2") {
                                                address.add(jsonObject.getString("long_name"))
                                            }
                                            if (types[j] == "sublocality_level_1") {
                                                address.add(jsonObject.getString("long_name"))
                                            }
                                            if (types[j] == "country") {
                                                countryText = jsonObject.getString("long_name")
                                            }
                                            if (types[j] == "administrative_area_level_1") {
                                                stateText= jsonObject.getString("long_name")
                                            }
                                            if (types[j] == "administrative_area_level_3") {
                                                cityName = jsonObject.getString("long_name")
                                            }
                                            if (types[j] == "postal_code") {
                                                zipcodeText = jsonObject.getString("long_name")
                                            }
                                        }
                                    }

                                    addressLine1 = address.joinToString(",")
                                    addressLine2 = address.joinToString(",")
                                    country = countryText
                                    state = stateText
                                    city = cityName
                                    zipCode = zipcodeText


                                }

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

    private fun getLatLong() {
        val sb = StringBuffer()
        sb.append(startLat)
        sb.append(",")
        sb.append(startLong)
        val url = ("https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + sb.toString() + "&key=${SavedPrefManager.getStringPreferences(this@CardDetailsActivity,SavedPrefManager.GMKEY)}")
        val queue = Volley.newRequestQueue(this)
        val stateReq =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    var location: JSONObject
                    try {
                        Log.d("tag","response.toString() => ${response.toString()}")
                        var address = ArrayList<String>()
                        var countryText = ""
                        var stateText = ""
                        var cityName = ""
                        var zipcodeText = ""

                        var objectData = JSONObject(response.toString())
                        val jsonArrayData: JSONArray = objectData.getJSONArray("results")

                        if (jsonArrayData.length() > 0) {
                            val data = jsonArrayData[0] as JSONObject
                            val address_components = data.getJSONArray("address_components")
                            for (i in 0 until address_components.length()) {
                                val jsonObject = address_components[i] as JSONObject
                                val types = jsonObject.getJSONArray("types")
                                for (j in 0 until types.length()) {
                                    if (types[j] == "route") {
                                        address.add(jsonObject.getString("long_name"))
                                    }
                                    if (types[j] == "sublocality_level_3") {
                                        address.add(jsonObject.getString("long_name"))
                                    }
                                    if (types[j] == "sublocality_level_2") {
                                        address.add(jsonObject.getString("long_name"))
                                    }
                                    if (types[j] == "sublocality_level_1") {
                                        address.add(jsonObject.getString("long_name"))
                                    }
                                    if (types[j] == "country") {
                                        countryText = jsonObject.getString("long_name")
                                    }
                                    if (types[j] == "administrative_area_level_1") {
                                        stateText= jsonObject.getString("long_name")
                                    }
                                    if (types[j] == "administrative_area_level_3") {
                                        cityName = jsonObject.getString("long_name")
                                    }
                                    if (types[j] == "postal_code") {
                                        zipcodeText = jsonObject.getString("long_name")
                                    }
                                }
                            }

                            addressLine1 = address.joinToString(",")
                            addressLine2 = address.joinToString(",")
                            country = countryText
                            state = stateText
                            city = cityName
                            zipCode = zipcodeText


                        }

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }
            ) { error -> Log.d("Error.Response", error.toString()) }

        queue.add(stateReq)


    }





}