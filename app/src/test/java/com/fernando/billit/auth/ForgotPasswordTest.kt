package com.fernando.billit.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.R
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.ForgotPasswordViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
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
    private lateinit var repository: AuthRepository

    @Mock
    private lateinit var usersObserver: Observer<AuthResource<UserModel>>

    lateinit var viewModel: ForgotPasswordViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ForgotPasswordViewModel()
        viewModel.authRepository = repository

        viewModel.userResultObserver().observeForever(usersObserver)
    }

    @After
    fun tearDown() {
        // do something if required
    }

    @Test
    fun `given error for empty email field, when sendResetPasswordEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.required_email)

            //When
            viewModel.sendResetPasswordEmail("")

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error for invalid email, when sendResetPasswordEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.invalid_email)

            //When
            viewModel.sendResetPasswordEmail("email")

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given success send email, when sendResetPasswordEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.ResetPassword
            Mockito.`when`(repository.sendResetPasswordEmail(anyString())).thenReturn(resource)

            //When
            viewModel.sendResetPasswordEmail("email@test.com")

            delay(1000)

            //Then
            verify(usersObserver).onChanged(AuthResource.Loading)
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error sending email, when sendResetPasswordEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.error_send_email)
            Mockito.`when`(repository.sendResetPasswordEmail(anyString())).thenReturn(resource)

            //When
            viewModel.sendResetPasswordEmail("email@test.com")

            delay(500)

            //Then
            verify(usersObserver).onChanged(AuthResource.Loading)
            verify(usersObserver).onChanged(resource)
        }

}