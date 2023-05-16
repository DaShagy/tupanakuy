package com.dshagapps.tupanakuy.common.data.repository

import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.getDomainEntities
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.getDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.observeDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.setDomainEntity
import com.dshagapps.tupanakuy.common.data.repository.util.FirestoreExtensions.setDomainEntityIfNotExists
import com.dshagapps.tupanakuy.common.domain.model.Chat
import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.model.Message
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

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
        val classroomDocRef = firestore.collection("classrooms").document()

        if (classroom.chatUID.isNotEmpty()) {
            classroomDocRef.setDomainEntity(classroom, listener)
        } else {
            val chatDocRef =firestore.collection("chats").document()
            chatDocRef.setDomainEntityIfNotExists(Chat()) {
                when (it) {
                    is OperationResult.Failure -> listener(OperationResult.Failure(it.exception))
                    else -> classroomDocRef.setDomainEntity(
                        classroom.copy(chatUID = chatDocRef.id),
                        listener
                    )
                }
            }
        }
    }

    override fun getClassrooms(listener: (OperationResult<List<Classroom>>) -> Unit) {
        firestore.collection("classrooms").getDomainEntities(listener)
    }

    override fun getClassroomById(
        uid: String,
        listener: (OperationResult<Classroom>) -> Unit
    ) {
        firestore.collection("classrooms").document(uid).getDomainEntity(listener)
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
                if (!newStudentList.contains(studentUid)) {
                    newStudentList.add(studentUid)
                    val newClassroom = classroom.copy(studentUIDs = newStudentList)
                    docRef.setDomainEntity(newClassroom, listener)
                } else {
                    Tasks.forException(Exception("Student is already subscribed to classroom"))
                }
            }
        }.addOnSuccessListener {
            listener(OperationResult.Success(it))
        }.addOnFailureListener {
            listener(OperationResult.Failure(it))
        }
    }

    override fun removeStudentFromClassroom(
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
                if (newStudentList.contains(studentUid)) {
                    newStudentList.remove(studentUid)
                    val newClassroom = classroom.copy(studentUIDs = newStudentList)
                    docRef.setDomainEntity(newClassroom, listener)
                } else {
                    Tasks.forException(Exception("Student is not subscribed to classroom"))
                }
            }
        }.addOnSuccessListener {
            listener(OperationResult.Success(it))
        }.addOnFailureListener {
            listener(OperationResult.Failure(it))
        }
    }

    override fun sendMessageToChat(message: Message, chatUid: String, listener: (OperationResult<Message>) -> Unit) {
        val chatDocRef = firestore.collection("chats").document(chatUid)
        chatDocRef.get().onSuccessTask { doc ->
            Tasks.forResult(doc.toObject(Chat::class.java))
        }.continueWithTask { task ->
            task.result?.let { chat ->
                val messageDocRef = firestore.collection("messages").document()
                val newMessage = message.copyWithTimestamp(Timestamp.now()).copyWithUid(messageDocRef.id)
                messageDocRef.setDomainEntity(newMessage) { messageResult ->
                    when (messageResult) {
                        is OperationResult.Failure -> listener(OperationResult.Failure(messageResult.exception))
                        is OperationResult.Success -> {
                            val newMessageList = chat.messages.toMutableList()
                            newMessageList.add(newMessage as Message)
                            val newChat = chat.copy(messages = newMessageList)
                            chatDocRef.setDomainEntity(newChat) { chatResult ->
                                when (chatResult) {
                                    is OperationResult.Failure -> listener(OperationResult.Failure(chatResult.exception))
                                    is OperationResult.Success -> listener(OperationResult.Success(newMessage))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun registerChatListener(chatUid: String, chatListener: (OperationResult<Chat>) -> Unit): ListenerRegistration {
        return firestore.collection("chats").document(chatUid).observeDomainEntity(chatListener)
    }

    override fun removeChatListener(listenerRegistration: ListenerRegistration) {
        listenerRegistration.remove()
    }
}