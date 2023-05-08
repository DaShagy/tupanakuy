package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class AddStudentToClassroomUseCase(private val repository: DataRepository) {
    operator fun invoke(
        classroomUid: String,
        studentUid: String,
        listener: (OperationResult<Classroom>) -> Unit
    ) = repository.addStudentToClassroom(classroomUid, studentUid, listener)
}
