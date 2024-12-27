package com.example.chatbotavalon

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class RegisterActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val loginTextView = findViewById<TextView>(R.id.loginRedirect)

        val spannable = SpannableString("Already have an account? Login")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue_primary))

        spannable.setSpan(clickableSpan, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(colorSpan, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        loginTextView.text = spannable
        loginTextView.movementMethod = LinkMovementMethod.getInstance()

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}