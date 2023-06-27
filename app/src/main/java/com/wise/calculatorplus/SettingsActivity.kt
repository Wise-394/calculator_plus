package com.wise.calculatorplus

import android.app.Dialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.wise.calculatorplus.dialogs.Dialogs

class SettingsActivity : AppCompatActivity() {
    private lateinit var ibtnBack: ImageButton
    private lateinit var cvChangePass: CardView
    private lateinit var cvChangePassRecovery: CardView
    private lateinit var cvReset: CardView
    private val dialogs = Dialogs(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //innit
        ibtnBack = findViewById(R.id.ibtnBack)
      //  sf = getSharedPreferences("passTag", MODE_PRIVATE)
      //  sfEditor = sf.edit()
      //  defaultValue = getString(R.string.defaultValue)

        //dialog password
        cvChangePass = findViewById(R.id.cvChangePass)
        cvChangePass.setOnClickListener{
            dialogs.dialogChangePass(false){
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
            }
        }

        //dialog passRecovery
        cvChangePassRecovery = findViewById(R.id.cvChangePassRecovery)
        cvChangePassRecovery.setOnClickListener{
           dialogs.dialogChangePassRecovery()
        }
        ibtnBack.setOnClickListener{
            finish()
        }

        //dialog resetpass
        cvReset = findViewById(R.id.cvReset)
        cvReset.setOnClickListener{
            dialogs.dialogResetPass()
        }
    }
}