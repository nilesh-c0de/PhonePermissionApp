package com.pioneer.phonepermissionapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MessagesActivity : AppCompatActivity() {

    private lateinit var tvMessage: TextView
    var append = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        tvMessage = findViewById(R.id.tv_msg)

        val msg = Manifest.permission.READ_SMS
        if (ContextCompat.checkSelfPermission(
                this,
                msg
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val msgList = PhoneData.getAllMessages(applicationContext)
            for (item in msgList) {
                Log.i("np_msgs", item.body)
                append += item.body + "\n\n"
                tvMessage.text = append
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(msg), 102)
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

                val msgList = PhoneData.getAllMessages(applicationContext)
                for (item in msgList) {
                    Log.i("np_msgs", item.body)
                }

            } else {
                Log.e("GalleryActivity", "Permission denied.")
                // Handle permission denied scenario
            }
        }
    }
}