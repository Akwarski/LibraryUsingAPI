package com.example.libraryusingapi.get

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import com.example.libraryusingapi.API.QueryUtils
import com.example.libraryusingapi.model.Book

class Fetch(context: Context, var bookArray:ArrayList<Book>, val mQuery:String) : AsyncTaskLoader<ArrayList<Book>>(context) {

    override fun loadInBackground(): ArrayList<Book>? {
        Log.d("pacz1", "Fetch!")
        //QueryUtils.getPreferences(context)
        val queryUrl = QueryUtils.buildQueryUrl(mQuery)
        val jsonResponseString = QueryUtils.makeHttpRequest(queryUrl) as String
        bookArray = QueryUtils.parseJsonToArrayList(jsonResponseString, bookArray)

        Log.d("pacz", ""+bookArray)

        return bookArray
    }//3
}