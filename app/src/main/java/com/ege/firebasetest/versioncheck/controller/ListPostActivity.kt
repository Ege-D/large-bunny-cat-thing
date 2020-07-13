package com.ege.firebasetest.versioncheck.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ege.firebasetest.R
import com.ege.firebasetest.versioncheck.adapters.PostAdapter
import com.ege.firebasetest.versioncheck.model.Post
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_post.*

class ListPostActivity : AppCompatActivity() {
    lateinit var postListener: ValueEventListener
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var postAdapter: PostAdapter
    lateinit var data: Post

    var posts = ArrayList<Post>()
    val postRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("post")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_post)
        linearLayoutManager = LinearLayoutManager(this)
        homePostList.layoutManager = linearLayoutManager
        postAdapter = PostAdapter(this, posts)
        homePostList.adapter = postAdapter


    }

    override fun onStart() {
        super.onStart()
        postListener = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("CANCEL: ", "ListPostActivity:onCancelled")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    data = childSnapshot.getValue(Post::class.java)!!
                    runOnUiThread {
                        posts.add(data)
                        postAdapter.notifyItemInserted(posts.size-1)
                    }

                }
            }

        }
        postRef.addValueEventListener(postListener)
    }


}