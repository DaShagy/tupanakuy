package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class GetClassroomByIdUseCase(private val repository: DataRepository) {
    operator fun invoke(uid: String, listener: (OperationResult<Classroom>) -> Unit) =
        repository.getClassroomById(uid, listener)
}
