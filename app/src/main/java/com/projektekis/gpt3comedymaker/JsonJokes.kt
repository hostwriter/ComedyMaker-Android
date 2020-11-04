package com.projektekis.gpt3comedymaker

class JsonJokes(val theme: String, val likes: Int, val dislikes: Int, val comments: List<Comments>, val id: String, val title: String, val body: String, val author: String )
class Comments(val content: String, val author: String)
