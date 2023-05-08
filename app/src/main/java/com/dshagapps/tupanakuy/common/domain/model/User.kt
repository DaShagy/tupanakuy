package com.dshagapps.tupanakuy.common.domain.model

import com.dshagapps.tupanakuy.common.domain.model.enum.UserType

data class User(
    val uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val userType: UserType = UserType.STUDENT
)
