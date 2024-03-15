package com.hyphenate.chat.demo.viewmodel

import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.feature.invitation.helper.EaseNotificationMsgManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * As the repository of ChatManager, handles ChatManager related logic
 */
class ChatManagerRepository: BaseRepository() {

    /**
     * Get all unread message count.
     */
    suspend fun getAllUnreadMessageCount(): Int =
        withContext(Dispatchers.IO) {
            ChatClient.getInstance().chatManager().unreadMessageCount
        }

    /**
     * Get all unread request count.
     */
    suspend fun getRequestUnreadCount():Int =
        withContext(Dispatchers.IO) {
            val systemConversation = EaseNotificationMsgManager.getInstance().getConversation()
            systemConversation.unreadMsgCount
        }

}