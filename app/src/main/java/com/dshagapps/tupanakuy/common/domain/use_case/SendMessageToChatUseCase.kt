package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.model.Chat
import com.dshagapps.tupanakuy.common.domain.model.Message
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult

class SendMessageToChatUseCase(private val repository: DataRepository) {
    operator fun invoke(message: Message, chatUid: String, listener: (OperationResult<Chat>) -> Unit) =
        repository.sendMessageToChat(message, chatUid, listener)
}