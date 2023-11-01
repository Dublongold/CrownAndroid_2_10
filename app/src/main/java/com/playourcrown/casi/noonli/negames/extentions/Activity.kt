package com.playourcrown.casi.noonli.negames.extentions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.playourcrown.casi.noonli.negames.views.activities.WebActivity

fun AppCompatActivity.openWebView() {
    startActivity(makeActivity(WebActivity::class.java))
    finish()
}

fun AppCompatActivity.makeActivity(clazz: Class<*>): Intent {
    return Intent(this, clazz)
}