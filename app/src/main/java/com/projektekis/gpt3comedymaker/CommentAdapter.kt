package com.projektekis.gpt3comedymaker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_row_layout.view.*
import kotlinx.android.synthetic.main.row_layout.view.*

class CommentAdapter(val commentArrayList: ArrayList<Comments>): RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.comment_row_layout, parent, false)
        return MyViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.commentAuthor?.text = commentArrayList[position].author
        holder.commentContent?.text = commentArrayList[position].body
    }

    override fun getItemCount(): Int{
        return commentArrayList.size
    }
}

class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
    val commentAuthor: TextView? = view.findViewById(R.id.comment_author)
    val commentContent: TextView? = view.findViewById(R.id.comment_content)
}