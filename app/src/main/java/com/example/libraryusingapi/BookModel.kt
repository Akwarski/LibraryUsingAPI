package com.example.libraryusingapi

import java.io.Serializable
import java.net.URL

data class BookModel(val imageResId: String,
                     val title: String,
                     var description: String,
                     val url: String): Serializable