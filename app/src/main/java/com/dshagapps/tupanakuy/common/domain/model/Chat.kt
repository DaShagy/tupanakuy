package com.dshagapps.tupanakuy.common.domain.model

data class Chat (
    override val uid: String = "",
    val messages: List<Message> = listOf()
): Entity() {
    override fun copyWithUid(uid: String): Entity {
        return this.copy(uid = uid)
    }
}