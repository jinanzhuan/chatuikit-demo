package com.hyphenate.chatdemo.ui.me

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.hyphenate.chatdemo.BuildConfig
import com.hyphenate.chatdemo.utils.CameraAndCropFileUtils
import com.hyphenate.easeui.common.ChatImageUtils
import com.hyphenate.easeui.common.ChatLog
import com.hyphenate.easeui.common.extensions.isSdcardExist
import java.io.File

class CameraAndCroppingController(
    var context: Context
) {
    companion object {
        const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileProvider"
    }
    private var cameraFile: File? = null
    private var imageCropFile:File? = null

    private var resultImageUri:Uri? = null

    fun selectPicFromCamera(launcher: ActivityResultLauncher<Intent>?){
        if (!isSdcardExist()) {
            return
        }
        cameraFile = CameraAndCropFileUtils.createImageFile( context, false)

        cameraFile?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                // 如果是 11 以上系统 通过MediaStore获取 uri
                ChatLog.e("CameraAndCroppingController","selectPicFromCamera version >= 11 putExtra: ${CameraAndCropFileUtils.uri}")
                intent.putExtra(MediaStore.EXTRA_OUTPUT,CameraAndCropFileUtils.uri)
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                // 如果是 7.0 且低于 11 系统 需要使用FileProvider
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val imgUri = FileProvider.getUriForFile(context, AUTHORITY, it)
                ChatLog.e("CameraAndCroppingController","selectPicFromCamera 7 <= version < 11 putExtra: $imgUri")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            }else{
                // 低于 7.0 系统
                ChatLog.e("CameraAndCroppingController","selectPicFromCamera version < 7 putExtra: ${Uri.fromFile(it)}")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }
            launcher?.launch(intent)
        }
    }

    fun resultForCamera(data: Intent?):Uri?{
        cameraFile?.let {
            //判断文件是否存在
            if (it.exists()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    // 如果是 11 以上系统 通过MediaStore获取 uri
                    resultImageUri = CameraAndCropFileUtils.uri
                    ChatLog.e("CameraAndCroppingController","resultForCamera version >= 11 putExtra: $resultImageUri")
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    // 如果是 7.0 且低于 11 系统 需要使用FileProvider 创建一个content类型的Uri
                    resultImageUri = FileProvider.getUriForFile(context, AUTHORITY, it)
                    ChatLog.e("CameraAndCroppingController","resultForCamera 7 <= version < 11 putExtra: $resultImageUri")
                }else{
                    resultImageUri = Uri.fromFile(it)
                    ChatLog.e("CameraAndCroppingController","resultForCamera version < 7 putExtra: $resultImageUri")
                }
            }
        }
        return resultImageUri
    }

    fun gotoCrop(sourceUri: Uri?,launcher: ActivityResultLauncher<Intent>?){
        imageCropFile = CameraAndCropFileUtils.createImageFile(context, true)
        imageCropFile?.let { crop->
            sourceUri?.let {
                crop.parentFile?.mkdirs()
                val intent = Intent("com.android.camera.action.CROP")
                intent.setDataAndType(it, "image/*")
                intent.putExtra("crop", "true")
                intent.putExtra("aspectX", 1)    //X方向上的比例
                intent.putExtra("aspectY", 1)    //Y方向上的比例
                intent.putExtra("outputX", 500)  //裁剪区的宽
                intent.putExtra("outputY", 500)  //裁剪区的高
                intent.putExtra("scale ", true)  //是否保留比例
                intent.putExtra("return-data", false)
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, CameraAndCropFileUtils.uri)
                    resultImageUri = CameraAndCropFileUtils.uri
                }else{
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    val imgCropUri = Uri.fromFile(crop)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgCropUri)
                    resultImageUri = imgCropUri
                }
                launcher?.launch(intent)
            }
        }
    }

    fun resultForCropFile(data: Intent?):File?{
        if (imageCropFile != null && imageCropFile?.absolutePath != null){
            imageCropFile?.let {
                return it
            }
        }
        return null
    }

    fun getImageCropUri():Uri?{
        imageCropFile?.let {
            val uri = Uri.parse(it.absolutePath)
            return ChatImageUtils.checkDegreeAndRestoreImage(context, uri)
        }
       return null
    }

}