package com.callisdairy.Vendor.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.Interface.AddListenerVendor
import com.callisdairy.Interface.RenewSubscription
import com.callisdairy.R
import com.callisdairy.Socket.*
import com.callisdairy.UI.Activities.AddPetActivity
import com.callisdairy.UI.Activities.ChatsActivity
import com.callisdairy.UI.Activities.subscription.DialogBoxAlertRenew
import com.callisdairy.UI.Activities.subscription.DialogBoxExpiredRenew
import com.callisdairy.UI.Activities.subscription.DialogBoxTrial
import com.callisdairy.UI.Activities.subscription.SubscriptionManageActivity
import com.callisdairy.UI.dialogs.VendorAddPosts
import com.callisdairy.Utils.CommonConverter
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Fragmnets.*
import com.callisdairy.Vendor.Fragmnets.doctorRole.AppointmentFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.DoctorMenuFragment
import com.callisdairy.api.Constants
import com.callisdairy.api.response.KeysData
import com.callisdairy.databinding.ActivityVendorContainerBinding
import com.callisdairy.viewModel.LoginViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class VendorContainerActivity : AppCompatActivity(), View.OnClickListener, AddListenerVendor,
    RenewSubscription {
    private lateinit var binding: ActivityVendorContainerBinding

    lateinit var HomeBottomTab: RelativeLayout
    lateinit var productTabs: RelativeLayout
    lateinit var DoctorTab: RelativeLayout
    lateinit var petsTab: RelativeLayout
    lateinit var serviceTab: RelativeLayout
    lateinit var settingsTab: RelativeLayout
    lateinit var settingsTabDoctor: RelativeLayout
    private var mLastClickTime: Long = 0
    lateinit var socketInstance: SocketManager
    var token = ""

    lateinit var homeTv: TextView
    lateinit var productTv: TextView
    lateinit var petTv: TextView
    lateinit var serviceTv: TextView
    lateinit var settingsTv: TextView
    lateinit var settingsTvDoctor: TextView
    lateinit var DocotorTv: TextView


    lateinit var settingsView: View
    lateinit var DoctorView: View
    lateinit var serviceView: View
    lateinit var petsView: View
    lateinit var productView: View
    lateinit var Homeview: View
    lateinit var settingsViewDoctor: View

    var userId = ""
    var userRole = ""

    lateinit var ChatClick: ImageView
    lateinit var addVendors: ImageView
    lateinit var NotificationVendor: ImageView

    private val viewModel: LoginViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        findIdS()
        RequestPermission.requestMultiplePermissions(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.vendorContainer, VendorHomeFragment()).commit()
        HomeBottomTab.isEnabled = false
        socketInstance = SocketManager.getInstance(this)

        viewModel.appConfig()

        getAllApiKeys()


        userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        if (!socketInstance.isConnected) {
            socketInstance.connect()
        }
        socketInstance.onlineUser(userId)




        userRole =
            SavedPrefManager.getStringPreferences(this, SavedPrefManager.VendorRole).toString()

        val loginCount =
            SavedPrefManager.getStringPreferences(this, SavedPrefManager.LOGIN_COUNT).toString()
        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()

        if (loginCount == "0") {
            SavedPrefManager.saveStringPreferences(this, SavedPrefManager.LOGIN_COUNT,"1")
            supportFragmentManager.let { DialogBoxTrial().show(it, "MyCustomFragment") }
        }

        viewModel.getSubscriptionApi(token = token)
        getSubscriptionApi()


        when (userRole) {

            "VENDORPRODUCT" -> {
                productTabs.isVisible = true
                petsTab.isVisible = false
                serviceTab.isVisible = false
                DoctorTab.isVisible = false
                settingsTab.isVisible = true
                settingsTabDoctor.isVisible = false
                addVendors.isVisible = false
            }

            "VENDORPET" -> {
                petsTab.isVisible = true
                productTabs.isVisible = false
                serviceTab.isVisible = false
                DoctorTab.isVisible = false
                settingsTab.isVisible = true
                settingsTabDoctor.isVisible = false
                addVendors.isVisible = false
            }

            "VENDORSERVICE" -> {
                serviceTab.isVisible = true
                petsTab.isVisible = false
                productTabs.isVisible = false
                DoctorTab.isVisible = false
                settingsTab.isVisible = true
                addVendors.isVisible = false
                settingsTabDoctor.isVisible = false
            }

            "VENDORDOCTORVET" -> {
                serviceTab.isVisible = false
                petsTab.isVisible = false
                productTabs.isVisible = false
                DoctorTab.isVisible = true
                settingsTab.isVisible = false
                ChatClick.isVisible = true
                addVendors.isVisible = false
                settingsTabDoctor.isVisible = true
            }

        }








        clicks()


    }

    private fun clicks() {
        HomeBottomTab.setOnClickListener(this)
        productTabs.setOnClickListener(this)
        petsTab.setOnClickListener(this)
        serviceTab.setOnClickListener(this)
        settingsTab.setOnClickListener(this)
        ChatClick.setOnClickListener(this)
        addVendors.setOnClickListener(this)
        NotificationVendor.setOnClickListener(this)
        settingsTabDoctor.setOnClickListener(this)
        DoctorTab.setOnClickListener(this)
    }

    private fun findIdS() {
        NotificationVendor = findViewById(R.id.NotificationVendor)

        addVendors = findViewById(R.id.addVendors)
        HomeBottomTab = findViewById(R.id.HomeBottomTab)
        productTabs = findViewById(R.id.productTabs)
        petsTab = findViewById(R.id.petsTab)
        serviceTab = findViewById(R.id.serviceTab)
        DoctorTab = findViewById(R.id.DoctorTab)
        settingsTab = findViewById(R.id.settingsTab)
        settingsTabDoctor = findViewById(R.id.settingsTabDoctor)

        homeTv = findViewById(R.id.homeTv)
        productTv = findViewById(R.id.productTv)
        petTv = findViewById(R.id.petTv)
        serviceTv = findViewById(R.id.serviceTv)
        settingsTv = findViewById(R.id.settingsTv)
        settingsTvDoctor = findViewById(R.id.settingsTvDoctor)
        DocotorTv = findViewById(R.id.DocotorTv)

        DoctorView = findViewById(R.id.DoctorView)
        settingsView = findViewById(R.id.settingsView)
        serviceView = findViewById(R.id.serviceView)
        petsView = findViewById(R.id.petsView)
        productView = findViewById(R.id.productView)
        Homeview = findViewById(R.id.Homeview)
        settingsViewDoctor = findViewById(R.id.settingsViewDoctor)



        ChatClick = findViewById(R.id.ChatClick)
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()


        when (v?.id) {

            R.id.HomeBottomTab -> {
                viewModel.getSubscriptionApi(token = token)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.vendorContainer, VendorHomeFragment()).commit()
                HomeBottomTab.isEnabled = false

                homeTv.setTextColor(Color.parseColor("#6FCFB9"))
                productTv.setTextColor(Color.parseColor("#757575"))
                petTv.setTextColor(Color.parseColor("#757575"))
                serviceTv.setTextColor(Color.parseColor("#757575"))
                settingsTv.setTextColor(Color.parseColor("#757575"))
                settingsTvDoctor.setTextColor(Color.parseColor("#757575"))
                DocotorTv.setTextColor(Color.parseColor("#757575"))

                Homeview.isVisible = true
                productView.isVisible = false
                petsView.isVisible = false
                serviceView.isVisible = false
                settingsView.isVisible = false
                DoctorView.isVisible = false
                settingsViewDoctor.isVisible = false

            }

            R.id.productTabs -> {
                HomeBottomTab.isEnabled = true

                val bundle = Bundle()
                bundle.putString("product", "Total Products")
                val obj = VendorProductFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.vendorContainer, obj)
                    .addToBackStack("VendorProductFragment").commit()

                homeTv.setTextColor(Color.parseColor("#757575"))
                productTv.setTextColor(Color.parseColor("#6FCFB9"))
                petTv.setTextColor(Color.parseColor("#757575"))
                serviceTv.setTextColor(Color.parseColor("#757575"))
                settingsTv.setTextColor(Color.parseColor("#757575"))
                settingsTvDoctor.setTextColor(Color.parseColor("#757575"))
                DocotorTv.setTextColor(Color.parseColor("#757575"))

                Homeview.isVisible = false
                productView.isVisible = true
                petsView.isVisible = false
                serviceView.isVisible = false
                settingsView.isVisible = false
                settingsViewDoctor.isVisible = false
                DoctorView.isVisible = false
            }

            R.id.petsTab -> {
                HomeBottomTab.isEnabled = true
                supportFragmentManager.beginTransaction()
                    .replace(R.id.vendorContainer, VendorPetListFragment())
                    .addToBackStack("VendorProductFragment").commit()

                homeTv.setTextColor(Color.parseColor("#757575"))
                productTv.setTextColor(Color.parseColor("#757575"))
                petTv.setTextColor(Color.parseColor("#6FCFB9"))
                serviceTv.setTextColor(Color.parseColor("#757575"))
                settingsTv.setTextColor(Color.parseColor("#757575"))
                settingsTvDoctor.setTextColor(Color.parseColor("#757575"))
                DocotorTv.setTextColor(Color.parseColor("#757575"))

                Homeview.isVisible = false
                productView.isVisible = false
                petsView.isVisible = true
                serviceView.isVisible = false
                settingsView.isVisible = false
                settingsViewDoctor.isVisible = false
                DoctorView.isVisible = false
            }

            R.id.serviceTab -> {
                HomeBottomTab.isEnabled = true

                val bundle = Bundle()
                bundle.putString("service", "Total Services")
                val obj = VendorServiceListFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.vendorContainer, obj)
                    .addToBackStack("VendorProductFragment").commit()

                homeTv.setTextColor(Color.parseColor("#757575"))
                productTv.setTextColor(Color.parseColor("#757575"))
                petTv.setTextColor(Color.parseColor("#757575"))
                serviceTv.setTextColor(Color.parseColor("#6FCFB9"))
                settingsTv.setTextColor(Color.parseColor("#757575"))
                settingsTvDoctor.setTextColor(Color.parseColor("#757575"))
                DocotorTv.setTextColor(Color.parseColor("#757575"))

                Homeview.isVisible = false
                productView.isVisible = false
                petsView.isVisible = false
                serviceView.isVisible = true
                settingsView.isVisible = false
                settingsViewDoctor.isVisible = false
                DoctorView.isVisible = false
            }

            R.id.DoctorTab -> {
                HomeBottomTab.isEnabled = true

                val bundle = Bundle()
                bundle.putString("from", "")
                bundle.putString("isScreen", "Tab")
                val obj = AppointmentFragment()
                obj.arguments = bundle
                supportFragmentManager.beginTransaction().replace(
                    R.id.vendorContainer,
                    obj
                ).addToBackStack("AppointmentFragment").commit()



                homeTv.setTextColor(Color.parseColor("#757575"))
                productTv.setTextColor(Color.parseColor("#757575"))
                petTv.setTextColor(Color.parseColor("#757575"))
                serviceTv.setTextColor(Color.parseColor("#757575"))
                DocotorTv.setTextColor(Color.parseColor("#6FCFB9"))
                settingsTv.setTextColor(Color.parseColor("#757575"))
                settingsTvDoctor.setTextColor(Color.parseColor("#757575"))

                Homeview.isVisible = false
                productView.isVisible = false
                petsView.isVisible = false
                serviceView.isVisible = false
                settingsView.isVisible = false
                settingsViewDoctor.isVisible = false
                DoctorView.isVisible = true
            }

            R.id.settingsTab -> {
                HomeBottomTab.isEnabled = true


                supportFragmentManager.beginTransaction()
                    .replace(R.id.vendorContainer, MenuVendorFragment())
                    .addToBackStack("MenuVendorFragment").commit()

                homeTv.setTextColor(Color.parseColor("#757575"))
                productTv.setTextColor(Color.parseColor("#757575"))
                petTv.setTextColor(Color.parseColor("#757575"))
                serviceTv.setTextColor(Color.parseColor("#757575"))
                settingsTvDoctor.setTextColor(Color.parseColor("#757575"))
                DocotorTv.setTextColor(Color.parseColor("#757575"))
                settingsTv.setTextColor(Color.parseColor("#6FCFB9"))

                Homeview.isVisible = false
                productView.isVisible = false
                petsView.isVisible = false
                serviceView.isVisible = false
                DoctorView.isVisible = false
                settingsViewDoctor.isVisible = false
                settingsView.isVisible = true
            }

            R.id.settingsTabDoctor -> {
                HomeBottomTab.isEnabled = true


                supportFragmentManager.beginTransaction()
                    .replace(R.id.vendorContainer, DoctorMenuFragment())
                    .addToBackStack("DoctorMenuFragment").commit()

                homeTv.setTextColor(Color.parseColor("#757575"))
                productTv.setTextColor(Color.parseColor("#757575"))
                petTv.setTextColor(Color.parseColor("#757575"))
                serviceTv.setTextColor(Color.parseColor("#757575"))
                settingsTv.setTextColor(Color.parseColor("#757575"))
                DocotorTv.setTextColor(Color.parseColor("#757575"))
                settingsTvDoctor.setTextColor(Color.parseColor("#6FCFB9"))

                Homeview.isVisible = false
                productView.isVisible = false
                petsView.isVisible = false
                serviceView.isVisible = false
                DoctorView.isVisible = false
                settingsView.isVisible = false
                settingsViewDoctor.isVisible = true
            }

            R.id.ChatClick -> {
                val intent = Intent(this, ChatsActivity::class.java)
                startActivity(intent)
            }


            R.id.addVendors -> {

                supportFragmentManager.let { it1 ->
                    VendorAddPosts(this).show(
                        it1, "Follow Bottom Sheet Dialog Fragment"
                    )
                }
            }


            R.id.NotificationVendor -> {


                val intent = Intent(this, CommonContainerActivity::class.java)
                intent.putExtra("flag", "notification")
                startActivity(intent)


            }


        }
    }

    override fun pet() {
        val intent = Intent(this, AddPetActivity::class.java)
        intent.putExtra("flag", "Add Pet")
        startActivity(intent)
    }

    override fun product() {
        val intent = Intent(this, CommonContainerActivity::class.java)
        intent.putExtra("flag", "addProduct")
        startActivity(intent)
    }

    override fun service() {
        val intent = Intent(this, CommonContainerActivity::class.java)
        intent.putExtra("flag", "addService")
        intent.putExtra("from", "add")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        socketInstance.onlineUser(userId)
        onAddListeners()
    }


    private fun onAddListeners() {
        socketInstance.initialize(object : SocketManager.SocketListener {
            override fun onConnected() {
                Log.e("browse_page_err", "omd " + "onConnected")

            }

            override fun onDisConnected() {
                socketInstance.connect()
            }

            override fun chatHistroy(listdat: ArrayList<ChatHistoryResult>) {

            }


            override fun chatListData(listdat: ArrayList<chatDataResult>) {

            }

            override fun viewchat(listdat: ArrayList<MessagesChat>) {

            }

            override fun oneToOneChat(listdat: ChatHistoryResult) {

            }

            override fun onlineUser(listdat: ArrayList<OnlineUserResult>) {

            }

            override fun offlineUser(listdat: CheckOnlineUserResult) {

            }

            override fun checkOnlineUser(listdat: CheckOnlineUserResult) {

            }

            override fun typing(listdat: JsonObject) {

            }

            override fun typingUser(listdat: UserTypingResult) {

            }


        })
    }


    override fun onPause() {
        super.onPause()
        socketInstance.offlineUser(userId)
    }


    private fun getAllApiKeys() {

        lifecycleScope.launch {
            viewModel._appConfigData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        if (response.data!!.responseCode == 200) {
                            try {
                                val decodeKeyJsonString =
                                    CommonConverter.dynamicSaltResponseConverter(
                                        this@VendorContainerActivity,
                                        response.data.result
                                    )
                                val decodeKey = JSONObject(decodeKeyJsonString!!)

                                val keysData = KeysData(
                                    gMapKey = decodeKey.getString("G_MAP_KEY"),
                                    ipApiKey = decodeKey.getString("IP_API_Key"),
                                    appId = decodeKey.getString("appId"),
                                    appCertificate = decodeKey.getString("appCertificate"),
                                    customerKey = decodeKey.getString("customerKey"),
                                    customerSecret = decodeKey.getString("customerSecret")
                                )

                                Constants.initializeKeys(keysData)

                                SavedPrefManager.saveStringPreferences(
                                    this@VendorContainerActivity, SavedPrefManager.GMKEY,
                                    Constants.getKeysData()!!.gMapKey
                                )
                                SavedPrefManager.saveStringPreferences(
                                    this@VendorContainerActivity, SavedPrefManager.AgoraAppId,
                                    Constants.getKeysData()!!.appId
                                )


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

    private fun getSubscriptionApi() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._getSubscriptionData.collect { response ->

                    when (response) {
                        is Resource.Success -> {
                            if (response.data!!.responseCode == 200) {
                                try {
                                    val days = SavedPrefManager.getStringPreferences(this@VendorContainerActivity, SavedPrefManager.DAYS).toString()
                                    if (days > response.data!!.result.daysLeft && response.data.result.daysLeft != "0") {
                                        SavedPrefManager.saveStringPreferences(this@VendorContainerActivity, SavedPrefManager.DAYS, response.data!!.result.daysLeft)
                                        supportFragmentManager.let { DialogBoxAlertRenew(this@VendorContainerActivity,response.data.result.daysLeft).show(it, "MyCustomFragment") }
                                    }


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

    override fun renewNow() {
        val intent = Intent(this, SubscriptionManageActivity::class.java)
        intent.putExtra("expired", false)
        startActivity(intent)
    }


}