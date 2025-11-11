package com.deendayalproject.model.request

data class RfFinalSubmitReq(

    val loginId: String,
    val appVersion: String,
    val imeiNo: String,
    val trainingCentre: Int,
    val sanctionOrder: String,
    val facilityId: String

)
