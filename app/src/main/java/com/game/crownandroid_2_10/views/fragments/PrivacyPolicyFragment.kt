package com.game.crownandroid_2_10.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.game.crownandroid_2_10.R
import com.game.crownandroid_2_10.extentions.FragmentDestination
import com.game.crownandroid_2_10.extentions.navigate
import com.game.crownandroid_2_10.util.FragmentViewBuilder
import com.game.crownandroid_2_10.views.custom.PrivacyPolicyText

class PrivacyPolicyFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentViewBuilder()
            .inflater(inflater)
            .resourceId(R.layout.fragment_privacy_policy)
            .container(container)
            .attachToRoot(false)
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.run {
            findViewById<ComposeView>(R.id.privacy_policy_text).run {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    PrivacyPolicyText()
                }
            }
            setBackButton()
        }
    }

    private fun setBackButton() {
        val view = view
        if(view != null) {
            val button = view.findViewById<ImageButton?>(R.id.backButton)
            if(button != null) {
                button.setOnClickListener {
                    navigate(FragmentDestination.MAIN)
                }
            }
            else {
                throw IllegalStateException("Where a button?")
            }
        }
        else {
            throw IllegalStateException("Why do you invoke this function when view is null?")
        }
    }
}