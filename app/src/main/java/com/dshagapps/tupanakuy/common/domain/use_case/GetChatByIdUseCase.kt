package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.Chat
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class GetChatByIdUseCase(private val repository: DataRepository) {
    operator fun invoke(chatUid: String, listener: (OperationResult<Chat>) -> Unit) =
        repository.getChatById(chatUid, listener)
}