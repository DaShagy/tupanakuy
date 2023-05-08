package com.dshagapps.tupanakuy.common.domain.model

data class Classroom(
    override val uid: String = "",
    val teacherUID: String = "",
    val studentUIDs: List<String> = listOf()
): Entity(uid) {
    override fun copyWithUid(uid: String): Entity {
        return copy(uid = uid)
    }
}
