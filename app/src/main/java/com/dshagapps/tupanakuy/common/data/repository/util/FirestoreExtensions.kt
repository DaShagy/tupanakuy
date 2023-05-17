package com.dshagapps.tupanakuy.common.data.repository.util

import com.dshagapps.tupanakuy.common.domain.model.Entity
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

object FirestoreExtensions {
    inline fun <reified T: Entity> CollectionReference.getDomainEntities(crossinline listener: (OperationResult<List<T>>) -> Unit): Task<List<T>> {
        return this.get().continueWithTask { task ->
            if (task.isSuccessful) {
                val entities = mutableListOf<T>()
                task.result?.forEach { doc ->
                    doc.toObject(T::class.java)?.let { entity ->
                        entities.add(entity)
                    }
                }
                Tasks.forResult(entities.toList())
            } else {
                Tasks.forException(task.exception ?: Exception("Couldn't retrieve ${T::class.java.simpleName} entities from firestore"))
            }
        }.addOnSuccessListener { entities ->
            listener(OperationResult.Success(entities))
        }.addOnFailureListener { exception ->
            listener(OperationResult.Failure(exception))
        }
    }

    inline fun <reified T: Entity> Query.getDomainEntities(crossinline listener: (OperationResult<List<T>>) -> Unit): Task<List<T>> {
        return this.get().continueWithTask { task ->
            if (task.isSuccessful) {
                val entities = mutableListOf<T>()
                task.result?.forEach { doc ->
                    doc.toObject(T::class.java)?.let { entity ->
                        entities.add(entity)
                    }
                }
                Tasks.forResult(entities.toList())
            } else {
                Tasks.forException(task.exception ?: Exception("Couldn't retrieve ${T::class.java.simpleName} entities from firestore"))
            }
        }.addOnSuccessListener { entities ->
            listener(OperationResult.Success(entities))
        }.addOnFailureListener { exception ->
            listener(OperationResult.Failure(exception))
        }
    }

    inline fun <reified T: Entity> DocumentReference.getDomainEntity(crossinline listener: (OperationResult<T>) -> Unit): Task<T?> {
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

    inline fun <reified T : Entity> DocumentReference.setDomainEntity(entity: T, crossinline listener: (OperationResult<T>) -> Unit): Task<T> {
        return this.set(entity.copyWithUid(this.id))
            .onSuccessTask {
                Tasks.forResult(entity)
            }.addOnSuccessListener { newEntity ->
                listener(OperationResult.Success(newEntity))
            }.addOnFailureListener { exception ->
                listener(OperationResult.Failure(exception))
            }
    }

    inline fun <reified T : Entity> DocumentReference.setDomainEntityIfNotExists(
        entity: T,
        crossinline listener: (OperationResult<T>) -> Unit
    ): Task<T> {
        return this.get()
            .onSuccessTask { doc ->
                Tasks.forResult(doc.toObject(T::class.java))
            }.continueWithTask { task ->
                if (task.result == null) {
                    this.set(entity.copyWithUid(this.id)).onSuccessTask {
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

    inline fun <reified T : Entity> DocumentReference.observeDomainEntity(crossinline listener: (OperationResult<T>) -> Unit): ListenerRegistration {
        return this.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener(OperationResult.Failure(exception))
                return@addSnapshotListener
            }

            val entity = snapshot?.toObject(T::class.java)

            entity?.let {
                listener(OperationResult.Success(it))
            } ?: listener(OperationResult.Failure(Exception("Couldn't retrieve ${T::class.java.simpleName} from firestore")))
        }
    }
}