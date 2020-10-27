package com.fernando.billit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var usersObserver: Observer<AuthResource<UserModel>>

    @InjectMocks
    lateinit var viewModel: LoginViewModel

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
    fun test_email_is_empty() {
        testCoroutineRule.runBlockingTest {

            viewModel.signInWithEmail("", "password")

            verify(usersObserver).onChanged(AuthResource.error(R.string.required_email))
        }
    }

    @Test
    fun test_password_is_empty() {
        testCoroutineRule.runBlockingTest {

            viewModel.signInWithEmail("email@gmail.com", "")

            verify(usersObserver).onChanged(AuthResource.error(R.string.required_password))
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }
}