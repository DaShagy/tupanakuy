package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class SetUserInfoUseCase(private val repository: DataRepository) {
    operator fun invoke(user: User, listener: (OperationResult<User>) -> Unit) =
        repository.setUserInfo(user, listener)
}