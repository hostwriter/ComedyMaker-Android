package com.projektekis.gpt3comedymaker

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TalkToAPI(private val suffix: String) {
    var url = "https://64c05aad7l.execute-api.us-east-2.amazonaws.com/Prod"

    fun sendFeedback(feedbackList: ArrayList<String>, fullJSONList: List<JsonJokes>){
        val toSend = arrayListOf<String>()
        for(items in feedbackList){
            for(jsonJoke in fullJSONList){
                if(jsonJoke.title+ "\n\n" + jsonJoke.body == items){
                    toSend.add(jsonJoke.id)
                }
            }
        }

        val client = OkHttpClient()
        val requestBody = "".toRequestBody()
        for (id in toSend) {
            val request = Request.Builder()
                .put(requestBody)
                .url("$url/$id/$suffix")
                .build()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException){
                    println("callback failed")
                }

                override fun onResponse(call: Call, response: Response) {
                    println(response)
                }
            })
        }
    }
}