package com.projektekis.gpt3comedymaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_layout.view.*

class SavedJokes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_jokes)

        val favoriteJokes = intent.getStringArrayListExtra("nav")
        println(favoriteJokes)


        val savedJokesList: RecyclerView = findViewById(R.id.saved_jokes_list)
        savedJokesList.layoutManager = LinearLayoutManager(this)
        savedJokesList.adapter = Adapter(jokeArrayList = favoriteJokes)
    }

    class Adapter(val jokeArrayList: ArrayList<String>): RecyclerView.Adapter<MyViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val cellForRow = layoutInflater.inflate(R.layout.row_layout, parent, false)
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

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}