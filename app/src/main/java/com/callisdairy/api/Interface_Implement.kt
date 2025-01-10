package com.callisdairy.api

import com.callisdairy.api.request.AddAppointmentRequest
import com.callisdairy.api.request.AddEventRequest
import com.callisdairy.api.request.AddMissingPetRequest
import com.callisdairy.api.request.AddPetRequest
import com.callisdairy.api.request.AddProductRequest
import com.callisdairy.api.request.AddServiceRequest
import com.callisdairy.api.request.EditEventRequest
import com.callisdairy.api.request.EditProfileRequest
import com.callisdairy.api.request.EditProfileVendorRequest
import com.callisdairy.api.request.EditProfileVetDoctorRequest
import com.callisdairy.api.request.LoginRequest
import com.callisdairy.api.request.SignUpRequest
import com.callisdairy.api.request.SignUpRequestVendor
import com.callisdairy.api.request.SignUpRequestVendorDoctor
import com.callisdairy.api.request.UpdatePetRequest
import com.callisdairy.api.request.UpdateProductRequest
import com.callisdairy.api.request.UpdateServiceRequest
import com.callisdairy.api.response.AddEventResponse
import com.callisdairy.api.response.AddLikeResponse
import com.callisdairy.api.response.AddPostResponse
import com.callisdairy.api.response.AddStoryResponse
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.AgoraCallingResponse
import com.callisdairy.api.response.AppConfigResponse
import com.callisdairy.api.response.AppointmentDateResponse
import com.callisdairy.api.response.AppointmentListResponse
import com.callisdairy.api.response.BannerListResponse
import com.callisdairy.api.response.BlockedUserResponse
import com.callisdairy.api.response.CategoryBasedResponse
import com.callisdairy.api.response.CheckLimitPlanResponse
import com.callisdairy.api.response.CommentListResponse
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.EditProfileResponse
import com.callisdairy.api.response.ExploreListResponse
import com.callisdairy.api.response.FaqResponse
import com.callisdairy.api.response.FavPetResponse
import com.callisdairy.api.response.FavProductsResponse
import com.callisdairy.api.response.FavServiceResponse
import com.callisdairy.api.response.FollowersListResponse
import com.callisdairy.api.response.FollowingUserListResponse
import com.callisdairy.api.response.GetSubscriptionDetailsResponse
import com.callisdairy.api.response.GlobalSearchResponse
import com.callisdairy.api.response.HomePageListResponse
import com.callisdairy.api.response.ImageUploadResponse
import com.callisdairy.api.response.InterestedUserResponse
import com.callisdairy.api.response.IntrestedPetResponse
import com.callisdairy.api.response.LikeUnlikeProductsResponse
import com.callisdairy.api.response.ListCategoryResponse
import com.callisdairy.api.response.ListEventResponse
import com.callisdairy.api.response.LogInResponse
import com.callisdairy.api.response.MissingPetResponse
import com.callisdairy.api.response.MyPetListResponse
import com.callisdairy.api.response.MyPostResponse
import com.callisdairy.api.response.NotificationListResponse
import com.callisdairy.api.response.OtherUserResponse
import com.callisdairy.api.response.PetCategoryListResponse
import com.callisdairy.api.response.PetListResponse
import com.callisdairy.api.response.PetProfileResponse
import com.callisdairy.api.response.PlanListResponse
import com.callisdairy.api.response.ProductListResponse
import com.callisdairy.api.response.ProductsResponse
import com.callisdairy.api.response.RepliesListResponse
import com.callisdairy.api.response.RequestedListResponse
import com.callisdairy.api.response.RewardsPointResponse
import com.callisdairy.api.response.RewardsResponseVendor
import com.callisdairy.api.response.SearchPeopleResponse
import com.callisdairy.api.response.ServiceListResponse
import com.callisdairy.api.response.SignUpResponse
import com.callisdairy.api.response.StaticContentResponse
import com.callisdairy.api.response.SubCategoryResponse
import com.callisdairy.api.response.TagPeopleResponse
import com.callisdairy.api.response.UploadFileResponse
import com.callisdairy.api.response.UserFeedBackListResponse
import com.callisdairy.api.response.UserStoriesResponse
import com.callisdairy.api.response.VendorDashboardResponse
import com.callisdairy.api.response.VerifyOtpResponse
import com.callisdairy.api.response.VetOrDoctorResponse
import com.callisdairy.api.response.ViewAppointmentResponse
import com.callisdairy.api.response.ViewEventResponse
import com.callisdairy.api.response.ViewMissingPetResponse
import com.callisdairy.api.response.ViewPetResponse
import com.callisdairy.api.response.ViewPostResponse
import com.callisdairy.api.response.ViewProductResponse
import com.callisdairy.api.response.ViewServiceResponse
import com.callisdairy.api.response.ViewStatusResponse
import com.callisdairy.api.response.suggestionListResponse
import com.callisdairy.api.response.updateCommentResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


