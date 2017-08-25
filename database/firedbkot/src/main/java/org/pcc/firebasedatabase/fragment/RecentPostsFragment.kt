package org.pcc.firebasedatabase.fragment

/**
 * Created by ptyagi on 8/24/17.
 */
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query


class RecentPostsFragment : PostListFragment() {

    override fun getQuery(databaseReference: DatabaseReference?): Query {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        // [END recent_posts_query]

        return databaseReference!!.child("posts")
                .limitToFirst(100)
    }
}