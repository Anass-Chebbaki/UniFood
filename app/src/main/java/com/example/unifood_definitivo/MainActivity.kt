package com.example.loginsignupauth

import User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import com.example.unifood_definitivo.Model.CiboData
import com.example.unifood_definitivo.R
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
//sss

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()

        val isLoggedIn = firebaseAuth.currentUser != null

        if (!isLoggedIn) {
            // Utente non registrato o non ha effettuato l'accesso
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        val user = intent.getSerializableExtra("user") as? User

        if (user != null) {
            // Qui puoi utilizzare le informazioni dell'utente ricevute dall'intent
            val userId=user.id
            val userName = user.name
            val userSurname = user.surname
            val userEmail = user.email
            val userInitialBalance = user.initialBalance
            /*Log.d("UserActivity", "Id: $userId")
            Log.d("UserActivity", "Name: $userName")
            Log.d("UserActivity", "Surname: $userSurname")
            Log.d("UserActivity", "Email: $userEmail")
            Log.d("UserActivity", "Initial Balance: $userInitialBalance")*/
            //con il codice di sopra posso stampare a console tutte le informazioni dell'utente che
            //ha appena fatto il login
            val welcomeTextView = findViewById<TextView>(R.id.textView)
            welcomeTextView.text = "Ciao $userName!"


        } else {

        }
    }
}
