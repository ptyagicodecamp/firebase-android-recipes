package org.pcc.firebasedatabase.models

/**
 * Created by ptyagi on 8/24/17.
 */
import com.google.firebase.database.IgnoreExtraProperties

// [START comment_class]
@IgnoreExtraProperties
class Comment {

    lateinit var uid: String
    lateinit var author: String
    lateinit var text: String

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    constructor(uid: String, author: String, text: String) {
        this.uid = uid
        this.author = author
        this.text = text
    }

}
// [END comment_class]
