package com.playourcrown.casi.noonli.negames.extentions

import androidx.activity.result.ActivityResult

fun ActivityResult.validResultCode(): Boolean {
    return resultCode == -1
}