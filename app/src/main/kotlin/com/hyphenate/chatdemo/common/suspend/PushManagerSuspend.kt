package com.hyphenate.chatdemo.common.suspend

import com.hyphenate.easeui.common.ChatException
import com.hyphenate.easeui.common.ChatPushManager
import com.hyphenate.easeui.common.ChatSilentModeParam
import com.hyphenate.easeui.common.ChatSilentModeResult
import com.hyphenate.easeui.common.impl.ValueCallbackImpl
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Set the silent mode for the App.
 * @param silentModeParam The silent mode param, see [ChatSilentModeParam].
 * @return The result of setting silent mode. See [ChatSilentModeResult].
 */
suspend fun ChatPushManager.setSilentModeForApp(silentModeParam: ChatSilentModeParam): ChatSilentModeResult {
    return suspendCoroutine { continuation ->
        setSilentModeForAll(silentModeParam, ValueCallbackImpl<ChatSilentModeResult>(
            onSuccess = {
                continuation.resume(it)
            },
            onError = { error, errorDescription ->
                continuation.resumeWithException(ChatException(error, errorDescription))
            }
        ))
    }
}

/**
 * Get the silent mode for the App.
 * @return The result of getting silent mode. See [ChatSilentModeResult].
 */
suspend fun ChatPushManager.getSilentModeForApp(): ChatSilentModeResult {
    return suspendCoroutine { continuation ->
        getSilentModeForAll(ValueCallbackImpl<ChatSilentModeResult>(
            onSuccess = {
                continuation.resume(it)
            },
            onError = { error, errorDescription ->
                continuation.resumeWithException(ChatException(error, errorDescription))
            }
        ))
    }
}