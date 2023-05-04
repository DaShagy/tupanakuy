package com.dshagapps.tupanakuy.common.util

sealed class OperationResult<out T> {
    class Success<T>(val data: T): OperationResult<T>()
    class Failure(val exception: Exception): OperationResult<Nothing>()
}