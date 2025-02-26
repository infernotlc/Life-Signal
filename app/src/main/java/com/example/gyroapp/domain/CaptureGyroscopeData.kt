package com.example.gyroapp.data

import GyroscopeViewModel
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class CaptureGyroscopeData(
    private val context: Context,
    private val gyroscopeViewModel: GyroscopeViewModel,
    private val sensorManager: SensorManager,
    private val telephonyManager: TelephonyManager,
    private val dispatcher: CoroutineDispatcher,
) : SensorEventListener {

    private val executor = dispatcher.asExecutor()
    private val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var isCallActive = false

    @RequiresApi(Build.VERSION_CODES.S)
    private val telephonyCallback =
        object : TelephonyCallback(), TelephonyCallback.CallStateListener {
            override fun onCallStateChanged(state: Int) {
                isCallActive = state == TelephonyManager.CALL_STATE_OFFHOOK
                if (isCallActive) {
                    CoroutineScope(Dispatchers.Default).launch {
                        try {
                            if (checkPermissions()) {
                                val gyroscopeData = captureGyroscopeData()
                                if (gyroscopeData != null) {
                                    insertGyroscopeData(
                                        gyroscopeData.id,
                                        gyroscopeData.x,
                                        gyroscopeData.y,
                                        gyroscopeData.z
                                    )
                                }
                            }
                        } catch (e: SecurityException) {
                            Log.e("CaptureGyroscopeData", "Error capturing gyroscope data", e)
                        }
                    }
                } else {
                    stopCapture()
                }
            }
        }

    private fun checkPermissions(): Boolean {
      
        return true 
    }

    private fun requestPhoneStatePermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.READ_PHONE_STATE),
            PHONE_STATE_PERMISSION_REQUEST_CODE
        )
    }

    fun startCapture() {
        gyroscopeSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        if (checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                telephonyManager.registerTelephonyCallback(
                    Dispatchers.Default.asExecutor(),
                    telephonyCallback
                )
            }
        } else {
            requestPhoneStatePermission()
        }
    }

    fun stopCapture() {
        sensorManager.unregisterListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.unregisterTelephonyCallback(telephonyCallback)
        }
    }

    private suspend fun insertGyroscopeData(id: Int, x: Float, y: Float, z: Float) {
        val gyroscopeData = GyroscopeData(
            id = id,
            x = x,
            y = y,
            z = z,
            timestamp = System.currentTimeMillis()
        )
        withContext(Dispatchers.Main) {
            gyroscopeViewModel.insertGyroscopeData(gyroscopeData)
        }
    }

    private suspend fun captureGyroscopeData(): GyroscopeData? {
        val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)// or GYROSCOPE

        return if (gyroscopeSensor != null) {
            // Use a CompletableDeferred to wait for the gyroscope data
            val deferred = CompletableDeferred<GyroscopeData?>()

            val gyroscopeEventListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Handle accuracy changes if needed
                }

                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        val gyroscopeData = GyroscopeData(
                            id = 0, // Replace with actual id
                            x = event.values[0],
                            y = event.values[1],
                            z = event.values[2],
                            timestamp = System.currentTimeMillis()
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            insertGyroscopeData(gyroscopeData.id, gyroscopeData.x, gyroscopeData.y, gyroscopeData.z)
                        }

                        // Complete the deferred with the captured gyroscope data
                        deferred.complete(gyroscopeData)
                    }
                }
            }

            // Register the gyroscope event listener
            sensorManager.registerListener(
                gyroscopeEventListener,
                gyroscopeSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            // Wait for the gyroscope data or a timeout (adjust the timeout duration as needed)
            val timeoutMillis = 5000L // 5 seconds timeout
            val gyroscopeData = withTimeoutOrNull(timeoutMillis) { deferred.await() }

            // Unregister the gyroscope event listener
            sensorManager.unregisterListener(gyroscopeEventListener)

            gyroscopeData
        } else {
            null  // Return null if gyroscope sensor is not available on the device
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for gyroscope data
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // This method will be called when gyroscope sensor values change
        // Implement logic here if needed
    }

    companion object {
        private const val PHONE_STATE_PERMISSION_REQUEST_CODE = 123
    }
}
