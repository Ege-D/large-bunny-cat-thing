package com.ege.firebasetest.versioncheck.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ege.firebasetest.R
import kotlinx.android.synthetic.main.activity_post_detail.*
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PostDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")
        val date = intent.getLongExtra("date", 0)
        val image = intent.getStringExtra("image")
        setView(title, body, date, image)

    }
    private fun setView(title: String?, body: String?, date: Long, image: String?) {
        postDetailTitleTxt.text = title
        postDetailBodyTxt.text = body
        postDetailDateTxt.text = getDateTime(date)

        Glide.with(this)
            .load(image)
            .override(200, 200)
            .centerCrop()
            .into(postDetailImageView)
    }


    private fun getDateTime(timeStamp: Long?): String? {
        val date = LocalDateTime.ofInstant(timeStamp?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault())
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm")
        return date.format(formatter)
    }


}