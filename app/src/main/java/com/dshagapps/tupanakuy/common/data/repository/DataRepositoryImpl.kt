package com.dshagapps.tupanakuy.common.data.repository

import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.getDomainEntities
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.getDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.setDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.setDomainEntityIfNotExists
import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore

class DataRepositoryImpl(private val firestore: FirebaseFirestore): DataRepository {
    override fun getUserInfo(uid: String, listener: (OperationResult<User>) -> Unit) {
        firestore.collection("users").document(uid).getDomainEntity(listener)
    }

    override fun setUserInfo(user: User, listener: (OperationResult<User>) -> Unit) {
        firestore.collection("users").document(user.uid).setDomainEntity(user, listener)
    }

    override fun setUserInfoIfNotExists(user: User, listener: (OperationResult<User>) -> Unit) {
        firestore.collection("users").document(user.uid).setDomainEntityIfNotExists(user, listener)
    }

    override fun setClassroom(
        classroom: Classroom,
        listener: (OperationResult<Classroom>) -> Unit
    ) {
        firestore.collection("classrooms").document().setDomainEntity(classroom, listener)
    }

    override fun getClassrooms(listener: (OperationResult<List<Classroom>>) -> Unit) {
        firestore.collection("classrooms").getDomainEntities(listener)
    }

    override fun addStudentToClassroom(
        classroomUid: String,
        studentUid: String,
        listener: (OperationResult<Classroom>) -> Unit
    ) {
        val docRef = firestore.collection("classrooms").document(classroomUid)
        docRef.get().onSuccessTask { doc ->
            Tasks.forResult(doc.toObject(Classroom::class.java))
        }.continueWithTask { task ->
            task.result?.let { classroom ->
                val newStudentList = classroom.studentUIDs.toMutableList()
                if (!newStudentList.contains(studentUid))
                    newStudentList.add(studentUid)
                val newClassroom = classroom.copy(studentUIDs = newStudentList)
                docRef.setDomainEntity(newClassroom, listener)
            }
        }.addOnSuccessListener {
            it?.let { data -> listener(OperationResult.Success(data)) }
                ?: listener(OperationResult.Failure(Exception("Something went wrong")))
        }.addOnFailureListener {
            listener(OperationResult.Failure(it))
        }
    }
}