package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.UI.Fragments.AccountPrivacyFragment
import com.callisdairy.UI.Fragments.BlockUnBlockDoctorFragment
import com.callisdairy.UI.Fragments.DoctorVetFragment
import com.callisdairy.UI.Fragments.ListedPetFragment
import com.callisdairy.UI.Fragments.MakeAppointmentFragment
import com.callisdairy.UI.Fragments.MarketProductCategoryFragment
import com.callisdairy.UI.Fragments.MissingPetDescriptionFragment
import com.callisdairy.UI.Fragments.MyEventListFragment
import com.callisdairy.UI.Fragments.PetDescriptionFragment
import com.callisdairy.UI.Fragments.ServiceCategoryMarketFragment
import com.callisdairy.UI.Fragments.ViewDoctorVetFragment
import com.callisdairy.UI.Fragments.ViewUserAppointmentHistoryFragment
import com.callisdairy.UI.Fragments.postview.PostViewFragment
import com.callisdairy.UI.Fragments.profile.FollowUnfollowFragment
import com.callisdairy.UI.Fragments.profile.UserProfileFragment
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Fragmnets.doctorRole.AppointmentHistoryFragment
import com.callisdairy.databinding.ActivityCommonForViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonActivityForViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommonForViewBinding

    var flag = ""
    var from = ""
    var petId = ""
    var allId = ""
    var userName = ""
    var userType = ""
    var following = ""
    var Followers = ""
    var typeUser = ""

    lateinit var socketInstance: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonForViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)

        intent.getStringExtra("flag")?.let {
            flag = it
        }
        intent.getStringExtra("from")?.let {
            from = it
        }

        intent.getStringExtra("petId")?.let {
            petId = it
        }

        intent.getStringExtra("id")?.let {
            allId = it
        }

        intent.getStringExtra("typeUser")?.let {
            typeUser = it
        }

        intent.getStringExtra("userName")?.let {
            userName = it
        }

        intent.getStringExtra("userType")?.let {
            userType = it
        }

        intent.getStringExtra("Followers")?.let {
            Followers = it
        }

        intent.getStringExtra("following")?.let {
            following = it
        }

        replaceFragments()


    }

    @SuppressLint("SetTextI18n")
    private fun replaceFragments() {
        when (flag) {

            "MarketProducts" -> {
                binding.Username.text = getString(R.string.products)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, MarketProductCategoryFragment()).addToBackStack(null)
                    .commit()
            }

            "Service" -> {
                binding.Username.text = getString(R.string.services)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, ServiceCategoryMarketFragment()).addToBackStack(null)
                    .commit()
            }

            "ListedPet" -> {

                binding.Username.text = getString(R.string.listed_pets)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, ListedPetFragment()).addToBackStack(null)
                    .commit()
            }

            "Doctor" -> {

                binding.Username.text = getString(R.string.vet_doctor)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, DoctorVetFragment()).addToBackStack(null)
                    .commit()
            }

            "viewDoctors" -> {

                binding.Username.text = getString(R.string.view_doctors)

                val bundle = Bundle()
                bundle.putString("id",allId)
                val obj = ViewDoctorVetFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, obj).addToBackStack(null)
                    .commit()
            }


            "MyEventList" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, MyEventListFragment()).addToBackStack(null)
                    .commit()
            }


            "Account" -> {
                binding.Username.text =  getString(R.string.account_privacy)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, AccountPrivacyFragment()).addToBackStack(null)
                    .commit()
            }


            "ViewMissingPet" -> {
                val bundle = Bundle()
                bundle.putString("flag", "")
                bundle.putString("petId", petId)
                bundle.putString("from", from)
                val obj = MissingPetDescriptionFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, obj).addToBackStack(null)
                    .commit()
            }


            "ViewMyPost" -> {
                val bundle = Bundle()
                bundle.putString("flag", "")
                bundle.putString("id", allId)
                val obj = PostViewFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, obj).addToBackStack(null)
                    .commit()
            }


            "ViewMyPet" -> {
                val bundle = Bundle()
                bundle.putString("flag", "")
                bundle.putString("id", allId)
                bundle.putString("typeUser", typeUser)
                val obj = PetDescriptionFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, obj).addToBackStack(null)
                    .commit()
            }


            "OtherUsers" -> {
                binding.Username.text =  userName


                val bundle = Bundle()
                bundle.putString("userName",userName)
                bundle.putString("id",allId)
                val obj = UserProfileFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj ).addToBackStack(null).commit()

            }

            "Following" -> {



                val bundle = Bundle()
                bundle.putString("flag", flag)
                bundle.putString("userType", userType)
                bundle.putString("userId",allId)
                bundle.putString("following",following)
                bundle.putString("Followers",Followers)

                val obj = FollowUnfollowFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj ).addToBackStack(null).commit()

            }

            "Followers" -> {

                val bundle = Bundle()
                bundle.putString("flag", flag)
                bundle.putString("userType", userType)
                bundle.putString("userId",allId)
                bundle.putString("following",following)
                bundle.putString("Followers",Followers)

                val obj = FollowUnfollowFragment()
                obj.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,obj ).addToBackStack(null).commit()

            }

            "Block UnBlock" -> {
                binding.Username.text = getString(R.string.blocked_users)
                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,BlockUnBlockDoctorFragment() ).addToBackStack("BlockUnBlockDoctorFragment").commit()

            }

            "Appointment History" -> {
                binding.Username.text = getString(R.string.appointment_history)
                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    AppointmentHistoryFragment() ).addToBackStack("AppointmentHistoryFragment").commit()

            }



            "Make Appointment" -> {
                binding.Username.text = getString(R.string.make_appointment)

                val bundle = Bundle()
                bundle.putString("vetId",allId)
                val obj = MakeAppointmentFragment()
                obj.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.CommonContainer, obj).addToBackStack(null)
                    .commit()
            }


            "View User AppointmentHistory Fragment" -> {
                binding.Username.text = getString(R.string.appointment_details)

                val bundle = Bundle()
                bundle.putString("id",allId)
                val obj = ViewUserAppointmentHistoryFragment()
                obj.arguments = bundle


                supportFragmentManager.beginTransaction().replace(R.id.CommonContainer,
                    obj
                ).addToBackStack("ViewUserAppointmentHistoryFragment").commit()

            }



            else -> {

            }


        }
    }






}