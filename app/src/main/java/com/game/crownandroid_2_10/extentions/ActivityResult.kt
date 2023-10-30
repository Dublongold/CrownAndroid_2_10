package com.game.crownandroid_2_10.extentions

import androidx.activity.result.ActivityResult

fun ActivityResult.validResultCode(): Boolean {
    return resultCode == -1
}