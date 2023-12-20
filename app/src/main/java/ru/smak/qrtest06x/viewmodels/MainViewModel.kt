package ru.smak.qrtest06x.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.common.InputImage
import ru.smak.qrtest06x.ImageLoader
import ru.smak.qrtest06x.R
import ru.smak.qrtest06x.ScanType

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val qrScanner by lazy{
        GmsBarcodeScanning.getClient(app.applicationContext)
    }

    var qrData by mutableStateOf("")

    fun scanQr(){
        qrScanner.startScan()
            .addOnSuccessListener { barcode ->
                qrData = barcode.rawValue.toString()
            }
            .addOnFailureListener {
                qrData = getApplication<Application>().getString(R.string.qr_error)
            }
    }

    fun scanFileQr(pic: Bitmap?) {
        pic?.let {
            val img = InputImage.fromBitmap(it, 0)
            val bcs = BarcodeScanning.getClient()
            bcs.process(img).apply {
                addOnSuccessListener {
                    if (it.size > 0) {
                        qrData = it[0].rawValue.toString()
                    } else {
                        qrData = getApplication<Application>().getString(R.string.qr_error)
                    }
                }
            }
        }
    }
}