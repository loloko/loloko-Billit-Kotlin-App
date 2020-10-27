package com.fernando.billit.common.actions

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class CallOnClickAction private constructor() : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(ViewMatchers.isClickable(), ViewMatchers.isDisplayed())
    }

    override fun getDescription(): String {
        return "CallOnClick"
    }

    override fun perform(uiController: UiController, view: View) {
        view.callOnClick()
    }

    companion object {
        fun callOnClick(): CallOnClickAction {
            return CallOnClickAction()
        }
    }
}
