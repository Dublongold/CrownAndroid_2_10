package com.game.crownandroid_2_10.extentions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.game.crownandroid_2_10.views.activities.WebActivity

fun AppCompatActivity.openWebView() {
    startActivity(makeActivity(WebActivity::class.java))
    finish()
}

fun AppCompatActivity.makeActivity(clazz: Class<*>): Intent {
    return Intent(this, clazz)
}