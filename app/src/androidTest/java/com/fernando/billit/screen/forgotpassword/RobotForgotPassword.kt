package com.fernando.billit.screen.forgotpassword

import com.fernando.billit.R
import com.fernando.billit.common.ScreenRobot

class RobotForgotPassword : ScreenRobot<RobotForgotPassword>() {

    companion object {
        private const val FIELD_EMAIL: Int = R.id.et_email_reset
        private const val BT_RESET: Int = R.id.bt_reset_email

        private const val LOGIN_EMAIL = "fer@gmail.com"
    }

    fun verifyScreenIsForgotPassword(): RobotForgotPassword {
        checkViewContainsText(R.string.reset_password)
        return this
    }


    fun verifyBtReset(): RobotForgotPassword {
        checkIsDisplayed(BT_RESET)
        return this
    }

    fun clickBtReset(): RobotForgotPassword {
        clickOnView(BT_RESET)
        return this
    }

    fun checkMessageEmail(): RobotForgotPassword {
        checkMessageDisplayed(R.string.required_email)
        return this
    }

    fun checkMessageInvalidEmail(): RobotForgotPassword {
        checkMessageDisplayed(R.string.invalid_email)
        return this
    }

    fun checkMessageResetPasswordEmailSent(): RobotForgotPassword {
        checkMessageDisplayed(R.string.email_sent)
        return this
    }

    fun typeEmail(): RobotForgotPassword {
        enterTextIntoView(FIELD_EMAIL, LOGIN_EMAIL)
        return this
    }

    fun typeWrongEmail(): RobotForgotPassword {
        enterTextIntoView(FIELD_EMAIL, "anything")
        return this
    }

}

