package com.projektekis.gpt3comedymaker

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    val url = "https://64c05aad7l.execute-api.us-east-2.amazonaws.com/Prod"
    var retrievedJsonJokesList: List<JsonJokes> = arrayListOf()
    private val goodFeedbackList: ArrayList<String> = arrayListOf()
    private val badFeedbackList: ArrayList<String> = arrayListOf()

    private val savedJokesList: ArrayList<String> = arrayListOf()
    private val defaultList = arrayListOf("Do I have a right to own a guitar? I don't.",
        "I like to joke. I do iced iced iced ices.",
        "I'll give you a list of the top 1% of the population who doesn't work two days a week, and " +
                "a list of the worst 6% that doesn't work any more... and a great name.",
        "Wont forget my last job. I can't do this anymore. I've lost my mind.")

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
        val loadBtn = findViewById<Button>(R.id.load_jokes)
        var jokeIndex = 0

        saveButton.setOnClickListener{
            if(!savedJokesList.contains(jokeHeader.text.toString()))
                savedJokesList.add(jokeHeader.text.toString())
        }

        //var jokeIterator = setJokeIterator(incomingJokes)
        jokeHeader.setOnClickListener{
            jokePunchline.isVisible = true
        }

        loadBtn.setOnClickListener{
            jokeHeader.text = retrievedJsonJokesList[jokeIndex].title
            jokeAuthor.text = retrievedJsonJokesList[jokeIndex].author
            jokePunchline.text = retrievedJsonJokesList[jokeIndex].body
            jokeIndex++
            //jokeIterator = setJokeIterator(incomingJokes)
            //jokeHeader.text = jokeIterator.next()
        }

        downVoteBtn.setOnClickListener{
            jokePunchline.isGone = true
            jokeIndex++
            if(jokeIndex >= retrievedJsonJokesList.size) jokeIndex = 0
            if(!badFeedbackList.contains(retrievedJsonJokesList[jokeIndex].id)) {
                badFeedbackList.add(retrievedJsonJokesList[jokeIndex].id)
            }
            jokeHeader.text = retrievedJsonJokesList[jokeIndex].title
            jokeAuthor.text = retrievedJsonJokesList[jokeIndex].author
            jokePunchline.text = retrievedJsonJokesList[jokeIndex].body

        }

        upVoteBtn.setOnClickListener{
            jokePunchline.isGone = true
            jokeIndex++
            if(jokeIndex >= retrievedJsonJokesList.size) jokeIndex = 0
            if(!goodFeedbackList.contains(retrievedJsonJokesList[jokeIndex].id)) {
                goodFeedbackList.add(retrievedJsonJokesList[jokeIndex].id)
            }
            jokeHeader.text = retrievedJsonJokesList[jokeIndex].title
            jokeAuthor.text = retrievedJsonJokesList[jokeIndex].author
            jokePunchline.text = retrievedJsonJokesList[jokeIndex].body
        }
         //TODO: Grab comments from each joke and add them to a list to send
//        val commentList: RecyclerView = findViewById(R.id.main_comment_list)
//        commentList.layoutManager = LinearLayoutManager(this)
//        commentList.adapter = CommentAdapter(arraylistOf(""))

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

    fun goToSavedList(sendSavedJokes: ArrayList<String>){
        val send = Intent(this, SavedJokes::class.java)
        send.putStringArrayListExtra("nav", sendSavedJokes)
        startActivity(send)
    }

    fun talkToApi(){
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
                retrievedJsonJokesList = collectIncomingJson(jsonJokesList)
            }
        })
    }

    fun collectIncomingJson(jsonJokesList: List<JsonJokes>): List<JsonJokes>{
        println("collectIncomingJson size: " + jsonJokesList.size)
        return jsonJokesList
    }

//    fun setJokeIterator(jokeList: ArrayList<String>): Iterator<String>{
//        return if (jokeList.isEmpty())
//            defaultList.iterator()
//        else
//            jokeList.iterator()
//    }
}