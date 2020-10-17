package com.fernando.billit.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.databinding.ActivityRegisterBinding
import com.fernando.billit.extension.TAG
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.util.AuthResource.AuthStatus.*
import com.fernando.billit.viewmodel.RegisterViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class RegisterActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var loadingPopup: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(RegisterViewModel::class.java)

        // Create loading dialog
        loadingPopup = createLoadingPopup()

        init()
        observers()
    }

    private fun init() {
        binding.imBackPress.setOnClickListener {
            navToLoginScreen()
        }

        // Register click button
        binding.btRegister.setOnClickListener {

            viewModel.user.apply {
                name = binding.etName.text.toString()
                email = binding.etEmail.text.toString()
                password = binding.etPassword.text.toString()
                retryPassword = binding.etConfirmPassword.text.toString()
            }

            viewModel.registerUser()
        }
    }

    private fun observers() {
        viewModel.userResultObserver().observe(this, { user ->
            if (user != null) {
                when (user.status) {
                    LOADING -> {
                        loadingPopup.show()
                    }
                    AUTHENTICATED -> {
                        loadingPopup.dismiss()
                        navToMainScreen()
                    }
                    ERROR -> {
                        toastMessage(user.message, isWarning = true)
                        loadingPopup.dismiss()
                    }
                    NOT_AUTHENTICATED -> {
                        loadingPopup.dismiss()
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navToLoginScreen()
    }

    private fun navToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun navToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
