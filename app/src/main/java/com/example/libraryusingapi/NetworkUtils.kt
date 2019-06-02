package com.example.libraryusingapi

import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
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
                Log.d("pacz", "jeden")
                var builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build()

                var requestURL = URL(builtUri.toString())

                urlConnection = requestURL.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                var inputStream = urlConnection.inputStream
                var buffer = StringBuffer()
                if (inputStream == null) {
                    return null
                }
                bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line = bufferedReader.readLine()

                while ((line) != null) {
                    buffer.append(line + "\n")
                }
                if (buffer.length == 0) {
                    return null
                }
                Log.d("pacz", "dwa")
                bookJsonString = bufferedReader.toString()

            } catch (e: Exception) {
                e.printStackTrace()
                return null
            } finally {
                Log.d("pacz", "finally")
                Log.d("pacz2",  bookJsonString)
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

                Log.d("pacz2",  bookJsonString)
                return bookJsonString
            }

        }
    }
}