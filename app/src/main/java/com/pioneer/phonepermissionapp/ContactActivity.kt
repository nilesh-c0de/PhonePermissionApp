package com.pioneer.phonepermissionapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactActivity : AppCompatActivity() {

    lateinit var tvContact: TextView
    var append = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        tvContact = findViewById(R.id.tv_contact)

        val contact = Manifest.permission.READ_CONTACTS
        if (ContextCompat.checkSelfPermission(
                this,
                contact
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val contactList = PhoneData.getAllContacts(applicationContext)
            for (item in contactList) {
                Log.i("np_cons", "contact no: ${item.displayName}")
                append += item.displayName + "\n"
                tvContact.text = append

            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(contact), 100)
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

                val contactList = PhoneData.getAllContacts(applicationContext)
                for (item in contactList) {
                    Log.i("np_contacts", "contact no: ${item.displayName}")
                }
            } else {
                Log.e("GalleryActivity", "Permission denied.")
                // Handle permission denied scenario
            }
        }
    }
}