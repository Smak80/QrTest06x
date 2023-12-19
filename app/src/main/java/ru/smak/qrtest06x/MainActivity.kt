package ru.smak.qrtest06x

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.smak.qrtest06x.ui.theme.QrTest06xTheme
import ru.smak.qrtest06x.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private val mvm: MainViewModel by viewModels()
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLoader = ImageLoader(this){
            mvm.scanFileQr(it)
        }
        setContent {
            QrTest06xTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUI(
                        mvm.qrData,
                        Modifier.fillMaxSize(),
                    ){
                        if (it == ScanType.CAMERA)
                            mvm.scanQr(it, this)
                        else
                            imageLoader.loadAsync()
                    }
                }
            }
        }
    }
}

@Composable
fun MainUI(
    qrText: String,
    modifier: Modifier = Modifier,
    onDoScan: (ScanType)->Unit = {},
){
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { onDoScan(ScanType.CAMERA) }) {
                Text(stringResource(R.string.camera_scan_button))
            }
            Button(onClick = { onDoScan(ScanType.FILE) }) {
                Text(stringResource(R.string.file_scan_button))
            }
        }
        Text(text = qrText)
    }
}