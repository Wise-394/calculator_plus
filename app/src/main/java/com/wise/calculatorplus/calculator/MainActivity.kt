package com.wise.calculatorplus.calculator

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wise.calculatorplus.notes.Notes_MainActivity
import com.wise.calculatorplus.R
import com.wise.calculatorplus.dialogs.Dialogs

//welcome this is my first android project so expect some spaghetti code tho i did
// my best to make this code readable as possible
class MainActivity : AppCompatActivity(), View.OnClickListener {
    //late init
    private lateinit var tvAns: TextView
    private lateinit var tvNum1: TextView
    private lateinit var tvNum2: TextView
    private lateinit var tvPrev: TextView
    private lateinit var calculator: Calculator
    private lateinit var tvSign: TextView
    private var state1 = true
    private lateinit var sf: SharedPreferences
    private lateinit var sfEditor: SharedPreferences.Editor
    private lateinit var spPass: String
    private lateinit var spAnswer: String
    private lateinit var spQuestion: String
    private lateinit var fabInfo: FloatingActionButton
    private var forgotPassCounter: Int = 0
    private var defaultValue: String = ""
    private val dialogs = Dialogs(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load db and init ui
        loadSP()
        initUI()
        firstTimeLogin()

        fabInfo.setOnClickListener {
            dialogPassRecovery()
        }

        //observe
        calculator.ans.observe(this) {
            val formattedString = if (it % 1 == 0f) {
                String.format("%.0f", it)
            } else {
                it.toString()
            }
            tvAns.text = formattedString
        }
        calculator.num1.observe(this) {
            tvNum1.text = it.toString()
        }
        calculator.num2.observe(this) {
            tvNum2.text = it.toString()
        }
        //sign
        tvSign = findViewById(R.id.tvSign)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn0 -> {
                    onClickNumButton("0")
                }

                R.id.btn1 -> {
                    onClickNumButton("1")
                }

                R.id.btn2 -> {
                    onClickNumButton("2")
                }

                R.id.btn3 -> {
                    onClickNumButton("3")
                }

                R.id.btn4 -> {
                    onClickNumButton("4")
                }

                R.id.btn5 -> {
                    onClickNumButton("5")
                }

                R.id.btn6 -> {
                    onClickNumButton("6")
                }

                R.id.btn7 -> {
                    onClickNumButton("7")
                }

                R.id.btn8 -> {
                    onClickNumButton("8")
                }

                R.id.btn9 -> {
                    onClickNumButton("9")
                }
                //operations
                R.id.btnPlus -> {
                    solve("+")
                }

                R.id.btnMinus -> {
                    solve("-")
                }

                R.id.btnMultiply -> {
                    solve("*")
                }

                R.id.btnDivide -> {
                    solve("/")
                }

                R.id.btnAns -> {
                    if (calculator.ans.value != 0f) {
                        if (state1) {
                            calculator.num1.value =
                                "${calculator.num1.value}${calculator.ans.value}"
                        } else {
                            calculator.num2.value =
                                "${calculator.num2.value}${calculator.ans.value}"
                        }
                    }
                }

                R.id.btnAllClear -> {
                    clearAll()
                }

                R.id.btnEqual -> {
                    if (state1 && !calculator.num1.value.isNullOrBlank() && tvSign.text.isNullOrBlank()) {
                        goToMainScr(spPass, calculator.num1.value!!)
                    } else if (!calculator.num1.value.isNullOrEmpty() && !calculator.num2.value.isNullOrEmpty()) {
                        when (tvSign.text) {
                            "+" -> calculator.add()
                            "-" -> calculator.subtract()
                            "*" -> calculator.multiply()
                            "/" -> calculator.divide()
                        }
                        showResultAndReset(true, "")
                    }
                }

                R.id.btnClear -> {
                    if (state1) {
                        if (!calculator.num1.value.isNullOrEmpty()) {
                            val newValue = calculator.num1.value!!.substring(
                                0,
                                calculator.num1.value!!.length - 1
                            )
                            val updatedValue =
                                if (newValue.endsWith(".") || newValue.endsWith("-")) newValue.dropLast(
                                    1
                                ) else newValue
                            calculator.num1.value = updatedValue
                        }
                    } else {
                        if (!calculator.num2.value.isNullOrEmpty()) {
                            val newValue = calculator.num2.value!!.substring(
                                0,
                                calculator.num2.value!!.length - 1
                            )
                            val updatedValue =
                                if (newValue.endsWith(".") || newValue.endsWith("-")) newValue.dropLast(
                                    1
                                ) else newValue
                            calculator.num2.value = updatedValue
                        } else if (!tvSign.text.isNullOrBlank()) {
                            tvSign.text = ""
                            state1 = true
                        }
                    }
                }

                R.id.btnNegativeOrPositive -> {
                    if (state1) {
                        if ((calculator.num1.value != "0") && !calculator.num1.value.isNullOrBlank()) {
                            val num1 = calculator.num1.value!!.toFloat()
                            calculator.num1.value = (num1 * -1).toString()
                        }
                    } else {
                        if ((calculator.num2.value != "0") && !calculator.num2.value.isNullOrBlank()) {
                            val num2 = calculator.num2.value!!.toFloat()
                            calculator.num2.value = (num2 * -1).toString()
                        }
                    }
                }
            }
        }
    }

    private fun clearAll() {
        tvAns.visibility = View.INVISIBLE
        calculator.ans.value = 0f
        calculator.num1.value = ""
        calculator.num2.value = ""
        tvSign.text = ""
        state1 = true
        tvPrev.text = ""
    }

    private fun showResultAndReset(stateFirst: Boolean, sign: String) {
        if (stateFirst) {
            tvPrev.text = "${calculator.num1.value} ${tvSign.text} ${calculator.num2.value}"
            tvAns.visibility = View.VISIBLE
            calculator.num1.value = ""
            calculator.num2.value = ""
            tvSign.text = sign
            state1 = true
        } else {
            tvPrev.text = "${calculator.num1.value} ${tvSign.text} ${calculator.num2.value}"
            tvAns.visibility = View.VISIBLE
            calculator.num1.value = calculator.ans.value.toString()
            calculator.num2.value = ""
            tvSign.text = sign
            state1 = false
        }
    }

    private fun solve(sign: String) {
        if (state1 && !calculator.num1.value.isNullOrBlank()) {
            state1 = false
            tvSign.text = sign
        } else {
            if (tvSign.text.isNullOrBlank() && !state1) {
                tvSign.text = sign
            } else if (!calculator.num1.value.isNullOrBlank() && !calculator.num2.value.isNullOrBlank()) {
                when (tvSign.text) {
                    "+" -> {
                        calculator.add()
                        showResultAndReset(false, "+")
                    }

                    "-" -> {
                        calculator.subtract()
                        showResultAndReset(false, "-")
                    }

                    "*" -> {
                        calculator.multiply()
                        showResultAndReset(false, "*")
                    }

                    "/" -> {
                        calculator.multiply()
                        showResultAndReset(false, "/")
                    }
                }
            }
        }
    }

    private fun onClickNumButton(input: String) {
        if (state1) {
            if ((calculator.num1.value?.length ?: 0) <= 13) {
                calculator.num1.value = "${calculator.num1.value}$input"
            } else {
                showMaxDigitToast()
            }
        } else {
            if ((calculator.num2.value?.length ?: 0) <= 13) {
                calculator.num2.value = "${calculator.num2.value}$input"
            } else {
                showMaxDigitToast()
            }
        }
    }

    private fun showMaxDigitToast() {
        val toast = Toast.makeText(applicationContext, "Maximum digit is 14", Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun initUI(){
        calculator = ViewModelProvider(this)[Calculator::class.java]
        tvAns = findViewById(R.id.tvAns)
        tvNum1 = findViewById(R.id.tvNum1)
        tvNum2 = findViewById(R.id.tvNum2)
        tvPrev = findViewById(R.id.tvPrev)
        fabInfo = findViewById(R.id.fabInfo)

        val numberButtonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtonIds.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener(this)
        }

        //init btn op
        val operatorButtonIds = listOf(
            R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply,
            R.id.btnDivide, R.id.btnAllClear, R.id.btnEqual,
            R.id.btnAns, R.id.btnClear, R.id.btnNegativeOrPositive
        )

        operatorButtonIds.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener(this)
        }

        defaultValue = getString(R.string.defaultValue)

    }

