package com.pioneer.phonepermissionapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MusicActivity : AppCompatActivity() {

    private var prev: Button? = null
    private var next: Button? = null
    private var play: Button? = null
    private var tvMusic: TextView? = null
    private var mediaPlayer: MediaPlayer? = null

    //    private var musicFiles: ArrayList<String> = ArrayList()
    private var musicFiles: List<MusicModel> = ArrayList()
    private var currentMusicIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        prev = findViewById(R.id.btn_prev)
        next = findViewById(R.id.btn_nxt)
        play = findViewById(R.id.btn_current)
        tvMusic = findViewById(R.id.tv_music_name)
        play?.text = "Play"

        play?.setOnClickListener {
            playCurrentMusic()
        }

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer()

        // Set completion listener to play the next music file
        mediaPlayer?.setOnCompletionListener {
            playNextMusic()
        }

        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            val musicList = Secret.getAllMusicFiles(applicationContext)
//            for (item in musicList) {
//                Log.i("phondenilesh", item)
//                musicFiles.add(item)
//            }
            musicFiles = PhoneData.getAllMusicFiles(applicationContext)
            for (item in musicFiles) {
                Log.i("np_music", "${item.data}")
            }

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 100)
        }

    }

    // Play the next music file
    private fun playNextMusic() {
        currentMusicIndex = (currentMusicIndex + 1) % musicFiles.size
        tvMusic?.text = musicFiles.get(currentMusicIndex).title
        playMusic()
    }

    // Play the previous music file
    private fun playPreviousMusic() {
        currentMusicIndex = (currentMusicIndex - 1 + musicFiles.size) % musicFiles.size
        tvMusic?.text = musicFiles.get(currentMusicIndex).title
        playMusic()
    }

    // Play the current music file
    private fun playMusic() {

        if (play?.text != "Play") {

            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }

            val currentMusicUri = Uri.parse(musicFiles.get(currentMusicIndex).data)
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(applicationContext, currentMusicUri)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        }
    }

    private fun playCurrentMusic() {
        tvMusic?.text = musicFiles.get(currentMusicIndex).title

        if (play?.text == "Play") {

            play?.text = "Stop"
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }

            val currentMusicUri = Uri.parse(musicFiles.get(currentMusicIndex).data)
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(applicationContext, currentMusicUri)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } else {
            play?.text = "Play"
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }

        }

    }

    // Button click handlers
    fun onNextButtonClick(view: View) {
        playNextMusic()
    }

    fun onPreviousButtonClick(view: View) {
        playPreviousMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
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

                musicFiles = PhoneData.getAllMusicFiles(applicationContext)
                for (item in musicFiles) {
                    Log.i("np_music", "${item.data}")
                }
            } else {
                Log.e("GalleryActivity", "Permission denied.")
                // Handle permission denied scenario
            }
        }
    }
}