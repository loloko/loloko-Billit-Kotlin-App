package com.fernando.billit

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.R
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.ForgotPasswordViewModel
import com.fernando.billit.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ForgotPasswordTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var usersObserver: Observer<AuthResource<UserModel>>

    @InjectMocks
    lateinit var viewModel: ForgotPasswordViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel.userResultObserver().observeForever(usersObserver)
//        viewModel.authRepository = AuthRepository(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().reference)
    }


    @Test
    fun test_email_is_empty() {
        testCoroutineRule.runBlockingTest {

            viewModel.sendResetPasswordEmail("")

            verify(usersObserver).onChanged(AuthResource.error(R.string.required_email))
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }

}