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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import kotlinx.android.synthetic.main.activity_home.*



class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val registerBoolean = intent.getBooleanExtra("register", true)
        homeRegisterBtn.isClickable = registerBoolean
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso);
        homeLoginBtn.setOnClickListener(GoogleSignInListener(googleSignInClient))
        auth = Firebase.auth
        homeCreatePostBtn.visibility = View.INVISIBLE

    }

    fun createPostClicked(view: View) {
        val createPostIntent = Intent(this, CreatePostActivity::class.java)
        startActivity(createPostIntent)
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            textView.text = currentUser.displayName.toString()
            homeLoginBtn.visibility = View.INVISIBLE
            homeRegisterBtn.visibility = View.INVISIBLE
            homeCreatePostBtn.visibility = View.VISIBLE
        }
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
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("SCS:", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERR:", "Google sign in failed", e)
                // ...
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SCS", "signInWithCredential:success")
                    val user = auth.currentUser
                    if (user != null) {
                        textView.text = user.displayName.toString()
                        homeLoginBtn.visibility = View.INVISIBLE
                        homeRegisterBtn.visibility = View.INVISIBLE
                        homeCreatePostBtn.visibility = View.VISIBLE
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ERR:", "signInWithCredential:failure", task.exception)
                    // ...
                    Snackbar.make(homeLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    textView.text = "Hello World"
                    homeLoginBtn.visibility = View.VISIBLE
                    homeRegisterBtn.visibility = View.VISIBLE
                    homeCreatePostBtn.visibility = View.INVISIBLE
                }

                // ...
            }
    }

   /* private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
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
    }*/
}
