package com.example.unifood_definitivo

import com.example.loginsignupauth.LoginActivity
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LoginTest{
    private val repository: LoginActivity= LoginActivity()
    @Test
    fun signIn_success() = runBlocking {
        val email = "mariorossi@gmail.com"
        val password = "mariorossi123"
        //fkfkkd

    }
}