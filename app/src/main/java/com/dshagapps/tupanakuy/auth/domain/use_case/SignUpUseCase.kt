package com.dshagapps.tupanakuy.auth.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.auth.domain.repository.AuthRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class SignUpUseCase(private val repository: AuthRepository): AuthUseCase() {
    override operator fun invoke(
        email: String,
        password: String,
        onCompleteListener: (OperationResult<User>) -> Unit
    ) = repository.signUp(email, password, onCompleteListener)
}