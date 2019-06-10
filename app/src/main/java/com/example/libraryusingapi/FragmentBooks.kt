package com.example.libraryusingapi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.bumptech.glide.Glide
import com.example.libraryusingapi.API.QueryUtils
import com.example.libraryusingapi.get.Fetch
import com.example.libraryusingapi.model.Book
import android.widget.ArrayAdapter as ArrayAdapter
import android.net.Uri
import kotlin.collections.ArrayList
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.R.drawable.btn_plus
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.ListFragment

class FragmentBooks : ListFragment(), LoaderManager.LoaderCallbacks<ArrayList<Book>>{
    var query: String = ""
    lateinit var bookArrayList: ArrayList<Book>
    var arrayListSize: Int = 0
    lateinit var bookAdapter: BookAdapter

    companion object {
        var httpResponseCode: Int = 0
        fun newInstance():FragmentBooks{
            return FragmentBooks()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Book>> {
        return Fetch(context!!, bookArrayList, query)
    } //2

    override fun onLoadFinished(loader: Loader<ArrayList<Book>>, data: ArrayList<Book>?) {
        if(httpResponseCode != 200){
            Toast.makeText(context, "connection error", Toast.LENGTH_LONG).show()
            return
        }
        if(QueryUtils.numberOfResult == 0){
            Toast.makeText(context, "Not found books", Toast.LENGTH_LONG).show()
            return
        }

        arrayListSize = data!!.size
        bookAdapter.notifyDataSetChanged()

    } //5

    override fun onLoaderReset(loader: Loader<ArrayList<Book>>) {
        fragmentManager?.popBackStack()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_books, container, false)

        bookAdapter = BookAdapter(context!!, R.layout.fragment_books, bookArrayList)
        val listView = view.findViewById(android.R.id.list) as ListView

        /*listView.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            Log.d("ggg", "setOnTouchListener")
            true
        }*/

        /*listView.onItemClickListener = OnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            Log.d("ggg", "onItemClickListener")
        }

        listView.onItemClickListener = object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(getActivity(),"yolo", Toast.LENGTH_LONG).show()
                Log.d("ggg", "onItemClick")
            }

        }*/

        listView.adapter = bookAdapter
        bookAdapter.notifyDataSetChanged()

        return view
    } //4

    interface onBookSelected{
        fun OnBookSelected(book: Book)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        Log.d("ggg", "onListItemClick")
        super.onListItemClick(l, v, position, id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bookArrayList = ArrayList()
        bookArrayList.clear()
        //loaderManager.initLoader(0, null, this).forceLoad()
        loaderManager.restartLoader(0, null, this)
        loaderManager.getLoader<Any>(0)?.forceLoad()
    }//1



    open inner class BookAdapter(context: Context, resource: Int, bookArrayList: ArrayList<Book>) : ArrayAdapter<Book>(context, resource, bookArrayList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var viewHolder: ViewHolder?
            var retView: View

            if(convertView == null){
                val layoutInflater = LayoutInflater.from(context)
                val container = layoutInflater.inflate(R.layout.fragment_books, null, false)
                retView = layoutInflater.inflate(R.layout.recycler_book_model, container.findViewById(R.id.containerFL))

                viewHolder = ViewHolder()
                viewHolder.imageView = retView.findViewById(R.id.books_listitem_imageview)
                viewHolder.textViewTitle = retView.findViewById(R.id.books_listitem_textview_title)
                viewHolder.textViewAuthor = retView.findViewById(R.id.books_listitem_textview_author)
                viewHolder.textViewYear = retView.findViewById(R.id.books_listitem_textview_year)
            }else{
                retView = convertView
                viewHolder = convertView.getTag() as? ViewHolder
            }

            val currentBook = getItem(position) ?: return convertView

            /*listView.onItemClickListener = object: AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.d("ggg", ""+position)
                    val currentBook = bookAdapter.getItem(position)
                    if(currentBook.infoPageUri.isNotEmpty()){
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.infoPageUri))
                        if(intent.resolveActivity(context!!.packageManager) != null)
                            startActivity(intent)
                        else
                            Toast.makeText(context, "Information page not available", Toast.LENGTH_LONG).show()
                    }
                }
            }*/

            /*convertView?.setOnClickListener(View.OnClickListener {
                Log.d("ggg", "convertView.setOnClickListener")
            })*/

            if(currentBook.thumbnailUri.isNotEmpty()){
                val https = currentBook.thumbnailUri.substring(0,4) +
                        "s" + currentBook.thumbnailUri.substring(4)
                val parseUri = Uri.parse(https)
                viewHolder?.imageView?.let {
                    Glide.with(context)
                        .asBitmap()
                        .load(parseUri)
                        .into(it)
                }
            }else{
                viewHolder?.imageView?.let {
                    Glide.with(context)
                        .load(R.drawable.ic_launcher_foreground)
                        .into(it)
                }
            }

            viewHolder?.textViewTitle?.text = currentBook.title
            viewHolder?.textViewAuthor?.text = currentBook.author
            val publishedYearDate = "("+currentBook.publishedYear+")"
            viewHolder?.textViewYear?.text = publishedYearDate

            return retView
        }
    }

    internal inner class ViewHolder {
        internal var imageView: ImageView? = null
        internal var textViewTitle: TextView? = null
        internal var textViewAuthor: TextView? = null
        internal var textViewYear: TextView? = null
    }
}