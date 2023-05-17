package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class GetClassroomUsersInfoUseCase(private val repository: DataRepository) {
    operator fun invoke(userUIDs: List<String>, listener: (OperationResult<List<User>>) -> Unit) =
        repository.getUsersInfo(userUIDs, listener)
}