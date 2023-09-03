package com.example.loginsignupauth


import com.example.unifood_definitivo.Model.User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.unifood_definitivo.AdminActivity
import com.example.unifood_definitivo.R


import com.google.firebase.database.*
/**
 * Questa classe gestisce l'attività di accesso dell'utente.
 * Gli utenti possono inserire le proprie credenziali (email e password) per accedere all'app.
 * Se le credenziali sono corrette, vengono reindirizzati alla schermata principale dell'app.
 * Se l'utente è un amministratore, verranno reindirizzati alla schermata di amministrazione.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupText: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Inizializza le view e il riferimento al database Firebase
        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupText = findViewById(R.id.signuptext)
        database = FirebaseDatabase.getInstance("https://unifood-89f3d-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Utenti")
        // Gestisce il click sul pulsante di accesso
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(email, password)
            } else {
                Toast.makeText(this, "Inserisci email e password", Toast.LENGTH_SHORT).show()
            }
        }
        // Gestisce il click sul testo di registrazione
        signupText.setOnClickListener {
            openSignupActivity()
        }
    }
    /**
     * Questa funzione verifica le credenziali dell'utente utilizzando l'email e la password fornite.
     * Se le credenziali sono corrette, reindirizza l'utente alla schermata appropriata:
     * - Se l'email e la password corrispondono all'account amministratore, reindirizza all'attività dell'amministratore.
     * - Altrimenti, reindirizza all'attività principale con i dati dell'utente autenticato.
     * Se le credenziali non sono valide, mostra un messaggio di errore.
     *
     * @param email L'email fornita dall'utente.
     * @param password La password fornita dall'utente.
     */
    private fun authenticateUser(email: String, password: String) {
        getUserInformation(email, password) { user ->
            if (user != null) {
                if (email == "unifood44@gmail.com" && password == "unifood") {
                    val adminIntent = Intent(this@LoginActivity, AdminActivity::class.java)
                    startActivity(adminIntent)
                    finish()
                    // L'utente è un amministratore, fai qualcosa qui se necessario
                } else {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("user", user)
                    //Log.d("LoginActivity", "Starting MainActivity with user data...")// Passa l'oggetto com.example.unifood_definitivo.Model.User all'activity successiva
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this@LoginActivity, "Credenziali errate", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * Questa funzione cerca un utente nel database Firebase utilizzando l'email fornita.
     * Se l'utente viene trovato e la password corrisponde, il callback restituirà l'oggetto User.
     * Se l'utente non viene trovato o la password non corrisponde, il callback restituirà null.
     *
     * @param email L'email dell'utente da cercare.
     * @param password La password fornita dall'utente per la verifica.
     * @param callback La funzione di callback che verrà chiamata con l'oggetto User o null.
     */
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
    /**
     * Apre l'activity di registrazione (SignupActivity) quando l'utente preme il pulsante "Iscriviti".
     */
    private fun openSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}