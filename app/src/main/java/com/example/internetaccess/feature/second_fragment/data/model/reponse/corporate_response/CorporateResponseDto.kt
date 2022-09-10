package com.example.internetaccess.feature.second_fragment.data.model.reponse.corporate_response


import com.google.gson.annotations.SerializedName

data class CorporateResponseDto(
    @SerializedName("billingAccount")
    val corporateBillingAccount: CorporateBillingAccount,
    @SerializedName("billingAddress")
    val billingAddress: String,
    @SerializedName("capitalValue")
    val capitalValue: Int,
    @SerializedName("cityId")
    val cityId: Int,
    @SerializedName("commercialRegistrationNumber")
    val commercialRegistrationNumber: String,
    @SerializedName("corporateLevelId")
    val corporateLevelId: Int,
    @SerializedName("countryId")
    val countryId: Int,
    @SerializedName("creationDate")
    val creationDate: String,
    @SerializedName("creatorId")
    val creatorId: Int,
    @SerializedName("deleted")
    val deleted: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("enName")
    val enName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastModifiedDate")
    val lastModifiedDate: String,
    @SerializedName("legalrepresentativeId")
    val legalrepresentativeId: String,
    @SerializedName("legalrepresentativeName")
    val legalrepresentativeName: String,
    @SerializedName("localeName")
    val localeName: String,
    @SerializedName("suspended")
    val suspended: Boolean,
    @SerializedName("taxId")
    val taxId: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("zoneId")
    val zoneId: Int
)