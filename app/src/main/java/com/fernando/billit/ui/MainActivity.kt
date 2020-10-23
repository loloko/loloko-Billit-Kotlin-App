package com.fernando.billit.ui

import android.os.Bundle
import android.util.Log
import com.fernando.billit.BaseActivity
import com.fernando.billit.R
import com.fernando.billit.extension.TAG

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {

            Log.e(TAG, "onCreate: " + sessionManager.getAuthUser().value?.data?.email)


        } catch (e: Exception) {

        }
    }
}