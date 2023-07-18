package com.pioneer.phonepermissionapp

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

class GalleryAdapter(var listener: OnItemClickListener, var list: List<String>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var image: ImageView = itemView.findViewById<ImageView>(R.id.iv_xd)
        var pb: ProgressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)

        Log.i("nileshphonde", "onCreateViewHolder")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[position]
        Log.i("phondenilesh", "${Uri.fromFile(File(model))}")

        holder.pb.visibility = View.VISIBLE
        Glide.with(holder.itemView.context)
            .load(Uri.fromFile(File(model)))
//            .error(R.drawable.fb)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pb.visibility = View.GONE
                    Log.i("phondenilesh", "Error loading image", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pb.visibility = View.GONE
                    return false
                }

            })
            .into(holder.image)
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, Uri.fromFile(File(model)))
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

