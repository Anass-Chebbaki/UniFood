package com.example.unifood_definitivo

import com.example.unifood_definitivo.Model.User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity() {
    private lateinit var userCategorySpinner: Spinner
    private lateinit var deleteButton: Button
    private lateinit var usersRef: DatabaseReference
    private val userList: MutableList<User> = mutableListOf()
    private lateinit var userAdapter: ArrayAdapter<String>
    private var selectedUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        userCategorySpinner = findViewById(R.id.userCategorySpinner)
        deleteButton = findViewById(R.id.deleteButton)

        usersRef = FirebaseDatabase.getInstance("https://unifood-89f3d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Utenti")

        userAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userCategorySpinner.adapter = userAdapter

        usersRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    userList.add(it)
                    userAdapter.add("${it.id} - ${it.name} ${it.surname}")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Non è necessario gestire questo evento in questo caso
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedUser = snapshot.getValue(User::class.java)
                removedUser?.let {
                    userList.remove(removedUser)
                    userAdapter.clear()
                    userList.forEach { user ->
                        userAdapter.add("${user.id} - ${user.name} ${user.surname}")
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Non è necessario gestire questo evento in questo caso
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci errori
            }
        })

        userCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedUser = userList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedUser = null
            }
        }

        deleteButton.setOnClickListener {
            selectedUser?.let {
                deleteUser(it.id)
            }
        }
    }

    private fun deleteUser(userId: String) {
        usersRef.child(userId).removeValue()
            .addOnSuccessListener {
                selectedUser = null
            }
            .addOnFailureListener {
                // Gestisci l'errore
            }
    }
}