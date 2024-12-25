package com.a_s.objectdetection.od

import android.R.attr.bottom
import android.R.attr.label
import android.R.attr.left
import android.R.attr.right
import android.R.attr.top
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.a_s.objectdetection.od.ObjectDetectorHelper.DetectorListener
import com.a_s.objectdetection.utils.Constants.TAG
import org.tensorflow.lite.task.gms.vision.detector.Detection

class DetectorListenerImpl : DetectorListener {
    override fun onInitialized() {
        Log.d(TAG, "onInitialized: model initialized")
    }

    override fun onError(error: String) {
        Log.e(TAG, "DetectorListener: $error")
    }

    override fun onResults(
        results: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int,
        image: Bitmap
    ) {
        resultImage.value = image
        time.value = inferenceTime
        detectedObjects.clear()

        results?.forEach { result ->
            detectedObjects.add(
                DetectedObject(
                    label = result.categories.first().label,
                    score = result.categories.first().score,
                    index = result.categories.first().index,
                    displayName = result.categories.first().displayName,
                    boundingBox = result.boundingBox.toBoundingBox()
                )
            )
        }
    }
}

data class DetectedObject(
    val label: String,
    val score: Float,
    val index: Int,
    val displayName: String,
    val boundingBox: BoundingBox
)

data class BoundingBox(
    val top: Float,
    val left: Float,
    val bottom: Float,
    val right: Float
)

private fun RectF.toBoundingBox(): BoundingBox {
    return BoundingBox(
        top = top,
        left = left,
        bottom = bottom,
        right = right
    )
}

var resultImage: MutableState<Bitmap?> = mutableStateOf(null)
var detectedObjects = mutableStateListOf<DetectedObject>()
var time : MutableState<Long> = mutableLongStateOf(0)