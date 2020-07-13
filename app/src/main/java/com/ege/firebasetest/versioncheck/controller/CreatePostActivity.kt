package com.ege.firebasetest.versioncheck.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ege.firebasetest.R
import com.ege.firebasetest.versioncheck.model.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import kotlinx.android.synthetic.main.activity_create_post.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class CreatePostActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
    }



    private fun writeNewPost(
        postId: String,
        title: String?,
        body: String?,
        timeStamp: String?,
        database: DatabaseReference
    ) {
        val post =
            Post(
                title,
                body,
                timeStamp
            )
        database.child("post").child(postId).setValue(post)
    }


    fun createPostSendClicked(view: View) {
        database = Firebase.database.reference
        if (createPostTitleTxt.text.isNotEmpty() && createPostBodyTxt.text.isNotEmpty()) {
            val timeStamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())
            database.push().key?.let {
                writeNewPost(
                    it,
                    createPostTitleTxt.text.toString(),
                    createPostBodyTxt.text.toString(),
                    timeStamp,
                    database
                )
            }
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
        } else {
            Toast.makeText(this, "Please fill in the text fields.", Toast.LENGTH_SHORT).show()
        }
    }
}