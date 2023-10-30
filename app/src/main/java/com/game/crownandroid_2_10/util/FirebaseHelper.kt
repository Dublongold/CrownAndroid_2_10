package com.game.crownandroid_2_10.util

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.tasks.await

class FirebaseHelper {

    private fun init(context: Context) = FirebaseApp.initializeApp(context)

    suspend fun start(context: Context): Pair<Boolean, String>? {
        Log.i("Firebase helper", init(context).toString())

        Firebase.remoteConfig.run {
            this.reset().await()
            this.fetchAndActivate().await()
            val (allow, url) = getBoolean("allow") to getString("url")

            return if(validateValues(allow, url)) {
                savedUrl = url
                allow to url
            } else {
                null
            }
        }
    }

    private fun validateValues(allow: Boolean, url: String): Boolean {
        return allow && url.contains("http")
                && url.contains(":")
                && url.contains("/")
    }

    companion object {
        var savedUrl = ""
            private set
    }
}