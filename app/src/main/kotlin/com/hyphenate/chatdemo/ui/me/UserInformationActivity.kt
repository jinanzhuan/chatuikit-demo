package com.hyphenate.chatdemo.ui.me

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.hyphenate.chatdemo.R
import com.hyphenate.chatdemo.common.DemoConstant
import com.hyphenate.chatdemo.databinding.DemoActivityMeInformationBinding
import com.hyphenate.chatdemo.viewmodel.ProfileInfoViewModel
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.base.EaseBaseActivity
import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.common.ChatImageUtils
import com.hyphenate.easeui.common.ChatLog
import com.hyphenate.easeui.common.ChatPathUtils
import com.hyphenate.easeui.common.EaseConstant
import com.hyphenate.easeui.common.bus.EaseFlowBus
import com.hyphenate.easeui.common.dialog.CustomDialog
import com.hyphenate.easeui.common.dialog.SimpleListSheetDialog
import com.hyphenate.easeui.common.extensions.catchChatException
import com.hyphenate.easeui.common.extensions.dpToPx
import com.hyphenate.easeui.common.extensions.isSdcardExist
import com.hyphenate.easeui.common.extensions.mainScope
import com.hyphenate.easeui.common.extensions.showToast
import com.hyphenate.easeui.common.permission.PermissionCompat
import com.hyphenate.easeui.common.utils.EaseCompat
import com.hyphenate.easeui.common.utils.EaseFileUtils
import com.hyphenate.easeui.configs.setAvatarStyle
import com.hyphenate.easeui.interfaces.SimpleListSheetItemClickListener
import com.hyphenate.easeui.model.EaseEvent
import com.hyphenate.easeui.model.EaseMenuItem
import com.hyphenate.easeui.model.EaseProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File


