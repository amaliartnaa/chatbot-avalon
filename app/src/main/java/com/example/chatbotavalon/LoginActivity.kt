package com.example.chatbotavalon

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.chatbotavalon.data.dao.UserDao
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(){
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerTextView = findViewById<TextView>(R.id.registerRedirect)
        val loginButton = findViewById<Button>(R.id.loginButton)

        val database = AppDatabase.getDatabase(this)
        userDao = database.userDao()

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

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password  = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val user = userDao.ambilSemuaUser().find {
                        it.email.equals(email, ignoreCase = true) && it.password == password
                    }
                    if (user != null) {
                        Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}