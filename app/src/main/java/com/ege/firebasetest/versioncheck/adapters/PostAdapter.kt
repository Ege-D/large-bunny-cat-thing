package com.ege.firebasetest.versioncheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ege.firebasetest.R
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
        val body = itemView?.findViewById<TextView>(R.id.postListBodyTxt)
        val date = itemView?.findViewById<TextView>(R.id.postListDateTxt)
        val dateAgo = itemView?.findViewById<TextView>(R.id.postListDateAgoTxt)

        fun bindPost(context: Context, post: Post) {
            title?.text = post.title
            body?.text = post.body
            date?.text = getDateTime(post.timeStamp)
            dateAgo?.text = getDateAgo(post.timeStamp)
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
    }

    fun getDateTime(timeStamp: Long?): String? {
        val date = LocalDateTime.ofInstant(timeStamp?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault())
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.format(formatter)
    }

    fun getDateAgo(timeStamp: Long?): String? {
        val dateNow = System.currentTimeMillis()
        val dateAgo = (dateNow - timeStamp!!)/1000
        var stringAgo : String? = ""
        if (dateAgo < 60) {
            stringAgo = "${dateAgo.toString()} seconds ago"
        } else if (dateAgo < 3600) {
            stringAgo = "${floor(((dateAgo/60).toDouble())).toString().dropLast(2)} minutes ago"
        } else if (dateAgo < 86400) {
            stringAgo = "${floor(((dateAgo/3600).toDouble())).toString().dropLast(2)} hours ago"
        } else if (dateAgo < 604800) {
            stringAgo = "${floor(((dateAgo/86400).toDouble())).toString().dropLast(2)} days ago"
        } else if (dateAgo < 2629743.83) {
            stringAgo = "${floor(((dateAgo/604800).toDouble())).toString().dropLast(2)} weeks ago"
        } else if (dateAgo < 31556926) {
            stringAgo = "${floor(((dateAgo/2629743.83).toDouble())).toString().dropLast(2)} months ago"
        } else {
            stringAgo = "${floor(((dateAgo/31556926).toDouble())).toString().dropLast(2)} years ago"
        }
        return stringAgo
    }
}
