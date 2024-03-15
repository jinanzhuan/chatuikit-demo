package com.hyphenate.chat.demo.callkit.extensions

import com.hyphenate.easecallkit.base.EaseCallUserInfo
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.model.EaseProfile
import com.hyphenate.easeui.provider.getSyncUser

/**
 * Get userInfo.
 */
internal fun EaseCallUserInfo.getUserInfo(groupId: String?): Any {
    return if (!groupId.isNullOrEmpty()) {
        EaseProfile.getGroupMember(groupId, this.userId)?.let {
            this.nickName = it.getRemarkOrName()
            this.headImage = it.avatar
        }
        this
    } else {
        EaseIM.getUserProvider()?.getSyncUser(this.userId)?.let {
            this.nickName = it.getRemarkOrName()
            this.headImage = it.avatar
        }
        this
    }
}