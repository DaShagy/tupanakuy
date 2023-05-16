package com.dshagapps.tupanakuy.common.domain.use_case

import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.firebase.firestore.ListenerRegistration

class RemoveChatObserverUseCase(private val repository: DataRepository) {
    operator fun invoke(listenerRegistration: ListenerRegistration) =
        repository.removeChatListener(listenerRegistration)
}