package com.hyphenate.chatdemo

import android.content.Context
import android.util.Log
import com.hyphenate.chatdemo.callkit.CallKitManager
import com.hyphenate.chatdemo.common.DemoDataModel
import com.hyphenate.chatdemo.common.extensions.internal.checkAppKey
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.common.ChatOptions
import com.hyphenate.easeui.common.PushConfigBuilder

class DemoHelper private constructor(){

    private lateinit var dataModel: DemoDataModel
    var hasAppKey = false
    lateinit var context: Context

    @Synchronized
    fun init(context: Context) {
        this.context = context.applicationContext
        dataModel = DemoDataModel(context)
    }

    fun getDataModel(): DemoDataModel {
        return dataModel
    }

    /**
     * Check if the SDK has been initialized.
     */
    fun isSDKInited(): Boolean {
        return EaseIM.isInited()
    }

    /**
     * Initialize the SDK.
     */
    @Synchronized
    fun initSDK() {
        if (::context.isInitialized.not()) {
            Log.e(TAG, "Please call init method first.")
            return
        }
        initChatOptions(context).apply {
            hasAppKey = checkAppKey(context)
            if (!hasAppKey) {
                Log.e(TAG, "App key is null or empty.")
                return
            }
            EaseIM.init(context, this)
            if (EaseIM.isInited()) {
                // debug mode, you'd better set it to false, if you want release your App officially.
                ChatClient.getInstance().setDebugMode(true)
                // Initialize push.
                // Set the UIKit options.
                // Initialize the callkit module.
                initCallKit()
            }
        }
    }

    private fun initCallKit() {
        CallKitManager.init(context)
    }

    /**
     * Set chat options.
     * Note: Developers need to set the options according to needs.
     */
    private fun initChatOptions(context: Context): ChatOptions {
        return ChatOptions().apply {
            // set the appkey
            appKey = BuildConfig.APPKEY
            // set if accept the invitation automatically, default true
            acceptInvitationAlways = false
            // set if you need read ack
            requireAck = true
            // set if you need delivery ack
            requireDeliveryAck = false
            // Set whether the sent message is included in the message listener, default false
            isIncludeSendMessageInMessageListener = true
            // Set whether to turn on local message traffic statistics, default false
            isEnableStatistics = true
            /**
             * Note: Developers need to apply your own push accounts and replace the following
             */
            pushConfig = PushConfigBuilder(context)
                .enableVivoPush()                                   // need to configure appid and appkey in AndroidManifest.xml
                .enableMiPush(BuildConfig.MI_PUSH_APPID, BuildConfig.MI_PUSH_APPID)
                .enableOppoPush(BuildConfig.OPPO_PUSH_APPKEY, BuildConfig.OPPO_PUSH_APPSECRET)
                .enableHWPush()                                     // need to configure appid in AndroidManifest.xml
                .enableFCM(BuildConfig.FCM_SENDERID)
                .enableHonorPush()                                  // need to configure appid in AndroidManifest.xml
                .build()

            if (dataModel.isCustomSetEnable()) {
                dataModel.getCustomAppKey()?.let {
                    appKey = it
                }
                if (dataModel.isCustomServerEnable()) {
                    // Turn off DNS configuration
                    enableDNSConfig(false)
                    restServer = dataModel.getRestServer()?.ifEmpty { null }
                    setIMServer(dataModel.getIMServer()?.let {
                        if (it.contains(":")) {
                            imPort = it.split(":")[1].toInt()
                            it.split(":")[0]
                        } else {
                            it.ifEmpty { null }
                        }
                    })
                    val port = dataModel.getIMServerPort()
                    if (port != 0) {
                        imPort = port
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "DemoHelper"
        private var instance: DemoHelper? = null
        fun getInstance(): DemoHelper {
            if (instance == null) {
                synchronized(DemoHelper::class.java) {
                    if (instance == null) {
                        instance = DemoHelper()
                    }
                }
            }
            return instance!!
        }
    }
}