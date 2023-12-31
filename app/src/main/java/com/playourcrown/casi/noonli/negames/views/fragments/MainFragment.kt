package com.playourcrown.casi.noonli.negames.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.playourcrown.casi.noonli.negames.R
import com.playourcrown.casi.noonli.negames.extentions.FragmentDestination
import com.playourcrown.casi.noonli.negames.extentions.navigate
import com.playourcrown.casi.noonli.negames.util.FragmentViewBuilder

class MainFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanseState: Bundle?
    ): View? {
        return FragmentViewBuilder()
            .inflater(inflater)
            .resourceId(R.layout.fragment_main)
            .container(container)
            .attachToRoot(false)
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPrivacyPolicyButton()
        setPlayButton()
    }

    private fun setPrivacyPolicyButton() {
        val view = view
        if(view != null) {
            val button = view.findViewById<ImageButton?>(R.id.privacyPolicyButton)
            if(button != null) {
                button.setOnClickListener {
                    navigate(FragmentDestination.PRIVACY_POLICY)
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

    private fun setPlayButton() {
        val view = view
            ?: throw IllegalStateException("Why do you invoke this function when view is null?")
        val button = view.findViewById<ImageButton?>(R.id.playButton)
            ?: throw IllegalStateException("Where a button?")
        button.setOnClickListener {
            navigate(FragmentDestination.GAME)
        }
    }
}