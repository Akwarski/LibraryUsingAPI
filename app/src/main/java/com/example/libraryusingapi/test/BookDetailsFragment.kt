package com.example.libraryusingapi.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.libraryusingapi.R
import com.example.libraryusingapi.databinding.FragmentBookDetailsBinding
import com.example.libraryusingapi.model.Book

class BookDetailsFragment: Fragment() {

    companion object {
        fun newInstance(bookModel: Book): BookDetailsFragment {
            val arguments = Bundle()
            arguments.putSerializable("model", bookModel)

            val fragment = BookDetailsFragment()
            fragment.arguments = arguments

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentBookDetailsBinding = FragmentBookDetailsBinding.inflate(inflater, container, false)

        val model = arguments!!.getSerializable("model") as BookModel
        fragmentBookDetailsBinding.bookModel = model
        model.description = String.format(getString(R.string.format), model.description, model.url)
        return fragmentBookDetailsBinding.root
    }
}