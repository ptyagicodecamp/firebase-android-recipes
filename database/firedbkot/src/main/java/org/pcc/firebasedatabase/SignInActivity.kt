package org.pcc.firebasedatabase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.pcc.firebasedatabase.models.User

/**
 * Created by ptyagi on 8/24/17.
 */

class SignInActivity : BaseActivity(),
                       View.OnClickListener {
    private val TAG = "SignInActivity"

    private var mDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    private var mEmailField: EditText? = null
    private var mPasswordField: EditText? = null
    private var mSignInButton: Button? = null
    private var mSignUpButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        FirebaseApp.initializeApp(this.applicationContext)

        mDatabase = FirebaseDatabase.getInstance()!!.reference
        mAuth = FirebaseAuth.getInstance()

        // Views
        mEmailField = findViewById<View>(R.id.field_email) as EditText
        mPasswordField = findViewById<View>(R.id.field_password) as EditText
        mSignInButton = findViewById<View>(R.id.button_sign_in) as Button
        mSignUpButton = findViewById<View>(R.id.button_sign_up) as Button

        // Click listeners
        mSignInButton!!.setOnClickListener(this)
        mSignUpButton!!.setOnClickListener(this)
    }

    public override fun onStart() {
        super.onStart()

        // Check auth on Activity start
        if (mAuth!!.getCurrentUser() != null) {
            onAuthSuccess(mAuth!!.getCurrentUser())
        }
    }

    private fun signIn() {
        Log.d(TAG, "signIn")
        if (!validateForm()) {
            return
        }

        showProgressDialog()
        val email = mEmailField!!.text.toString()
        val password = mPasswordField!!.text.toString()

        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful())
                        hideProgressDialog()

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser())
                        } else {
                            Toast.makeText(this@SignInActivity, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }

    private fun signUp() {
        Log.d(TAG, "signUp")
        if (!validateForm()) {
            return
        }

        showProgressDialog()
        val email = mEmailField!!.text.toString()
        val password = mPasswordField!!.text.toString()

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful())
                        hideProgressDialog()

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser())
                        } else {
                            Toast.makeText(this@SignInActivity, "Sign Up Failed: " + task.getException()!!,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }

    private fun onAuthSuccess(user: FirebaseUser?) {
        val username = usernameFromEmail(user!!.getEmail())

        // Write new user
        writeNewUser(user!!.getUid(), username, user!!.getEmail()!!)

        // Go to MainActivity
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

    private fun usernameFromEmail(email: String?): String {
        return if (email!!.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(mEmailField!!.text.toString())) {
            mEmailField!!.error = "Required"
            result = false
        } else {
            mEmailField!!.error = null
        }

        if (TextUtils.isEmpty(mPasswordField!!.text.toString())) {
            mPasswordField!!.error = "Required"
            result = false
        } else {
            mPasswordField!!.error = null
        }

        return result
    }

    // [START basic_write]
    private fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        mDatabase!!.child("users").child(userId).setValue(user)
    }
    // [END basic_write]

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.button_sign_in) {
            signIn()
        } else if (i == R.id.button_sign_up) {
            signUp()
        }
    }
}