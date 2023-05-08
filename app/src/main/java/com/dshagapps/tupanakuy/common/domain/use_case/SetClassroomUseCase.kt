package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class SetClassroomUseCase(private val repository: DataRepository) {
    operator fun invoke(classroom: Classroom, listener: (OperationResult<Classroom>) -> Unit) =
        repository.setClassroom(classroom, listener)
}