package com.tito.pappelleco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tito.pappelleco.ActivityRedefSenha
import com.tito.pappelleco.ActivityRegistro
import com.tito.pappelleco.R
import com.tito.pappelleco.SQLiteHelper

class ActivityLogin : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = SQLiteHelper(this)

        val loginEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextText2)
        val loginButton = findViewById<Button>(R.id.button5)
        val forgotPasswordText = findViewById<TextView>(R.id.textView4)
        val registerText = findViewById<TextView>(R.id.textView5)

        loginButton.setOnClickListener {
            val email = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email == "admin" && password == "1234") {
                Toast.makeText(this, "Bem-vindo, Administrador!", Toast.LENGTH_SHORT).show()
                val adminIntent = Intent(this, ActivityAdmin::class.java)
                startActivity(adminIntent)
                finish()
                return@setOnClickListener
            }

            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
                val mainIntent = Intent(this, ActivityInicial::class.java)
                startActivity(mainIntent)
                finish()
            } else {
                Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show()
            }
        }

        registerText.setOnClickListener {
            val registerIntent = Intent(this, ActivityRegistro::class.java)
            startActivity(registerIntent)
        }

        forgotPasswordText.setOnClickListener {
            val forgotPasswordIntent = Intent(this, ActivityRedefSenha::class.java)
            startActivity(forgotPasswordIntent)
        }
    }
}
