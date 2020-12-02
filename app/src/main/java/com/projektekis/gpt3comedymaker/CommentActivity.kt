package com.projektekis.gpt3comedymaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val jokesComment = intent.getParcelableArrayListExtra<Comments>("item")

        val commentRecyclerView: RecyclerView = findViewById(R.id.comment_recycler_view)

        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentRecyclerView.adapter = CommentAdapter(jokesComment)

    }
}