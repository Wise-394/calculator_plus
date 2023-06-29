package com.wise.calculatorplus.dialogs

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.wise.calculatorplus.R

class Dialogs(private val context: Context) {
    private lateinit var btnConfirm: Button
    private lateinit var btnCancel: Button
    private lateinit var etPass: EditText
    private lateinit var etQuestion: EditText
    private lateinit var etAnswer: EditText

    fun dialogChangePassRecovery(callback: () -> Unit) {



        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_change_passrecovery)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.animation

        etQuestion = dialog.findViewById(R.id.etQuestion)
        etAnswer = dialog.findViewById(R.id.etPassAnswer)
        btnConfirm = dialog.findViewById(R.id.btnConfirm)
        btnCancel = dialog.findViewById(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            if (etQuestion.text.isNotBlank() && etAnswer.text.isNotBlank()) {
                val question = etQuestion.text.toString()
                val answer = etAnswer.text.toString()
                changePassRecovery(question, answer)
               Toast.makeText(context, "Password recovery added successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                callback()
            } else {
                Toast.makeText(context, "Invalid question/answer", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun dialogChangePass(firstTimeLogin: Boolean, callback: () -> Unit) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_change_pass)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.animation

        etPass = dialog.findViewById(R.id.etPassAnswer)
        btnConfirm = dialog.findViewById(R.id.btnConfirm)
        btnCancel = dialog.findViewById(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            val pass = etPass.text.toString()
            if (pass.isNotBlank() && pass.isDigitsOnly()) {
                changePass(pass)
                dialog.dismiss()

                // Invoke the callback function to notify that the user is done with their input
                callback()
            } else {
                Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener {
            if(!firstTimeLogin) {
                dialog.dismiss()
            }
            else{
                Toast.makeText(context, "enter a password", Toast.LENGTH_SHORT).show()
            }
        }

        // Show the dialog
        dialog.show()
    }


    fun dialogResetPass(){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_reset_password)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.animation

        btnConfirm = dialog.findViewById(R.id.btnConfirm)
        btnCancel = dialog.findViewById(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            resetPassword()
            dialog.dismiss()
            Toast.makeText(context, "Password Reset Successfully", Toast.LENGTH_SHORT).show()


            }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun dialogTipPassRecovery(){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_show_passrecovery_tip)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.animation

        btnConfirm = dialog.findViewById(R.id.btnConfirm)

        btnConfirm.setOnClickListener {
            dialog.dismiss()
           // Toast.makeText(context, "Password Reset Successfully", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }



    private fun changePass(newPass: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("passTag", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("sf_pass", newPass)
        editor.apply()
    }

    private fun changePassRecovery(question: String, answer: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("passTag", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("sf_question", question)
        editor.putString("sf_answer", answer)
        editor.apply()
    }

    private fun resetPassword(){
        val defaultPass: String = context.getString(R.string.defaultValue)
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("passTag", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("sf_pass",defaultPass)
        editor.apply()
    }
}