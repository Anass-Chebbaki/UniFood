package com.example.loginsignupauth

import User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.unifood_definitivo.R

import com.google.android.material.button.MaterialButton


import com.google.firebase.database.*

class SignupActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var initialBalanceEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: MaterialButton
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.signup_name)
        surnameEditText = findViewById(R.id.signup_surname)
        emailEditText = findViewById(R.id.signup_email)
        initialBalanceEditText = findViewById(R.id.signup_initial_balance)
        passwordEditText = findViewById(R.id.signup_password)
        confirmPasswordEditText = findViewById(R.id.signup_confirm)

        registerButton = findViewById(R.id.signup_button)

        database = FirebaseDatabase.getInstance("https://unifood-89f3d-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference()

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val initialBalance = initialBalanceEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                initialBalance.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Mostra un messaggio di errore se almeno un campo è vuoto
                Toast.makeText(this, "Compila tutti i campi prima di procedere", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                showPasswordTooShortDialog()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showPasswordsNotMatchingDialog()
                return@setOnClickListener
            }

            val id = generateRandomId(6)
            val user = User(id, name, surname, email, initialBalance.toDouble(), password)

            val emailQuery = database.child("Utenti").orderByChild("email").equalTo(email)
            emailQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        showEmailAlreadyRegisteredDialog()
                    } else {
                        database.child("Utenti").child(id).setValue(user)
                        redirectToLoginScreen()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showDatabaseErrorDialog()
                }
            })
        }

        val accediText = findViewById<TextView>(R.id.acceditext)
        accediText.setOnClickListener {
            openLoginActivity()
        }
    }

    private fun generateRandomId(length: Int): String {
        val charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    private fun showEmailAlreadyRegisteredDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.email_already_registered_message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showPasswordTooShortDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.password_too_short_message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDatabaseErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.database_error_message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun redirectToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Opzionale: chiude l'attività corrente
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showPasswordsNotMatchingDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.passwords_not_matching_message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}












