package com.dshagapps.tupanakuy.common.data.repository

import com.dshagapps.tupanakuy.common.data.repository.util.FirebaseExtensions.checkIfDomainEntityExists
import com.dshagapps.tupanakuy.common.data.repository.util.FirebaseExtensions.getDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirebaseExtensions.setDomainEntity
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.firebase.firestore.FirebaseFirestore

class DataRepositoryImpl(private val firestore: FirebaseFirestore): DataRepository {
    override fun getUserInfo(uid: String, listener: (OperationResult<User>) -> Unit) {
        firestore.collection("users").document(uid).getDomainEntity(listener)
    }

    override fun setUserInfo(user: User, listener: (OperationResult<User>) -> Unit) {
        firestore.collection("users").document(user.uid).setDomainEntity(user, listener)
    }

    override fun checkUserInfo(uid: String, listener: (OperationResult<Unit>) -> Unit) {
        firestore.collection("users").document(uid).checkIfDomainEntityExists<User>(listener)
    }
}