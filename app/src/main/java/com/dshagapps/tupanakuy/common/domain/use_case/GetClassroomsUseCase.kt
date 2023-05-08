package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class GetClassroomsUseCase(private val repository: DataRepository) {
    operator fun invoke(listener: (OperationResult<List<Classroom>>) -> Unit) =
        repository.getClassrooms(listener)
}