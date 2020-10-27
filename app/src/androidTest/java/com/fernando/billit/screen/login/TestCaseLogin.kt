package com.fernando.billit.screen.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.fernando.billit.ui.auth.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestCaseLogin {

    @get:Rule
    var activityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setup() {
    }

    @After
    fun finish() {
    }

    @Test
    fun checkIfIsLoginScreen() {
        RobotLogin()
            .verifyScreenSignIn()
            .verifyBtSignIn()
    }

    @Test
    fun checkRequiredEmailMessage() {
        RobotLogin()
            .clickBtSignIn()
            .checkMessageEmail()
    }

    @Test
    fun checkRequiredPasswordMessage() {
        RobotLogin()
            .typeEmail()
            .clickBtSignIn()
            .checkMessagePassword()
    }

    @Test
    fun checkInvalidCredentialsMessage() {
        RobotLogin()
            .typeWrongEmail()
            .typePassword()
            .clickBtSignIn()
            .checkMessageCredentials()
    }

//    @Test
//    @Throws(InterruptedException::class)
//    fun checkLoginSuccessful() {
//        RobotLogin()
//            .typeEmail()
//            .typePassword()
//            .clickBtSignIn()
//            .sleep(10)
//            .checkIfTrackScreenIsDisplayed()
//    }

    @Test
    fun checkScreenForgotPassword() {
        RobotLogin()
            .clickBtForgotPassword()
            .verifyScreenResetPassword()
            .pressBack()
            .verifyScreenSignIn()
    }

    @Test
    fun checkScreenRegisterUser() {
        RobotLogin()
            .clickBtRegister()
            .verifyScreenRegister()
            .pressBack()
            .verifyScreenSignIn()
    }
}
