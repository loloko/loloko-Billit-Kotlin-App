package com.fernando.billit.screen.register

import com.fernando.billit.R
import com.fernando.billit.common.ScreenRobot


class RobotRegister : ScreenRobot<RobotRegister>() {

    companion object {
        private const val FIELD_NAME: Int = R.id.et_name
        private const val FIELD_EMAIL: Int = R.id.et_email
        private const val FIELD_PASSWORD: Int = R.id.et_password
        private const val FIELD_CONFIRM_PASSWORD: Int = R.id.et_confirm_password
        private const val BT_REGISTER: Int = R.id.bt_register
        private const val REGISTER_NAME = "test24"
        private const val REGISTER_EMAIL = "test24@gmail.com"
        private const val REGISTER_PASSWORD = "123456d"
        private const val REGISTER_EMAIL_COLLISION = "fernando2@gmail.com"
    }

    fun verifyScreenRegister(): RobotRegister {
        checkViewContainsText(R.string.register_email)
        return this
    }

    fun verifyBtRegister(): RobotRegister {
        checkIsDisplayed(BT_REGISTER)
        return this
    }

    fun clickBtRegister(): RobotRegister {
        clickOnView(BT_REGISTER)
        return this
    }

    fun checkMessageNameRequired(): RobotRegister {
        checkMessageDisplayed(R.string.required_name)
        return this
    }

    fun checkMessageEmailRequired(): RobotRegister {
        checkMessageDisplayed(R.string.required_email)
        return this
    }

    fun checkMessagePasswordRequired(): RobotRegister {
        checkMessageDisplayed(R.string.required_password)
        return this
    }

    fun checkMessageConfirmPasswordRequired(): RobotRegister {
        checkMessageDisplayed(R.string.required_confirm_password)
        return this
    }

    fun typeName(): RobotRegister {
        enterTextIntoView(FIELD_NAME, REGISTER_NAME)
        return this
    }

    fun typeEmail(): RobotRegister {
        enterTextIntoView(FIELD_EMAIL, REGISTER_EMAIL)
        return this
    }

    fun typePassword(): RobotRegister {
        enterTextIntoView(FIELD_PASSWORD, REGISTER_PASSWORD)
        return this
    }

    fun typeConfirmPassword(): RobotRegister {
        enterTextIntoView(FIELD_CONFIRM_PASSWORD, REGISTER_PASSWORD)
        return this
    }

    fun typeDifferentPasswordBoth(): RobotRegister {
        enterTextIntoView(FIELD_PASSWORD, REGISTER_PASSWORD)
        enterTextIntoView(FIELD_CONFIRM_PASSWORD, "123")
        return this
    }

    fun typeInvalidEmail(): RobotRegister {
        enterTextIntoView(FIELD_EMAIL, "anything")
        return this
    }

    fun typeCollisionEmail(): RobotRegister {
        enterTextIntoView(FIELD_EMAIL, REGISTER_EMAIL_COLLISION)
        return this
    }

    fun typeWeakPasswordBothFields(): RobotRegister {
        enterTextIntoView(FIELD_PASSWORD, "1234")
        enterTextIntoView(FIELD_CONFIRM_PASSWORD, "1234")
        return this
    }

    fun checkMessageWeakPassword(): RobotRegister {
        checkMessageDisplayed(R.string.weak_password)
        return this
    }

    fun checkMessageCollisionEmail(): RobotRegister {
        checkMessageDisplayed(R.string.user_collision)
        return this
    }

    fun checkMessageInvalidEmail(): RobotRegister {
        checkMessageDisplayed(R.string.invalid_email)
        return this
    }

    fun checkMessagePasswordsDoNotMatch(): RobotRegister {
        checkMessageDisplayed(R.string.password_not_match)
        return this
    }

}
