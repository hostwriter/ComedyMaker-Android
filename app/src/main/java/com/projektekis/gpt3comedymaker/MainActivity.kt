package com.projektekis.gpt3comedymaker

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    val url = "https://64c05aad7l.execute-api.us-east-2.amazonaws.com/Prod"
    var retrievedJsonJokesList: List<JsonJokes> = listOf()
    val goodFeedbackList: ArrayList<String> = arrayListOf()
    val badFeedbackList: ArrayList<String> = arrayListOf()

    val savedJokesList: ArrayList<String> = arrayListOf()
    val defaultList = arrayListOf("Do I have a right to own a guitar? I don't.",
        "I like to joke. I do iced iced iced ices.",
        "I'll give you a list of the top 1% of the population who doesn't work two days a week, and " +
                "a list of the worst 6% that doesn't work any more... and a great name.",
        "Wont forget my last job. I can't do this anymore. I've lost my mind.")

    override fun onCreate(savedInstanceState: Bundle?) {
        val incomingJokes = talkToApi()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigationDrawer()

        val downVoteBtn = findViewById<FloatingActionButton>(R.id.dislikeFloatingButton)
        val upVoteBtn = findViewById<FloatingActionButton>(R.id.likeFloatingButton)
        val shownJoke = findViewById<TextView>(R.id.textView)
        val saveButton = findViewById<Button>(R.id.save_button)
        val loadBtn = findViewById<Button>(R.id.load_jokes)

        saveButton.setOnClickListener{
            if(!savedJokesList.contains(shownJoke.text.toString()))
                savedJokesList.add(shownJoke.text.toString())
        }

        var jokeIterator = setJokeIterator(incomingJokes)

        loadBtn.setOnClickListener{
            jokeIterator = setJokeIterator(incomingJokes)
            shownJoke.text = jokeIterator.next()
        }

        downVoteBtn.setOnClickListener{
            if(jokeIterator.hasNext()) {
                if(!badFeedbackList.contains(shownJoke.text.toString())) {
                    badFeedbackList.add(shownJoke.text.toString())
                }
                shownJoke.text = jokeIterator.next()
            }else{
                if(!badFeedbackList.contains(shownJoke.text.toString())) {
                    badFeedbackList.add(shownJoke.text.toString())
                }
                jokeIterator = setJokeIterator(incomingJokes)
                shownJoke.text = jokeIterator.next()
            }
        }

        upVoteBtn.setOnClickListener{
            if(jokeIterator.hasNext()) {
                if(!goodFeedbackList.contains(shownJoke.text.toString())) {
                    goodFeedbackList.add(shownJoke.text.toString())
                }
                shownJoke.text = jokeIterator.next()

            }else{
                jokeIterator = setJokeIterator(incomingJokes)
                if(!goodFeedbackList.contains(shownJoke.text.toString())) {
                    goodFeedbackList.add(shownJoke.text.toString())
                }
                shownJoke.text = jokeIterator.next()

            }
        }

    }

    private fun setUpNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_favorites -> {
                    goToSavedList(savedJokesList)
                }
                R.id.nav_about -> {
                    val intent = Intent(this, AboutPage::class.java)
                    startActivity(intent)
                }
                R.id.nav_home ->{
                    val intent = Intent(this, TopJokesPage::class.java)
                    startActivity(intent)
                }
                R.id.send_feedback->{
                    sendLikeFeedback(goodFeedbackList)
                    sendDislikeFeedback(badFeedbackList)
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToSavedList(sendSavedJokes: ArrayList<String>){
        val send = Intent(this, SavedJokes::class.java)
        send.putStringArrayListExtra("nav", sendSavedJokes)
        startActivity(send)
    }


    fun talkToApi(): ArrayList<String> {
        val contentList: ArrayList<String> = arrayListOf()
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request.")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                val gson = GsonBuilder().create()

                val jsonJokesList = gson.fromJson(body, Array<JsonJokes>::class.java).toList()
                println(jsonJokesList)
               for (jsonJoke in jsonJokesList){
                    contentList.add(jsonJoke.title + "\n\n"+ jsonJoke.body)
                }
                //println(contentList)
                retrievedJsonJokesList = collectIncomingJson(jsonJokesList)
            }

        })

        return contentList
    }

    class JsonJokes(val theme: String, val comments: List<Comments>, val id: String, val title: String, val body: String, val author: String )

    class Comments(val content: String, val author: String)


    fun sendLikeFeedback(likedJokes: ArrayList<String>){
        val toSend = arrayListOf<String>()
        for(items in likedJokes){
            for(jsonJoke in retrievedJsonJokesList){
                if(jsonJoke.title+ "\n\n" + jsonJoke.body == items){
                    toSend.add(jsonJoke.id)
                }
            }
        }

        val client = OkHttpClient()
        val requestBody = "".toRequestBody()
        println("TO SEND FEEDBACK SIZE: " + toSend.size)
        for (id in toSend) {
            val request = Request.Builder()
                .put(requestBody)
                .url("$url/$id/like")
                .build()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException){
                    println("callback failed")
                }

                override fun onResponse(call: Call, response: Response) {
                    println(response)
                }
            })
        }

    }

    fun collectIncomingJson(jsonJokesList: List<JsonJokes>): List<JsonJokes>{
        println("COLLECT JSON METHOD SIZE: " + jsonJokesList.size)
        return jsonJokesList
    }

    fun setJokeIterator(jokeList: ArrayList<String>): Iterator<String>{
        return if (jokeList.isEmpty())
            defaultList.iterator()
        else
            jokeList.iterator()

    }

    fun sendDislikeFeedback(likedJokes: ArrayList<String>){
        val toSend = arrayListOf<String>()
        for(items in likedJokes){
            for(jsonJoke in retrievedJsonJokesList){
                if(jsonJoke.title+ "\n\n" + jsonJoke.body == items){
                    toSend.add(jsonJoke.id)
                }
            }
        }

        val client = OkHttpClient()
        val requestBody = "".toRequestBody()
        println("TO SEND FEEDBACK SIZE: " + toSend.size)
        for (id in toSend) {
            val request = Request.Builder()
                .put(requestBody)
                .url("$url/$id/dislike")
                .build()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException){
                    println("callback failed")
                }

                override fun onResponse(call: Call, response: Response) {
                    println(response)
                }
            })
        }

    }

    fun getSingleJokeFromApi(){
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request.")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //println(body)
                val gson = GsonBuilder().create()

                val jsonJokesList = gson.fromJson(body, Array<JsonJokes>::class.java).toList()
                println(jsonJokesList)

                //println(contentList)
                retrievedJsonJokesList = collectIncomingJson(jsonJokesList)
            }

        })
    }


}