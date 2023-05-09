package com.dshagapps.tupanakuy.common.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Message(
    override val uid: String = "",
    val authorUID: String = "",
    val content: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null
): Entity() {
    override fun copyWithUid(uid: String): Entity {
        return this.copy(uid = uid)
    }
    fun copyWithTimestamp(timestamp: Timestamp): Message {
        return this.copy(timestamp = timestamp)
    }
}