class Interface_Implement @Inject constructor(private val apiService: Api_Interface) {


    suspend fun signUpAPi(request: SignUpRequest): Response<SignUpResponse> =
        apiService.signUpAPi(request)

    suspend fun signInApiVendor(request: SignUpRequestVendor): Response<SignUpResponse> =
        apiService.signInApiVendor(request)

    suspend fun signInApiVendorDoctor(request: SignUpRequestVendorDoctor): Response<SignUpResponse> =
        apiService.signInApiVendorDoctor(request)

    suspend fun getAllCountryApi(): Response<CountryResponse> = apiService.getAllCountryApi()

    suspend fun getAllStateApi(countryCode: String): Response<CountryResponse> =
        apiService.getAllStateApi(countryCode)

    suspend fun petCategoryApi(): Response<CountryResponse> = apiService.petCategoryApi()

    suspend fun getAllCityApi(countryCode: String, stateCode: String): Response<CountryResponse> =
        apiService.getAllCityApi(countryCode, stateCode)

    suspend fun login(request: LoginRequest): Response<LogInResponse> = apiService.login(request)

    suspend fun socialLogin(request: JsonObject): Response<LogInResponse> = apiService.socialLogin(request)

    suspend fun verifyOTPApi(jsonObject: JsonObject): Response<VerifyOtpResponse> =
        apiService.verifyOTPApi(jsonObject)

    suspend fun resendOTPApi(jsonObject: JsonObject): Response<VerifyOtpResponse> =
        apiService.resendOTPApi(jsonObject)

    suspend fun forgotPasswordApi(jsonObject: JsonObject): Response<SignUpResponse> =
        apiService.forgotPasswordApi(jsonObject)

    suspend fun resetPasswordApi(
        token: String,
        password: String,
        confirmPassword: String
    ): Response<SignUpResponse> = apiService.resetPasswordApi(token, password, confirmPassword)


    suspend fun tagPeopleListApi(token: String, search: String): Response<TagPeopleResponse> =
        apiService.tagPeopleListApi(token, search)

    suspend fun listCategoryApi(
        categoryTypes: String,
        page: Int,
        limit: Int
    ): Response<ListCategoryResponse> = apiService.listCategoryApi(categoryTypes, page, limit)

    suspend fun listProductApi(status: String): Response<ProductListResponse> =
        apiService.listProductApi(status)

    suspend fun changePassword(token: String, jsonObject: JsonObject): Response<SignUpResponse> =
        apiService.changePassword(token, jsonObject)

    suspend fun addPetApi(token: String, request: AddPetRequest): Response<AddToIntrestedResponse> =
        apiService.addPetApi(token, request)

    suspend fun updatePetApi(
        token: String,
        petId: String,
        request: UpdatePetRequest
    ): Response<AddToIntrestedResponse> = apiService.updatePetApi(token, petId, request)

    suspend fun addPostApi(token: String,file: ArrayList<MultipartBody.Part>,caption:String,requestMetaWords:ArrayList<String>,
                           requestTagPeople:ArrayList<String>,latitude:Double,longitude:Double,LocationName:String): Response<AddPostResponse> =
        apiService.addPostApi(token, file,caption,requestMetaWords,requestTagPeople,latitude,longitude,LocationName)



    suspend fun editProfileApi(
        token: String,
        request: EditProfileRequest
    ): Response<EditProfileResponse> = apiService.editProfileApi(token, request)



    suspend fun editProfileVendorApi(
        token: String,
        request: EditProfileVendorRequest
    ): Response<EditProfileResponse> = apiService.editProfileVendorApi(token, request)


    suspend fun getProfileApi(token: String): Response<EditProfileResponse> =
        apiService.getProfileApi(token)

