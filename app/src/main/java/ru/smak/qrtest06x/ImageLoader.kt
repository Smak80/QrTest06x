package ru.smak.qrtest06x

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class ImageLoader(
    parentActivity: ComponentActivity,
    private val onLoad: (Bitmap?)->Unit,
) {

    private val resultLauncher: ActivityResultLauncher<Intent?>

    init{
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

    fun loadAsync(){
        resultLauncher.launch(
        Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        })
    }
}