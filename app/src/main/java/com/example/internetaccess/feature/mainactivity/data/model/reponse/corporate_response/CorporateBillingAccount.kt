package com.example.internetaccess.feature.mainactivity.data.model.reponse.corporate_response


import com.google.gson.annotations.SerializedName

data class CorporateBillingAccount(
    @SerializedName("accountTypeId")
    val accountTypeId: Int,
    @SerializedName("commissionRate")
    val commissionRate: Int,
    @SerializedName("corporateId")
    val corporateId: Int,
    @SerializedName("creationDate")
    val creationDate: String,
    @SerializedName("creatorId")
    val creatorId: Int,
    @SerializedName("currentBalance")
    val currentBalance: Double,
    @SerializedName("deleted")
    val deleted: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastModifiedDate")
    val lastModifiedDate: String,
    @SerializedName("openingBalance")
    val openingBalance: Double,
    @SerializedName("subscriptionAmount")
    val subscriptionAmount: Int,
    @SerializedName("suspended")
    val suspended: Boolean,
    @SerializedName("thresholdAmount")
    val thresholdAmount: Int,
    @SerializedName("version")
    val version: Int
)