    suspend fun faqListApi(search:String): Response<FaqResponse> = apiService.faqListApi(search)

    suspend fun addEventApi(token: String, request: AddEventRequest): Response<AddEventResponse> =
        apiService.addEventApi(token, request)

    suspend fun updateEventApi(
        token: String,
        request: EditEventRequest
    ): Response<AddEventResponse> = apiService.updateEventApi(token, request)


    suspend fun addStoryApi(token: String, file: ArrayList<MultipartBody.Part>,caption:String): Response<AddStoryResponse> =
        apiService.addStoryApi(token,file, caption)


    suspend fun uploadMultipleFileApi(file: ArrayList<MultipartBody.Part>): Response<UploadFileResponse> =
        apiService.uploadMultipleFileApi(file)


    suspend fun homePagePostListApi(
        token: String,
        page: Int,
        limit: Int,
        radius: String,
        country: String,
        city: String,
        state: String,
        lat: Double,
        long: Double,
        petBreedId:String
    ): Response<HomePageListResponse> = apiService.homePagePostListApi(token, page, limit,radius, country, city, state,lat,long,petBreedId)


    suspend fun commentListApi(
        token: String,
        postId: String,
        page: Int,
        limit: Int
    ): Response<CommentListResponse> = apiService.commentListApi(token, postId, page, limit)


    suspend fun addCommentApi(
        token: String, typeOfPost: String,
        postId: String, comment: String, commentId: String, reply: String
    ): Response<HomePageListResponse> =
        apiService.addCommentApi(token, typeOfPost, postId, comment, commentId, reply)


    suspend fun updateCommentApi(
        token: String, typeOfPost: String,
        postId: String, comment: String, commentId: String, reply: String
    ): Response<updateCommentResponse> =
        apiService.updateCommentApi(token, typeOfPost, postId, comment, commentId, reply)

    suspend fun addStoryCommentApi(
        token: String, typeOfPost: String,
        storyId: String, reply: String
    ): Response<AddEventResponse> = apiService.addStoryCommentApi(token, typeOfPost, storyId, reply)


    suspend fun repliesListApi(
        token:String,
        commentId: String,
        page: Int,
        limit: Int
    ): Response<RepliesListResponse> = apiService.repliesListApi(token,commentId, page, limit)


    suspend fun addLikeApi(
        token: String,
        typeOfPost: String,
        postId: String,
        commentId: String
    ): Response<AddLikeResponse> = apiService.addLikeApi(token, typeOfPost, postId, commentId)


    suspend fun listInterestedApi(
        token: String,
        type: String,
        page: Int,
        limit: Int
    ): Response<IntrestedPetResponse> = apiService.listInterestedApi(token, type, page, limit)


    suspend fun viewPetApi(token: String, id: String): Response<ViewPetResponse> =
        apiService.viewPetApi(token, id)

    suspend fun viewServiceApi(token: String, id: String): Response<ViewServiceResponse> =
        apiService.viewServiceApi(id, token)

    suspend fun viewProductApi(token: String, id: String): Response<ViewProductResponse> =
        apiService.viewProductApi(id, token)


    suspend fun addToInterestedApi(
        token: String,
        type: String,
        petId: String,
        productId: String,
        serviceId: String
    ): Response<AddToIntrestedResponse> =
        apiService.addToInterestedApi(token, type, petId, productId, serviceId)


    suspend fun likeUnlikeProductApi(
        token: String,
        id: String
    ): Response<LikeUnlikeProductsResponse> = apiService.likeUnlikeProductApi(token, id)


    suspend fun likeUnlikeServicesApi(
        token: String,
        id: String
    ): Response<LikeUnlikeProductsResponse> = apiService.likeUnlikeServicesApi(token, id)


    suspend fun favUnfavPetApi(token: String, id: String): Response<LikeUnlikeProductsResponse> =
        apiService.favUnfavPetApi(token, id)

    suspend fun myFavPets(token: String, page: Int, limit: Int): Response<FavPetResponse> =
        apiService.myFavPets(token, page, limit)

    suspend fun myLikesServicesApi(
        token: String,
        page: Int,
        limit: Int
    ): Response<FavServiceResponse> = apiService.myLikesServicesApi(token, page, limit)


    suspend fun myLikesProductApi(
        token: String,
        page: Int,
        limit: Int
    ): Response<FavProductsResponse> = apiService.myLikesProductApi(token, page, limit)


