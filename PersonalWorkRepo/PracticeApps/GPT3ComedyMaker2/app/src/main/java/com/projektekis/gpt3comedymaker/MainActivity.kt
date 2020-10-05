package com.projektekis.gpt3comedymaker

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_saved_jokes.*
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigationDrawer()

        val goodFeedbackList: ArrayList<String> = arrayListOf()
        val downVoteBtn = findViewById<FloatingActionButton>(R.id.dislikeFloatingButton)
        val upVoteBtn = findViewById<FloatingActionButton>(R.id.likeFloatingButton)
        val shownJoke = findViewById<TextView>(R.id.textView)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener{
            if(!savedJokesList.contains(shownJoke.text.toString()))
                savedJokesList.add(shownJoke.text.toString())
        }

        talkToApi()

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

    private fun setUpNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_favorites -> Toast.makeText(this, "Go to Favorites", Toast.LENGTH_SHORT).show()
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


    fun talkToApi(): Boolean {
        //TODO("Get info from api")
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

//                val jsonJokesList = gson.fromJson(body, JsonJokesList::class.java)
//
//                var contentList: ArrayList<String> = arrayListOf()


//                for (content in jsonJokesList.jsonJokes){
//                    contentList.add(jsonJokesList.)
//                }

            }

        })

        return false
    }


    class JsonJokesList(val jsonJokes: List<jsonJoke>){
        fun getItemCount(): Int{
            return jsonJokes.size
        }

    }

    class jsonJoke(val id: Int, val content: String, val name: String, val setup: String ){

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