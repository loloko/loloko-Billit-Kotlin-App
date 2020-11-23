package com.fernando.billit.ui.auth

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.fernando.billit.R
import com.fernando.billit.databinding.ActivityForgotPasswordBinding
import com.fernando.billit.extension.createFinishDialog
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.isNetworkAvailable
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.util.AuthResource.*
import com.fernando.billit.viewmodel.ForgotPasswordViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ForgotPasswordActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var binding: ActivityForgotPasswordBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val loadingPopup by lazy {
        createLoadingPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(ForgotPasswordViewModel::class.java)

        touchActionScreenInit()
        subscribeObservers()
    }

    private fun touchActionScreenInit() {
        // Close screen image arrow back
        binding.imBackPress.setOnClickListener {
            finish()
        }

        // Button Reset password
        binding.btResetEmail.setOnClickListener {
            if (isNetworkAvailable())
                viewModel.sendResetPasswordEmail(binding.etEmailReset.text.toString())
        }
    }

    private fun subscribeObservers() {
        viewModel.userResultObserver().observe(this) { result ->
            when (result) {
                is Loading -> {
                    loadingPopup.show()
                }
                is ResetPassword -> {
                    loadingPopup.dismiss()
                    createFinishDialog(R.string.email_sent)
                }
                is Error -> {
                    toastMessage(result.message, isWarning = true)
                    loadingPopup.dismiss()
                }

                else -> loadingPopup.dismiss()
            }
        }
    }

}
