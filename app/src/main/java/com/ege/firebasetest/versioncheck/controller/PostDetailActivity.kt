package com.ege.firebasetest.versioncheck.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ege.firebasetest.R
import kotlinx.android.synthetic.main.activity_post_detail.*
import com.bumptech.glide.Glide

class PostDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")
        val date = intent.getStringExtra("date")
        val image = intent.getStringExtra("image")
        postDetailTitleTxt.text = title
        postDetailBodyTxt.text = body
        postDetailDateTxt.text = date
        Glide.with(this)
            .load(image)
            .override(200, 200)
            .centerCrop()
            .into(postDetailImageView)
    }
}