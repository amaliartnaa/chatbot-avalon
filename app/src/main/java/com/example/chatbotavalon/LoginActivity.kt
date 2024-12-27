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

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val registerTextView = findViewById<TextView>(R.id.registerRedirect)

        val spannable = SpannableString("Don't have an account? Register")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue_primary))

        spannable.setSpan(clickableSpan, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(colorSpan, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        registerTextView.text = spannable
        registerTextView.movementMethod = LinkMovementMethod.getInstance()

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}