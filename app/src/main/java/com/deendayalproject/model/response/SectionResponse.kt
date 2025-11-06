package com.deendayalproject.model.response

data class SectionResponse(
    val wrappedList: List<SectionData>,
    val errorsMap: Map<String, Any>,
    val responseCode: Int,
    val responseDesc: String
)

data class SectionData(
    val livingAreaInfoSection: Int,
    val toiletSection: Int,
    val basiInfoSection: Int,
    val infraDtlComplianceSection: Int,
    val rfAvailableSection: Int,
    val nonLivingAreaSection: Int,
    val indoorGameDtlSection: Int,
    val supportFacltySection: Int
)
