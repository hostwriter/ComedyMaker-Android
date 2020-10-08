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
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

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

        //TODO (This will be replaced by incoming info from api)
        var jokeIterator = setJokeIterator(incomingJokes)

        loadBtn.setOnClickListener{
            jokeIterator = setJokeIterator(incomingJokes)
        }

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

    private fun setUpNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_favorites -> {
                    val intent = Intent(this, SavedJokes::class.java)
                    intent.putStringArrayListExtra("nav",savedJokesList)
                    startActivity(intent)
                }
                R.id.nav_about -> {
                    val intent = Intent(this, AboutPage::class.java)
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
        send.putStringArrayListExtra("key", savedJokesList)
        startActivity(send)
    }


    fun talkToApi(): ArrayList<String> {
        val contentList: ArrayList<String> = arrayListOf()
        val url = "https://64c05aad7l.execute-api.us-east-2.amazonaws.com/Prod"
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

                val jsonJokesList: List<JsonJokesList> = gson.fromJson(body, Array<JsonJokesList>::class.java).toList()

               for (jsonJoke in jsonJokesList){
                    contentList.add(jsonJoke.content)
                }
                println(contentList)
            }

        })

        return contentList
    }

    class JsonJokesList(val content: String, val comments: List<Comments>, val date: String, val likes: Int, val id: String )

    class Comments(val content: String, val id: String, val likes: Int)

//    [
//    {"content":"I made joke again",
//    "comments":[{"content":"blah blah blah","id":"1","likes":2},{"content":"lame","id":"2","likes":99}],
//    "date":"2020-10-06",
//    "likes":10000,
//    "id":"1db9c402-0833-11eb-8430-5f572836b1bf"},
//
//    {"content":"I made joke","comments":[{"content":"blah blah blah",
//    "id":"1","likes":2},{"content":"lame","id":"2","likes":99}],"date":"2020-10-06","likes"
//         :10000,"id":"fb622de2-0830-11eb-b6d2-f327a69147d3"},


    fun sendFeedback(){
        //TODO("Send list of liked jokes back to the api")
    }

    fun setJokeIterator(jokeList: ArrayList<String>): Iterator<String>{
        return if (jokeList.isEmpty())
            defaultList.iterator()
        else
            jokeList.iterator()

    }

}