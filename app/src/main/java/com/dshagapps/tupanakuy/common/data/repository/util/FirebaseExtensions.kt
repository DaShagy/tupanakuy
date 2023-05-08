package com.dshagapps.tupanakuy.common.data.repository.util

import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference

object FirebaseExtensions {
    inline fun <reified T: Any> DocumentReference.getDomainEntity(crossinline listener: (OperationResult<T>) -> Unit): Task<T?> {
        return this.get()
            .onSuccessTask { doc ->
                Tasks.forResult(doc.toObject(T::class.java))
            }.addOnSuccessListener { entity ->
                entity?.let {
                    listener(OperationResult.Success(it))
                } ?: listener(OperationResult.Failure(Exception("Couldn't retrieve ${T::class.java.simpleName} from firestore")))
            }.addOnFailureListener { exception ->
                listener(OperationResult.Failure(exception))
            }
    }

    inline fun <reified T : Any> DocumentReference.setDomainEntity(entity: T, crossinline listener: (OperationResult<T>) -> Unit): Task<T> {
        return this.set(entity)
            .onSuccessTask {
                Tasks.forResult(entity)
            }.addOnSuccessListener { newEntity ->
                listener(OperationResult.Success(newEntity))
            }.addOnFailureListener { exception ->
                listener(OperationResult.Failure(exception))
            }
    }

    inline fun <reified T : Any> DocumentReference.setDomainEntityIfNotExists(
        entity: T,
        crossinline listener: (OperationResult<T>) -> Unit
    ): Task<T> {
        return this.get()
            .onSuccessTask { doc ->
                Tasks.forResult(doc.toObject(T::class.java))
            }.continueWith { task ->
                if (task.result == null) {
                    this.set(entity).onSuccessTask {
                        Tasks.forResult(entity)
                    }
                } else {
                    Tasks.forResult(task.result)
                }
            }.onSuccessTask {
                Tasks.forResult(entity)
            }.addOnSuccessListener {
                listener(OperationResult.Success(entity))
            }.addOnFailureListener { exception ->
                listener(OperationResult.Failure(exception))
            }
    }
}