    suspend fun listProductV2Api(
        token: String,
        lat: Double,
        lng: Double,
        maxDistance: Int,
        categoryId: String,
        subCategoryId: String,
        page: Int,
        limit: Int,
        radius : String,
        country : String,
        state : String,
        city : String,
        search: String,
    ): Response<ProductListResponse> = apiService.listProductV2Api(
        token,
        lat,
        lng,
        categoryId,
        subCategoryId,
        page,
        limit,
        maxDistance,
        radius, country, city, state,search
    )

    suspend fun listPetApi(
        token: String,
        page: Int,
        limit: Int,
        lat: Double,
        lng: Double,
        maxDistance: Int,
        radius : String,
        country : String,
        state : String,
        city : String,search: String
    ): Response<PetListResponse> = apiService.listPetApi(
        token, page, limit, lat, lng,radius, country, city, state,search)


    suspend fun listServiceV2Api(
        token: String, page: Int, limit: Int, categoryId: String,
        subCategoryId: String, lat: Double, lng: Double, maxDistance: Int,radius : String,
        country : String,
        state : String,
        city : String,
        search: String
    ): Response<ServiceListResponse> = apiService.listServiceV2Api(
        token,
        page,
        limit,
        categoryId,
        subCategoryId,
        lat,
        lng,
        maxDistance,radius, country, city, state,search
    )

    suspend fun listSuggestedUserApi(
        token: String,
        search: String,
        page: Int,
        limit: Int,
        discover:String
    ): Response<suggestionListResponse> =
        apiService.listSuggestedUserApi(token,discover, search, page, limit)


    suspend fun listSubCategoryApi(categoryId: String): Response<SubCategoryResponse> =
        apiService.listSubCategoryApi(categoryId)

    suspend fun listSubCategoryApis(categoryId: String): Response<PetCategoryListResponse> =
        apiService.listSubCategoryApis(categoryId)

    suspend fun listEventApi(token: String,search: String, page: Int, limit: Int,radius : String,
                             country : String,
                             state : String,
                             city : String, lat: Double, lng: Double,petBreedId:String): Response<ListEventResponse> =
        apiService.listEventApi(token,search, page, limit,radius, country, city, state,lat,lng)


    suspend fun peopleSearchApi(token: String, search: String): Response<SearchPeopleResponse> =
        apiService.peopleSearchListApi(token, search)


    suspend fun myListEventApi(
        token: String,
        search: String,
        page: Int,
        limit: Int
    ): Response<ListEventResponse> = apiService.myListEventApi(token, search, page, limit)


    suspend fun viewEvent(eventId: String): Response<ViewEventResponse> =
        apiService.viewEvent(eventId)


    suspend fun deleteEventApi(token: String, eventId: String): Response<AddEventResponse> =
        apiService.deleteEventApi(token, eventId)


    suspend fun missingPetListApi(
        token: String,
        search: String,
        type: String,
        page: Int,
        limit: Int,
        radius : String,
        country : String,
        state : String,
        city : String, lat: Double, lng: Double,petBreedId:String
    ): Response<MissingPetResponse> = apiService.missingPetListApi(token, search, type, page, limit,radius,country,state,city,lat, lng,petBreedId)


    suspend fun viewMissingPetApi(token: String, petId: String): Response<ViewMissingPetResponse> =
        apiService.viewMissingPetApi(token, petId)


    suspend fun addMissingPetApi(
        token: String,
        request: AddMissingPetRequest
    ): Response<AddToIntrestedResponse> = apiService.addMissingPetApi(token, request)


    suspend fun editMissingPetApi(
        token: String,
        id: String,
        request: AddMissingPetRequest
    ): Response<AddToIntrestedResponse> = apiService.editMissingPetApi(token, id, request)

    suspend fun deleteMissingPetApi(token: String, id: String): Response<AddEventResponse> =
        apiService.deleteMissingPetApi(token, id)

    suspend fun viewRewardsPointsApi(token: String): Response<RewardsPointResponse> =
        apiService.viewRewardsPointsApi(token)

    suspend fun staticContentApi(type: String): Response<StaticContentResponse> =
        apiService.staticContentApi(type)

    suspend fun viewStatusApi(token: String): Response<ViewStatusResponse> =
        apiService.viewStatusApi(token)

