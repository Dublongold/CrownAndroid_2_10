package com.game.crownandroid_2_10.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import com.game.crownandroid_2_10.R
import com.game.crownandroid_2_10.extentions.FragmentDestination
import com.game.crownandroid_2_10.extentions.navigate
import com.game.crownandroid_2_10.views.fragments.GameFragment
import com.game.crownandroid_2_10.views.fragments.LoadingFragment
import com.game.crownandroid_2_10.views.fragments.MainFragment
import com.game.crownandroid_2_10.views.fragments.PrivacyPolicyFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            )
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragments, LoadingFragment())
            .commit()
        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = supportFragmentManager.fragments.single()
                if(fragment is MainFragment) {
                    finish()
                }
                else if (fragment !is LoadingFragment) {
                    fragment.navigate(FragmentDestination.MAIN)
                }
            }
        })
    }
}