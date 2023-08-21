package com.example.loginsignupauth


import User
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
        getUserInformation(email, password) { user ->
            if (user != null) {
                if (email == "unifood44@gmail.com" && password == "unifood") {
                    val adminIntent = Intent(this@LoginActivity, AdminActivity::class.java)
                    startActivity(adminIntent)
                    finish()
                    // L'utente è un amministratore, fai qualcosa qui se necessario
                } else {
                    val intent = Intent(this@LoginActivity, UserActivity::class.java)
                    intent.putExtra("user", user) // Passa l'oggetto User all'activity successiva
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this@LoginActivity, "Credenziali errate", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserInformation(email: String, password: String, callback: (User?) -> Unit) {
        val query = database.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var user: User? = null

                    for (userSnapshot in snapshot.children) {
                        val retrievedUser = userSnapshot.getValue(User::class.java)
                        if (retrievedUser != null && retrievedUser.password == password) {
                            user = retrievedUser
                            break
                        }
                    }

                    callback(user)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    private fun openSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}