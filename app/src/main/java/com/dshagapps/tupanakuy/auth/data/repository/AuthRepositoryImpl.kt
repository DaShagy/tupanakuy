package com.dshagapps.tupanakuy.auth.data.repository

import com.dshagapps.tupanakuy.auth.data.mapper.toDomainUser
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.auth.domain.repository.AuthRepository
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl(private val auth: FirebaseAuth): AuthRepository {
    override fun checkAuthState(): OperationResult<User> {
        val currentUser = auth.currentUser
        return currentUser?.let { OperationResult.Success(it.toDomainUser()) }
            ?: OperationResult.Failure(Exception("No user signed in"))
    }

    override fun signUp(email: String, password: String, onCompleteListener: (OperationResult<User>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.let {
                    onCompleteListener(OperationResult.Success(it.toDomainUser()))
                } ?: OperationResult.Failure(Exception("No user signed in"))
            } else {
                onCompleteListener(OperationResult.Failure(Exception("Could not sign up new user")))
            }
        }
    }

    override fun signOut(): OperationResult<Unit> {
        return OperationResult.Success(auth.signOut())
    }

    override fun signIn(email: String, password: String, onCompleteListener: (OperationResult<User>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.let {
                    onCompleteListener(OperationResult.Success(it.toDomainUser()))
                } ?: OperationResult.Failure(Exception("No user signed in"))
            } else {
                onCompleteListener(OperationResult.Failure(Exception("Could not sign in user")))
            }
        }
    }
}