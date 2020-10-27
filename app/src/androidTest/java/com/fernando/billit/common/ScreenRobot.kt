package com.fernando.billit.common

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.fernando.billit.common.actions.CallOnClickAction
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.Is


abstract class ScreenRobot<T : ScreenRobot<T>> {
    private val activityContext: Activity? = null

    val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @JvmOverloads
    @Throws(InterruptedException::class)
    fun sleep(seconds: Int = 1): T {
        Thread.sleep(seconds * 1000L)
        return this as T
    }

    fun checkIsDisplayed(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            Espresso.onView(ViewMatchers.withId(viewId)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        return this as T
    }

    fun checkIsClickable(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            Espresso.onView(ViewMatchers.withId(viewId)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
        }
        return this as T
    }

    fun checkIsHidden(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            Espresso.onView(ViewMatchers.withId(viewId)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        }
        return this as T
    }

    fun checkDoesNotExist(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            Espresso.onView(ViewMatchers.withId(viewId)).check(ViewAssertions.doesNotExist())
        }
        return this as T
    }

    fun checkViewHasText(@IdRes viewId: Int, @StringRes messageResId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId)).check(ViewAssertions.matches(ViewMatchers.withText(messageResId)))
        return this as T
    }

    fun checkViewHasDrawableAndTag(imageResId: Int): T {
        Espresso.onView(ViewMatchers.withTagValue(Is.`is`(imageResId as Any))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this as T
    }

    fun scrollViewDown(@IdRes viewIds: Int): T {
        Espresso.onView(ViewMatchers.withId(viewIds)).perform(ViewActions.swipeUp(), ViewActions.click())
        return this as T
    }

    fun checkViewHasText(@IdRes viewId: Int, expected: String?): T {
        Espresso.onView(ViewMatchers.withId(viewId)).check(ViewAssertions.matches(ViewMatchers.withText(expected)))
        return this as T
    }

    fun scrollViewUp(@IdRes viewIds: Int): T {
        Espresso.onView(ViewMatchers.withId(viewIds)).perform(ViewActions.swipeDown(), ViewActions.click())
        return this as T
    }

    fun checkViewContainsText(@StringRes text: Int): T {
        Espresso.onView(ViewMatchers.withText(text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this as T
    }

    fun checkViewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId)).check(ViewAssertions.matches(ViewMatchers.withHint(messageResId)))
        return this as T
    }

    fun callOnClickOnView(@IdRes viewId: Int): T {
        // On small Views, click action isn't always detected.
        // To avoid this, use callOnClick().
        Espresso.onView(ViewMatchers.withId(viewId)).perform(CallOnClickAction.callOnClick())
        return this as T
    }

    fun clickOnView(@IdRes viewId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.click())
        return this as T
    }

    fun pressBack(): T {
        Espresso.pressBack()
        return this as T
    }

//    fun goBackFromToolbar(): T {
//        Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click())
//        return this as T
//    }

    fun closeKeyboard(): T {
        Espresso.closeSoftKeyboard()
        return this as T
    }

    fun pressImeAction(@IdRes viewId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.pressImeActionButton())
        return this as T
    }

    fun enterTextIntoView(@IdRes viewId: Int, text: String?): T {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.typeText(text))
        Espresso.closeSoftKeyboard()
        return this as T
    }

    fun checkMessageDisplayed(@StringRes message: Int): T {
        onView(withId(message))
        return this as T
    }

    fun checkDialogWithTextIsDisplayed(@StringRes messageResId: Int): T {
        Espresso.onView(ViewMatchers.withText(messageResId))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this as T
    }

    fun clickButtonDialog(@StringRes stringID: Int): T {
        Espresso.onView(ViewMatchers.withText(stringID))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        return this as T
    }

//    fun checkToast(@StringRes messageResId: Int, activity: Activity) : T{
//        onView(withText(messageResId))
//            .inRoot(ToastMa)
//        return this as T
//    }

    fun swipeLeftOnView(@IdRes viewId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeLeft())
        return this as T
    }

    fun swipeRightOnView(@IdRes viewId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeRight())
        return this as T
    }

    fun clickOnCardForList(@IdRes viewId: Int, position: Int): T {
        Espresso.onView(withIndex(ViewMatchers.withId(viewId), position)).perform(ViewActions.click())
        return this as T
    }

    companion object {
        fun <T : ScreenRobot<*>?> withRobot(screenRobotClass: Class<T>?): T {
            requireNotNull(screenRobotClass) { "instance class == null" }
            return try {
                screenRobotClass.newInstance()
            } catch (iae: IllegalAccessException) {
                throw RuntimeException("IllegalAccessException", iae)
            } catch (ie: InstantiationException) {
                throw RuntimeException("InstantiationException", ie)
            }
        }

        fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                var currentIndex = 0
                override fun describeTo(description: Description) {
                    description.appendText("with index: ")
                    description.appendValue(index)
                    matcher.describeTo(description)
                }

                public override fun matchesSafely(view: View?): Boolean {
                    return matcher.matches(view) && currentIndex++ == index
                }
            }
        }
    }
}




