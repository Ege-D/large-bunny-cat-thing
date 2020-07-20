package com.ege.firebasetest.versioncheck.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ege.firebasetest.R
import com.ege.firebasetest.versioncheck.controller.PostDetailActivity
import com.ege.firebasetest.versioncheck.model.Post
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.math.floor

class PostAdapter (val context: Context, val posts: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val title = itemView?.findViewById<TextView>(R.id.postListTitleTxt)
        val dateAgo = itemView?.findViewById<TextView>(R.id.postListDateAgoTxt)
        val image = itemView?.findViewById<ImageView>(R.id.postListImageView)

        fun bindPost(context: Context, post: Post) {
            title?.text = post.title
            dateAgo?.text = getDateAgo(post.timeStamp)
            if (image != null) {
                Glide.with(context)
                    .load(post.URL)
                    .override(200, 200)
                    .centerCrop()
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindPost(context, posts[position])
        holder.itemView.setOnClickListener {
            val postDetailIntent = Intent(context, PostDetailActivity::class.java)
            postDetailIntent.putExtra("title", posts[position].title)
            postDetailIntent.putExtra("body", posts[position].body)
            postDetailIntent.putExtra("date", posts[position].timeStamp)
            postDetailIntent.putExtra("image", posts[position].URL)
            context.startActivity(postDetailIntent)
        }
    }


    fun getDateAgo(timeStamp: Long?): String? {
        val dateNow = System.currentTimeMillis()
        val dateAgo = (dateNow - timeStamp!!)/1000
        var stringAgo : String? = ""
        when {
            dateAgo < 60 -> {
                stringAgo = "${dateAgo.toString()} seconds ago"
            }
            dateAgo < 3600 -> {
                stringAgo = "${floor(((dateAgo/60).toDouble())).toString().dropLast(2)} minutes ago"
            }
            dateAgo < 86400 -> {
                stringAgo = "${floor(((dateAgo/3600).toDouble())).toString().dropLast(2)} hours ago"
            }
            dateAgo < 604800 -> {
                stringAgo = "${floor(((dateAgo/86400).toDouble())).toString().dropLast(2)} days ago"
            }
            dateAgo < 2629743.83 -> {
                stringAgo = "${floor(((dateAgo/604800).toDouble())).toString().dropLast(2)} weeks ago"
            }
            dateAgo < 31556926 -> {
                stringAgo = "${floor(((dateAgo/2629743.83).toDouble())).toString().dropLast(2)} months ago"
            }
            else -> {
                stringAgo = "${floor(((dateAgo/31556926).toDouble())).toString().dropLast(2)} years ago"
            }
        }
        return stringAgo
    }
}
