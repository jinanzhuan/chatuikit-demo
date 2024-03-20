package com.hyphenate.chatdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hyphenate.easeui.common.ChatClient
import kotlinx.coroutines.flow.flow

class ProfileInfoViewModel(application: Application) : AndroidViewModel(application)  {
    private val mRepository: ProfileInfoRepository = ProfileInfoRepository()
    fun uploadAvatar(filePath: String?)= flow {
        emit(mRepository.uploadAvatar(filePath))
    }

    fun setUserRemark(username:String,remark:String) = flow {
        emit(mRepository.setUserRemark(username,remark))
    }

    fun fetchLocalUserRemark(userId:String):String{
        val contact = ChatClient.getInstance().contactManager().fetchContactFromLocal(userId)
        return contact.remark
    }

    fun getGroupAvatar(groupId:String?) = flow {
        emit(mRepository.getGroupAvatar(groupId))
    }

}