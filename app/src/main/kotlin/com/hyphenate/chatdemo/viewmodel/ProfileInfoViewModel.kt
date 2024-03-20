package com.hyphenate.chatdemo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.hyphenate.easeui.model.EaseProfile
import kotlinx.coroutines.flow.flow

class ProfileInfoViewModel(application: Application) : AndroidViewModel(application)  {
    private val mRepository: ProfileInfoRepository = ProfileInfoRepository()
    fun uploadAvatar(filePath: String?)= flow {
        Log.e("apex","uploadAvatar $filePath")
        emit(mRepository.uploadAvatar(filePath))
    }

    fun setUserRemark(username:String,remark:String) = flow {
        emit(mRepository.setUserRemark(username,remark))
    }

    fun getGroupAvatar(groupId:String?) = flow {
        emit(mRepository.getGroupAvatar(groupId))
    }

}