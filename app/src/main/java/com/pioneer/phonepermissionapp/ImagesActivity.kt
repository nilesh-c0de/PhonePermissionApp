package com.pioneer.phonepermissionapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImagesActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        recyclerView = findViewById(R.id.rv)
        recyclerView?.layoutManager = GridLayoutManager(this, 4)

        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            getData()

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 100)
        }

    }

    private fun getData() {

        val list = PhoneData.getAllImageFiles(applicationContext)
        for (item in list) {
            Log.i("np_images", item)
        }

        val adapter = GalleryAdapter(object : OnItemClickListener {
            override fun onItemClick(position: Int, uri: Uri) {
                openImageDialog(uri)
            }

        }, list)
        recyclerView?.adapter = adapter
    }

    private fun openImageDialog(uri: Uri) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.item_full_image, null)
        val img = dialogView.findViewById<ImageView>(R.id.iv_full)

        Glide.with(applicationContext).load(uri).into(img)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
                getData()

            } else {
                Log.e("GalleryActivity", "Permission denied.")
                // Handle permission denied scenario
            }
        }
    }
}