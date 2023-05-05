package com.dshagapps.tupanakuy.common.domain.repository

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.util.OperationResult

interface DataRepository {
    fun getUserInfo(uid: String, listener: (OperationResult<User>) -> Unit)
    fun setUserInfo(user: User, listener: (OperationResult<User>) -> Unit)
    fun checkUserInfo(uid: String, listener: (OperationResult<Unit>) -> Unit)
}