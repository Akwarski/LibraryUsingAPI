package com.example.libraryusingapi

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.example.libraryusingapi.databinding.RecyclerBookModelBinding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentBooks : Fragment() {
    lateinit var books: Array<String>
    lateinit var imageResIds: Array<String>
    lateinit var titles: Array<String>
    lateinit var descriptions: Array<String>
    lateinit var urls: Array<String>
    private lateinit var listener: OnBookSelected

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_books, container, false)
        val activity = activity as Context
        val recyclerView = view.findViewById<RecyclerView>(R.id.booksList)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = BookAdapter(activity)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBookSelected) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
        val resources = context.resources
        imageResIds = resources.getStringArray(R.array.names)
        titles = resources.getStringArray(R.array.names)
        descriptions = resources.getStringArray(R.array.names)
        urls = resources.getStringArray(R.array.names)

        books = titles
    }

    interface OnBookSelected{
        fun onBookSelected(bookModel: BookModel)
    }

    companion object {
        /*@JvmStatic
        fun newInstance() =
            FragmentBooks().apply{
            arguments = Bundle().apply {
            }
        }*/
        fun newInstance():FragmentBooks{
            return FragmentBooks()
        }
    }

    internal inner class BookAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {
        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val recyclerBookModelBinding = RecyclerBookModelBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(recyclerBookModelBinding.root, recyclerBookModelBinding)
        }

        override fun getItemCount() = titles.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val book = BookModel(imageResIds[position], titles[position], descriptions[position], urls[position])
            holder.setData(book)
            holder.itemView.setOnClickListener{listener.onBookSelected(book)}
        }
    }

    internal inner class ViewHolder constructor(itemView:View, private val recyclerBookModelBinding : RecyclerBookModelBinding):
            RecyclerView.ViewHolder(itemView){
        fun setData(bookModel: BookModel){
            recyclerBookModelBinding.bookModel = bookModel
        }
    }
}
