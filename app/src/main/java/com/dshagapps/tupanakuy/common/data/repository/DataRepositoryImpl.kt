package com.dshagapps.tupanakuy.common.data.repository

import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.getDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.setDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.setDomainEntityIfNotExists
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

    override fun setUserInfoIfNotExists(user: User, listener: (OperationResult<User>) -> Unit) {
        firestore.collection("users").document(user.uid).setDomainEntityIfNotExists(user, listener)
    }
}