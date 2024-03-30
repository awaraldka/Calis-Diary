package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class BlockedUserResponse(
    @SerializedName("result") val result: BlockedUserResult,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val responseCode: Int
)


class BlockedUserResult(
    @SerializedName("docs") val docs: List<AppointmentListUserId>,
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("totalPages") val pages: Int
)
