package com.playourcrown.casi.noonli.negames.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.playourcrown.casi.noonli.negames.R
import com.playourcrown.casi.noonli.negames.extentions.FragmentDestination
import com.playourcrown.casi.noonli.negames.extentions.navigate
import com.playourcrown.casi.noonli.negames.util.FragmentViewBuilder
import com.playourcrown.casi.noonli.negames.views.custom.PrivacyPolicyText

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