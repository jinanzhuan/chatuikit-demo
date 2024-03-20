package com.hyphenate.chatdemo.viewmodel

import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.common.ChatException
import com.hyphenate.easeui.common.ChatGroup
import com.hyphenate.easeui.common.ChatGroupManager
import com.hyphenate.easeui.common.ChatValueCallback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GroupRepository: BaseRepository() {
    val groupManager = ChatClient.getInstance().groupManager()

    /**
     * Suspend method for [ChatGroupManager.asyncGetJoinedGroupsFromServer()]
     */
    suspend fun asyncGetJoinedGroupsFromServer(): List<ChatGroup> {
        return suspendCoroutine { continuation->
            groupManager.asyncGetJoinedGroupsFromServer(object : ChatValueCallback<MutableList<ChatGroup>> {
                override fun onSuccess(value: MutableList<ChatGroup>) {
                    continuation.resume(value)
                }
                override fun onError(error: Int, errorMsg: String?) {
                    continuation.resumeWithException(ChatException(error, errorMsg))
                }
            })
        }
    }

}