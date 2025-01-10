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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface Api_Interface {

    @POST("user/signUp")
    suspend fun signUpAPi(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("user/signUp")
    suspend fun signInApiVendor(@Body request: SignUpRequestVendor): Response<SignUpResponse>

    @POST("user/signUp")
    suspend fun signInApiVendorDoctor(@Body request: SignUpRequestVendorDoctor): Response<SignUpResponse>


    @GET("user/getAllCountry")
    suspend fun getAllCountryApi(): Response<CountryResponse>

    @GET("user/getAllState")
    suspend fun getAllStateApi(@Query("countryCode") countryCode: String): Response<CountryResponse>

    @POST("pet/petCategoryList")
    suspend fun petCategoryApi(): Response<CountryResponse>

    @GET("user/getAllCity")
    suspend fun getAllCityApi(
        @Query("countryCode") countryCode: String,
        @Query("stateCode") stateCode: String
    ): Response<CountryResponse>


    @PATCH("user/verifyOTP")
    suspend fun verifyOTPApi(@Body jsonObject: JsonObject): Response<VerifyOtpResponse>

    @PUT("user/resendOTP")
    suspend fun resendOTPApi(@Body jsonObject: JsonObject): Response<VerifyOtpResponse>


    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<LogInResponse>

    @POST("user/socialLogin")
    suspend fun socialLogin(@Body request: JsonObject): Response<LogInResponse>


    @PUT("user/forgotPassword")
    suspend fun forgotPasswordApi(@Body jsonObject: JsonObject): Response<SignUpResponse>


    @FormUrlEncoded
    @POST("user/resetPassword")
    suspend fun resetPasswordApi(
        @Field("_email") token: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): Response<SignUpResponse>


    @FormUrlEncoded
    @POST("user/tagPeopleList")
    suspend fun tagPeopleListApi(
        @Header("token") token: String,
        @Field("search") search: String
    ): Response<TagPeopleResponse>


    @GET("category/listCategory")
    suspend fun listCategoryApi(
        @Query("type") categoryTypes: String, @Query("page") page: Int, @Query("limit") limit: Int): Response<ListCategoryResponse>


    @FormUrlEncoded
    @POST("product/listProduct")
    suspend fun listProductApi(@Field("approveStatus") approveStatus: String): Response<ProductListResponse>


    @PATCH("user/changePassword")
    suspend fun changePassword(
        @Header("token") token: String,
        @Body jsonObject: JsonObject
    ): Response<SignUpResponse>


    @Multipart
    @POST("post/addPost")
    suspend fun addPostApi(
        @Header("token") token: String,
        @Part file: ArrayList<MultipartBody.Part>,
        @Part("title") caption: String,
        @Part("metaWords") metaWords: ArrayList<String>,
        @Part("tagPeople") tagPeople: ArrayList<String>,
        @Part("lat") lat: Double,
        @Part("long") long: Double,
        @Part("address") address: String,
    ): Response<AddPostResponse>



    @PUT("user/editProfile")
    suspend fun editProfileApi(
        @Header("token") token: String,
        @Body request: EditProfileRequest
    ): Response<EditProfileResponse>


    @GET("user/profile")
    suspend fun getProfileApi(@Header("token") token: String): Response<EditProfileResponse>


    @GET("static/faqList")
    suspend fun faqListApi(@Query("search")search:String): Response<FaqResponse>



    @POST("event/addEventForMobile")
    suspend fun addEventApi(
        @Header("token") token: String,
        @Body request: AddEventRequest
    ): Response<AddEventResponse>


    @PUT("event/updateEventForMobile")
    suspend fun updateEventApi(
        @Header("token") token: String,
        @Body request: EditEventRequest
    ): Response<AddEventResponse>


    @Multipart
    @POST("post/addStoryMobile")
    suspend fun addStoryApi(
        @Header("token") token: String, @Part file: ArrayList<MultipartBody.Part>,
        @Part("caption")caption:String): Response<AddStoryResponse>


    @Multipart
    @POST("post/uploadMultipleFile")
    suspend fun uploadMultipleFileApi(@Part file: ArrayList<MultipartBody.Part>): Response<UploadFileResponse>


    @FormUrlEncoded
    @POST("post/homePagePostList")
    suspend fun homePagePostListApi(
        @Header("token") token: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int,
        @Field("radius") radius: String,
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("lat") lat: Double,
        @Field("long") long: Double,
        @Field("petBreedId") petBreedId: String
    ): Response<HomePageListResponse>


    @FormUrlEncoded
    @POST("post/commentList")
    suspend fun commentListApi(
        @Header("token") token: String,
        @Field("postId") postId: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<CommentListResponse>


    @FormUrlEncoded
    @POST("post/addComment")
    suspend fun addCommentApi(
        @Header("token") token: String,
        @Field("typeOfPost") typeOfPost: String,
        @Field("postId") postId: String,
        @Field("comment") comment: String,
        @Field("commentId") commentId: String,
        @Field("reply") reply: String
    ): Response<HomePageListResponse>

    @FormUrlEncoded
    @POST("post/updateComment")
    suspend fun updateCommentApi(
        @Header("token") token: String,
        @Field("typeOfPost") typeOfPost: String,
        @Field("postId") postId: String,
        @Field("comment") comment: String,
        @Field("commentId") commentId: String,
        @Field("reply") reply: String
    ): Response<updateCommentResponse>


    @FormUrlEncoded
    @POST("post/addComment")
    suspend fun addStoryCommentApi(
        @Header("token") token: String,
        @Field("typeOfPost") typeOfPost: String,
        @Field("storyId") storyId: String,
        @Field("reply") reply: String
    ): Response<AddEventResponse>


    @GET("post/repliesList")
    suspend fun repliesListApi(
        @Header("token") token: String,
        @Query("commentId") commentId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<RepliesListResponse>


    @FormUrlEncoded
    @POST("post/addLike")
    suspend fun addLikeApi(
        @Header("token") token: String,
        @Field("typeOfPost") typeOfPost: String,
        @Field("postId") postId: String,
        @Field("commentId") commentId: String
    ): Response<AddLikeResponse>


    @FormUrlEncoded
    @POST("user/listInterested")
    suspend fun listInterestedApi(
        @Header("token") token: String,
        @Field("type") type: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<IntrestedPetResponse>


    @GET("pet/viewPet")
    suspend fun viewPetApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<ViewPetResponse>


    @GET("service/viewService")
    suspend fun viewServiceApi(
        @Header("token") token: String,
        @Query("serviceId") _id: String
    ): Response<ViewServiceResponse>

    @FormUrlEncoded
    @POST("user/addToInterested")
    suspend fun addToInterestedApi(
        @Header("token") token: String, @Field("type") type: String, @Field("petId") petId: String,
        @Field("productId") productId: String, @Field("serviceId") serviceId: String
    ): Response<AddToIntrestedResponse>


    @GET("product/viewProduct")
    suspend fun viewProductApi(
        @Header("token") token: String,
        @Query("productId") productId: String
    ): Response<ViewProductResponse>


    @PUT("product/likeUnlikeProduct")
    suspend fun likeUnlikeProductApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<LikeUnlikeProductsResponse>


    @PUT("service/likeUnlikeServices")
    suspend fun likeUnlikeServicesApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<LikeUnlikeProductsResponse>


    @PUT("pet/favUnfavPet")
    suspend fun favUnfavPetApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<LikeUnlikeProductsResponse>


    @GET("pet/myFavPets")
    suspend fun myFavPets(
        @Header("token") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<FavPetResponse>


        @GET("service/myLikesServices")
    suspend fun myLikesServicesApi(
        @Header("token") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<FavServiceResponse>


    @GET("product/myLikesProduct")
    suspend fun myLikesProductApi(
        @Header("token") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<FavProductsResponse>


    @FormUrlEncoded
    @POST("product/listProductV2")
    suspend fun listProductV2Api(
        @Header("token") token: String,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double,
        @Field("categoryId") categoryId: String,
        @Field("subCategoryId") subCategoryId: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int,
        @Field("maxDistance") maxDistance: Int,
        @Field("radius") radius: String,
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("search") search: String,
    ): Response<ProductListResponse>

    @FormUrlEncoded
    @POST("pet/listPet")
    suspend fun listPetApi(
        @Header("token") token: String,
        @Field("page") page: Int, @Field("limit") limit: Int, @Field("lat") lat: Double,
        @Field("long") lng: Double,
        @Field("radius") radius: String,
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("search") search: String,
    ): Response<PetListResponse>


    @FormUrlEncoded
        @POST("service/listServiceV2")
    suspend fun listServiceV2Api(
        @Header("token") token: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int,
        @Field("categoryId") categoryId: String,
        @Field("subCategoryId") subCategoryId: String,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double,
        @Field("maxDistance") maxDistance: Int,
        @Field("radius") radius: String,
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("search") search: String,
    ): Response<ServiceListResponse>


    @GET("user/listUser")
    suspend fun listSuggestedUserApi(
        @Header("token") token: String,
        @Query("discover") discover: String,
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<suggestionListResponse>


    @GET("category/listSubCategory")
    suspend fun listSubCategoryApi(@Query("categoryId") categoryId: String): Response<SubCategoryResponse>


    @GET("category/listSubCategory")
    suspend fun listSubCategoryApis(@Query("categoryId") categoryId: String): Response<PetCategoryListResponse>

    @FormUrlEncoded
    @POST("event/listEvent")
    suspend fun listEventApi(
        @Header("token") token: String,
        @Field("search") search: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int,
        @Field("radius") radius: String,
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("lat") lat: Double,
        @Field("long") long: Double
    ): Response<ListEventResponse>

    @FormUrlEncoded
    @POST("event/myListEvent")
    suspend fun myListEventApi(
        @Header("token") token: String,
        @Field("search") search: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<ListEventResponse>


    @GET("event/viewEvent")
    suspend fun viewEvent(@Query("eventId") eventId: String): Response<ViewEventResponse>


    @DELETE("event/deleteEvent")
    suspend fun deleteEventApi(
        @Header("token") token: String,
        @Query("eventId") eventId: String
    ): Response<AddEventResponse>


    @FormUrlEncoded
    @POST("user/missingPetList")
    suspend fun missingPetListApi(
        @Header("token") token: String,
        @Field("search") search: String,
        @Field("type") type: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int,
        @Field("radius")  radius : String,
        @Field("country")  country : String,
        @Field("state")  state : String,
        @Field("city")  city : String,
        @Field("lat") lat: Double,
        @Field("long") long: Double,
        @Field("petBreedId") petBreedId:String
    ): Response<MissingPetResponse>


    @GET("user/viewMissingPet")
    suspend fun viewMissingPetApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<ViewMissingPetResponse>


    @GET("user/listSearchUser")
    suspend fun peopleSearchListApi(
        @Header("token") token: String,
        @Query("search") search: String
    ): Response<SearchPeopleResponse>


    @POST("user/addMissingPet")
    suspend fun addMissingPetApi(
        @Header("token") token: String,
        @Body request: AddMissingPetRequest
    ): Response<AddToIntrestedResponse>


    @PUT("user/editMissingPet")
    suspend fun editMissingPetApi(
        @Header("token") token: String,
        @Query("_id") _id: String,
        @Body request: AddMissingPetRequest
    ): Response<AddToIntrestedResponse>


    @DELETE("user/deleteMissingPet")
    suspend fun deleteMissingPetApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<AddEventResponse>


    @GET("admin/viewRewardsPoints")
    suspend fun viewRewardsPointsApi(@Header("token") token: String): Response<RewardsPointResponse>


    @GET("static/static/{type}")
    suspend fun staticContentApi(@Path("type") aboutUs: String): Response<StaticContentResponse>


    @GET("user/viewStatus")
    suspend fun viewStatusApi(@Header("token") token: String): Response<ViewStatusResponse>


    @GET("user/updateStatus")
    suspend fun updateStatusApi(@Header("token") token: String): Response<AddEventResponse>


    @PUT("user/followUnfollowUser/{userId}")
    suspend fun followUnfollowUserApi(
        @Header("token") token: String,
        @Path("userId") userId: String
    ): Response<AddToIntrestedResponse>


    @FormUrlEncoded
    @POST("user/followingUserList")
    suspend fun followingUserListApi(@Header("token") token: String,@Field("page") page: Int,
                                     @Field("limit") limit: Int, @Field("search") search: String): Response<FollowingUserListResponse>


    @FormUrlEncoded
    @POST("post/myPostList")
    suspend fun myPostListApi(
        @Header("token") token: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<MyPostResponse>


    @GET("post/viewPost")
    suspend fun viewPostApi(
        @Header("token") token: String,
        @Query("postId") postId: String
    ): Response<ViewPostResponse>


    @FormUrlEncoded
    @POST("pet/myPetList")
    suspend fun myPetListApi(
        @Header("token") token: String,
        @Field("search") search: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int,
        @Field("fromDate")fromDate: String,
        @Field("toDate")toDate: String,
        @Field("publishStatus")publishStatus: String,
    ): Response<MyPetListResponse>


    @GET("user/othersProfile")
    suspend fun othersProfile(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<OtherUserResponse>


    @FormUrlEncoded
    @POST("pet/otherUserPetList")
    suspend fun otherUserPetListApi(
        @Header("token") token: String,
        @Query("_id") _id: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<MyPetListResponse>


    @FormUrlEncoded
    @POST("post/otherUserPostList")
    suspend fun otherUserPostListApi(
        @Header("token") token: String,
        @Field("userId") userId: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<MyPostResponse>


    @GET("user/otherfollowerUserList")
    suspend fun otherfollowerUserListApi(
        @Header("token") token: String,
        @Query("_id") _id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String

    ): Response<FollowersListResponse>


    @GET("user/otherfollowingUserList")
    suspend fun otherfollowingUserListApi(
        @Header("token") token: String,
        @Query("_id") _id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String
    ): Response<FollowingUserListResponse>


    @FormUrlEncoded
    @POST("user/followerUserList")
    suspend fun followerUserListApi(@Header("token") token: String,@Field("page") page: Int,
                                    @Field("limit") limit: Int, @Field("search") search: String
    ): Response<FollowersListResponse>


    @FormUrlEncoded
    @POST("user/globalSearch")
    suspend fun globalSearchApi(
        @Header("token") token: String,
        @Field("search") search: String
    ): Response<GlobalSearchResponse>


    @Multipart
    @POST("post/uploadMultipleFile")
    suspend fun uploadMultipleFile(@Part file: ArrayList<MultipartBody.Part>?): Response<ImageUploadResponse>


    @POST("pet/addPetForMobile")
    suspend fun addPetApi(
        @Header("token") token: String,
        @Body request: AddPetRequest
    ): Response<AddToIntrestedResponse>


    @POST("pet/updatepet")
    suspend fun updatePetApi(
        @Header("token") token: String,
        @Query("_id") petId: String,
        @Body request: UpdatePetRequest
    ): Response<AddToIntrestedResponse>


    @GET("user/notificationList")
    suspend fun notificationListApi(@Header("token") token: String,
                                    @Query("page") page: Int,
                                    @Query("limit") limit: Int): Response<NotificationListResponse>


    @DELETE("user/clearNotification")
    suspend fun clearNotificationApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<AddEventResponse>

    @DELETE("user/deleteUserAccount")
    suspend fun deleteAccount(
        @Header("token") token: String
    ): Response<AddEventResponse>

    @POST("user/deactivateUserAccount")
    suspend fun deactivateAccount(
        @Header("token") token: String
    ): Response<AddEventResponse>


    @GET("category/listCategory")
    suspend fun listCategoryApi(@Query("type") type: String): Response<PetCategoryListResponse>


    @FormUrlEncoded
    @POST("user/requestList")
    suspend fun requestListApi(
        @Header("token") token: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<RequestedListResponse>


    @FormUrlEncoded
    @POST("user/acceptOrDenyRequest")
    suspend fun acceptOrDenyRequestApi(
        @Header("token") token: String,
        @Field("type") type: String,
        @Field("requestId") requestId: String
    ): Response<AddEventResponse>


    @GET("user/privateOrPublicAccount")
    suspend fun privateOrPublicAccountApi(@Header("token") token: String): Response<AddEventResponse>


    @POST("pet/addPetToMarketPlace")
    suspend fun addPetToNFTMarketPlaceApi(
        @Header("token") token: String,
        @Query("_id") id: String,
        @Query("price") price: String,
        @Query("currency") currency: String,
    ): Response<AddEventResponse>


    @POST("pet/removePetToMarketPlace")
    suspend fun removePetToMarketPlaceApi(
        @Header("token") token: String,
        @Query("_id") id: String
    ): Response<AddEventResponse>


    @DELETE("pet/removePet")
    suspend fun removePetApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<AddEventResponse>

    @POST("post/storyList")
    suspend fun storyList(@Header("token") token: String): Response<UserStoriesResponse>


    @FormUrlEncoded
    @POST("post/userPostHide")
    suspend fun hidePostApi(
        @Header("token") token: String,
        @Field("_id") _id: String
    ): Response<AddEventResponse>


    @DELETE("post/removePost")
    suspend fun removePostApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<AddEventResponse>


    @FormUrlEncoded
        @POST("post/createReport")
    suspend fun createReportApi(
        @Header("token") token: String,
        @Field("postId") _id: String,
        @Field("description") description: String,
    ): Response<AddEventResponse>


    @GET("user/agoraTokenGenerator")
    suspend fun agoraTokenGeneratorApi(@Header("token") token: String): Response<AgoraCallingResponse>


    @FormUrlEncoded
    @POST("user/callUser")
    suspend fun callUserApi(
        @Header("token") token: String,
        @Field("receiverId") receiverId: String,
        @Field("callType") callType: String
    ): Response<AddToIntrestedResponse>

    @FormUrlEncoded
    @POST("post/deleteComment")
    suspend fun deleteCommentApi(
        @Header("token") token: String,
        @Field("typeOfPost") typeOfPost: String,
        @Field("postId") postId: String,
        @Field("comment") comment: String,
        @Field("commentId") commentId: String,
        @Field("reply") reply: String
    ): Response<AddEventResponse>


    @FormUrlEncoded
    @POST("user/addUserProfile")
    suspend fun addUserProfileApi(
        @Header("token") token: String,
        @Field("petCategoryType") petCategoryType: String,
        @Field("name") name: String,
        @Field("userProfileImage") userProfileImage: String,
        @Field("petCategoryId") petCategoryId : String,
        @Field("petBreedId") petBreedId : String
    ):Response<AddToIntrestedResponse>


    @POST("user/userProfileList")
    suspend fun userProfileListApi(@Header("token")token:String):Response<PetProfileResponse>


    @FormUrlEncoded
    @POST("user/setDefaultUserProfile")
    suspend fun setDefaultUserProfileApi(@Header("token")token:String,@Field("petCategoryId")petCategoryId :String):Response<AddToIntrestedResponse>


    @POST("pet/petCategoryDetails")
    suspend fun petCategoryDetailsApi(@Query("_id")petTypeId:String):Response<CategoryBasedResponse>


    @GET("admin/listBanner")
    suspend fun listBannerListApi():Response<BannerListResponse>



    @FormUrlEncoded
    @POST("user/setDefaultUserLanguage")
    suspend fun setDefaultUserLanguageApi(@Header("token")token:String,@Field("languageCode")languageCode:String):Response<AddPostResponse>

    @GET("user/logOut")
    suspend fun userLogout(@Header("token")token:String,@Query("fireToken")fireToken:String):Response<AddEventResponse>


    @GET("admin/listExplore")
    suspend fun listExploreApi(@Query("search")search:String,@Query("page")page:Int,@Query("limit")limit:Int):Response<ExploreListResponse>

    @GET("user/vendorDashboard")
    suspend fun vendorDashboard(@Header("token")token:String):Response<VendorDashboardResponse>


    @FormUrlEncoded
    @POST("product/myProductList")
    suspend fun myProductListApi(@Header("token")token:String,@Field("search")search:String
    ,@Field("fromDate")fromDate:String,@Field("toDate")toDate:String,@Field("page")page:Int
    ,@Field("limit")limit:Int,@Field("approveStatus")approveStatus:String):Response<ProductsResponse>


    @POST("product/addProduct")
    suspend fun addProductApi(@Header("token")token:String, @Body request: AddProductRequest):Response<AddPostResponse>


    @FormUrlEncoded
    @POST("service/myServiceList")
    suspend fun myServiceListApi(@Header("token")token:String,@Field("search")search:String
    ,@Field("fromDate")fromDate:String,@Field("toDate")toDate:String,@Field("page")page:Int
    ,@Field("limit")limit:Int,@Field("approveStatus")approveStatus:String):Response<ProductsResponse>


    @POST("service/addService")
    suspend fun addServiceApi(@Header("token")token:String, @Body request: AddServiceRequest):Response<AddPostResponse>


    @DELETE("service/deleteService")
    suspend fun deleteServiceApi(@Header("token")token:String, @Query("_id")_id:String):Response<AddPostResponse>

    @DELETE("product/deleteProduct")
    suspend fun deleteProductApi(@Header("token")token:String, @Query("_id")_id:String):Response<AddPostResponse>


    @FormUrlEncoded
    @POST("user/interestedClientList")
    suspend fun interestedClientListApi(@Header("token")token:String,@Field("type")type :String,
    @Field("page")page :Int,@Field("limit")limit :Int,@Field("productId")productId:String,@Field("serviceId")serviceId:String,@Field("petId")petId:String):Response<InterestedUserResponse>


    @FormUrlEncoded
    @POST("user/interestedProductList")
    suspend fun interestedProductListApi(@Header("token")token:String,@Field("type")type :String,
    @Field("page")page :Int,@Field("limit")limit :Int,@Field("productId")productId:String,@Field("serviceId")serviceId:String,@Field("petId")petId:String):Response<InterestedUserResponse>

    @POST("product/updateProduct")
    suspend fun updateProductApi(@Header("token")token:String, @Body request: UpdateProductRequest):Response<AddPostResponse>

    @PUT("service/updateService")
    suspend fun updateServiceApi(@Header("token")token:String, @Body request: UpdateServiceRequest):Response<AddPostResponse>


    @PUT("user/editProfile")
    suspend fun editProfileVendorApi(
        @Header("token") token: String,
        @Body request: EditProfileVendorRequest
    ): Response<EditProfileResponse>

    @FormUrlEncoded
    @POST("user/redeemPointsList")
    suspend fun redeemPointsListApi(
        @Header("token") token: String,
        @Field("search")search: String,
        @Field("fromDate")fromDate: String,
        @Field("toDate")toDate: String,
        @Field("page")page: Int,
        @Field("limit")limit: Int,
    ): Response<RewardsResponseVendor>



    @POST("user/editDoctorVetProfileAdditionalDetails")
    suspend fun editDoctorVetProfile(@Header("token")token:String, @Body request: EditProfileVetDoctorRequest):Response<AddPostResponse>



    @GET("user/availableSlot")
    suspend fun availableSlotApi(@Header("token")token:String,@Query("date")date:String,@Query("doctorVetId")doctorVetId:String):Response<AppointmentDateResponse>


    @FormUrlEncoded
    @POST("user/listVendor")
    suspend fun listVendorApi(@Header("token")token:String,@Field("userTypes")userTypes:String,@Field("search")search:String,
                              @Field("page")page:Int,@Field("limit",)limit:Int,
                              @Field("radius") radius: String,
                              @Field("country") country: String,
                              @Field("city") city: String,
                              @Field("state") state: String,
                              @Field("lat") lat: Double,
                              @Field("long") long: Double,):Response<VetOrDoctorResponse>

    @GET("user/viewUser")
    suspend fun viewUser(@Header("token")token:String,@Query("_id")_id:String):Response<EditProfileResponse>


    @POST("user/addAppointment")
    suspend fun addAppointmentApi(@Header("token")token:String,@Body request: AddAppointmentRequest):Response<AddPostResponse>

    @FormUrlEncoded
    @POST("user/listUserAppointment")
    suspend fun listUserAppointmentApi(@Header("token")token:String,@Field("search")search:String,
                                       @Field("page")page:Int,@Field("limit")limit:Int,@Field("consultingType")consultingType:String,@Field("appointmentStatus")appointmentStatus:String):Response<AppointmentListResponse>



    @GET("user/viewAppointment")
    suspend fun viewAppointmentApi(@Header("token")token:String,@Query("_id")id:String):Response<ViewAppointmentResponse>


    @FormUrlEncoded
    @POST("user/addBlockDoctorUser")
    suspend fun addBlockDoctorUserAPi(@Header("token")token:String,@Field("userId")userId:String):Response<AddPostResponse>


    @FormUrlEncoded
    @POST("user/markasDoneAppointment")
    suspend fun markAsDoneAppointmentApi(@Header("token")token:String,@Field("appointmentId")appointmentId :String):Response<AddPostResponse>



    @GET("user/listBlockDoctorUser")
    suspend fun listBlockDoctorUserApi(@Header("token")token:String,@Query("search")search:String
                                       ,@Query("page")page:Int,@Query("limit")limit:Int):Response<BlockedUserResponse>



    @FormUrlEncoded
    @POST("user/addFeedbacknrating")
    suspend fun addFeedbacknratingApi(@Header("token")token: String,@Field("appointmentId")appointmentId:String,@Field("doctorId")doctorId:String,
                                      @Field("title")title :String,@Field("rating")rating:Double,@Field("message")message:String):Response<AddPostResponse>


    @GET("user/listfeedbacknrating")
    suspend fun listfeedbacknratingApi(@Header("token")token:String,@Query("search")search:String,@Query("page")page:Int,@Query("limit")limit:Int):Response<UserFeedBackListResponse>



    @FormUrlEncoded
    @POST("user/canceleAppointment")
    suspend fun cancelAppointmentApi(@Header("token")token:String,@Field("_id")_id :String):Response<AddPostResponse>

    @POST("user/canceleAppointmentByday")
    suspend fun canceleAppointmentByday(@Header("token")token:String,@Query("date")date :String):Response<AddPostResponse>




    @FormUrlEncoded
    @POST("pet/petBreedList")
    suspend fun petBreedListApi(@Field("petCategoryId") petCategoryId: String): Response<CountryResponse>



    @GET("admin/appConfig")
    suspend fun appConfig():Response<AppConfigResponse>



    @POST("user/getSubscription")
    suspend fun getSubscriptionApi(@Header("token")token:String):Response<GetSubscriptionDetailsResponse>


    @FormUrlEncoded
    @POST("user/makePayment")
    suspend fun makePaymentApi(@Header("token")token:String,@Field("stripeToken")stripeToken:String,
                               @Field("totalCost")totalCost:String,@Field("curr")curr:String,@Field("plan_id")plan_id:String): Response<AddPostResponse>


    @POST("user/checkPlan")
    suspend fun checkPlanApi(@Header("token")token:String):Response<CheckLimitPlanResponse>


    @FormUrlEncoded
    @POST("admin/listSubscription")
    suspend fun listSubscriptionApi(@Field("type")type:String,@Field("userType")userType:String,@Field("moduleName")moduleName:String):Response<PlanListResponse>


    @FormUrlEncoded
    @POST("contact/createContactUs")
    suspend fun createContactUsApi(@Header("token")token:String,@Field("name")name:String,@Field("email")email:String,@Field("mobileNumber")mobileNumber:String,@Field("reason")reason:String):Response<AddPostResponse>


}








