package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.Chat
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class ObserveChatByIdUseCase(private val repository: DataRepository) {
    operator fun invoke(chatUid: String, chatListener: (OperationResult<Chat>) -> Unit) =
        repository.setChatListener(chatUid, chatListener)
}