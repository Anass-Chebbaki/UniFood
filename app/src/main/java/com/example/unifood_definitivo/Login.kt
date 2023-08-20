package com.example.loginsignupauth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.unifood_definitivo.AdminActivity
import com.example.unifood_definitivo.R
import com.example.unifood_definitivo.User




import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {




    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupText: TextView

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupText = findViewById(R.id.signuptext)

        database = FirebaseDatabase.getInstance("https://unifood-89f3d-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Utenti")

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(email, password)
            } else {
                Toast.makeText(this, "Inserisci email e password", Toast.LENGTH_SHORT).show()
            }
        }

        signupText.setOnClickListener {
            openSignupActivity()
        }
    }

    private fun authenticateUser(email: String, password: String) {
        val query = database.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var isAuthenticated = false
                    var isAdmin = false

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.password == password) {
                            // Credenziali corrette
                            isAuthenticated = true
                            if (email == "unifood44@gmail.com" && password == "unifood") {
                                isAdmin = true
                            }
                            break
                        }
                    }

                    if (isAuthenticated) {
                        if (isAdmin) {
                            val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                            startActivity(intent)
                        } else {
                            // L'utente è autenticato, reindirizza all'activity successiva
                            val intent = Intent(this@LoginActivity, UserActivity::class.java)
                            startActivity(intent)
                        }
                        finish()
                    } else {
                        // Credenziali errate
                        Toast.makeText(this@LoginActivity, "Credenziali errate", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Nessun utente trovato con quella email
                    Toast.makeText(this@LoginActivity, "Nessun utente trovato con questa email", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Errore nel database", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}