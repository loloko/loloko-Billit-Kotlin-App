package com.fernando.billit.common.actions

import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap

object CoordinatesClickAction {
    fun clickOnCoordinates(x: Float, y: Float): GeneralClickAction {
        return GeneralClickAction(
            Tap.SINGLE,
            { view ->
                val screenPos = IntArray(2)
                view.getLocationOnScreen(screenPos)
                val screenX = screenPos[0] + x
                val screenY = screenPos[1] + y
                floatArrayOf(screenX, screenY)
            },
            Press.FINGER
        )
    }
}