package com.hyphenate.chatdemo

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hyphenate.chatdemo.callkit.CallKitManager
import com.hyphenate.chatdemo.common.DemoDataModel
import com.hyphenate.chatdemo.common.ListenersWrapper
import com.hyphenate.chatdemo.common.extensions.internal.checkAppKey
import com.hyphenate.chatdemo.common.push.PushManager
import com.hyphenate.chatdemo.ui.chat.ChatActivity
import com.hyphenate.chatdemo.ui.contact.ChatContactDetailActivity
import com.hyphenate.chatdemo.ui.group.ChatGroupDetailActivity
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.common.ChatMessage
import com.hyphenate.easeui.common.ChatOptions
import com.hyphenate.easeui.common.PushConfigBuilder
import com.hyphenate.easeui.common.extensions.toProfile
import com.hyphenate.easeui.common.impl.OnValueSuccess
import com.hyphenate.easeui.feature.chat.activities.EaseChatActivity
import com.hyphenate.easeui.feature.contact.EaseContactDetailsActivity
import com.hyphenate.easeui.feature.group.EaseGroupDetailActivity
import com.hyphenate.easeui.model.EaseGroupProfile
import com.hyphenate.easeui.model.EaseProfile
import com.hyphenate.easeui.provider.EaseCustomActivityRoute
import com.hyphenate.easeui.provider.EaseGroupProfileProvider
import com.hyphenate.easeui.provider.EaseSettingsProvider
import com.hyphenate.easeui.provider.EaseUserProfileProvider

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
            // Register necessary listeners
            ListenersWrapper.registerListeners()
            EaseIM.init(context, this)
            if (EaseIM.isInited()) {
                // debug mode, you'd better set it to false, if you want release your App officially.
                ChatClient.getInstance().setDebugMode(true)
                // Initialize push.
                initPush()
                // Set the UIKit options.
                addUIKitSettings()
                // Initialize the callkit module.
                initCallKit()
            }
        }
    }

    private fun addUIKitSettings() {
        EaseIM.setUserProfileProvider(object : EaseUserProfileProvider {
                override fun getUser(userId: String?): EaseProfile? {
                    return getDataModel().getUser(userId)?.toProfile()
                }

                override fun fetchUsers(
                    userIds: List<String>,
                    onValueSuccess: OnValueSuccess<List<EaseProfile>>
                ) {
                    // fetch users from server and call call onValueSuccess.onSuccess(users) after successfully getting users
                }
            })
            .setGroupProfileProvider(object : EaseGroupProfileProvider {

                override fun getGroup(id: String?): EaseGroupProfile? {
                    return null
                }

                override fun fetchGroups(
                    groupIds: List<String>,
                    onValueSuccess: OnValueSuccess<List<EaseGroupProfile>>
                ) {

                }
            })
            .setSettingsProvider(object : EaseSettingsProvider {
                override fun isMsgNotifyAllowed(message: ChatMessage?): Boolean {
                    return true
                }

                override fun isMsgSoundAllowed(message: ChatMessage?): Boolean {
                    return false
                }

                override fun isMsgVibrateAllowed(message: ChatMessage?): Boolean {
                    return false
                }

                override val isSpeakerOpened: Boolean
                    get() = true

            })
            .setCustomActivityRoute(object : EaseCustomActivityRoute {
                override fun getActivityRoute(intent: Intent): Intent? {
                    intent.component?.className?.let {
                        when(it) {
                            EaseChatActivity::class.java.name -> {
                                intent.setClass(context, ChatActivity::class.java)
                            }
                            EaseGroupDetailActivity::class.java.name -> {
                                intent.setClass(context, ChatGroupDetailActivity::class.java)
                            }
                            EaseContactDetailsActivity::class.java.name -> {
                                intent.setClass(context, ChatContactDetailActivity::class.java)
                            }

                            else -> {
                                return intent
                            }
                        }
                    }
                    return intent
                }

            })

        EaseIM.getConfig()?.avatarConfig?.let {
            it.avatarShape = com.hyphenate.easeui.widget.EaseImageView.ShapeType.RECTANGLE
            it.avatarRadius = context.resources.getDimensionPixelSize(com.hyphenate.easeui.R.dimen.ease_corner_extra_small)
        }
    }

    private fun initPush() {
        PushManager.initPush(context)
    }

    /**
     * Get the notifier.
     */
    fun getNotifier() = EaseIM.getNotifier()

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