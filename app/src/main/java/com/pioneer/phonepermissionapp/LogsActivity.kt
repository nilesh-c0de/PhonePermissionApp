package com.pioneer.phonepermissionapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LogsActivity : AppCompatActivity() {

    lateinit var tvLog: TextView
    var append = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        tvLog = findViewById(R.id.tv_log)

        val log = Manifest.permission.READ_CALL_LOG
        if (ContextCompat.checkSelfPermission(
                this,
                log
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val logList = PhoneData.getAllCallLogs(applicationContext)
            for (item in logList) {
                Log.i("np_logs", "log: ${item.number}")
                append += item.number + "\n"
                tvLog.text = append
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(log), 102)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                val list = Secret.getAllGalleryImages(applicationContext)
//                val adapter = GalleryAdapter(this, list)
//                recyclerView.adapter = adapter

                val logList = PhoneData.getAllCallLogs(applicationContext)
                for (item in logList) {
                    Log.i("np_logs", "log: ${item.number}")
                }

            } else {
                Log.e("GalleryActivity", "Permission denied.")
                // Handle permission denied scenario
            }
        }
    }
}