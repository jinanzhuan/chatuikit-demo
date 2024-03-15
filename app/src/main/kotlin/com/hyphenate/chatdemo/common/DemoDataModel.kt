package com.hyphenate.chatdemo.common

import android.content.Context

class DemoDataModel(private val context: Context) {

    init {
        PreferenceManager.init(context)
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


    companion object {
        private const val KEY_DEVELOPER_MODE = "shared_is_developer"
        private const val KEY_AGREE_AGREEMENT = "shared_key_agree_agreement"
        private const val KEY_CUSTOM_APPKEY = "SHARED_KEY_CUSTOM_APPKEY"
        private const val KEY_REST_SERVER = "SHARED_KEY_REST_SERVER"
        private const val KEY_IM_SERVER = "SHARED_KEY_IM_SERVER"
        private const val KEY_IM_SERVER_PORT = "SHARED_KEY_IM_SERVER_PORT"
        private const val KEY_ENABLE_CUSTOM_SERVER = "SHARED_KEY_ENABLE_CUSTOM_SERVER"
        private const val KEY_ENABLE_CUSTOM_SET = "SHARED_KEY_ENABLE_CUSTOM_SET"
    }

}