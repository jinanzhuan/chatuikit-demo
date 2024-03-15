package com.hyphenate.chat.demo.interfaces

import com.hyphenate.easeui.viewmodel.IAttachView

interface IMainRequest: IAttachView {

    /**
     * Get all unread message count.
     */
    fun getUnreadMessageCount()
}