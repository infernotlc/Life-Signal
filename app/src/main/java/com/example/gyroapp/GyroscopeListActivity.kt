package com.example.gyroapp

import GyroscopeViewModel
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gyroapp.data.AppDatabase
import com.example.gyroapp.data.CaptureGyroscopeData
import com.example.gyroapp.data.GyroscopeDataDao
import kotlinx.coroutines.Dispatchers

class GyroscopeListActivity : AppCompatActivity() {

    private lateinit var telephonyManager: TelephonyManager
    private val gyroscopeViewModel: GyroscopeViewModel by viewModels()

    companion object {
        private const val REQUEST_PHONE_STATE_PERMISSION = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyroscope_list)

        // Check and request READ_PHONE_STATE permission at runtime
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                REQUEST_PHONE_STATE_PERMISSION
            )
        } else {
            // Permission has already been granted
            initializeActivity()
        }
    }

    private fun initializeActivity() {
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val gyroscopeDataAdapter = GyroscopeDataAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = gyroscopeDataAdapter

        gyroscopeViewModel.allGyroscopeData.observe(this, Observer { data ->
            gyroscopeDataAdapter.submitList(data)
        })

        // Start capturing gyroscope data when the activity starts
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gyroscopeDataDao: GyroscopeDataDao = AppDatabase.getInstance(application).gyroscopeDataDao()
        val captureGyroscopeData =
            CaptureGyroscopeData(this, gyroscopeViewModel, sensorManager, telephonyManager, Dispatchers.Default)
        captureGyroscopeData.startCapture()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PHONE_STATE_PERMISSION -> {
                // Check if the permission is granted
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeActivity()
                } else {
                    // Permission denied. Handle accordingly (e.g., show a message to the user)
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onDestroy() {
        // Stop capturing gyroscope data when the activity is destroyed
        // to prevent memory leaks

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gyroscopeDataDao: GyroscopeDataDao = AppDatabase.getInstance(application).gyroscopeDataDao()

        val captureGyroscopeData = CaptureGyroscopeData(this, gyroscopeViewModel, sensorManager, telephonyManager, Dispatchers.Default)
        captureGyroscopeData.stopCapture()

        super.onDestroy()
    }
}
