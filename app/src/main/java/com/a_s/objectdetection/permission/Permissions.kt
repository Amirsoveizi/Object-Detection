package com.a_s.objectdetection.permission

import android.Manifest
import com.a_s.objectdetection.R

val cameraPermission = Permission(
    permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
    ),
    title = R.string.permission_camera_title,
    description = R.string.permission_camera_description,
    permanentlyDeniedDescription = R.string.permission_camera_permanently_denied_description,
)