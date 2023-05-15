package com.dshagapps.tupanakuy.auth.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.util.OperationResult

abstract class AuthUseCase {
    abstract operator fun invoke(
        email: String,
        password: String,
        onCompleteListener: (OperationResult<User>) -> Unit
    )
}