//hidden feature related
    private fun goToMainScr(DBpass : String,num1Pass: String){
    if(DBpass == num1Pass){
        val intent = Intent(this, Notes_MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
    private fun dialogPassRecovery(){
        val dialog = Dialog(this)
        if (forgotPassCounter >= 5) {
            if (spQuestion.isNotBlank() && spAnswer.isNotBlank()) {
                dialog.setContentView(R.layout.dialog_passrecovery)
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.setCancelable(false)
                dialog.window?.attributes?.windowAnimations = R.style.animation

                //init
                val tvQUestion = dialog.findViewById<TextView>(R.id.tvQuestionToAnswer)
                val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirm)
                val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
                val etAnswer = dialog.findViewById<EditText>(R.id.etPassAnswer)
                tvQUestion.text = spQuestion
                btnConfirm.setOnClickListener {
                    if (etAnswer.text.toString().isNotBlank() && etAnswer.text.toString() == spAnswer
                    ) {
                        val intent = Intent(this, Notes_MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        else{
            forgotPassCounter++
        }
    }
     fun loadSP(){
        sf = getSharedPreferences("passTag", MODE_PRIVATE)
        sfEditor = sf.edit()
        spPass = sf.getString("sf_pass", "0000").toString()
        spQuestion = sf.getString("sf_question","").toString()
        spAnswer = sf.getString("sf_answer","").toString()

    }

    private fun firstTimeLogin() {
        if (spPass == defaultValue) {
            dialogs.dialogChangePass(true) {
                firstTimeInit()
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
            }
           // TODO("show how to login")
           // TODO("show how to use recovery password")
        }
    }
    private fun firstTimeInit(){
        loadSP()
        tvPrev.text = "Enter ${spPass} and press the = key"
    }

}


