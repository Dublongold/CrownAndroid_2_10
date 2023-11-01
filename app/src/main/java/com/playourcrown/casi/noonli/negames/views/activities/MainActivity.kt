package com.playourcrown.casi.noonli.negames.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import com.playourcrown.casi.noonli.negames.R
import com.playourcrown.casi.noonli.negames.extentions.FragmentDestination
import com.playourcrown.casi.noonli.negames.extentions.navigate
import com.playourcrown.casi.noonli.negames.views.fragments.LoadingFragment
import com.playourcrown.casi.noonli.negames.views.fragments.MainFragment

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