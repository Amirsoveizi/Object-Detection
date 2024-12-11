package com.a_s.objectdetection.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.a_s.objectdetection.camerax.CameraScreen
import com.a_s.objectdetection.permission.PermissionProvider
import com.a_s.objectdetection.permission.cameraPermission
import com.a_s.objectdetection.ui.theme.ObjectDetectionTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ObjectDetectionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    PermissionProvider(permission = cameraPermission) {
                        CameraScreen()
                    }
                }
            }
        }
    }
}
