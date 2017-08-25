package org.pcc.firebasedatabase.fragment

/**
 * Created by ptyagi on 8/24/17.
 */

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.pcc.firebasedatabase.PostDetailActivity
import org.pcc.firebasedatabase.R
import org.pcc.firebasedatabase.models.Post
import org.pcc.firebasedatabase.viewholder.PostViewHolder


abstract class PostListFragment : Fragment() {

    // [START define_database_reference]
    private var mDatabase: DatabaseReference? = null
    // [END define_database_reference]

    private var mAdapter: FirebaseRecyclerAdapter<Post, PostViewHolder>? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_all_posts, container, false)

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().reference
        // [END create_database_reference]

        mRecycler = rootView.findViewById<View>(R.id.messages_list) as RecyclerView
        mRecycler!!.setHasFixedSize(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set up Layout Manager, reverse layout
        mManager = LinearLayoutManager(activity)
        mManager!!.reverseLayout = true
        mManager!!.stackFromEnd = true
        mRecycler!!.layoutManager = mManager

        // Set up FirebaseRecyclerAdapter with the Query
        val postsQuery = getQuery(mDatabase)
        mAdapter = object : FirebaseRecyclerAdapter<Post, PostViewHolder>(Post::class.java, R.layout.item_post,
                PostViewHolder::class.java, postsQuery) {
            override fun populateViewHolder(viewHolder: PostViewHolder, model: Post, position: Int) {
                val postRef = getRef(position)

                // Set click listener for the whole post view
                val postKey = postRef.key
                viewHolder.itemView.setOnClickListener(View.OnClickListener {
                    // Launch PostDetailActivity
                    val intent = Intent(activity, PostDetailActivity::class.java)
                    intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey)
                    startActivity(intent)
                })

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(uid)) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24)
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24)
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, View.OnClickListener {
                    // Need to write to both places the post is stored
                    val globalPostRef = mDatabase!!.child("posts").child(postRef.key)
                    val userPostRef = mDatabase!!.child("user-posts").child(model.uid).child(postRef.key)

                    // Run two transactions
                    onStarClicked(globalPostRef)
                    onStarClicked(userPostRef)
                })
            }
        }
        mRecycler!!.adapter = mAdapter
    }

    // [START post_stars_transaction]
    private fun onStarClicked(postRef: DatabaseReference) {
        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val p = mutableData.getValue(Post::class.java) ?: return Transaction.success(mutableData)

                if (p.stars.containsKey(uid)) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1
                    p.stars.remove(uid)
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1
                    p.stars.put(uid, true)
                }

                // Set value and report transaction success
                mutableData.setValue(p)
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError, b: Boolean,
                                    dataSnapshot: DataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError)
            }
        })
    }
    // [END post_stars_transaction]

    override fun onDestroy() {
        super.onDestroy()
        if (mAdapter != null) {
            mAdapter!!.cleanup()
        }
    }

    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    abstract fun getQuery(databaseReference: DatabaseReference?): Query

    companion object {

        private val TAG = "PostListFragment"
    }

}
