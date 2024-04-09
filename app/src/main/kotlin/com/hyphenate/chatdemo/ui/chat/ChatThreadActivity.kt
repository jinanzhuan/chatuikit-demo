package com.hyphenate.chatdemo.ui.chat

import com.hyphenate.easeui.common.enums.EaseTranslationLanguageType
import com.hyphenate.easeui.feature.chat.EaseChatFragment
import com.hyphenate.easeui.feature.thread.EaseChatThreadActivity
import java.util.Locale

class ChatThreadActivity: EaseChatThreadActivity() {
    override fun setChildSettings(builder: EaseChatFragment.Builder) {
        super.setChildSettings(builder)
        builder.setTargetTranslation(EaseTranslationLanguageType.from(Locale.getDefault().language))
    }
}