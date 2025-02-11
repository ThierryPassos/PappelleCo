package com.tito.pappelleco

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityRedefSenha : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redefine_senha)

        dbHelper = SQLiteHelper(this)

        val cpfEditText = findViewById<EditText>(R.id.editTextcpf)
        val newPasswordEditText = findViewById<EditText>(R.id.editTextNewSenha)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfSenha)
        val resetPasswordButton = findViewById<Button>(R.id.buttonRedefSenha)

        resetPasswordButton.setOnClickListener {
            val cpf = cpfEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (cpf.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "As senhas não correspondem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Atualizar a senha no banco de dados
            val isUpdated = dbHelper.updatePassword(cpf, newPassword)
            if (isUpdated) {
                Toast.makeText(this, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show()
                finish() // Fecha a tela após a redefinição
            } else {
                Toast.makeText(this, "CPF não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

    }
}


