package com.callisdairy.Vendor.Fragmnets

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.callisdairy.AdapterVendors.VendorHomeAdapter
import com.callisdairy.Interface.VendorHomeClick
import com.callisdairy.ModalClass.VendorHomeModalClass
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.Vendor.Fragmnets.doctorRole.AppointmentFragment
import com.callisdairy.databinding.FragmentVendorHomeBinding
import com.callisdairy.viewModel.HomeViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorHomeFragment : Fragment(), VendorHomeClick {

    private var _binding: FragmentVendorHomeBinding? =  null
    private val binding get() = _binding!!

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
    lateinit var HomeBottomTab:RelativeLayout
    lateinit var addVendors:ImageView
    var token = ""



    lateinit var settingsViewDoctor:View
    lateinit var DoctorView:View
    lateinit var settingsTvDoctor:TextView
    lateinit var DocotorTv:TextView



    var loaderFlag = true
    var homeData = ArrayList<VendorHomeModalClass>()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentVendorHomeBinding.inflate(layoutInflater, container, false)
        findIdAndHandleTab()
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()

        if (homeData.isEmpty()){
            viewModel.vendorHomeApi(token)
        }



        observeResponseHomeList()
        return binding.root
    }




    private fun observeResponseHomeList() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._vendorHomeData.collectLatest{ response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data?.statusCode == 200) {
                                try {
                                    loaderFlag = false
                                    homeData.clear()
                                     val rejectedServices = response.data.result.totalService - (response.data.result.approveService + response.data.result.pendingService)


                                    val userRole = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.VendorRole).toString()

                                    with(response.data.result) {
                                        when (userRole) {
                                            "VENDORPRODUCT" -> {
                                                homeData.addAll(
                                                    listOf(
                                                        VendorHomeModalClass(getString(R.string.total_products), R.drawable.product, totalProduct),
                                                        VendorHomeModalClass(getString(R.string.approved_products), R.drawable.product, approveProduct),
                                                        VendorHomeModalClass(getString(R.string.pending_products), R.drawable.product, pendingProduct),
                                                        VendorHomeModalClass(getString(R.string.rejected_products), R.drawable.product, rejectProduct),
                                                        VendorHomeModalClass(getString(R.string.client_intrested_products), R.drawable.product, clientInterestedInProduct)
                                                    )
                                                )
                                            }
                                            "VENDORPET" -> {
                                                homeData.addAll(
                                                    listOf(
                                                        VendorHomeModalClass(getString(R.string.total_pets), R.drawable.pet_tab, totalPet),
                                                        VendorHomeModalClass(getString(R.string.published_pets), R.drawable.pet_tab, totalPublished),
                                                        VendorHomeModalClass(getString(R.string.unpublished_pets), R.drawable.pet_tab, totalUnpublished),
                                                        VendorHomeModalClass(getString(R.string.client_intrested_pets), R.drawable.pet_tab, clientInterestedInPet),
                                                    )
                                                )
                                            }
                                            "VENDORSERVICE" -> {
                                                homeData.addAll(
                                                    listOf(
                                                        VendorHomeModalClass(getString(R.string.total_services), R.drawable.service_vendor, totalService),
                                                        VendorHomeModalClass(getString(R.string.pending_services), R.drawable.service_vendor, pendingService),
                                                        VendorHomeModalClass(getString(R.string.rejected_services), R.drawable.service_vendor, rejectedServices),
                                                        VendorHomeModalClass(getString(R.string.approved_services), R.drawable.service_vendor, approveService),
                                                        VendorHomeModalClass(getString(R.string.client_intrested_services), R.drawable.service_vendor, clientInterestedInService)
                                                    )
                                                )
                                            }
                                            "VENDORDOCTORVET" -> {
                                                homeData.addAll(
                                                    listOf(
                                                        VendorHomeModalClass(getString(R.string.total_appoitments), R.drawable.service_vendor, totalAppointments),
                                                        VendorHomeModalClass(getString(R.string.confirmed_inperson), R.drawable.service_vendor, totalInpersonConfirmed),
                                                        VendorHomeModalClass(getString(R.string.completed_inperson), R.drawable.service_vendor, totalInperson),
                                                        VendorHomeModalClass(getString(R.string.confirmed_telehealth), R.drawable.service_vendor, totalTelehealthConfirmed),
//                                                        VendorHomeModalClass("Total TeleHealth", R.drawable.service_vendor, totalTelehealth),
//                                                        VendorHomeModalClass("Total InPerson", R.drawable.service_vendor, totalInperson),
                                                        VendorHomeModalClass(getString(R.string.completed_telehealth), R.drawable.service_vendor, totalTelehealth),
                                                    )
                                                )
                                            }

                                            else -> {}
                                        }
                                    }


                                    setAdapter()
                                }catch (e:Exception){
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
                            if (loaderFlag){
                                Progresss.start(requireContext())
                            }

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }


        }



    }

    private fun setAdapter() {
        binding.dashboardRecycler.layoutManager = GridLayoutManager(requireContext(),2)
        val adapter = VendorHomeAdapter(requireContext(),homeData,this)
        binding.dashboardRecycler.adapter = adapter
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
        HomeBottomTab = activity?.findViewById(R.id.HomeBottomTab)!!


        settingsViewDoctor = activity?.findViewById(R.id.settingsViewDoctor)!!
        settingsTvDoctor = activity?.findViewById(R.id.settingsTvDoctor)!!
        DoctorView = activity?.findViewById(R.id.DoctorView)!!
        DocotorTv = activity?.findViewById(R.id.DocotorTv)!!



        homeTv.setTextColor(Color.parseColor("#6FCFB9"))
        productTv.setTextColor(Color.parseColor("#757575"))
        petTv.setTextColor(Color.parseColor("#757575"))
        serviceTv.setTextColor(Color.parseColor("#757575"))
        settingsTv.setTextColor(Color.parseColor("#757575"))
        DocotorTv.setTextColor(Color.parseColor("#757575"))



        addVendors.isVisible = false
        Homeview.isVisible = true
        productView.isVisible = false
        petsView.isVisible = false
        serviceView.isVisible = false
        settingsView.isVisible = false
        settingsViewDoctor.isVisible = false
        DoctorView.isVisible = false
    }

    override fun viewDetails(type: String) {
        HomeBottomTab.isEnabled = true

        when (type) {
            getString(R.string.total_products), getString(R.string.approved_products), getString(R.string.pending_products), getString(R.string.rejected_products) -> {

                val typeRequest = when (type) {

                    getString(R.string.pending_products) -> {
                        "Pending Products"
                    }
                    getString(R.string.approved_products) -> {
                        "Approved Products"
                    }
                    getString(R.string.rejected_products) -> {
                        "Rejected Products"
                    }
                    else -> {
                        "Total Products"
                    }
                }

                val bundle = Bundle()
                bundle.putString("product",typeRequest)
                val obj = VendorProductFragment()
                obj.arguments = bundle

                parentFragmentManager.beginTransaction().replace(R.id.vendorContainer,obj).addToBackStack("VendorProductFragment").commit()


            }
            getString(R.string.total_services), getString(R.string.pending_services),getString(R.string.approved_services), getString(R.string.rejected_services) -> {
                val typeRequest = when (type) {

                    getString(R.string.pending_services) -> {
                        "Pending Services"
                    }
                    getString(R.string.approved_services) -> {
                        "Approved Services"
                    }
                    getString(R.string.rejected_services) -> {
                        "Rejected Services"
                    }
                    else -> {
                        "Total Services"
                    }
                }


                val bundle = Bundle()
                bundle.putString("service",typeRequest)
                val obj = VendorServiceListFragment()
                obj.arguments = bundle

                parentFragmentManager.beginTransaction().replace(R.id.vendorContainer, obj).addToBackStack("VendorServiceListFragment").commit()


            }
            getString(R.string.total_pets), getString(R.string.published_pets), getString(R.string.unpublished_pets) -> {

                val typeRequest = when (type) {
                    getString(R.string.published_pets) -> {
                        "published"
                    }
                    getString(R.string.unpublished_pets) -> {
                        "unpublished"
                    }
                    else -> {
                        ""
                    }
                }

                val bundle = Bundle()
                bundle.putString("petType",typeRequest)
                val obj = VendorPetListFragment()
                obj.arguments = bundle

                parentFragmentManager.beginTransaction().replace(R.id.vendorContainer, obj).addToBackStack("VendorPetListFragment").commit()


            }
            getString(R.string.client_intrested_products) -> {
                val intent = Intent(requireContext(), CommonContainerActivity::class.java)
                intent.putExtra("flag","viewInterested")
                intent.putExtra("from","HomeProduct")
                startActivity(intent)
            }
            getString(R.string.client_intrested_pets) -> {
                val intent = Intent(requireContext(), CommonContainerActivity::class.java)
                intent.putExtra("flag","viewInterested")
                intent.putExtra("from","HomePet")
                startActivity(intent)
            }

            getString(R.string.confirmed_inperson),getString(R.string.completed_inperson),
            getString(R.string.confirmed_telehealth),getString(R.string.completed_telehealth), getString(R.string.total_appoitments)-> {

                val typeRequest = when (type) {
                    getString(R.string.confirmed_inperson) -> {
                        "Confirmed InPerson"
                    }
                    getString(R.string.completed_inperson) -> {
                        "Completed InPerson"
                    }
                    getString(R.string.confirmed_telehealth) -> {
                        "Confirmed TeleHealth"
                    }
                    getString(R.string.completed_telehealth) -> {
                        "Completed TeleHealth"
                    }
                    else -> {
                        "Total Appointments"
                    }
                }


                val bundle = Bundle()
                bundle.putString("from",typeRequest)
                bundle.putString("isScreen","Home")
                val obj = AppointmentFragment()
                obj.arguments = bundle
                parentFragmentManager.beginTransaction().replace(R.id.vendorContainer, obj).addToBackStack("AppointmentFragment").commit()
            }

            getString(R.string.client_intrested_services) -> {
                val intent = Intent(requireContext(), CommonContainerActivity::class.java)
                intent.putExtra("flag","viewInterested")
                intent.putExtra("from","HomeService")
                startActivity(intent)
            }


        }



    }











}