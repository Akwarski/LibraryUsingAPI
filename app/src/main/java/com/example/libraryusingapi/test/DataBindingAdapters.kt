package com.example.libraryusingapi.test

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object DataBindingAdapters {
    @BindingAdapter("android:src")
    fun setImageResoruce(imageView: ImageView, resource: Uri) {
        imageView.setImageURI(resource)
    }
}