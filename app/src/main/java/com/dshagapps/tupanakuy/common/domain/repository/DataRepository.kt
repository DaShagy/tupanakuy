package com.dshagapps.tupanakuy.common.domain.repository

import android.net.nsd.NsdManager.RegistrationListener
import com.dshagapps.tupanakuy.common.domain.model.Chat
import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.model.Message
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.google.firebase.firestore.ListenerRegistration

interface DataRepository {
    fun getUserInfo(uid: String, listener: (OperationResult<User>) -> Unit)
    fun setUserInfo(user: User, listener: (OperationResult<User>) -> Unit)
    fun setUserInfoIfNotExists(user: User, listener: (OperationResult<User>) -> Unit)
    fun setClassroom(classroom: Classroom, listener: (OperationResult<Classroom>) -> Unit)
    fun getClassrooms(listener: (OperationResult<List<Classroom>>) -> Unit)
    fun getClassroomById(uid: String, listener: (OperationResult<Classroom>) -> Unit)
    fun addStudentToClassroom(
        classroomUid: String,
        studentUid: String,
        listener: (OperationResult<Classroom>) -> Unit
    )
    fun removeStudentFromClassroom(
        classroomUid: String,
        studentUid: String,
        listener: (OperationResult<Classroom>) -> Unit
    )
    fun sendMessageToChat(message: Message, chatUid: String, listener: (OperationResult<Message>) -> Unit)
    fun registerChatListener(chatUid: String, chatListener: (OperationResult<Chat>) -> Unit): ListenerRegistration
    fun removeChatListener(listenerRegistration: ListenerRegistration)
    fun getUsersInfo(userUIDs: List<String> = listOf(), listener: (OperationResult<List<User>>) -> Unit)
}