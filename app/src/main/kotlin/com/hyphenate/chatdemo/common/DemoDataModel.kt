package com.hyphenate.chatdemo.common

import android.content.Context
import com.hyphenate.chatdemo.common.room.AppDatabase
import com.hyphenate.chatdemo.common.room.dao.DemoGroupDao
import com.hyphenate.chatdemo.common.room.dao.DemoUserDao
import com.hyphenate.chatdemo.common.room.entity.parse
import com.hyphenate.chatdemo.common.room.extensions.parseToDbBean
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.model.EaseProfile

class DemoDataModel(private val context: Context) {

    private val database by lazy { AppDatabase.getDatabase(context, ChatClient.getInstance().currentUser) }

    init {
        PreferenceManager.init(context)
    }

    /**
     * Initialize the local database.
     */
    fun initDb() {
        if (EaseIM.isInited().not()) {
            throw IllegalStateException("EaseIM SDK must be inited before using.")
        }
        database
    }

    /**
     * Get the user data access object.
     */
    fun getUserDao(): DemoUserDao {
        if (EaseIM.isInited().not()) {
            throw IllegalStateException("EaseIM SDK must be inited before using.")
        }
        return database.userDao()
    }

    /**
     * Get the group data access object.
     */
    fun getGroupDao(): DemoGroupDao {
        if (EaseIM.isInited().not()) {
            throw IllegalStateException("EaseIM SDK must be inited before using.")
        }
        return database.groupDao()
    }

    /**
     * Get user by userId from local db.
     */
    fun getUser(userId: String?): EaseProfile? {
        if (userId.isNullOrEmpty()) {
            return null
        }
        return getUserDao().getUser(userId)?.parse()
    }

    /**
     * Insert user to local db.
     */
    fun insertUser(user: EaseProfile) {
        getUserDao().insertUser(user.parseToDbBean())
    }

    /**
     * Insert users to local db.
     */
    fun insertUsers(users: List<EaseProfile>) {
        getUserDao().insertUsers(users.map { it.parseToDbBean() })
    }


    /**
     * Set the flag whether to use google push.
     * @param useFCM
     */
    fun setUseFCM(useFCM: Boolean) {
        PreferenceManager.putValue(KEY_PUSH_USE_FCM, useFCM)
    }

    /**
     * Get the flag whether to use google push.
     * @return
     */
    fun isUseFCM(): Boolean {
        return PreferenceManager.getValue(KEY_PUSH_USE_FCM, false)
    }

    /**
     * Set the developer mode.
     * @param isDeveloperMode The developer mode.
     */
    fun setDeveloperMode(isDeveloperMode: Boolean) {
        PreferenceManager.putValue(KEY_DEVELOPER_MODE, isDeveloperMode)
    }

    /**
     * Get the developer mode.
     * @return The developer mode.
     */
    fun isDeveloperMode(): Boolean {
        return PreferenceManager.getValue(KEY_DEVELOPER_MODE, false)
    }

    /**
     * Set the flag whether the user has agreed the agreement.
     */
    fun setAgreeAgreement(isAgreed: Boolean) {
        PreferenceManager.putValue(KEY_AGREE_AGREEMENT, isAgreed)
    }

    /**
     * Get the flag whether the user has agreed the agreement.
     */
    fun isAgreeAgreement(): Boolean {
        return PreferenceManager.getValue(KEY_AGREE_AGREEMENT, false)
    }

    /**
     * Set the custom appKey.
     * @param appKey
     */
    fun setCustomAppKey(appKey: String?) {
        PreferenceManager.putValue(KEY_CUSTOM_APPKEY, appKey)
    }

    /**
     * Get the custom appKey.
     * @return
     */
    fun getCustomAppKey(): String? {
        return PreferenceManager.getValue(KEY_CUSTOM_APPKEY, null)
    }

    /**
     * Get whether the custom configuration is enabled.
     * @return
     */
    fun isCustomSetEnable(): Boolean {
        return PreferenceManager.getValue(KEY_ENABLE_CUSTOM_SET, false)
    }

    /**
     * Set whether the custom configuration is enabled.
     * @param enable
     */
    fun enableCustomSet(enable: Boolean) {
        PreferenceManager.putValue(KEY_ENABLE_CUSTOM_SET, enable)
    }

    /**
     * Get whether the custom server is enabled.
     * @return
     */
    fun isCustomServerEnable(): Boolean {
        return PreferenceManager.getValue(KEY_ENABLE_CUSTOM_SERVER, false)
    }

    /**
     * Set whether the custom server is enabled.
     * @param enable
     */
    fun enableCustomServer(enable: Boolean) {
        PreferenceManager.putValue(KEY_ENABLE_CUSTOM_SERVER, enable)
    }

    /**
     * Set the REST server.
     * @param restServer
     */
    fun setRestServer(restServer: String?) {
        PreferenceManager.putValue(KEY_REST_SERVER, restServer)
    }

    /**
     * Get the REST server.
     * @return
     */
    fun getRestServer(): String? {
        return PreferenceManager.getValue(KEY_REST_SERVER, "")
    }

    /**
     * Set the IM server.
     * @param imServer
     */
    fun setIMServer(imServer: String?) {
        PreferenceManager.putValue(KEY_IM_SERVER, imServer)
    }

    /**
     * Get the IM server.
     * @return
     */
    fun getIMServer(): String? {
        return PreferenceManager.getValue(KEY_IM_SERVER, "")
    }

    /**
     * Set the port of the IM server.
     * @param port
     */
    fun setIMServerPort(port: Int) {
        PreferenceManager.putValue(KEY_IM_SERVER_PORT, port)
    }

    /**
     * Get the port of the IM server.
     */
    fun getIMServerPort(): Int {
        return PreferenceManager.getValue(KEY_IM_SERVER_PORT, 0)
    }

    /**
     * Set the silent mode for the App.
     */
    fun setAppPushSilent(isSilent: Boolean) {
        PreferenceManager.putValue(KEY_PUSH_APP_SILENT_MODEL, isSilent)
    }

    /**
     * Get the silent mode for the App.
     */
    fun isAppPushSilent(): Boolean {
        return PreferenceManager.getValue(KEY_PUSH_APP_SILENT_MODEL, false)
    }


    companion object {
        private const val KEY_DEVELOPER_MODE = "shared_is_developer"
        private const val KEY_AGREE_AGREEMENT = "shared_key_agree_agreement"
        private const val KEY_CUSTOM_APPKEY = "SHARED_KEY_CUSTOM_APPKEY"
        private const val KEY_REST_SERVER = "SHARED_KEY_REST_SERVER"
        private const val KEY_IM_SERVER = "SHARED_KEY_IM_SERVER"
        private const val KEY_IM_SERVER_PORT = "SHARED_KEY_IM_SERVER_PORT"
        private const val KEY_ENABLE_CUSTOM_SERVER = "SHARED_KEY_ENABLE_CUSTOM_SERVER"
        private const val KEY_ENABLE_CUSTOM_SET = "SHARED_KEY_ENABLE_CUSTOM_SET"
        private const val KEY_PUSH_USE_FCM = "shared_key_push_use_fcm"
        private const val KEY_PUSH_APP_SILENT_MODEL = "key_push_app_silent_model"
    }

}