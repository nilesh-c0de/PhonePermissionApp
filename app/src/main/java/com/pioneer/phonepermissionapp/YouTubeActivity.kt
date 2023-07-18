package com.pioneer.phonepermissionapp

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.InputStreamReader

class YouTubeActivity : AppCompatActivity() {

    private lateinit var downloadButton: Button
    private lateinit var youtubeLinkEditText: EditText

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_tube)

        downloadButton = findViewById(R.id.btn_download)
        youtubeLinkEditText = findViewById(R.id.et_yt_link)

        downloadButton.setOnClickListener {
            val youtubeLink = youtubeLinkEditText.text.toString()
            downloadYouTubeVideo(youtubeLink)
        }
    }

    private fun downloadYouTubeVideo(youtubeLink: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            return
        }

        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val command = arrayOf("youtube-dl", "-o", "$downloadDir/%(title)s.%(ext)s", youtubeLink)

        val processBuilder = ProcessBuilder(*command)
        processBuilder.redirectErrorStream(true)

        try {
            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                // Process and display output if required
            }
            process.waitFor()
            Toast.makeText(this, "Video downloaded successfully.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error occurred while downloading video.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val youtubeLink = youtubeLinkEditText.text.toString()
            downloadYouTubeVideo(youtubeLink)
        } else {
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
        }
    }
}