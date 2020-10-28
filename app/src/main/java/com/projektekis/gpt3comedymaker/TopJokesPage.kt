package com.projektekis.gpt3comedymaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.row_layout.view.*

class TopJokesPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_jokes_page)



        val defaultList = arrayListOf("Do I have a right to own a guitar? I don't.",
            "I like to joke. I do iced iced iced ices.",
            "I'll give you a list of the top 1% of the population who doesn't work two days a week, and " +
                    "a list of the worst 6% that doesn't work any more... and a great name.",
            "Wont forget my last job. I can't do this anymore. I've lost my mind.")

        val startButton = findViewById<FloatingActionButton>(R.id.start_button)

        startButton.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        val homeList: RecyclerView = findViewById(R.id.home_list)
        homeList.layoutManager = LinearLayoutManager(this)
        homeList.adapter = TopJokesPage.Adapter(jokeArrayList = defaultList)
    }

    class Adapter(val jokeArrayList: ArrayList<String>): RecyclerView.Adapter<MyViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val cellForRow = layoutInflater.inflate(R.layout.home_row_layout, parent, false)
            return MyViewHolder(cellForRow)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val favoriteJoke = jokeArrayList[position]
            holder.itemView.joke_list_item?.text = favoriteJoke
        }

        override fun getItemCount(): Int{
            return jokeArrayList.size
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}