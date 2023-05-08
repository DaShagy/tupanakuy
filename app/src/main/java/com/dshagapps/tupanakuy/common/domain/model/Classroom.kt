package com.dshagapps.tupanakuy.common.domain.model

data class Classroom(
    val teacherUID: String = "",
    val studentUIDs: List<String> = listOf()
)
