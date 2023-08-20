package com.example.loginsignupauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.unifood_definitivo.R
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()

        val isLoggedIn = firebaseAuth.currentUser != null

        if (!isLoggedIn) {
            // Utente non registrato o non ha effettuato l'accesso
            //dio maialino e pure stronzetto
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()

        }


    }
}