package org.pcc.firebasedatabase.fragment

/**
 * Created by ptyagi on 8/24/17.
 */
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query


class MyPostsFragment : PostListFragment() {
    override fun getQuery(databaseReference: DatabaseReference?): Query {
        // All my posts
        return databaseReference!!.child("user-posts")
                .child(uid)
    }
}
