package com.example.unifood_definitivo

import android.content.Intent
import com.example.unifood_definitivo.Model.User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.loginsignupauth.MainActivity
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity() {
    private lateinit var editText: EditText

    private lateinit var database: FirebaseDatabase
    private lateinit var sendButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        editText = findViewById(R.id.textView5)
        sendButton = findViewById(R.id.sendButton)

        database = FirebaseDatabase.getInstance()

        val reference = database.reference.child("PrimoDelGiorno")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)
                editText.setText(value) // Imposta il testo dell'EditText con il valore dal database
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
            }
        })

        sendButton.setOnClickListener {
            val userInput = editText.text.toString()
            reference.setValue(userInput) // Aggiorna il valore nel database
        }
    }
}
