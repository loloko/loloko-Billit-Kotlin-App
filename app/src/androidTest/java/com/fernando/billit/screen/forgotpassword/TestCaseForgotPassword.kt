package com.fernando.billit.screen.forgotpassword

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fernando.billit.ui.auth.ForgotPasswordActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestCaseForgotPassword {

    @get:Rule
    var activityRule = ActivityScenarioRule(ForgotPasswordActivity::class.java)

    @Before
    fun setup() {
    }

    @After
    fun finish() {
    }

    @Test
    fun test1_checkIfIsLoginScreen() {
        RobotForgotPassword()
            .verifyScreenIsForgotPassword()
            .verifyBtReset()
    }

    @Test
    fun test2_checkRequiredEmailMessage() {
        RobotForgotPassword()
            .verifyBtReset()
            .checkMessageEmail()
    }

    @Test
    fun test3_checkInvalidEmailMessage() {
        RobotForgotPassword()
            .typeWrongEmail()
            .clickBtReset()
            .sleep(2)
            .checkMessageInvalidEmail()
    }

    @Test
    fun test4_checkResetPasswordSentToEmail() {
        RobotForgotPassword()
            .typeEmail()
            .clickBtReset()
            .sleep(2)
            .checkMessageResetPasswordEmailSent()
    }
}
