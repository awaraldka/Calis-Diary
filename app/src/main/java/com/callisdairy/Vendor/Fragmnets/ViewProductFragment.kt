package com.callisdairy.Vendor.Fragmnets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.Adapter.ImageSliderAdaptor
import com.callisdairy.Interface.DeleteClick
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.databinding.FragmentViewProductBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.VendorCommonViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewProductFragment : Fragment(), DeleteClick {

    private var _binding: FragmentViewProductBinding? =  null
    private val binding get() = _binding!!

    var productId = ""
    var token = ""
    var from = ""
    lateinit var imageAdaptor: ImageSliderAdaptor

    lateinit var title:TextView
    lateinit var backVendor:ImageView

    private val viewModel : VendorCommonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentViewProductBinding.inflate(layoutInflater, container, false)
        title =activity?.findViewById(R.id.titleVendor)!!
        arguments?.getString("productId")?.let { productId = it }
        arguments?.getString("from")?.let { from = it }
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()

        backVendor = activity?.findViewById(R.id.backVendor)!!


        title.text  = from



        backVendor.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

        binding.delete.setOnClickListener {
            if (from == "Service") {
                androidExtension.alertBoxDelete(
                    getString(R.string.service_delete),
                    requireContext(),
                    this
                )
            } else {
                androidExtension.alertBoxDelete(
                    getString(R.string.product_delete),
                    requireContext(),
                    this
                )

            }
        }


        binding.editProduct.setSafeOnClickListener{
            if (from.lowercase() == "service"){
                val intent = Intent(requireContext(),CommonContainerActivity::class.java)
                intent.putExtra("flag","editService")
                intent.putExtra("from","editService")
                intent.putExtra("id", productId)
                startActivity(intent)
            }else{
                val intent = Intent(requireContext(), CommonContainerActivity::class.java)
                intent.putExtra("flag", "editProduct")
                intent.putExtra("id", productId)
                startActivity(intent)
            }

        }






        binding.seeUsersList.setOnClickListener {
            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag","viewInterestedUser")
            intent.putExtra("from",from)
            intent.putExtra("id",productId)
            startActivity(intent)
        }




        return binding.root
    }


    override fun onStart() {
        super.onStart()
        if (from == "Service"){
            binding.details.text = getString(R.string.service_details)
            binding.name.text = getString(R.string.service_name)
            binding.serviceId.text = getString(R.string.service_id)
            binding.type.text = getString(R.string.experience)
            binding.editType.text = getString(R.string.service_Update)
            viewModel.serviceDescriptionApi(token,productId)
        }else{
            viewModel.productDescriptionApi(token,productId)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })
        observeResponseServiceDetails()

        observeResponseProductDetails()
        observeResponseDeleteProduct()
        observeResponseDeleteService()
    }
    override fun deleteItem() {
        if (from == "Service") {
            viewModel.deleteServiceApi(token,productId)
        }else{
            viewModel.deleteProductApi(token,productId)
        }

    }


    private fun observeResponseDeleteService() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deleteServiceData.collectLatest { response ->

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



    private fun observeResponseDeleteProduct() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deleteProductData.collectLatest { response ->

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


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun observeResponseProductDetails() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel._productDescriptionData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if(response.data?.responseCode == 200) {
                                try {

                                    with(response.data.result){
                                        setFieldValue(binding.productName,productName)
                                        setFieldValue(binding.productId,productGenerateId)
                                        setFieldValue(binding.categoryName,categoryId.categoryName)
                                        setFieldValue(binding.subCategoryName,subCategoryId.subCategoryName)
                                        setFieldValue(binding.date,DateFormat.dateFormatCommon(response.data.result.createdAt,"yyyy-MM-dd'T'HH:mm:ss"))
                                        setFieldValue(binding.productStatus,approveStatus)
                                        setFieldValue(binding.productWeight,"${response.data.result.weight} KG")
                                        setFieldValue(binding.productPrice,"$currency ${response.data.result.price}")
                                        setFieldValue(binding.Description,description)
                                    }




                                    if (response.data.result.approveStatus.lowercase() == "approved"){
                                        binding.productStatus.setTextColor(android.graphics.Color.parseColor("#023020"))
                                    }else{
                                        binding.productStatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                                    }

                                    binding.indicator.isVisible = response.data.result.productImage.size >1


                                    setImageAdaptor(response.data.result.productImage)

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





    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun observeResponseServiceDetails() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._serviceDescriptionData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if(response.data?.responseCode == 200) {
                                try {

                                    response.data.result.apply{
                                        setFieldValue(binding.productName,serviceName)
                                        setFieldValue(binding.productId,serviceGenerateId)
                                        setFieldValue(binding.categoryName,categoryId.categoryName)
                                        setFieldValue(binding.subCategoryName,subCategoryId.subCategoryName)
                                        setFieldValue(binding.date,DateFormat.dateFormatCommon(response.data.result.createdAt,"yyyy-MM-dd'T'HH:mm:ss"))
                                        setFieldValue(binding.productStatus,approveStatus)
                                        setFieldValue(binding.productWeight,"${response.data.result.experience} years")
                                        setFieldValue(binding.productPrice,"$currency ${response.data.result.price}")
                                        setFieldValue(binding.Description,description)



                                        if (approveStatus!!.lowercase() == "approved"){
                                            binding.productStatus.setTextColor(android.graphics.Color.parseColor("#49CC90"))
                                        }else{
                                            binding.productStatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                                        }

                                        binding.indicator.isVisible = serviceImage.size >1


                                        setImageAdaptor(serviceImage)

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






    private fun setImageAdaptor(productName: ArrayList<String>) {
        imageAdaptor = ImageSliderAdaptor(requireContext(),productName)
        binding.storeViewpager.adapter = imageAdaptor
        binding.indicator.setViewPager(binding.storeViewpager)

    }




    private fun setFieldValue(field: TextView, value: String?) {
        field.text = value?.takeIf { it.isNotBlank() } ?: "NA"
    }


}