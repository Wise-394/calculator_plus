package com.wise.calculatorplus.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Calculator: ViewModel() {
    var ans = MutableLiveData<Float>()
    var num1 = MutableLiveData<String>()
    var num2 = MutableLiveData<String>()
    init {
        ans.value = 0f
        num1.value = ""
        num2.value = ""
    }

    // Operations
    fun add() {
        ans.value = num1.value!!.toFloat() + num2.value!!.toFloat()
    }

    fun subtract() {
        ans.value = num1.value!!.toFloat() - num2.value!!.toFloat()
    }

    fun multiply() {
        ans.value = num1.value!!.toFloat() * num2.value!!.toFloat()
    }

    fun divide() {
        ans.value = num1.value!!.toFloat() / num2.value!!.toFloat()
    }

}
