package com.dshagapps.tupanakuy.auth.data.mapper

import com.dshagapps.tupanakuy.auth.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomainUser(): User =
    User(
        uid,
        email,
        displayName,
        photoUrl?.toString(),
        phoneNumber
    )