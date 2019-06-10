package com.example.libraryusingapi

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.example.libraryusingapi.model.Book

class MainActivity : AppCompatActivity(){
    /*override fun OnBookSelected(book: Book) {
        Log.d("ggg", "finish")
    }*/

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
        editText = findViewById(R.id.editText)
        imageView = findViewById(R.id.searchImageView)

        //night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        //animation path
        //debugMode = MotionLayout.DEBUG_SHOW_PATH
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
        fragment.query = editText.text.toString()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentFrame, fragment)
            .addToBackStack("main")
            .commit()
    }

    fun searchBooks(view: View){
        display()
    }
}