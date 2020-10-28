package com.fernando.billit.screen.login

import com.fernando.billit.R
import com.fernando.billit.common.ScreenRobot

class RobotLogin : ScreenRobot<RobotLogin>() {

    companion object {
        private const val FIELD_EMAIL: Int = R.id.et_email
        private const val FIELD_PASSWORD: Int = R.id.et_password
        private const val BT_LOGIN: Int = R.id.bt_sign_in_with_email
        private const val BT_FORGOT_PASSWORD: Int = R.id.tv_forgot_password
        private const val BT_REGISTER: Int = R.id.tv_sign_up

        private const val LOGIN_EMAIL = "fer@gmail.com"
        private const val LOGIN_PASSWORD = "123456"
    }

    fun verifyScreenSignIn(): RobotLogin {
        checkViewContainsText(R.string.forgot_password)
        return this
    }


    fun verifyBtSignIn(): RobotLogin {
        checkIsDisplayed(BT_LOGIN)
        return this
    }

    fun clickBtSignIn(): RobotLogin {
        clickOnView(BT_LOGIN)
        return this
    }

    fun clickBtForgotPassword(): RobotLogin {
        clickOnView(BT_FORGOT_PASSWORD)
        return this
    }

    fun clickBtRegister(): RobotLogin {
        clickOnView(BT_REGISTER)
        return this
    }

    fun checkMessageEmail(): RobotLogin {
        checkMessageDisplayed(R.string.required_email)
        return this
    }

    fun checkMessagePassword(): RobotLogin {
        checkMessageDisplayed(R.string.required_password)
        return this
    }

    fun checkMessageCredentials(): RobotLogin {
        checkMessageDisplayed(R.string.invalid_credentials)
        return this
    }

    fun typeEmail(): RobotLogin {
        enterTextIntoView(FIELD_EMAIL, LOGIN_EMAIL)
        return this
    }

    fun typeWrongEmail(): RobotLogin {
        enterTextIntoView(FIELD_EMAIL, "anything")
        return this
    }

    fun typePassword(): RobotLogin {
        enterTextIntoView(FIELD_PASSWORD, LOGIN_PASSWORD)
        return this
    }

    fun verifyScreenRegister(): RobotLogin {
        checkViewContainsText(R.string.register_email)
        return this
    }

    fun verifyScreenResetPassword(): RobotLogin {
        checkViewContainsText(R.string.reset_password)
        return this
    }

}

