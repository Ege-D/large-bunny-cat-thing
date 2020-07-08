package com.ege.firebasetest.versioncheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ege.firebasetest.R
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

    data class Post(
        var title: String? = "",
        var body: String? = ""
    )

    private fun writeNewPost(
        postId: String,
        title: String?,
        body: String?,
        database: DatabaseReference
    ) {
        val post = Post(title, body)
        database.child("post").child(postId).setValue(post)
    }

    fun createPostSendClicked(view: View) {
        database = Firebase.database.reference
        if (createPostTitleTxt.text.isNotEmpty() && createPostBodyTxt.text.isNotEmpty()) {
            database.push().key?.let {
                writeNewPost(
                    it,
                    createPostTitleTxt.text.toString(),
                    createPostBodyTxt.text.toString(),
                    database
                )
            }

        }
    }
}