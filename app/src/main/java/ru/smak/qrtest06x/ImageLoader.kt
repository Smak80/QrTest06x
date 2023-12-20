package ru.smak.qrtest06x

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class ImageLoader(
    private val onLoad: (Bitmap?)->Unit,
){

    private var resultLauncher: ActivityResultLauncher<Intent?>? = null

    fun registerForResult(
        parentActivity: ComponentActivity,
    ){
        resultLauncher = parentActivity
            .registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ){ result ->
                try {
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.data?.let { uri ->
                            onLoad(
                                ImageDecoder.decodeBitmap(
                                    ImageDecoder.createSource(parentActivity.contentResolver, uri)
                                )
                            )
                        }
                    }
                } catch (_: Throwable){
                    onLoad(null)
                }
            }
    }

    fun loadImageAsync(){
        resultLauncher?.launch(
            Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
        )
    }
}