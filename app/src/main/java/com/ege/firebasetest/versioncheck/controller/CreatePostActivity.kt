package com.ege.firebasetest.versioncheck.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ege.firebasetest.R
import com.ege.firebasetest.versioncheck.model.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import kotlinx.android.synthetic.main.activity_create_post.*


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
        timeStamp: Long?,
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
        val timeStamp = (System.currentTimeMillis())
        if (createPostTitleTxt.text.isNotEmpty() && createPostBodyTxt.text.isNotEmpty()) {
            database.push().key?.let {
                writeNewPost(
                    it,
                    createPostTitleTxt.text.toString(),
                    createPostBodyTxt.text.toString(),
                    timeStamp,
                    database
                )
            }

        }
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }
}