package com.playourcrown.casi.noonli.negames.views.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.playourcrown.casi.noonli.negames.R
import com.playourcrown.casi.noonli.negames.extentions.FragmentDestination
import com.playourcrown.casi.noonli.negames.extentions.navigate
import com.playourcrown.casi.noonli.negames.util.FragmentViewBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment: Fragment() {

    private val fragmentLogic: GameFragmentLogic by viewModels()

    private var balance: Int
        get() = getSharedPreferences().getInt("balance", 10_000)
        set(value) {
            getSharedPreferences().edit { putInt("balance", value) }
        }

    private fun getSharedPreferences(): SharedPreferences {
        return requireActivity()
            .getSharedPreferences("application_data", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentViewBuilder()
            .inflater(inflater)
            .resourceId(R.layout.fragment_game)
            .container(container)
            .attachToRoot(false)
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentLogic.setBalance(balance)

        val balanceInfo: TextView
        val totalWinInfo: TextView
        val betInfo: TextView

        val balanceText = getString(R.string.balance)
        val totalWinText = getString(R.string.total_win)
        val betText = getString(R.string.bet)

        val slotElementTag = getString(R.string.slot_element)

        val slotElements = view.allViews.filter {
            it is ImageView && it.tag == slotElementTag
        }.map {
            it as ImageView
        }.toList()

        view.run {
            balanceInfo = findViewById(R.id.balanceInfo)
            totalWinInfo = findViewById(R.id.totalWinInfo)
            betInfo = findViewById(R.id.betInfo)
            setBackButton()
            findViewById<ImageButton>(R.id.plusButton).let {
                it.setOnClickListener {
                    fragmentLogic.plus()
                }
                it.setOnLongClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        var times = 1
                        launch {
                            delay(1000)
                            times = 2
                        }
                        while (it.isPressed) {
                            repeat(times) {
                                fragmentLogic.plus()
                            }
                            delay(100)
                        }
                    }
                    true
                }
            }
            findViewById<ImageButton>(R.id.minusButton).let {
                it.setOnClickListener {
                    fragmentLogic.minus()
                }

                it.setOnLongClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        var times = 1
                        launch {
                            delay(1000)
                            times = 2
                        }
                        while (it.isPressed) {
                            repeat(times) {
                                fragmentLogic.minus()
                            }
                            delay(100)
                        }
                    }
                    true
                }
            }
            findViewById<AppCompatButton>(R.id.spinButton).setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    it.isEnabled = false

                    if(!fragmentLogic.doSpin(slotElements)) {
                        if(fragmentLogic.getGameValue(GameValue.BALANCE).value?.let { it < 100}
                            == true) {
                            AlertDialog.Builder(requireContext())
                                .setTitle(R.string.balance_to_low)
                                .setPositiveButton(R.string.yes) { dialog, _ ->
                                    balance = 10000
                                    fragmentLogic.setBalance(10000)
                                    dialog.dismiss()
                                }
                                .setNegativeButton(R.string.no) { dialog, _ ->
                                    dialog.dismiss()
                                }.show()
                        }
                        else {
                            Toast
                                .makeText(context, "Place your bet below.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else {
                        fragmentLogic.getGameValue(GameValue.BALANCE).value?.let {
                            balance = it
                        }
                    }
                    it.isEnabled = true
                }
            }
        }
        fragmentLogic.getGameValue(GameValue.BALANCE).observe(viewLifecycleOwner) {
            balanceInfo.text = balanceText.format(it)
        }
        fragmentLogic.getGameValue(GameValue.BET).observe(viewLifecycleOwner) {
            betInfo.text = betText.format(it)
        }
        fragmentLogic.getGameValue(GameValue.TOTAL_WIN).observe(viewLifecycleOwner) {
            totalWinInfo.text = totalWinText.format(it)
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