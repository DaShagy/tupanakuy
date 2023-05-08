package com.dshagapps.tupanakuy.common.domain.repository

import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.util.OperationResult

interface DataRepository {
    fun getUserInfo(uid: String, listener: (OperationResult<User>) -> Unit)
    fun setUserInfo(user: User, listener: (OperationResult<User>) -> Unit)
    fun setUserInfoIfNotExists(user: User, listener: (OperationResult<User>) -> Unit)
    fun setClassroom(classroom: Classroom, listener: (OperationResult<Classroom>) -> Unit)
}