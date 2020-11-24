package com.fernando.billit.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterTest {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: AuthRepository

    @Mock
    private lateinit var usersObserver: Observer<AuthResource<UserModel>>

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = RegisterViewModel()
        viewModel.sessionManager = SessionManager()
        viewModel.authRepository = repository

        viewModel.userResultObserver().observeForever(usersObserver)
    }

    @After
    fun tearDown() {
        // do something if required
    }

    @Test
    fun `given error for empty name field, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.required_name)

            //When
            viewModel.registerUser()

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error for empty email field, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.required_email)
            viewModel.user.apply {
                name = "Name Test"
            }

            //When
            viewModel.registerUser()

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error for empty password field, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.required_password)
            viewModel.user.apply {
                name = "Name Test"
                email = "email@gmail.com"
            }

            //When
            viewModel.registerUser()

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error for empty confirm password field, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.required_confirm_password)
            viewModel.user.apply {
                name = "Name Test"
                email = "email@gmail.com"
                password = "1234"
            }

            //When
            viewModel.registerUser()

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error for passwords do not match, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.password_not_match)
            viewModel.user.apply {
                name = "Name Test"
                email = "email@gmail.com"
                password = "1234"
                retryPassword = "4321"
            }

            //When
            viewModel.registerUser()

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error for invalid email, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.invalid_email)
            viewModel.user.apply {
                name = "Name Test"
                email = "invalidEmail"
                password = "123456"
                retryPassword = "123456"
            }

            //When
            viewModel.registerUser()

            //Then
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given error for weak password, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = AuthResource.Error(R.string.weak_password)
            viewModel.user.apply {
                name = "Name Test"
                email = "email@test.com"
                password = "12"
                retryPassword = "12"
            }
            `when`(repository.registerUserWithEmail(viewModel.user)).thenReturn(resource)

            //When
            viewModel.registerUser()

            delay(1000)

            //Then
            verify(usersObserver).onChanged(AuthResource.Loading)
            verify(usersObserver).onChanged(resource)
        }

    @Test
    fun `given success register, when registerUser called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            viewModel.user.apply {
                name = "Name Test"
                email = "email@gmail.com"
                password = "123456"
                retryPassword = "123456"
            }
            val resource = AuthResource.Authenticated(viewModel.user)
            `when`(repository.registerUserWithEmail(viewModel.user)).thenReturn(resource)

            //When
            viewModel.registerUser()

            //Then
            verify(usersObserver).onChanged(AuthResource.Loading)
            verify(usersObserver).onChanged(resource)
        }

}