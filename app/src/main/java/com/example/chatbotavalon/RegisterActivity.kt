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
import com.example.chatbotavalon.data.entity.User
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(){
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        appDatabase = AppDatabase.getDatabase(this)

        val fullNameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.registerEmailEditText)
        val passwordEditText = findViewById<EditText>(R.id.registerPasswordEditText)
        val loginTextView = findViewById<TextView>(R.id.loginRedirect)
        val registerButton = findViewById<Button>(R.id.registerButton)

        val spannable = SpannableString("Already have an account? Login")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue_primary))

        spannable.setSpan(clickableSpan, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(colorSpan, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        loginTextView.text = spannable
        loginTextView.movementMethod = LinkMovementMethod.getInstance()

        registerButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (!fullName.matches(Regex("^[a-zA-Z\\s]+$"))) {
                Toast.makeText(this, "Full Name must contain only letters", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(
                    id = 0,
                    name = fullName,
                    email = email,
                    password = password
                )
                lifecycleScope.launch {
                    val database = AppDatabase.getDatabase(this@RegisterActivity)
                    database.userDao().tambahUser(user)

                    Toast.makeText(this@RegisterActivity, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}