package com.projektekis.gpt3comedymaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_layout.view.*

class SavedJokes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_jokes)


        val favoriteJokes = intent.getParcelableArrayListExtra<JsonJokes>("nav")

        val savedJokesList: RecyclerView = findViewById(R.id.saved_jokes_list)

        savedJokesList.layoutManager = LinearLayoutManager(this)
        savedJokesList.adapter = Adapter(jokeArrayList = favoriteJokes)


    }

    class Adapter(val jokeArrayList: ArrayList<JsonJokes>): RecyclerView.Adapter<MyViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val cellForRow = layoutInflater.inflate(R.layout.row_layout, parent, false)

            return MyViewHolder(cellForRow)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val favoriteJoke = jokeArrayList[position]
            holder.itemHeader.text = favoriteJoke.title
            holder.itemAuthor.text = favoriteJoke.author
            holder.itemPunchline.text = favoriteJoke.body

            val intent = Intent(holder.itemView.context, CommentActivity::class.java)
            val commentsList = jokeArrayList[position].comments
            holder.showComments.setOnClickListener{

                intent.putParcelableArrayListExtra("item", commentsList)
                holder.itemView.context.startActivity(intent)

            }
        }

        override fun getItemCount(): Int{
            return jokeArrayList.size
        }

    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val itemAuthor = view.findViewById<TextView>(R.id.text_author)
        val itemHeader = view.findViewById<TextView>(R.id.joke_list_item)
        val itemPunchline = view.findViewById<TextView>(R.id.joke_punchline)
        val showComments = view.findViewById<TextView>(R.id.show_comments)


    }

}


