package com.dshagapps.tupanakuy.auth.domain.use_case

import com.dshagapps.tupanakuy.auth.domain.repository.AuthRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class SignOutUseCase(private val repository: AuthRepository) {
    operator fun invoke(): OperationResult<Unit> = repository.signOut()
}