package com.fernando.billit.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.fernando.billit.databinding.ActivityRegisterBinding
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.isNetworkAvailable
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.ui.main.MainActivity
import com.fernando.billit.util.AuthResource.*
import com.fernando.billit.viewmodel.RegisterViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class RegisterActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val loadingPopup by lazy {
        createLoadingPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(RegisterViewModel::class.java)

        variablesInit()
        subscribeObservers()
    }

    private fun variablesInit() {
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

            if (isNetworkAvailable())
                viewModel.registerUser()
        }
    }

    private fun subscribeObservers() {
        viewModel.userResultObserver().observe(this) { user ->
            when (user) {
                is Loading -> {
                    loadingPopup.show()
                }
                is Authenticated -> {
                    loadingPopup.dismiss()
                    navToMainScreen()
                }
                is Error -> {
                    toastMessage(user.message, isWarning = true)
                    loadingPopup.dismiss()
                }
                is NotAuthenticated -> {
                    loadingPopup.dismiss()
                }
                else -> loadingPopup.dismiss()
            }
        }
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
