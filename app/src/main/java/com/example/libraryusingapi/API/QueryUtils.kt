package com.example.libraryusingapi.API

import android.net.Uri
import android.util.Log
import com.example.libraryusingapi.FragmentBooks
import com.example.libraryusingapi.model.Book
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class QueryUtils {

    companion object {

        val BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?"
        val QUERY_PARAM = "q"
        val PRINT_TYPE = "printType"

        var numberOfResult: Int = 0

        fun buildQueryUrl(searchQuery: String): URL? {

            /*val builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
            .appendQueryParameter(NetworkUtils.QUERY_PARAM, searchQuery)
            .build()*/
            val uriBuilder = Uri.Builder()
            uriBuilder.scheme("https")
                .authority("www.googleapis.com")
                .appendPath("books").appendPath("v1").appendPath("volumes")
                .appendQueryParameter("q", searchQuery)
                .appendQueryParameter("maxResults", "20")

            return createUrl(uriBuilder.toString())
        }

        private fun createUrl(stringUrl: String): URL? {
            var url: URL? = null
            try {
                url = URL(stringUrl)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            return url
        }

        fun parseJsonToArrayList(jsonResponseString: String, bookArrayList: ArrayList<Book>) : ArrayList<Book>{
            var bookTitle = ""
            var bookAuthor = ""
            var bookPublishedYear = ""
            var bookCoverThumbnailUriString = ""
            var bookInfoPageUriString = ""

            try {
                val jsonObject = JSONObject(jsonResponseString)
                numberOfResult = jsonObject.getInt("totalItems")
                val jsonArray = jsonObject.getJSONArray("items")
                //var maxIterations = if(numberOfResult > maxResultsValue) maxResultsValue else numberOfResult

                for(i in 0 until jsonArray.length()){
                    val volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo")
                    bookTitle = volumeInfo.getString("title")
                    try {
                        val authorsArray = volumeInfo.getJSONArray("authors")
                        val authorStringBuilder = StringBuilder()
                        authorStringBuilder.append(authorsArray.getString(0))
                        for(j in 0 until authorsArray.length()){
                            authorStringBuilder.append(", ")
                                .append(authorsArray.getString(j))
                        }
                        bookAuthor = authorStringBuilder.toString()
                    }catch (e: JSONException){ e.printStackTrace() }

                    try {
                        bookPublishedYear = volumeInfo.getString("publishedDate").substring(0,4)
                    }catch (e: JSONException){e.printStackTrace()}

                    try {
                        bookCoverThumbnailUriString = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail")
                    }catch (e: JSONException){e.printStackTrace()}

                    try {
                        bookInfoPageUriString = volumeInfo.getString("infoLink")
                    }catch (e: JSONException){e.printStackTrace()}

                    bookArrayList.add(Book(bookTitle, bookAuthor, bookPublishedYear, bookCoverThumbnailUriString, bookInfoPageUriString))
                }

            }catch (e: JSONException){
                e.printStackTrace()
            }

            return bookArrayList
        }

        fun makeHttpRequest(url: URL?): String{
            var jsonResponse = ""

            if(url == null){
                return jsonResponse
            }

            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null

            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 10000
                urlConnection.connectTimeout = 15000
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                FragmentBooks.httpResponseCode = urlConnection.responseCode
                if(FragmentBooks.httpResponseCode == 200){
                    inputStream = urlConnection.inputStream
                    jsonResponse = readFromStream(inputStream)
                }

            }catch (e: IOException){ e.printStackTrace()}
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect()
                if(inputStream != null){
                    try {
                        inputStream.close()
                    }catch (e: IOException){e.printStackTrace()}
                }
            }
            return jsonResponse
        }

        @Throws(IOException::class)
        fun readFromStream(inputStream: InputStream): String {
            val stringBuilder = StringBuilder()
            if(inputStream != null){
                val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
                val bufferedReader = BufferedReader(inputStreamReader)
                var line = bufferedReader.readLine()
                while(line != null){
                    stringBuilder.append(line + "\n")
                    line = bufferedReader.readLine()
                }
            }
            return stringBuilder.toString()
        }
    }
}