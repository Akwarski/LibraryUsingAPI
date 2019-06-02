package com.example.libraryusingapi

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentBooks.OnBookSelected{
    override fun onBookSelected(bookModel: BookModel) {
        val bookDetailsFragment = BookDetailsFragment.newInstance(bookModel)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentFrame, bookDetailsFragment, "bookDetails")
            .addToBackStack("details")
            .commit()
    }


    private lateinit var container: View
    var debugMode : Int = 0
    lateinit var editText : EditText
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }


        container = findViewById(R.id.motionLayout)
        //listView = findViewById(R.id.list)
        editText = findViewById(R.id.editText)
        imageView = findViewById(R.id.searchImageView)

        //night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        //animation path
        debugMode = MotionLayout.DEBUG_SHOW_PATH
    }

    fun changeState(view: View) {
        val motionLayout = container as? MotionLayout ?: return
        motionLayout.setDebugMode(debugMode)
        if (motionLayout.progress > 0.5f) {
            motionLayout.transitionToStart()
        } else {
            motionLayout.transitionToEnd()
        }
    }

    fun display(){
        //fragment definition
        val fragment = FragmentBooks.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentFrame, fragment)
            .addToBackStack("main")
            .commit()

        Toast.makeText(applicationContext, editText.text, Toast.LENGTH_LONG).show()
    }

    fun searchBooks(view: View){
        val mQuery = editText.text.toString()
        FetchBook().execute(mQuery)
        display()
    }
}