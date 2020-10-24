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
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    val url = "https://64c05aad7l.execute-api.us-east-2.amazonaws.com/Prod"
    var retrievedJsonJokesList: List<JsonJokes> = listOf()

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

        val goodFeedbackList: ArrayList<String> = arrayListOf()
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
        }

        downVoteBtn.setOnClickListener{
            sendFeedback(goodFeedbackList)
            if(jokeIterator.hasNext()) {
                shownJoke.text = jokeIterator.next()
            }else{
                jokeIterator = setJokeIterator(incomingJokes)
                shownJoke.text = jokeIterator.next()
            }
        }

        upVoteBtn.setOnClickListener{
            if(jokeIterator.hasNext()) {
                goodFeedbackList.add(shownJoke.text.toString())
                shownJoke.text = jokeIterator.next()

            }else{
                jokeIterator = setJokeIterator(incomingJokes)
                goodFeedbackList.add(shownJoke.text.toString())
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
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
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
                val body = response?.body?.string()
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


    fun sendFeedback(likedJokes: ArrayList<String>){
        //TODO("Send list of liked jokes back to the api")
        val toSend = arrayListOf<String>()
        for(items in likedJokes){
            for(jsonJoke in retrievedJsonJokesList){
                if(jsonJoke.title+ "\n\n" + jsonJoke.body == items){
                    toSend.add(jsonJoke.id)
                }
            }
        }

        val client = OkHttpClient()
        val payload = "test payload"
        val requestbody = payload.toRequestBody()
        val body = RequestBody.create(null, "")
        println("TO SEND FEEDBACK SIZE: " + toSend.size)
        for (id in toSend) {
            println("ID sent: " + id+"\nFull URL: " + "$url/$id/like")
            val request = Request.Builder()
                .put(requestbody)
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
//                .url("$url/$id/like")
//                .put(body)
//                .build()

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

    fun getSingleJokeFromApi(){
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request.")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()
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