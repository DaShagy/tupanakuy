package com.dshagapps.tupanakuy.common.domain.model

import com.dshagapps.tupanakuy.common.domain.model.enum.UserType

data class User(
    override val uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val userType: UserType = UserType.STUDENT
): Entity(uid) {
    override fun copyWithUid(uid: String): Entity {
        return copy(uid = uid)
    }
}
