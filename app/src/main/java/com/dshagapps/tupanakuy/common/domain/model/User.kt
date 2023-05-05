package com.dshagapps.tupanakuy.common.domain.model

data class User(
    val uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null
)
