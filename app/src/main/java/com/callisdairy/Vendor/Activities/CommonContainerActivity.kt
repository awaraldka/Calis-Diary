package com.callisdairy.Vendor.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.callisdairy.ModalClass.feedbackDetails
import com.callisdairy.R
import com.callisdairy.Socket.ChatHistoryResult
import com.callisdairy.Socket.CheckOnlineUserResult
import com.callisdairy.Socket.MessagesChat
import com.callisdairy.Socket.OnlineUserResult
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Socket.UserTypingResult
import com.callisdairy.Socket.chatDataResult
import com.callisdairy.UI.Fragments.RequestedListFragment
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Fragmnets.AddProductFragment
import com.callisdairy.Vendor.Fragmnets.AddServiceFragment
import com.callisdairy.Vendor.Fragmnets.EditVendorProfileFragment
import com.callisdairy.Vendor.Fragmnets.InterestedUserListFragment
import com.callisdairy.Vendor.Fragmnets.IntrestedClientInFragment
import com.callisdairy.Vendor.Fragmnets.NotificationVendorFragment
import com.callisdairy.Vendor.Fragmnets.ViewProductFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.AppointmentFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.BlockUnblockClientFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.EditDoctorProfileFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.FeedBackListFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.MyFriendListFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.ViewAppointmentDetailsFragment
import com.callisdairy.Vendor.Fragmnets.doctorRole.ViewFeedbackFragment
import com.callisdairy.databinding.ActivityCommonContinerBinding
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommonContinerBinding


    var flag = ""
    var productId = ""
    var from = ""
    var userId = ""
    var isScreen = ""
    lateinit var socketInstance: SocketManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonContinerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        socketInstance = SocketManager.getInstance(this)


        userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        if (!socketInstance.isConnected) {
            socketInstance.connect()
        }

        socketInstance.onlineUser(userId)







        intent.getStringExtra("flag")?.let {
            flag = it
        }
        intent.getStringExtra("id")?.let {
            productId = it
        }
        intent.getStringExtra("from")?.let {
            from = it
        }
        intent.getStringExtra("isScreen")?.let {
            isScreen = it
        }
        replaceFragments()

    }

    @SuppressLint("SetTextI18n")
    private fun replaceFragments() {
        when (flag) {

            "viewProduct" -> {

                val bundle = Bundle()
                bundle.putString("productId",productId)
                bundle.putString("from",from)
                val obj  = ViewProductFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj).addToBackStack("ViewProductFragment").commit()
            }

            "addProduct" , "editProduct" -> {


                val bundle = Bundle()
                bundle.putString("from",flag)
                bundle.putString("productId",productId)
                val obj  = AddProductFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj).addToBackStack("AddProductFragment").commit()
            }

            "addService", "editService" -> {
                binding.titleVendor.text = getString(R.string.add_service)

                val bundle = Bundle()
                bundle.putString("from",from)
                bundle.putString("serviceId",productId)
                val obj  = AddServiceFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj).addToBackStack("AddServiceFragment").commit()
            }

            "viewInterestedUser" -> {
                val bundle = Bundle()
                bundle.putString("from",from)
                bundle.putString("productId",productId)
                val obj  = InterestedUserListFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj).addToBackStack("InterestedUserListFragment").commit()
            }

            "viewInterested" -> {
                val bundle = Bundle()
                bundle.putString("from",from)
                bundle.putString("productId",productId)
                val obj  = IntrestedClientInFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj).addToBackStack("InterestedUserListFragment").commit()
            }

            "notification" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, NotificationVendorFragment()).addToBackStack(null)
                    .commit()

              }



            "EditDoctorProfileFragment" -> {
                binding.titleVendor.text = getString(R.string.edit_profile)

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    EditDoctorProfileFragment()
                ).addToBackStack("EditDoctorProfileFragment").commit()

            }


            "Appointment View" -> {
                binding.titleVendor.text = getString(R.string.appointment_details)

                val bundle = Bundle()
                bundle.putString("id",productId)
                bundle.putString("from",from)
                val obj = ViewAppointmentDetailsFragment()
                obj.arguments = bundle


                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    obj
                ).addToBackStack("ViewAppointmentDetailsFragment").commit()

            }



            "Appointment History" -> {
                binding.titleVendor.text = getString(R.string.appointment_history)

                val bundle = Bundle()
                bundle.putString("from","")
                bundle.putString("isScreen","Appointment History")
                val obj = AppointmentFragment()
                obj.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    obj
                ).addToBackStack("AppointmentFragment").commit()
            }


            "Block User" -> {
                binding.titleVendor.text = getString(R.string.blocked_users)

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    BlockUnblockClientFragment()
                ).addToBackStack("BlockUnBlockDoctorFragment").commit()

            }


            "Feedback List" -> {
                binding.titleVendor.text = getString(R.string.feedbacks)

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    FeedBackListFragment()
                ).addToBackStack("FeedBackListFragment").commit()

            }


            "Friend Requests" -> {
                binding.titleVendor.text = getString(R.string.friends)

                val bundle = Bundle()
                bundle.putSerializable("from","vendor")
                val obj = RequestedListFragment()
                obj.arguments = bundle



                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj).addToBackStack("RequestedListFragment").commit()

            }


            "MyFriend List" -> {
                binding.titleVendor.text = getString(R.string.my_friends)


                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    MyFriendListFragment()
                ).addToBackStack("MyFriendListFragment").commit()

            }


            "Feedback details" -> {
                binding.titleVendor.text = getString(R.string.feedbacks_details)
                val myObject = intent.getSerializableExtra("myObject") as? feedbackDetails

                val bundle = Bundle()
                bundle.putSerializable("myObject",myObject)
                val obj = ViewFeedbackFragment()
                 obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    obj).addToBackStack("ViewFeedbackFragment").commit()

            }

            else -> {
                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,EditVendorProfileFragment()).addToBackStack("InterestedUserListFragment").commit()

            }


        }
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

}