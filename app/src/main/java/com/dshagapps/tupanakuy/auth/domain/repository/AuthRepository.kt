package com.dshagapps.tupanakuy.auth.domain.repository

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.util.OperationResult

interface AuthRepository {
    fun checkAuthState(): OperationResult<User>
    fun signUp(email: String, password: String, onCompleteListener: (OperationResult<User>) -> Unit)
    fun signOut(): OperationResult<Unit>
    fun signIn(email: String, password: String, onCompleteListener: (OperationResult<User>) -> Unit)
}