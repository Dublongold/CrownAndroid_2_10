package com.playourcrown.casi.noonli.negames.extentions

import androidx.fragment.app.Fragment
import com.playourcrown.casi.noonli.negames.R
import com.playourcrown.casi.noonli.negames.views.fragments.GameFragment
import com.playourcrown.casi.noonli.negames.views.fragments.MainFragment
import com.playourcrown.casi.noonli.negames.views.fragments.PrivacyPolicyFragment

fun Fragment.navigate(destination: FragmentDestination) {
    when(destination) {
        FragmentDestination.MAIN -> goToMain()
        FragmentDestination.GAME -> goToGame()
        FragmentDestination.PRIVACY_POLICY -> goToPrivacyPolicy()
    }
}

fun Fragment.goToMain() {
    parentFragmentManager.beginTransaction()
        .remove(parentFragmentManager.fragments.first())
        .add(R.id.fragments, MainFragment())
        .commit()
}

fun Fragment.goToGame() {
    parentFragmentManager.beginTransaction()
        .remove(parentFragmentManager.fragments.first())
        .add(R.id.fragments, GameFragment())
        .commit()
}

fun Fragment.goToPrivacyPolicy() {
    parentFragmentManager.beginTransaction()
        .remove(parentFragmentManager.fragments.first())
        .add(R.id.fragments, PrivacyPolicyFragment())
        .commit()
}

enum class FragmentDestination {
    MAIN,
    GAME,
    PRIVACY_POLICY
}