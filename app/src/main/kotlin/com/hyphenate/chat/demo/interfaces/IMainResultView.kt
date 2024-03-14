package com.hyphenate.chat.demo.interfaces

import com.hyphenate.easeui.common.interfaces.IControlDataView

interface IMainResultView: IControlDataView {

    /**
     * Get unread message count successfully.
     */
    fun getUnreadCountSuccess(count: String?)
}