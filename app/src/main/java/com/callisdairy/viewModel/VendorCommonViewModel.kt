package com.callisdairy.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.CalisApp
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.request.*
import com.callisdairy.api.response.*
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VendorCommonViewModel @Inject constructor(app: Application, private val repo: CalisRespository, private val networkHelper: NetworkHelper) : AndroidViewModel(app) {


    private val viewData: MutableStateFlow<Resource<ProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _viewData: StateFlow<Resource<ProductsResponse>> = viewData

    private val vieListServiceData: MutableStateFlow<Resource<ProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _vieListServiceData: StateFlow<Resource<ProductsResponse>> = vieListServiceData


    private val petCategoryData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petCategoryData: StateFlow<Resource<CountryResponse>> = petCategoryData


    private val categoryData: MutableStateFlow<Resource<PetCategoryListResponse>> = MutableStateFlow(Resource.Empty())
    val _categoryData: StateFlow<Resource<PetCategoryListResponse>> = categoryData


    private val listSubCategoryData: MutableStateFlow<Resource<PetCategoryListResponse>> = MutableStateFlow(Resource.Empty())
    val _listSubCategoryData: StateFlow<Resource<PetCategoryListResponse>> =  listSubCategoryData


    private val uploadImagesData: MutableStateFlow<Resource<ImageUploadResponse>> = MutableStateFlow(Resource.Empty())
    val _uploadImagesData: StateFlow<Resource<ImageUploadResponse>> = uploadImagesData

    private val addProductData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _addProductData: StateFlow<Resource<AddPostResponse>> = addProductData

    private val addServiceData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _addServiceData: StateFlow<Resource<AddPostResponse>> = addServiceData

    private val deleteServiceData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _deleteServiceData: StateFlow<Resource<AddPostResponse>> = deleteServiceData

    private val deleteProductData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _deleteProductData: StateFlow<Resource<AddPostResponse>> = deleteProductData

    private val intrestedUserData: MutableStateFlow<Resource<InterestedUserResponse>> = MutableStateFlow(Resource.Empty())
    val _intrestedUserData: StateFlow<Resource<InterestedUserResponse>> = intrestedUserData

    private val intrestedUserListData: MutableStateFlow<Resource<InterestedUserResponse>> = MutableStateFlow(Resource.Empty())
    val _intrestedUserListData: StateFlow<Resource<InterestedUserResponse>> = intrestedUserListData


    private val productDescriptionData: MutableStateFlow<Resource<ViewProductResponse>> = MutableStateFlow(Resource.Empty())
    val _productDescriptionData: StateFlow<Resource<ViewProductResponse>> = productDescriptionData


    private val updateProductData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _updateProductData: StateFlow<Resource<AddPostResponse>> = updateProductData

    private val updateServiceData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _updateServiceData: StateFlow<Resource<AddPostResponse>> = updateServiceData


    private val serviceDescriptionData: MutableStateFlow<Resource<ViewServiceResponse>> = MutableStateFlow(Resource.Empty())
    val _serviceDescriptionData: StateFlow<Resource<ViewServiceResponse>> = serviceDescriptionData

    private val profileViewData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _profileViewData: StateFlow<Resource<EditProfileResponse>> = profileViewData

    private val profileUpdateDoctor: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _profileUpdateDoctor: StateFlow<Resource<AddPostResponse>> = profileUpdateDoctor

    private val checkPlanData: MutableStateFlow<Resource<CheckLimitPlanResponse>> = MutableStateFlow(Resource.Empty())
    val _checkPlanData: StateFlow<Resource<CheckLimitPlanResponse>> = checkPlanData





    //    Product list Api


    fun productListApi(token: String,search:String,fromDate:String,toDate:String,page:Int,
                          limit:Int,approveStatus:String) = viewModelScope.launch {
        viewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myProductListApi(token, search, fromDate, toDate, page, limit, approveStatus)
                .catch { e ->
                    viewData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewData.value = listResponseHandle(data)
                }
        }else{
            viewData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun listResponseHandle(response: Response<ProductsResponse>): Resource<ProductsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



    //    Product list Api


    fun serviceListApi(token: String,search:String,fromDate:String,toDate:String,page:Int,
                          limit:Int,approveStatus:String) = viewModelScope.launch {
        vieListServiceData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myServiceListApi(token, search, fromDate, toDate, page, limit, approveStatus)
                .catch { e ->
                    vieListServiceData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    vieListServiceData.value = listServiceResponseHandle(data)
                }
        }else{
            vieListServiceData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun listServiceResponseHandle(response: Response<ProductsResponse>): Resource<ProductsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


//     Get Pet Type  List

    fun getPetCategoryApi() = viewModelScope.launch {
        petCategoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.petCategoryApi()
                .catch { e ->
                    petCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    petCategoryData.value = petCategoryResponseHandle(data)
                }
        }else{
            petCategoryData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun petCategoryResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    //     category list Api


    fun categoryListApi(type:String) = viewModelScope.launch {
        categoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listCategoryApi(type)
                .catch { e ->
                    categoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    categoryData.value = categoryListHandle(data)
                }
        }else{
            categoryData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun categoryListHandle(response: Response<PetCategoryListResponse>): Resource<PetCategoryListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }





//    List Sub Category

    fun listSubCategoryApi(categoryId:String) = viewModelScope.launch {
        listSubCategoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listSubCategoryApis(categoryId)
                .catch { e ->
                    listSubCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listSubCategoryData.value = lisSubCategoryResponseHandle(data)
                }
        }else{
            listSubCategoryData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun lisSubCategoryResponseHandle(response: Response<PetCategoryListResponse>): Resource<PetCategoryListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }





//     UploadMultipleImages Api


    fun uploadMultipleImagesApi(file : ArrayList<MultipartBody.Part>) = viewModelScope.launch {
        uploadImagesData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.uploadMultipleFile(file)
                .catch { e ->
                    uploadImagesData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    uploadImagesData.value = uploadMultipleImagesHandle(data)
                }
        }else{
            uploadImagesData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun uploadMultipleImagesHandle(response: Response<ImageUploadResponse>): Resource<ImageUploadResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

//     Add product Api


    fun addProductApi(token:String,request: AddProductRequest) = viewModelScope.launch {
        addProductData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addProductApi(token,request)
                .catch { e ->
                    addProductData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addProductData.value = addProductHandle(data)
                }
        }else{
            addProductData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun addProductHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



//     Add service Api


    fun addServiceApi(token:String,request: AddServiceRequest) = viewModelScope.launch {
        addServiceData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addServiceApi(token,request)
                .catch { e ->
                    addServiceData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addServiceData.value = addServiceHandle(data)
                }
        }else{
            addServiceData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun addServiceHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


// Delete Service

    fun deleteServiceApi(token:String,serviceId: String) = viewModelScope.launch {
        deleteServiceData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.deleteServiceApi(token,serviceId)
                .catch { e ->
                    deleteServiceData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deleteServiceData.value = deleteServiceHandle(data)
                }
        }else{
            deleteServiceData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun deleteServiceHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



// Delete PRODUCT

    fun deleteProductApi(token:String,ProductId: String) = viewModelScope.launch {
        deleteProductData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.deleteProductApi(token,ProductId)
                .catch { e ->
                    deleteProductData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deleteProductData.value = deleteProductHandle(data)
                }
        }else{
            deleteProductData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun deleteProductHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




// Interested User Api

    fun interestedUserListApi(token: String,type :String,page :Int,limit :Int,productId:String,serviceId:String,petId:String) = viewModelScope.launch {
        intrestedUserData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.interestedClientListApi(token,type, page, limit,productId, serviceId, petId)
                .catch { e ->
                    intrestedUserData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    intrestedUserData.value = interestedUserHandle(data)
                }
        }else{
            intrestedUserData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun interestedUserHandle(response: Response<InterestedUserResponse>): Resource<InterestedUserResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




// Interested User Api

    fun interestedProductListApi(token: String,type :String,page :Int,limit :Int,productId:String,serviceId:String,petId:String) = viewModelScope.launch {
        intrestedUserListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.interestedProductListApi(token,type, page, limit,productId, serviceId, petId)
                .catch { e ->
                    intrestedUserListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    intrestedUserListData.value = interestedUserlistHandle(data)
                }
        }else{
            intrestedUserListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun interestedUserlistHandle(response: Response<InterestedUserResponse>): Resource<InterestedUserResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




    //    productDescriptionApi

    fun productDescriptionApi(token:String,id:String) = viewModelScope.launch {
        productDescriptionData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewProductApi(token,id)
                .catch { e ->
                    productDescriptionData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    productDescriptionData.value = viewProductResponseHandle(data)
                }
        }else{
            productDescriptionData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun viewProductResponseHandle(response: Response<ViewProductResponse>): Resource<ViewProductResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    //     update product Api


    fun updateProductApi(token:String,request: UpdateProductRequest) = viewModelScope.launch {
        updateProductData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.updateProductApi(token,request)
                .catch { e ->
                    updateProductData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    updateProductData.value = updateProductHandle(data)
                }
        }else{
            updateProductData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun updateProductHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



    //     update service Api


    fun updateServiceApi(token:String,request: UpdateServiceRequest) = viewModelScope.launch {
        updateServiceData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.updateServiceApi(token,request)
                .catch { e ->
                    updateServiceData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    updateServiceData.value = updateServiceHandle(data)
                }
        }else{
            updateServiceData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun updateServiceHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




    //    serviceDescriptionApi

    fun serviceDescriptionApi(token:String,id:String) = viewModelScope.launch {
        serviceDescriptionData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewServiceApi(token,id)
                .catch { e ->
                    serviceDescriptionData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    serviceDescriptionData.value = viewserviceResponseHandle(data)
                }
        }else{
            serviceDescriptionData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun viewserviceResponseHandle(response: Response<ViewServiceResponse>): Resource<ViewServiceResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    //    get Profile Api

    fun viewProfileApi(token:String) = viewModelScope.launch {
        profileViewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getProfileApi(token)
                .catch { e ->
                    profileViewData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    profileViewData.value = editViewProfileResponseHandle(data)
                }
        }else{
            profileViewData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun editViewProfileResponseHandle(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


// update doctor profile

    fun updateDoctorApi(token:String,request: EditProfileVetDoctorRequest) = viewModelScope.launch {
        profileUpdateDoctor.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.editDoctorVetProfile(token,request)
                .catch { e ->
                    profileUpdateDoctor.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    profileUpdateDoctor.value = updateDoctorApiResponseHandle(data)
                }
        }else{
            profileUpdateDoctor.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun updateDoctorApiResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



//     Check plans

    fun checkPlanApi(token:String) = viewModelScope.launch {
        checkPlanData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.checkPlanApi(token= token)
                .catch { e ->
                    checkPlanData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    checkPlanData.value = checkPlanResponseHandle(data)
                }
        }else{
            checkPlanData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun checkPlanResponseHandle(response: Response<CheckLimitPlanResponse>): Resource<CheckLimitPlanResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }





}