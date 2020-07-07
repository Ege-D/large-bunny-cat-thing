package com.ege.firebasetest.versioncheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ege.firebasetest.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_home.*


// ID: 936002256726-6a303763b968mcrera403vvppdhekq5l.apps.googleusercontent.com
// Secret: 60cchU6aBbjgZ9ip0fFdi0BA
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val registerBoolean = intent.getBooleanExtra("register", true)
        homeRegisterBtn.isClickable = registerBoolean
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso);
        homeLoginBtn.setOnClickListener(GoogleSignInListener(googleSignInClient))

    }

    inner class GoogleSignInListener(private val googleSignInClient : GoogleSignInClient) : View.OnClickListener {
        override fun onClick (view: View) {
            when (view.id) {
                R.id.homeLoginBtn -> signIn()
            }

        }
        private fun signIn() {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            //updateUI(account)
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName = acct.displayName
                textView.text = personName.toString()
            }


        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERR:", "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
            textView.text = "Hello World"
        }
    }
}
