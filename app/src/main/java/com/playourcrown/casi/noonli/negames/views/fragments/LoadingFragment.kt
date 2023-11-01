package com.playourcrown.casi.noonli.negames.views.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.playourcrown.casi.noonli.negames.R
import com.playourcrown.casi.noonli.negames.extentions.FragmentDestination
import com.playourcrown.casi.noonli.negames.extentions.navigate
import com.playourcrown.casi.noonli.negames.extentions.openWebView
import com.playourcrown.casi.noonli.negames.util.FirebaseHelper
import com.playourcrown.casi.noonli.negames.util.FragmentViewBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingFragment: Fragment() {

    private val firebaseHelper = FirebaseHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentViewBuilder()
            .inflater(inflater)
            .resourceId(R.layout.fragment_loading)
            .container(container)
            .attachToRoot(false)
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingIcon = view.findViewById<ImageView>(R.id.loadingIcon)
        viewLifecycleOwner.lifecycleScope.launch {
            while(viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                loadingIcon.rotation += 2
                if(loadingIcon.rotation > 360) {
                    loadingIcon.rotation -= 360
                }
                delay(5)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            delay(250)
            val isConfirmed = requireActivity()
                .getSharedPreferences("application_data", AppCompatActivity.MODE_PRIVATE)
                .getBoolean("confirmed", false)
            if(!isConfirmed) {
                AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.alert_message))
                    .setPositiveButton(R.string.allow, OnDialogDismiss())
                    .setNegativeButton(R.string.deny, OnDialogDismiss())
                    .show()
                requireActivity()
                    .getSharedPreferences("application_data", AppCompatActivity.MODE_PRIVATE)
                    .edit {
                        putBoolean("confirmed", true)
                    }
            }
            else {
                doAfterConfirm()
            }
        }
    }
    fun doAfterConfirm() {
        viewLifecycleOwner.lifecycleScope.launch {
            val value = firebaseHelper.start(requireContext())
            if(value != null) {
                Log.i("Loading fragment", "Yes. ${value.first}, ${value.second}.")
                (activity as? AppCompatActivity)?.openWebView()
            }
            else {
                navigate(FragmentDestination.MAIN)
            }
        }
    }

    inner class OnDialogDismiss: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            dialog?.dismiss()
            doAfterConfirm()
        }
    }
}