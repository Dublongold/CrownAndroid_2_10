package com.game.crownandroid_2_10.views.fragments

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.crownandroid_2_10.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameFragmentLogic: ViewModel() {
    private val totalWin = MutableLiveData(0)
    private val balance = MutableLiveData(0)
    private val bet = MutableLiveData(10)

    private val currentElements = mutableListOf(
        0, 1, 2,   3, 4, 5,   6, 7, 0,   1, 2, 3
    )

    fun getGameValue(gameValue: GameValue): LiveData<Int> {
        return privateGetGameValue(gameValue)
    }

    private fun privateGetGameValue(gameValue: GameValue): MutableLiveData<Int> {
        return when(gameValue) {
            GameValue.TOTAL_WIN -> totalWin
            GameValue.BALANCE -> balance
            GameValue.BET -> bet
        }
    }

    fun setBalance(value: Int) {
        if(value >= 0) {
            balance.value = value
        }
        else {
            balance.value = 0
        }
    }

    fun plus() {
        bet.value = bet.value?.let {
            it + 10
        }
    }

    fun minus() {
        if(bet.value?.let {it > 10} == true) {
            bet.value = bet.value?.let {
                it - 10
            }
        }
    }

    private fun changeSlotElements(slotElements: List<ImageView>, columns: Int) {
        slotElements.withIndex().reversed().forEach {
            val index = it.index
            val imageView = it.value
            if(columns * 3 > 0 && (columns * 3) - 12 + index + 1 > 0) {
                if(index % 3 == 0) {
                    currentElements[index] = Random.nextInt(0, 8)
                    imageView.setImageResource(R.drawable.img_element01 + currentElements[index])
                }
                else {
                    currentElements[index] = currentElements[index - 1]
                    imageView.setImageResource(R.drawable.img_element01 + currentElements[index])
                }
            }
        }
    }

    suspend fun doSpin(slotElements: List<ImageView>): Boolean {
        val betValue = bet.value ?: 0
        totalWin.value = 0
        if(balance.value?.let { it < betValue } == true) {
            return false
        }

        balance.value = balance.value?.let {
            it - betValue
        }
        repeat(23) {
            changeSlotElements(slotElements, 4)
            delay(100)
        }
        repeat(4) {
            changeSlotElements(slotElements, 3)
            delay(150)
        }
        repeat(4) {
            changeSlotElements(slotElements, 2)
            delay(200)
        }
        repeat(4) {
            changeSlotElements(slotElements, 1)
            delay(250)
        }

        val columns = GameColumns(listOf(
            GameColumn(currentElements.take(3)),
            GameColumn(currentElements.drop(3).take(3)),
            GameColumn(currentElements.drop(6).take(3)),
            GameColumn(currentElements.drop(9)),
        ))

        for(i in 0..7) {
            if(columns.containsElements(i)) {
                totalWin.value = totalWin.value?.let {
                    it + (betValue * (i + 1) * columns.getMultiplier(i)).toInt()
                }
            }
        }

        balance.value = balance.value?.let {
            totalWin.value?.plus(it)
        }

        return true
    }
}

enum class GameValue {
    TOTAL_WIN,
    BALANCE,
    BET
}

class GameColumn(
    private val slotElements: List<Int>
) {
    fun containsElements(index: Int): Boolean {
        return slotElements.contains(index)
    }

    fun getMultiplier(index: Int): Double {
        return slotElements.count { it == index } * 0.5 + 0.5
    }
}

class GameColumns(
    private val columns: List<GameColumn>
) {
    fun containsElements(index: Int): Boolean {
        return columns.all { it.containsElements(index) }
    }

    fun getMultiplier(index: Int): Double {
        var multiplier = 0.0
        for(column in columns) {
            multiplier += column.getMultiplier(index)
        }

        return multiplier / columns.size
    }
}