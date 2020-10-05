package com.projektekis.gpt3comedymaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedJokes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_jokes)

        val jokeIntent: ArrayList<String> = intent.getStringArrayListExtra("key")



        val savedJokesList: RecyclerView = findViewById(R.id.saved_jokes_list)
        savedJokesList.layoutManager = LinearLayoutManager(this)
        savedJokesList.adapter = adapter(jokeIntent)
    }

    class adapter(private val jokes: ArrayList<String>): RecyclerView.Adapter<adapter.MyViewHolder>(){
        class MyViewHolder(val textView: TextView):RecyclerView.ViewHolder(textView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout,parent,false) as TextView

            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = jokes[position]
        }

        override fun getItemCount() = jokes.size
    }
}