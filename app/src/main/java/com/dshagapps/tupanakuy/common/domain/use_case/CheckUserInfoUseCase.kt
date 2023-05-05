package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class CheckUserInfoUseCase(private val repository: DataRepository) {
    operator fun invoke(uid: String, listener: (OperationResult<Unit>) -> Unit) =
        repository.checkUserInfo(uid, listener)
}