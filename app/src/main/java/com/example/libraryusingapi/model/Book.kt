package com.example.libraryusingapi.model

import java.io.Serializable

data class Book(val title: String, val author: String, val publishedYear: String, val thumbnailUri: String, val infoPageUri: String) : Serializable