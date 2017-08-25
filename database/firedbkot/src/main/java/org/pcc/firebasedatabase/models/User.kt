package org.pcc.firebasedatabase.models

/**
 * Created by ptyagi on 8/24/17.
 */
import com.google.firebase.database.IgnoreExtraProperties

// [START blog_user_class]
@IgnoreExtraProperties
class User {

    lateinit var username: String
    lateinit var email: String

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(username: String, email: String) {
        this.username = username
        this.email = email
    }

}
// [END blog_user_class]