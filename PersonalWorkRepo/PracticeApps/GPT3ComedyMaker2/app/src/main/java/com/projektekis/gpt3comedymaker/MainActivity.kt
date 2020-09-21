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
        var testList = arrayListOf("Do I have a right to own a guitar? I don't.",
            "I like to joke. I do iced iced iced ices.",
            "I'll give you a list of the top 1% of the population who doesn't work two days a week, and a list of the worst 6% that doesn't work any more... and a great name.",
            "Wont forget my last job. I can't do this anymore. I've lost my mind.")

        var goodFeedbackList: ArrayList<String> = arrayListOf()


        var jokeIterator = testList.iterator()

        val downVoteBtn = findViewById<FloatingActionButton>(R.id.dislikeFloatingButton)
        val upVoteBtn = findViewById<FloatingActionButton>(R.id.likeFloatingButton)
        val shownJoke = findViewById<TextView>(R.id.textView)

        fun setJokeIterator(jokeList: ArrayList<String>){
            jokeIterator = if(jokeList.isEmpty()){
                //Test list will become a default list in case incoming lists are be empty
                testList.iterator()
            }else {
                jokeList.iterator()
            }
        }

        fun sendFeedback(){
            TODO("Send list of liked jokes back to the api")
        }

        downVoteBtn.setOnClickListener{
            if(jokeIterator.hasNext()) {
                shownJoke.text = jokeIterator.next()
            }else{
                setJokeIterator(testList)
                shownJoke.text = jokeIterator.next()
            }
        }

        upVoteBtn.setOnClickListener{
            if(jokeIterator.hasNext()) {
                goodFeedbackList.add(shownJoke.text.toString())
                shownJoke.text = jokeIterator.next()

            }else{
                setJokeIterator(testList)
                goodFeedbackList.add(shownJoke.text.toString())
                shownJoke.text = jokeIterator.next()
            }
        }

    }

}