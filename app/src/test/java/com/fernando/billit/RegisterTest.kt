package com.fernando.billit

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
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
class RegisterTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var usersObserver: Observer<AuthResource<UserModel>>

    @InjectMocks
    lateinit var viewModel: RegisterViewModel

    @InjectMocks
    lateinit var sessionManager: SessionManager


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

    }


    @Before
    fun before() {
        viewModel.sessionManager = sessionManager

        viewModel.userResultObserver().observeForever(usersObserver)
    }

    @Test
    fun test_name_is_empty() {
        testCoroutineRule.runBlockingTest {

            viewModel.registerUser()

            verify(usersObserver).onChanged(AuthResource.error(R.string.required_name))
        }
    }

    @Test
    fun test_email_is_empty() {
        testCoroutineRule.runBlockingTest {

            viewModel.user.apply {
                name = "Name Test"
            }

            viewModel.registerUser()

            verify(usersObserver).onChanged(AuthResource.error(R.string.required_email))
        }
    }

    @Test
    fun test_password_is_empty() {
        testCoroutineRule.runBlockingTest {

            viewModel.user.apply {
                name = "Name Test"
                email = "email@gmail.com"
            }

            viewModel.registerUser()

            verify(usersObserver).onChanged(AuthResource.error(R.string.required_password))
        }
    }

    @Test
    fun test_confirm_password_is_empty() {
        testCoroutineRule.runBlockingTest {

            viewModel.user.apply {
                name = "Name Test"
                email = "email@gmail.com"
                password = "1234"
            }

            viewModel.registerUser()

            verify(usersObserver).onChanged(AuthResource.error(R.string.required_confirm_password))
        }
    }

    @Test
    fun test_passwords_not_match() {
        testCoroutineRule.runBlockingTest {

            viewModel.user.apply {
                name = "Name Test"
                email = "email@gmail.com"
                password = "1234"
                retryPassword = "4321"
            }

            viewModel.registerUser()

            verify(usersObserver).onChanged(AuthResource.error(R.string.password_not_match))
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }

}