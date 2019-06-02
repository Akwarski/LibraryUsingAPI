package com.example.libraryusingapi

import android.os.AsyncTask
import android.widget.TextView
import com.example.libraryusingapi.NetworkUtils
import org.json.JSONObject

class FetchBook :
    AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String? {

        return NetworkUtils.getBookInfo(params[0])
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        val jsonObject = JSONObject(result)
        val itemArray = jsonObject.getJSONArray("items")

        for(i in 0..itemArray.length()){
            val book = itemArray.getJSONObject(i)
            val title : String? = null
            val authors : String? = null
            val volumeInfo = book.getJSONObject("volumeInfo")
        }
    }
}