    suspend fun updateStatusApi(token: String): Response<AddEventResponse> =
        apiService.updateStatusApi(token)

    suspend fun followUnfollowUserApi(
        token: String,
        userId: String
    ): Response<AddToIntrestedResponse> = apiService.followUnfollowUserApi(token, userId)

    suspend fun followingUserListApi(token: String,search: String, page: Int, limit: Int): Response<FollowingUserListResponse> =
        apiService.followingUserListApi(token,page, limit, search)

    suspend fun myPostListApi(token: String, page: Int, limit: Int): Response<MyPostResponse> =
        apiService.myPostListApi(token, page, limit)

    suspend fun viewPostApi(token: String, postId: String): Response<ViewPostResponse> =
        apiService.viewPostApi(token, postId)

    suspend fun myPetListApi(token: String,search: String, page: Int, limit: Int,fromDate: String,toDate: String,publishStatus: String): Response<MyPetListResponse> =
        apiService.myPetListApi(token,search, page, limit,fromDate,toDate,publishStatus)

    suspend fun othersProfile(token: String, id: String): Response<OtherUserResponse> =
        apiService.othersProfile(token, id)

    suspend fun otherUserPetListApi(
        token: String,
        id: String,
        page: Int,
        limit: Int
    ): Response<MyPetListResponse> = apiService.otherUserPetListApi(token, id, page, limit)

    suspend fun otherUserPostListApi(
        token: String,
        userId: String,
        page: Int,
        limit: Int
    ): Response<MyPostResponse> = apiService.otherUserPostListApi(token, userId, page, limit)

    suspend fun otherfollowerUserListApi(
        token: String,
        id: String,
        search: String, page: Int, limit: Int
    ): Response<FollowersListResponse> = apiService.otherfollowerUserListApi(token, id,page, limit, search)

    suspend fun followerUserListApi(token: String,search: String, page: Int, limit: Int): Response<FollowersListResponse> =
        apiService.followerUserListApi(token,page, limit, search)

    suspend fun otherfollowingUserListApi(
        token: String,
        id: String, page: Int, limit: Int,search: String
    ): Response<FollowingUserListResponse> = apiService.otherfollowingUserListApi(token, id,page, limit, search)

    suspend fun globalSearchApi(token: String, search: String): Response<GlobalSearchResponse> =
        apiService.globalSearchApi(token, search)

    suspend fun uploadMultipleFile(file: ArrayList<MultipartBody.Part>): Response<ImageUploadResponse> =
        apiService.uploadMultipleFile(file)

    suspend fun notificationListApi(token: String,
                                    page: Int,
                                    limit: Int): Response<NotificationListResponse> =
        apiService.notificationListApi(token,page, limit)

    suspend fun clearNotificationApi(
        token: String,
        notificationId: String
    ): Response<AddEventResponse> = apiService.clearNotificationApi(token, notificationId)

    suspend fun deleteAccount(
        token: String
    ): Response<AddEventResponse> = apiService.deleteAccount(token)


    suspend fun deactivateAccount(
        token: String
    ): Response<AddEventResponse> = apiService.deactivateAccount(token)


    suspend fun listCategoryApi(type: String): Response<PetCategoryListResponse> =
        apiService.listCategoryApi(type)

    suspend fun requestListApi(
        token: String,
        page: Int,
        limit: Int
    ): Response<RequestedListResponse> = apiService.requestListApi(token, page, limit)


    suspend fun acceptOrDenyRequestApi(
        token: String,
        type: String,
        requestId: String
    ): Response<AddEventResponse> = apiService.acceptOrDenyRequestApi(token, type, requestId)


    suspend fun privateOrPublicAccountApi(token: String): Response<AddEventResponse> =
        apiService.privateOrPublicAccountApi(token)

    suspend fun addPetToNFTMarketPlaceApi(
        token: String,
        id: String,
        price: String,
        currency: String,
    ): Response<AddEventResponse> = apiService.addPetToNFTMarketPlaceApi(token, id, price,currency)

    suspend fun removePetToMarketPlaceApi(token: String, id: String): Response<AddEventResponse> =
        apiService.removePetToMarketPlaceApi(token, id)

    suspend fun removePetApi(token: String, id: String): Response<AddEventResponse> =
        apiService.removePetApi(token, id)


