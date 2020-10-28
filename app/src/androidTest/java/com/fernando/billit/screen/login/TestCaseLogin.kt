package com.fernando.billit.screen.login

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
    var activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
    }

    @After
    fun finish() {
    }

    @Test
    fun test1_checkIfIsLoginScreen() {
        RobotLogin()
            .verifyScreenSignIn()
            .verifyBtSignIn()
    }

    @Test
    fun test2_checkRequiredEmailMessage() {
        RobotLogin()
            .clickBtSignIn()
            .checkMessageEmail()
    }

    @Test
    fun test3_checkRequiredPasswordMessage() {
        RobotLogin()
            .typeEmail()
            .clickBtSignIn()
            .checkMessagePassword()
    }

    @Test
    fun test4_checkInvalidCredentialsMessage() {
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
    fun test5_checkScreenForgotPassword() {
        RobotLogin()
            .clickBtForgotPassword()
            .verifyScreenResetPassword()
            .pressBack()
            .verifyScreenSignIn()
    }

    @Test
    fun test6_checkScreenRegisterUser() {
        RobotLogin()
            .clickBtRegister()
            .verifyScreenRegister()
            .pressBack()
            .verifyScreenSignIn()
    }
}
