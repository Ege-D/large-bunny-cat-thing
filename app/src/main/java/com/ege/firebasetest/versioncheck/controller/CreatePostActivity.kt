package com.ege.firebasetest.versioncheck.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ege.firebasetest.R
import com.ege.firebasetest.versioncheck.model.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.ByteArrayOutputStream


class CreatePostActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var file : Uri
    private lateinit var URL : Uri

    private val REQUEST_CODE = 100
    val storageRef = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

    }


    private fun writeNewPost(
        postId: String,
        title: String?,
        body: String?,
        timeStamp: Long?,
        URL: String?,
        database: DatabaseReference
    ) {
        val post =
            Post(
                title,
                body,
                timeStamp,
                URL
            )
        database.child("post").child(postId).setValue(post)
    }

    fun createPostSendClicked(view: View) {
        database = Firebase.database.reference
        val timeStamp = System.currentTimeMillis()
        val imageRef = storageRef.child("images/${file.lastPathSegment}")
        val uploadTask = imageRef.putFile(file)




        uploadTask.addOnFailureListener {
            Log.w("ERR: ", "Upload task error.", it.cause)
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                URL = uri
                if (createPostTitleTxt.text.isNotEmpty() && createPostBodyTxt.text.isNotEmpty()) {
                    database.push().key?.let {
                        writeNewPost(
                            it,
                            createPostTitleTxt.text.toString(),
                            createPostBodyTxt.text.toString(),
                            timeStamp,
                            URL.toString(),
                            database
                        )
                    }

                }
            }.addOnFailureListener {
                Log.w("ERR: ", "Couldn't get URL.", it.cause)
            }
      }



        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

    fun createPostAddBtnClicked(view: View) {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            file = data?.data!!
            createPostImageView.setImageURI(data?.data)
        }
    }

}