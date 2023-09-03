package com.example.unifood_definitivo

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.loginsignupauth.SignupActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule


/**
 * Questa è la classe di test Espresso per la SignupActivity,
 che verifica il comportamento della funzione redirectToLoginScreen
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignupActivity::class.java)

    @Test
    fun redirectToLoginScreen() {
        // Simula un click su un elemento della tua UI che dovrebbe innescare redirectToLoginScreen
        Espresso.onView(ViewMatchers.withId(R.id.acceditext)).perform(ViewActions.click())
        // Puoi aggiungere ulteriori asserzioni qui, se necessario, per verificare che il reindirizzamento sia avvenuto correttamente
    }
}