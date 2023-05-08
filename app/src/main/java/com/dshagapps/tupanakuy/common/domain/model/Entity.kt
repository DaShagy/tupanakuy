package com.dshagapps.tupanakuy.common.domain.model

abstract class Entity(open val uid: String = ""){
    abstract fun copyWithUid(uid: String): Entity
}