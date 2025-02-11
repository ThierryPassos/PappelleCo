package com.tito.pappelleco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityRegistro : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        dbHelper = SQLiteHelper(this)

        val nameEditText = findViewById<EditText>(R.id.editTextText4)
        val emailEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextText2)
        val cpfEditText = findViewById<EditText>(R.id.editTextcpf)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextText3)
        val registerButton = findViewById<Button>(R.id.button5)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val cpf = cpfEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não correspondem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cpf.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (dbHelper.cpfExists(cpf)) {
                Toast.makeText(this, "CPF já registrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.insertUser(name, email, password, cpf)) {
                Toast.makeText(this, "Registro bem-sucedido", Toast.LENGTH_SHORT).show()
                val loginIntent = Intent(this, ActivityLogin::class.java)
                startActivity(loginIntent)
                finish()
            } else {
                Toast.makeText(this, "Erro ao registrar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
