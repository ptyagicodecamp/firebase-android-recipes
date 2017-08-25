package org.pcc.firebasedatabase.viewholder

/**
 * Created by ptyagi on 8/24/17.
 */
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import org.pcc.firebasedatabase.R
import org.pcc.firebasedatabase.models.Post

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var titleView: TextView
    var authorView: TextView
    var starView: ImageView
    var numStarsView: TextView
    var bodyView: TextView

    init {

        titleView = itemView.findViewById<View>(R.id.post_title) as TextView
        authorView = itemView.findViewById<View>(R.id.post_author) as TextView
        starView = itemView.findViewById<View>(R.id.star) as ImageView
        numStarsView = itemView.findViewById<View>(R.id.post_num_stars) as TextView
        bodyView = itemView.findViewById<View>(R.id.post_body) as TextView
    }

    fun bindToPost(post: Post, starClickListener: View.OnClickListener) {
        titleView.text = post.title
        authorView.text = post.author
        numStarsView.text = post.starCount.toString()
        bodyView.text = post.body

        starView.setOnClickListener(starClickListener)
    }
}
