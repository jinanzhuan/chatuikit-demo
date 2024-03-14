package com.hyphenate.chat.demo.viewmodel

import com.hyphenate.easeui.common.ChatClient
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

}