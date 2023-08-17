package com.example.loginsignupauth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.unifood_definitivo.AdminActivity


import com.example.unifood_definitivo.databinding.ActivityLoginBinding

import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { loginTask ->
                        if (loginTask.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            if (user != null && user.isEmailVerified) {
                                val isAdmin = checkAdminEmail(email)
                                if (isAdmin) {
                                    val adminIntent = Intent(this, AdminActivity::class.java)
                                    startActivity(adminIntent)
                                } else {
                                    val userIntent = Intent(this, UserActivity::class.java)
                                    startActivity(userIntent)
                                }
                                finish() // Optional: Close the login activity
                            } else {
                                Toast.makeText(
                                    this,
                                    "Verifica prima l'indirizzo e-mail.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(this, "Errore durante il login.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "Inserisci l'indirizzo e-mail e la password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
    fun openSignupActivity(view: View) {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
    fun checkAdminEmail(email: String): Boolean {
        // Controlla se l'email è quella dell'amministratore
        return email.equals("unifood44@gmail.com", ignoreCase = true)
    }

}