class UserInformationActivity:EaseBaseActivity<DemoActivityMeInformationBinding>(),
    View.OnClickListener {

    private var cameraFile: File? = null
    private var selfProfile:EaseProfile? = null
    private var showSelectDialog:SimpleListSheetDialog? = null
    private var imageUri:Uri?= null
    private lateinit var model: ProfileInfoViewModel

    override fun getViewBinding(inflater: LayoutInflater): DemoActivityMeInformationBinding? {
        return DemoActivityMeInformationBinding.inflate(inflater)
    }
    companion object {
        private const val REQUEST_CODE_STORAGE_PICTURE = 111
        private const val REQUEST_CODE_CAMERA = 112
        private const val REQUEST_CODE_LOCAL_EDIT = 113
        private const val RESULT_CODE_CAMERA = 114
        private const val RESULT_CODE_LOCAL = 115
        private const val RESULT_CODE_LOCAL_EDIT = 116
        private const val RESULT_CODE_UPDATE_NAME = 117
        private const val RESULT_REFRESH = "isRefresh"
    }

    private val requestCameraPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            onRequestResult(
                result,
                REQUEST_CODE_CAMERA
            )
        }

    private val requestImagePermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            onRequestResult(
                result,
                REQUEST_CODE_STORAGE_PICTURE
            )
        }

    private val requestEditImagePermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            onRequestResult(
                result,
                REQUEST_CODE_LOCAL_EDIT
            )
        }

    private val launcherToCamera: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onActivityResult(result, RESULT_CODE_CAMERA)
    }
    private val launcherToAlbum: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onActivityResult(result, RESULT_CODE_LOCAL) }

    private val launcherToAlbumEdit: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onActivityResult(result, RESULT_CODE_LOCAL_EDIT) }

    private val launcherToUpdateName: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onActivityResult(result, RESULT_CODE_UPDATE_NAME) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
        initData()
    }

    private fun initView(){
        EaseIM.getConfig()?.avatarConfig?.setAvatarStyle(binding.ivAvatar)
        binding.run {
            ivAvatar.setRadius(8.dpToPx(this@UserInformationActivity))
            ivAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        updateLocalData()
    }

    private fun initListener(){
        binding.run {
            titleBar.setNavigationOnClickListener{ finish() }
            avatarLayout.setOnClickListener(this@UserInformationActivity)
            nickNameLayout.setOnClickListener(this@UserInformationActivity)
        }
    }

    private fun initData(){
        model = ViewModelProvider(this)[ProfileInfoViewModel::class.java]
    }

    private fun updateLocalData(){
        binding.run {
            selfProfile = EaseIM.getCurrentUser()
            selfProfile?.let {
                it.avatar?.let { avatar->
                    ivAvatar.load(avatar)
                }
                tvNickName.text = it.name?: ""
            }
        }
    }

    private fun showSelectDialog(){
        val context = this@UserInformationActivity
        showSelectDialog = SimpleListSheetDialog(
            context = context,
            itemList = mutableListOf(
                EaseMenuItem(
                    menuId = R.id.about_information_camera,
                    title = getString(R.string.main_about_me_information_camera),
                    titleColor = ContextCompat.getColor(context, com.hyphenate.easeui.R.color.ease_color_primary)
                ),
                EaseMenuItem(
                    menuId = R.id.about_information_picture,
                    title = getString(R.string.main_about_me_information_picture),
                    titleColor = ContextCompat.getColor(context, com.hyphenate.easeui.R.color.ease_color_primary)
                )
            ),
            object : SimpleListSheetItemClickListener {
                override fun onItemClickListener(position: Int, menu: EaseMenuItem) {
                    simpleMenuItemClickListener(menu)
                    showSelectDialog?.dismiss()
                }
            })
        supportFragmentManager.let { showSelectDialog?.show(it,"image_select_dialog") }
    }

    fun simpleMenuItemClickListener(menu: EaseMenuItem){
        when(menu.menuId){
            R.id.about_information_camera -> {
                if (PermissionCompat.checkPermission(
                        mContext,
                        requestCameraPermission,
                        Manifest.permission.CAMERA
                    )
                ) {
                    selectPicFromCamera(launcherToCamera)
                }
            }
            R.id.about_information_picture -> {
                if (PermissionCompat.checkMediaPermission(
                        mContext,
                        requestImagePermission,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    selectPicFromLocal(launcherToAlbum)
                }
            }
            else -> {}
        }
    }


    private fun onRequestResult(result: Map<String, Boolean>?, requestCode: Int) {
        if (!result.isNullOrEmpty()) {
            for ((key, value) in result) {
                ChatLog.e("chat", "onRequestResult: $key  $value")
            }
            if (PermissionCompat.getMediaAccess(mContext) !== PermissionCompat.StorageAccess.Denied) {
                if (requestCode == REQUEST_CODE_STORAGE_PICTURE) {
                    selectPicFromLocal(launcherToAlbum)
                }else if (requestCode == REQUEST_CODE_CAMERA){
                    selectPicFromCamera(launcherToCamera)
                }else if (requestCode == REQUEST_CODE_LOCAL_EDIT){
                    openImageEditor(imageUri)
                }
            }
        }
    }

    /**
     * It's the result from ActivityResultLauncher.
     * @param result
     * @param requestCode
     */
    private fun onActivityResult(result: ActivityResult, requestCode: Int) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            when (requestCode) {
                RESULT_CODE_CAMERA -> { // capture new image
                    onActivityResultForCamera(data)
                }
                RESULT_CODE_LOCAL -> {
                    onActivityResultForLocalPhotos(data)
                }
                RESULT_CODE_LOCAL_EDIT -> {
                    onActivityResultForEditPhotos(data)
                }
                RESULT_CODE_UPDATE_NAME -> {
                    data?.let {
                        if (it.hasExtra(RESULT_REFRESH) && it.getBooleanExtra(RESULT_REFRESH,false)){
                            updateUsername()
                        }
                    }
                }
            }
        }
    }

    private fun onActivityResultForCamera(data: Intent?) {
        cameraFile?.let {
            if (it.exists()){
                var uri = Uri.parse(it.absolutePath)
//                Check if the image is rotated and restore it
                uri = ChatImageUtils.checkDegreeAndRestoreImage(mContext, uri)
                imageUri = uri
                uploadFile(it.absolutePath)
            }
        }
    }

    private fun onActivityResultForLocalPhotos(data: Intent?) {
        if (data != null) {
            val selectedImage = data.data
            if (selectedImage != null) {
                var filePath: String = EaseFileUtils.getFilePath(mContext, selectedImage)
                if (!TextUtils.isEmpty(filePath) && File(filePath).exists()) {
                    imageUri = Uri.parse(filePath)
                } else {
                    imageUri = selectedImage
                    selectedImage.path?.let {
                        filePath = it
                    }
                }
                showEditImageDialog(selectedImage)
            }
        }
    }

    private fun onActivityResultForEditPhotos(data: Intent?){
        data?.let {
            val selectedImage = data.data
            selectedImage?.let {
                val filePath: String = EaseFileUtils.getFilePath(mContext, selectedImage)
                if (!TextUtils.isEmpty(filePath) && File(filePath).exists()) {
                    imageUri = Uri.parse(filePath)
                    uploadFile(filePath)
                }else{
                    imageUri = selectedImage
                    uploadFile(selectedImage)
                }
            }
        }
    }

    /**
     * select picture from camera
     */
    private fun selectPicFromCamera(launcher: ActivityResultLauncher<Intent>?) {
        if (!isSdcardExist()) {
            return
        }
        cameraFile = File(
            ChatPathUtils.getInstance().imagePath, (ChatClient.getInstance().currentUser
                    + System.currentTimeMillis()) + ".jpg"
        )
        cameraFile?.let {
            it.parentFile?.mkdirs()
            launcher?.launch(
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                    MediaStore.EXTRA_OUTPUT, EaseCompat.getUriForFile(
                        mContext, it
                    )
                )
            )
        }

    }

    /**
     * select local image
     */
    private fun selectPicFromLocal(launcher: ActivityResultLauncher<Intent>?) {
        EaseCompat.openImageByLauncher(launcher, mContext)
    }

    private fun openImageEditor(uri: Uri?) {
        uri?.let {
            val intent = Intent(Intent.ACTION_EDIT).apply {
                setDataAndType(uri, "image/*")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            }
            launcherToAlbumEdit.launch(intent)
        }
    }

    private fun showEditImageDialog(fileUri: Uri?){
        val editDialog = CustomDialog(
            context = mContext,
            title = resources.getString(R.string.main_about_me_information_edit_picture),
            isEditTextMode = false,
            onLeftButtonClickListener = {
                fileUri?.let {
                    uploadFile(it)
                }
            },
            onRightButtonClickListener = {
                if (PermissionCompat.checkPermission(
                        mContext,
                        requestEditImagePermission,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ){
                    openImageEditor(imageUri)
                }
            }
        )
        editDialog.show()
    }

    private fun updateUserAvatar(url:String){
        selfProfile = EaseIM.getCurrentUser()
        selfProfile?.avatar = url
        selfProfile?.let {
            EaseIM.updateCurrentUser(it)
            EaseFlowBus.with<EaseEvent>(EaseEvent.EVENT.UPDATE + EaseEvent.TYPE.CONTACT)
                .post(lifecycleScope, EaseEvent(DemoConstant.EVENT_UPDATE_SELF, EaseEvent.TYPE.CONTACT))
        }
    }

    private fun updateUsername(){
        binding.run {
            selfProfile = EaseIM.getCurrentUser()
            selfProfile?.let {
                tvNickName.text = it.name?: ""
            }
        }
    }

    private fun uploadFile(filePath:String?){
        val scaledImage = ChatImageUtils.getScaledImage(this,filePath)
        lifecycleScope.launch {
            model.uploadAvatar(scaledImage)
                .catchChatException { e ->
                    ChatLog.e("TAG", "uploadAvatar fail error message = " + e.description)
                    mainScope().launch {
                        mContext.showToast("uploadFile error ${e.errorCode} ${e.description}")
                    }
                }
                .stateIn(lifecycleScope, SharingStarted.WhileSubscribed(5000), null)
                .collect {
                    it?.let {
                        binding.ivAvatar.setImageURI(imageUri)
                        updateUserAvatar(it)
                    }
                }
        }
    }

    private fun uploadFile(fileUri:Uri?){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            val scaledImageUri =  ChatImageUtils.getScaledImage(this,fileUri)
            lifecycleScope.launch {
                val fileUrl = EaseCompat.getPath(this@UserInformationActivity,scaledImageUri)
                model.uploadAvatar(fileUrl)
                    .catchChatException { e ->
                        ChatLog.e("TAG", "uploadAvatar fail error message = " + e.description)
                        mainScope().launch {
                            mContext.showToast("uploadFile error ${e.errorCode} ${e.description}")
                        }
                    }
                    .stateIn(lifecycleScope, SharingStarted.WhileSubscribed(5000), null)
                    .collect {
                        it?.let {
                            binding.ivAvatar.setImageURI(imageUri)
                            updateUserAvatar(it)
                        }
                    }
            }
        }
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.avatar_layout -> { showSelectDialog() }
           R.id.nick_name_layout -> {
               launcherToUpdateName.launch(Intent(
                   this@UserInformationActivity,
                   EditUserNicknameActivity::class.java))
           }
           else -> {}
       }
    }

}