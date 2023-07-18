package com.pioneer.phonepermissionapp

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var cardContacts: CardView? = null
    var cardLogs: CardView? = null
    var cardMessages: CardView? = null
    var cardMusic: CardView? = null
    var cardPics: CardView? = null
    var cardVideos: CardView? = null
    var cardYt: CardView? = null
    lateinit var details: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        cardContacts = findViewById(R.id.card_contact)
        cardLogs = findViewById(R.id.card_logs)
        cardMessages = findViewById(R.id.card_messages)
        cardMusic = findViewById(R.id.card_music)
        cardPics = findViewById(R.id.card_img)
        cardVideos = findViewById(R.id.card_videos)
        cardYt = findViewById(R.id.card_yt)

        details = findViewById(R.id.tv_details)

        cardContacts?.setOnClickListener(this)
        cardLogs?.setOnClickListener(this)
        cardMessages?.setOnClickListener(this)
        cardMusic?.setOnClickListener(this)
        cardPics?.setOnClickListener(this)
        cardVideos?.setOnClickListener(this)
        cardYt?.setOnClickListener(this)

        val x = PhoneData.getPhoneDetails(applicationContext)
        details.text = x

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.card_contact -> {
//                Toast.makeText(applicationContext, "Contact", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ContactActivity::class.java)
                startActivity(intent)
            }
            R.id.card_logs -> {
//                Toast.makeText(applicationContext, "Logs", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LogsActivity::class.java)
                startActivity(intent)
            }
            R.id.card_messages -> {
//                Toast.makeText(applicationContext, "Mesgs", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MessagesActivity::class.java)
                startActivity(intent)
            }
            R.id.card_music -> {
//                Toast.makeText(applicationContext, "Music", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MusicActivity::class.java)
                startActivity(intent)
            }
            R.id.card_img -> {
//                Toast.makeText(applicationContext, "Images", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ImagesActivity::class.java)
                startActivity(intent)
            }
            R.id.card_videos -> {
//                Toast.makeText(applicationContext, "Videos", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VideosActivity::class.java)
                startActivity(intent)
            }
            R.id.card_yt -> {
//                Toast.makeText(applicationContext, "Videos", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, YouTubeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}