    suspend fun storyList(token: String): Response<UserStoriesResponse> =
        apiService.storyList(token)


    suspend fun hidePostApi(token: String, id: String): Response<AddEventResponse> =
        apiService.hidePostApi(token, id)

    suspend fun removePostApi(token: String, id: String): Response<AddEventResponse> =
        apiService.removePostApi(token, id)

    suspend fun createReportApi(token: String, id: String, description: String): Response<AddEventResponse> =
        apiService.createReportApi(token, id,description)

    suspend fun agoraTokenGeneratorApi(token: String): Response<AgoraCallingResponse> =
        apiService.agoraTokenGeneratorApi(token)

    suspend fun callUserApi(
        token: String,
        receiverId: String,
        type: String
    ): Response<AddToIntrestedResponse> = apiService.callUserApi(token, receiverId, type)


    suspend fun deleteCommentApi(
        token: String, typeOfPost: String,
        postId: String, comment: String, commentId: String, reply: String
    ): Response<AddEventResponse> =
        apiService.deleteCommentApi(token, typeOfPost, postId, comment, commentId, reply)


    suspend fun addUserProfileApi(
        token: String, petCategoryType: String,
        name: String, userProfileImage: String, petCategoryId: String, petBreedId: String
    ): Response<AddToIntrestedResponse> = apiService.addUserProfileApi(token, petCategoryType, name, userProfileImage, petCategoryId,petBreedId)


    suspend fun userProfileListApi(token: String): Response<PetProfileResponse> = apiService.userProfileListApi(token)

    suspend fun setDefaultUserProfileApi(token: String,petCategoryId :String): Response<AddToIntrestedResponse> = apiService.setDefaultUserProfileApi(token,petCategoryId)

    suspend fun petCategoryDetailsApi(petTypeId :String): Response<CategoryBasedResponse> = apiService.petCategoryDetailsApi(petTypeId)


    suspend fun listBannerListApi(): Response<BannerListResponse> = apiService.listBannerListApi()

    suspend fun setDefaultUserLanguageApi(token: String,languageCode :String): Response<AddPostResponse> = apiService.setDefaultUserLanguageApi(token,languageCode)

    suspend fun userLogout(token: String,fireToken: String): Response<AddEventResponse> = apiService.userLogout(token,fireToken)

    suspend fun listExploreApi(search:String,page:Int, limit:Int): Response<ExploreListResponse> = apiService.listExploreApi(search,page, limit)

    suspend fun vendorDashboard(token: String): Response<VendorDashboardResponse> = apiService.vendorDashboard(token)

    suspend fun myProductListApi(token: String,search:String,fromDate:String,toDate:String,page:Int,
    limit:Int,approveStatus:String): Response<ProductsResponse> = apiService.myProductListApi(token,search, fromDate, toDate, page, limit, approveStatus)

    suspend fun addProductApi(token: String,request: AddProductRequest): Response<AddPostResponse> = apiService.addProductApi(token,request)

    suspend fun myServiceListApi(token: String,search:String,fromDate:String,toDate:String,page:Int,
                                 limit:Int,approveStatus:String): Response<ProductsResponse> = apiService.myServiceListApi(token,search, fromDate, toDate, page, limit, approveStatus)

    suspend fun addServiceApi(token: String,request: AddServiceRequest): Response<AddPostResponse> = apiService.addServiceApi(token,request)

    suspend fun deleteServiceApi(token: String,serviceId: String): Response<AddPostResponse> = apiService.deleteServiceApi(token,serviceId)

    suspend fun deleteProductApi(token: String,productId: String): Response<AddPostResponse> = apiService.deleteProductApi(token,productId)


    suspend fun interestedClientListApi(token: String,type :String,page :Int,limit :Int,productId:String,serviceId:String,petId:String): Response<InterestedUserResponse> = apiService.interestedClientListApi(token,type, page, limit,productId, serviceId, petId)

    suspend fun interestedProductListApi(token: String,type :String,page :Int,limit :Int,productId:String,serviceId:String,petId:String): Response<InterestedUserResponse> = apiService.interestedProductListApi(token,type, page, limit,productId, serviceId, petId)


    suspend fun updateProductApi(token: String,request: UpdateProductRequest): Response<AddPostResponse> = apiService.updateProductApi(token,request)

    suspend fun updateServiceApi(token: String,request: UpdateServiceRequest): Response<AddPostResponse> = apiService.updateServiceApi(token,request)

