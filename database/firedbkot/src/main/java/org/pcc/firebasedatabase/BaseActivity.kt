package org.pcc.firebasedatabase

/**
 * Created by ptyagi on 8/24/17.
 */

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


open class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: ProgressDialog? = null

//    override fun onCreate(savedInstance : Bundle?) {
//        super.onCreate(savedInstance)
//        FirebaseApp.initializeApp(this.application)
//    }

    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.setMessage("Loading...")
        }

        mProgressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid


}