package com.indialone.indieflashlight

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {

    private lateinit var btnFlash: ImageButton
    private var state: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFlash = findViewById(R.id.btn_flash)

        requestPermission()

    }

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    runFlashLight()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@MainActivity,
                        "You should Provide permission to proceed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()
    }

    private fun runFlashLight() {

        btnFlash.setOnClickListener {
            val cameraManager: CameraManager =
                getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = cameraManager.cameraIdList[0]
            if (!state) {
                try {
                    cameraManager.setTorchMode(cameraId, true)
                    state = true
                    btnFlash.setImageResource(R.drawable.torch_on)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            } else {
                try {
                    cameraManager.setTorchMode(cameraId, false)
                    state = false
                    btnFlash.setImageResource(R.drawable.torch_off)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

        }

    }

}