    suspend fun redeemPointsListApi(token: String,search: String,fromDate: String,toDate: String,page: Int,limit: Int): Response<RewardsResponseVendor> = apiService.redeemPointsListApi(token,search, fromDate, toDate, page, limit)

    suspend fun editDoctorVetProfile(token: String,request: EditProfileVetDoctorRequest): Response<AddPostResponse> = apiService.editDoctorVetProfile(token,request)

    suspend fun availableSlotApi(token: String,date:String,doctorVetId:String): Response<AppointmentDateResponse> = apiService.availableSlotApi(token,date, doctorVetId)


    suspend fun listVendorApi(token: String,userTypes:String,search:String,page: Int,limit: Int,
                              radius : String,
                              country : String,
                              state : String,
                              city : String, lat: Double, lng: Double): Response<VetOrDoctorResponse>
    = apiService.listVendorApi(token,userTypes, search,page,limit,radius, country, city, state, lat , lng)


    suspend fun viewUser(token: String,id:String): Response<EditProfileResponse> = apiService.viewUser(token,id)


    suspend fun addAppointmentApi(token: String,request:AddAppointmentRequest): Response<AddPostResponse> = apiService.addAppointmentApi(token,request)


    suspend fun listUserAppointmentApi(token: String,search:String,page: Int,limit: Int,consultingType:String,appointmentStatus:String): Response<AppointmentListResponse> = apiService.listUserAppointmentApi(token,search, page, limit,consultingType,appointmentStatus)



    suspend fun viewAppointmentApi(token: String,id:String): Response<ViewAppointmentResponse> = apiService.viewAppointmentApi(token,id)

    suspend fun addBlockDoctorUserAPi(token: String,id:String): Response<AddPostResponse> = apiService.addBlockDoctorUserAPi(token,id)


    suspend fun markAsDoneAppointmentApi(token: String,id:String): Response<AddPostResponse> = apiService.markAsDoneAppointmentApi(token,id)


    suspend fun listBlockDoctorUserApi(token: String,search:String,page: Int,limit: Int): Response<BlockedUserResponse> = apiService.listBlockDoctorUserApi(token,search,page,limit)



    suspend fun addFeedbacknratingApi(token: String,appointmentId:String,doctorId: String,title: String,rating: Double,message: String): Response<AddPostResponse>
    = apiService.addFeedbacknratingApi(token=token,appointmentId=appointmentId,doctorId=doctorId,title=title,rating=rating,message=message)



    suspend fun listfeedbacknratingApi(token: String,search:String,page: Int,limit: Int): Response<UserFeedBackListResponse>
    = apiService.listfeedbacknratingApi(token=token,search=search,page=page,limit=limit)

    suspend fun cancelAppointmentApi(token: String,id: String): Response<AddPostResponse>
    = apiService.cancelAppointmentApi(token = token, _id = id)

    suspend fun canceleAppointmentByday(token: String,date: String): Response<AddPostResponse>
    = apiService.canceleAppointmentByday(token = token, date = date)

    suspend fun petBreedListApi(petCategoryId: String): Response<CountryResponse>
    = apiService.petBreedListApi(petCategoryId = petCategoryId)
    suspend fun appConfig(): Response<AppConfigResponse> = apiService.appConfig()
    suspend fun getSubscriptionApi(token:String): Response<GetSubscriptionDetailsResponse> = apiService.getSubscriptionApi(token = token)


    suspend fun makePaymentApi(token: String,stripeToken:String,totalCost:String,curr:String,plan_id:String): Response<AddPostResponse> = apiService.makePaymentApi(token=token, stripeToken=stripeToken, totalCost=totalCost, curr=curr,plan_id=plan_id)


    suspend fun checkPlanApi(token: String): Response<CheckLimitPlanResponse> = apiService.checkPlanApi(token=token)


    suspend fun listSubscriptionApi(type: String,userType: String,moduleName: String): Response<PlanListResponse> = apiService.listSubscriptionApi(type=type,userType=userType,moduleName=moduleName)



    suspend fun createContactUsApi(token: String,name: String,email: String,mobileNumber: String,reason: String): Response<AddPostResponse>
    = apiService.createContactUsApi(token=token,name=name,email=email,mobileNumber=mobileNumber,reason=reason)



}

