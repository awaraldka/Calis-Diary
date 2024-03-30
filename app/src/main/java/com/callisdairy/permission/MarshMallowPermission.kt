package com.stormstreet.myapplication.entity.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.callisdairy.R

class MarshMallowPermission( activity : Activity) {
    val RECORD_PERMISSION_REQUEST_CODE = 1
    val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2
    val CAMERA_PERMISSION_REQUEST_CODE = 3
    var activity: Activity = activity



    fun checkPermissionForRecord(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForExternalStorage(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForRecord() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            Toast.makeText(
                activity,
                activity.getString(R.string.Marshmallow_toast_string_microphone_permission),
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                activity,
                activity.getString(R.string.Marshmallow_toast_string_external_permission),
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(
                activity,
                activity.getString(R.string.Marshmallow_toast_string_Camera_permission),
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

}