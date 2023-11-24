package capstone.project.mushymatch.view.register

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import capstone.project.mushymatch.view.login.loginActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
   Created by Andre Eka Putra on 24/11/23
   andremoore431@gmail.com
*/

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(RegisterActivity::class.java)

    @Test
    fun registerSuccessMoveToLoginActivity() {
        val binding = intentsTestRule.activity.binding

        // Type text into the name field
        onView(withId(binding.username.id)).perform(typeText("John Doe"), closeSoftKeyboard())

        // Type text into the email field
        onView(withId(binding.email.id)).perform(typeText("john.doe@example.com"), closeSoftKeyboard())

        // Type text into the password field
        onView(withId(binding.password.id)).perform(typeText("password123"), closeSoftKeyboard())

        // Click the register button
        onView(withId(binding.btnMasuk.id)).perform(click())

        // Wait for 2 seconds (adjust the duration based on your needs)
        Thread.sleep(2000)

        // Check if the activity has switched to the LoginActivity
        intended(hasComponent(loginActivity::class.java.name))
    }
}