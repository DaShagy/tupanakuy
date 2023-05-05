package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class GetUserInfoUseCase(private val repository: DataRepository) {
    operator fun invoke(uid: String, listener: (OperationResult<User>) -> Unit) =
        repository.getUserInfo(uid, listener)
}