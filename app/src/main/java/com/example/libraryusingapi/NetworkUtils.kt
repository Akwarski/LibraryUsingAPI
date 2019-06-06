package com.example.libraryusingapi

import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {
    companion object {
        val BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?"
        val QUERY_PARAM = "q"
        val MAX_RESULTS = "maxResults"
        val PRINT_TYPE = "printType"

        fun getBookInfo(queryString: String?): String? {
            var urlConnection: HttpURLConnection? = null
            var bufferedReader: BufferedReader? = null
            var bookJsonString = ""

            try {
                val builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build()

                val requestURL = URL(builtUri.toString())

                urlConnection = requestURL.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                val buffer = StringBuilder()
                if (inputStream == null) {
                    return null
                }
                bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val line = bufferedReader.readLine()

                while ((line) != null) {
                    buffer.append(line + "\n")
                }
                if (buffer.length == 0) {
                    return null
                }
                bookJsonString = bufferedReader.toString()

            } catch (e: Exception) {
                e.printStackTrace()
                return null
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                Log.d("pacz",  bookJsonString)
                return bookJsonString
            }

        }
    }
}