package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.response.PlanListResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class CheckContentLimitViewModel @Inject constructor(app: Application, private val repo: CalisRespository, private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val planData: MutableStateFlow<Resource<PlanListResponse>> = MutableStateFlow(Resource.Empty())
    val _planData: StateFlow<Resource<PlanListResponse>> = planData




    fun planListApi(type: String,userType: String,moduleName: String) = viewModelScope.launch {
        planData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listSubscriptionApi(type=type,userType=userType,moduleName=moduleName)
                .catch { e ->
                    planData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    planData.value = planResponseHandle(data)
                }
        }else{
            planData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun planResponseHandle(response: Response<PlanListResponse>): Resource<PlanListResponse> {
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