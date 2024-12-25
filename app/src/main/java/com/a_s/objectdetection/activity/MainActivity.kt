package com.a_s.objectdetection.activity

import android.R.attr.bottom
import android.R.attr.left
import android.R.attr.right
import android.R.attr.top
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.a_s.objectdetection.camerax.CameraScreen
import com.a_s.objectdetection.od.DetectorListenerImpl
import com.a_s.objectdetection.od.ObjectDetectorHelper
import com.a_s.objectdetection.od.detectedObjects
import com.a_s.objectdetection.od.resultImage
import com.a_s.objectdetection.od.time
import com.a_s.objectdetection.permission.PermissionProvider
import com.a_s.objectdetection.permission.cameraPermission
import com.a_s.objectdetection.ui.theme.ObjectDetectionTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = ObjectDetectorHelper(
            context = this,
            objectDetectorListener = DetectorListenerImpl()
        )

        enableEdgeToEdge()
        setContent {
            ObjectDetectionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {


                    PermissionProvider(permission = cameraPermission) {
                        if (resultImage.value == null) {
                            CameraScreen(
                                detect = model::detect
                            )
                        } else {


                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Button(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 20.dp),
                                    onClick = {
                                        resultImage.value = null
                                    }) {
                                    Text("Take Again")
                                }
                                Image(
                                    bitmap = resultImage.value!!.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .rotate(90f)
                                        .align(Alignment.Center)
                                        .border(width = 4.dp,color = Color.Red,)
                                        .onGloballyPositioned { pos ->
                                            h.intValue = pos.size.height
                                        }
                                        ,
                                    contentScale = ContentScale.FillBounds,
                                )

                                // Draw a rectangle over the image
                                Canvas(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .height(410.dp)
                                        .fillMaxWidth()
                                        .border(width = 4.dp,color = Color.Green,)
                                ) {
                                    detectedObjects.forEach { item ->
                                        drawRect(
                                            color = Color.Red.copy(alpha = 0.5f), // Semi-transparent red
                                            topLeft = Offset(
                                                x = item.boundingBox.top,
                                                y = item.boundingBox.left
                                            ),
                                            size = Size(
                                                height = item.boundingBox.bottom - item.boundingBox.top,
                                                width = item.boundingBox.right - item.boundingBox.left
                                            )
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .defaultMinSize(minHeight = 80.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("time : " + time.value.toString())
                                    detectedObjects.forEach { item ->
                                        Text("item :${item.label} score:${item.score}")
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

var h = mutableIntStateOf(0)