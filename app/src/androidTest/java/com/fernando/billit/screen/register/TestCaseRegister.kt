package com.fernando.billit.screen.register

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fernando.billit.ui.auth.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestCaseRegister {

    @get:Rule
    var mActivityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Before
    fun setup() {
    }

    @After
    fun finish() {
    }

    @Test
    fun test1_checkIfIsScreenRegister() {
        RobotRegister()
            .verifyScreenRegister()
            .verifyBtRegister()
    }

    @Test
    fun test2_checkEmptyName() {
        RobotRegister()
            .clickBtRegister()
            .checkMessageNameRequired()
    }

    @Test
    fun test3_checkEmptyEmail() {
        RobotRegister()
            .typeName()
            .clickBtRegister()
            .checkMessageEmailRequired()
    }

    @Test
    fun test4_checkEmptyPassword() {
        RobotRegister()
            .typeName()
            .typeEmail()
            .clickBtRegister()
            .checkMessagePasswordRequired()
    }

    @Test
    fun test5_checkEmptyConfirmPassword() {
        RobotRegister()
            .typeName()
            .typeEmail()
            .typePassword()
            .clickBtRegister()
            .checkMessageConfirmPasswordRequired()
    }

    @Test
    fun test6_checkPasswordDoNoMatch() {
        RobotRegister()
            .typeName()
            .typeEmail()
            .typeDifferentPasswordBoth()
            .clickBtRegister()
            .checkMessagePasswordsDoNotMatch()
    }

    @Test
    @Throws(InterruptedException::class)
    fun test7_checkWeakPassword() {
        RobotRegister()
            .typeName()
            .typeEmail()
            .typeWeakPasswordBothFields()
            .clickBtRegister()
            .sleep(2)
            .checkMessageWeakPassword()
    }

    @Test
    @Throws(InterruptedException::class)
    fun test8_checkInvalidEmail() {
        RobotRegister()
            .typeName()
            .typeInvalidEmail()
            .typePassword()
            .typeConfirmPassword()
            .clickBtRegister()
            .sleep(2)
            .checkMessageInvalidEmail()
    }

    @Test
    @Throws(InterruptedException::class)
    fun test9_checkCollisionEmail() {
        RobotRegister()
            .typeName()
            .typeCollisionEmail()
            .typePassword()
            .typeConfirmPassword()
            .clickBtRegister()
            .sleep(2)
            .checkMessageCollisionEmail()
    }
}
