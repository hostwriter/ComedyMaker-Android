package com.projektekis.gpt3comedymaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Test list: This list will be replaced on app startup with data from the api
        val testList = arrayListOf("test joke 1",
            "test joke 2",
            "test joke 3",
            "knock knock joke")


        val jokeIterator = testList.iterator()

        val downVoteBtn = findViewById<FloatingActionButton>(R.id.dislikeFloatingButton)
        val upVoteBtn = findViewById<FloatingActionButton>(R.id.likeFloatingButton)
        val shownJoke = findViewById<TextView>(R.id.textView)

        downVoteBtn.setOnClickListener{
            shownJoke.text = jokeIterator.next()
        }

        upVoteBtn.setOnClickListener{
            shownJoke.text = jokeIterator.next()
        }

    }



}