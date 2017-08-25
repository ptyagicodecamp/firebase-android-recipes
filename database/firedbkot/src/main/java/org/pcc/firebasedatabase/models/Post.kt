package org.pcc.firebasedatabase.models

/**
 * Created by ptyagi on 8/24/17.
 */

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

// [START post_class]
@IgnoreExtraProperties
class Post {

    lateinit var uid: String
    lateinit var author: String
    lateinit var title: String
    lateinit var body: String
    var starCount = 0
    var stars: MutableMap<String, Boolean> = HashMap()

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    constructor(uid: String, author: String, title: String, body: String) {
        this.uid = uid
        this.author = author
        this.title = title
        this.body = body
    }

    // [START post_to_map]
    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("uid", uid)
        result.put("author", author)
        result.put("title", title)
        result.put("body", body)
        result.put("starCount", starCount)
        result.put("stars", stars)

        return result
    }
    // [END post_to_map]

}
// [END post_class]