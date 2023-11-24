package capstone.project.mushymatch.view.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
   Created by Andre Eka Putra on 24/11/23
   andremoore431@gmail.com
*/

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<SplashActivity>
            = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun testSplashScreenDisplayed() {
        val binding = activityRule.activity.binding
        onView(withId(binding.imageView5.id)).check(matches(isDisplayed()))
    }
}