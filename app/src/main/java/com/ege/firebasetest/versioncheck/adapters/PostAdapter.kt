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

class PostAdapter (val context: Context, val posts: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val title = itemView?.findViewById<TextView>(R.id.postListTitleTxt)
        val body = itemView?.findViewById<TextView>(R.id.postListBodyTxt)
        val date = itemView?.findViewById<TextView>(R.id.postListDateTxt)

        fun bindPost(context: Context, post: Post) {
            title?.text = post.title
            body?.text = post.body
            date?.text = getDateTime(post.timeStamp)
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
}
