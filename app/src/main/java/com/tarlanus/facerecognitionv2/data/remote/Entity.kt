package com.tarlanus.facerecognitionv2.data.remote

import com.google.gson.annotations.SerializedName


data class VerifyResponse(
    @SerializedName("verified")
    val verified: Boolean? = null,
    @SerializedName("distance")
    val distance: Float? = null,
    @SerializedName("max_threshold_to_verify")
    val max_threshold_to_verify: Float? = null
)