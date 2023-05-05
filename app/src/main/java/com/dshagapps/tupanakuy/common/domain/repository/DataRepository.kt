package com.dshagapps.tupanakuy.common.domain.repository

import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.util.OperationResult

interface DataRepository {
    fun getUserInfo(uid: String, listener: (OperationResult<User>) -> Unit)
}