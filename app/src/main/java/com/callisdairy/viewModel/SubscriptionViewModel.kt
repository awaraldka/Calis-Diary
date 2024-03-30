package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.response.AddPostResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(app: Application, private val repo: CalisRespository, private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val subscriptionData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _subscriptionData: StateFlow<Resource<AddPostResponse>> = subscriptionData


    fun paymentApi(token: String,stripeToken:String,totalCost:String,curr:String,plan_id:String) = viewModelScope.launch {
        subscriptionData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.makePaymentApi(token=token, stripeToken=stripeToken, totalCost=totalCost, curr=curr,plan_id=plan_id)
                .catch { e ->
                    subscriptionData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    subscriptionData.value = paymentResponseHandle(data)
                }
        }else{
            subscriptionData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun paymentResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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