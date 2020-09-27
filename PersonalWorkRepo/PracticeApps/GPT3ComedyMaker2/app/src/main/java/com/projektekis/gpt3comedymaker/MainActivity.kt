package com.projektekis.gpt3comedymaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    val defaultList = arrayListOf("Do I have a right to own a guitar? I don't.",
        "I like to joke. I do iced iced iced ices.",
        "I'll give you a list of the top 1% of the population who doesn't work two days a week, and " +
                "a list of the worst 6% that doesn't work any more... and a great name.",
        "Wont forget my last job. I can't do this anymore. I've lost my mind.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goodFeedbackList: ArrayList<String> = arrayListOf()
        val downVoteBtn = findViewById<FloatingActionButton>(R.id.dislikeFloatingButton)
        val upVoteBtn = findViewById<FloatingActionButton>(R.id.likeFloatingButton)
        val shownJoke = findViewById<TextView>(R.id.textView)

        //TODO (This will be replaced by incoming info from api)
        var jokeIterator = setJokeIterator(defaultList)


        downVoteBtn.setOnClickListener{
            if(jokeIterator.hasNext()) {
                shownJoke.text = jokeIterator.next()
            }else{
                jokeIterator = setJokeIterator(defaultList)
                shownJoke.text = jokeIterator.next()
            }
        }

        upVoteBtn.setOnClickListener{
            if(jokeIterator.hasNext()) {
                goodFeedbackList.add(shownJoke.text.toString())
                shownJoke.text = jokeIterator.next()

            }else{
                jokeIterator = setJokeIterator(defaultList)
                goodFeedbackList.add(shownJoke.text.toString())
                shownJoke.text = jokeIterator.next()
            }
        }

    }
    fun talkToApi(incomingJokes: ArrayList<String>): Boolean {
        //TODO("Get info from api")
        return incomingJokes.isNotEmpty()
    }

    fun sendFeedback(){
        //TODO("Send list of liked jokes back to the api")
    }

    fun setJokeIterator(jokeList: ArrayList<String>): Iterator<String>{
        if (jokeList.isEmpty())
            return defaultList.iterator()
        else
            return jokeList.iterator()
    }

}