package com.example.libraryusingapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.libraryusingapi.databinding.FragmentBookDetailsBinding

class BookDetailsFragment: Fragment() {

    companion object {
        fun newInstance(bookModel: BookModel): BookDetailsFragment {
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