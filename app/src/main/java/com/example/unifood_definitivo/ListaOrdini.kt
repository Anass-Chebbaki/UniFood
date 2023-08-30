package com.example.unifood_definitivo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginsignupauth.MainActivity

class ListaOrdini : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ordini)

        val currentUserId = MainActivity.UserManager.userId
    }
}