package com.hyphenate.chat.demo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.repository.EasePresenceRepository
import kotlinx.coroutines.flow.flow

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: EMClientRepository = EMClientRepository()
    private val presenceRepository by lazy { EasePresenceRepository() }

    /**
     * Register account.
     * @param userName
     * @param pwd
     * @return
     */
    fun register(userName: String?, pwd: String?) =
        flow {
            emit(mRepository.registerToHx(userName, pwd))
        }

    /**
     * Logout from Chat server.
     */
    fun logout() =
        flow {
            emit(mRepository.logout(true))
        }

    /**
     * fetch current user presence
     */
    fun fetchCurrentUserPresence()=
        flow {
            emit(presenceRepository.fetchPresenceStatus(mutableListOf(ChatClient.getInstance().currentUser)))
        }

}