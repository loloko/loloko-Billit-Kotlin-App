package com.fernando.billit.ui.auth

import android.app.Dialog
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.R
import com.fernando.billit.databinding.ActivityForgotPasswordBinding
import com.fernando.billit.extension.createFinishDialog
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.util.AuthResource.AuthStatus.*
import com.fernando.billit.viewmodel.ForgotPasswordViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ForgotPasswordActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var binding: ActivityForgotPasswordBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var loadingPopup: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(ForgotPasswordViewModel::class.java)

        // Create loading dialog
        loadingPopup = createLoadingPopup()

        touchActionScreen()
        observers()
    }

    private fun touchActionScreen() {
        // Close screen image arrow back
        binding.imBackPress.setOnClickListener {
            finish()
        }

        // Button Reset password
        binding.btResetEmail.setOnClickListener {
            viewModel.sendResetPasswordEmail(binding.etEmailReset.text.toString())
        }
    }

    private fun observers() {
        viewModel.userResultObserver().observe(this, { result ->
            if (result != null) {
                when (result.status) {
                    LOADING -> {
                        loadingPopup.show()
                    }
                    RESET_PASSWORD -> {
                        loadingPopup.dismiss()
                        createFinishDialog(R.string.email_sent)
                    }
                    ERROR -> {
                        toastMessage(result.message, isWarning = true)
                        loadingPopup.dismiss()
                    }

                    else -> loadingPopup.dismiss()
                }
            }
        })
    }

}
