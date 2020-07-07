package com.ege.firebasetest.versioncheck

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ege.firebasetest.BuildConfig
import com.ege.firebasetest.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.android.material.snackbar.Snackbar

class LaunchActivity : AppCompatActivity() {
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 2
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("SUCCESS:", "Config params updated: $updated")
                    val version = BuildConfig.VERSION_NAME.toDouble()
                    val checkedVersion = remoteConfig.getDouble("latest_stable_version")
                    if(checkedVersion <= version) {
                        Log.d("VERSION:", "Version check OK")
                        val registerBoolean = remoteConfig.getBoolean("register")
                        val versionCheckIntent = Intent(this, HomeActivity::class.java)
                        versionCheckIntent.putExtra("register", registerBoolean)
                        startActivity(versionCheckIntent)
                    } else {
                        val versionSnackbar = Snackbar.make(findViewById(R.id.mainLayout), "Please update to the latest version.", Snackbar.LENGTH_INDEFINITE)
                        versionSnackbar.setAction("Go to Play Store", GooglePlayListener())
                        versionSnackbar.show()
                    }
                } else {
                    Toast.makeText(this, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }
    inner class GooglePlayListener : View.OnClickListener {
        override fun onClick (view: View) {
            val url = Uri.parse("https://play.google.com/store")
            val launch = Intent(Intent.ACTION_VIEW)
            launch.data = url
            startActivity(launch)
        }
    }




}