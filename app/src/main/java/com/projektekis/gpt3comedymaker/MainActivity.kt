package com.projektekis.gpt3comedymaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException



class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    private val url = "https://64c05aad7l.execute-api.us-east-2.amazonaws.com/Prod"
    var retrievedJsonJokesList: List<JsonJokes> = arrayListOf()
    private val goodFeedbackList: ArrayList<String> = arrayListOf()
    private val badFeedbackList: ArrayList<String> = arrayListOf()

    private val savedJokesList: ArrayList<JsonJokes> = arrayListOf()

    private val defaultList = arrayListOf("Do I have a right to own a guitar? I don't.",
        "I like to joke. I do iced iced iced ices.",
        "I'll give you a list of the top 1% of the population who doesn't work two days a week, and " +
                "a list of the worst 6% that doesn't work any more... and a great name.",
        "Wont forget my last job. I can't do this anymore. I've lost my mind.")

    var jsonKeys: ArrayList<String> = arrayListOf()




    override fun onCreate(savedInstanceState: Bundle?) {
        talkToApi()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigationDrawer()

        val downVoteBtn = findViewById<FloatingActionButton>(R.id.dislikeFloatingButton)
        val upVoteBtn = findViewById<FloatingActionButton>(R.id.likeFloatingButton)
        val jokeHeader = findViewById<TextView>(R.id.titleTextView)
        val jokePunchline = findViewById<TextView>(R.id.textViewPunchline)
        val jokeAuthor = findViewById<TextView>(R.id.authorName)
        val saveButton = findViewById<FloatingActionButton>(R.id.save_button)
        val showComments = findViewById<TextView>(R.id.main_show_comments)
        val commentList: RecyclerView = findViewById(R.id.main_comment_list)
        val tinydb = TinyDB(this)

        jsonKeys = tinydb.getListString("yourkey")
        println("\n----Starting talk to api with keys---\n")
        talkToApi(jsonKeys)

        var jokeIndex = 0

        commentList.layoutManager = LinearLayoutManager(this)



        saveButton.setOnClickListener{
            if(!savedJokesList.contains(retrievedJsonJokesList[jokeIndex])) {
                savedJokesList.add(retrievedJsonJokesList[jokeIndex])

            }
        }

        jokeHeader.setOnClickListener{
            jokePunchline.isVisible = true
            if(retrievedJsonJokesList[jokeIndex].comments.isNotEmpty())
                showComments.isVisible = true
        }


        intro_layout.setOnClickListener{
            jokeHeader.text = retrievedJsonJokesList[jokeIndex].title
            jokeAuthor.text = retrievedJsonJokesList[jokeIndex].author
            jokePunchline.text = retrievedJsonJokesList[jokeIndex].body
            intro_layout.isGone = true
            println("current comments: " + retrievedJsonJokesList[jokeIndex].comments)
            commentList.adapter = CommentAdapter(retrievedJsonJokesList[jokeIndex].comments)

            jokeIndex++


        }

        showComments.setOnClickListener{
            commentList.isVisible = true

            Toast.makeText(this, "show comments pressed", Toast.LENGTH_SHORT).show()
        }

        downVoteBtn.setOnClickListener{
            showComments.isGone = true
            commentList.isGone = true
            jokePunchline.isGone = true
            jokeIndex++
            commentList.adapter = CommentAdapter(retrievedJsonJokesList[jokeIndex].comments)

            if(jokeIndex >= retrievedJsonJokesList.size) jokeIndex = 0
            if(!badFeedbackList.contains(retrievedJsonJokesList[jokeIndex].id)) {
                badFeedbackList.add(retrievedJsonJokesList[jokeIndex].id)
            }
            jokeHeader.text = retrievedJsonJokesList[jokeIndex].title
            jokeAuthor.text = retrievedJsonJokesList[jokeIndex].author
            jokePunchline.text = retrievedJsonJokesList[jokeIndex].body

        }

        upVoteBtn.setOnClickListener{
            showComments.isGone = true
            commentList.isGone = true
            jokePunchline.isGone = true
            jokeIndex++
            commentList.adapter = CommentAdapter(retrievedJsonJokesList[jokeIndex].comments)

            if(jokeIndex >= retrievedJsonJokesList.size) jokeIndex = 0
            if(!goodFeedbackList.contains(retrievedJsonJokesList[jokeIndex].id)) {
                goodFeedbackList.add(retrievedJsonJokesList[jokeIndex].id)
            }
            jokeHeader.text = retrievedJsonJokesList[jokeIndex].title
            jokeAuthor.text = retrievedJsonJokesList[jokeIndex].author
            jokePunchline.text = retrievedJsonJokesList[jokeIndex].body
        }


    }

    override fun onPause() {
        super.onPause()
        print("---- now in onPause() -----")
        val tinydb = TinyDB(this)
        for(keys in savedJokesList)
            jsonKeys.add(keys.id)
        tinydb.putListString("yourkey", jsonKeys)
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
                    TalkToAPI("like").sendFeedback(goodFeedbackList, retrievedJsonJokesList)
                    TalkToAPI("dislike").sendFeedback(badFeedbackList, retrievedJsonJokesList)
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

    fun goToSavedList(sendSavedJokes: ArrayList<JsonJokes>){
        val send = Intent(this, SavedJokes::class.java)
        send.putParcelableArrayListExtra("nav", sendSavedJokes)
        startActivity(send)
    }

    fun talkToApi(){
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
                //println(jsonJokesList)

                retrievedJsonJokesList = collectIncomingJson(jsonJokesList)
            }
        })
    }

    fun talkToApi(jsonKeys: ArrayList<String>){
        val client = OkHttpClient()

        for (key in jsonKeys){
            val request = Request.Builder().url("$url/$key").build()

            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    println("Failed to execute request.")
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    println(body)
                    val gson = GsonBuilder().create()
                    savedJokesList.add( gson.fromJson(body, JsonJokes::class.java))
                }
            })
        }


    }

    fun collectIncomingJson(jsonJokesList: List<JsonJokes>): List<JsonJokes>{
        println("collectIncomingJson size: " + jsonJokesList.size)
        return jsonJokesList
    }


}