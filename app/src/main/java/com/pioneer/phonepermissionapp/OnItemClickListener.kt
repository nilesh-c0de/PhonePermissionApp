package com.pioneer.phonepermissionapp

import android.net.Uri

interface OnItemClickListener {
    fun onItemClick(position: Int, uri: Uri)
}