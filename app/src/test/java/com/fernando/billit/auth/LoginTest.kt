package com.fernando.billit.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginTest {

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

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = LoginViewModel()
        viewModel.authRepository = repository
        viewModel.sessionManager = SessionManager()

        viewModel.userResultObserver().observeForever(usersObserver)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `given error state for empty email field, when signInWithEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val message = R.string.required_email

            //When
            viewModel.signInWithEmail("", anyString())

            //Then
            verify(usersObserver).onChanged(AuthResource.Error(message))
        }

    @Test
    fun `given error state for empty password field, when signInWithEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val message = R.string.required_password

            //When
            viewModel.signInWithEmail("ëmail@test.com", "")

            //Then
            verify(usersObserver).onChanged(AuthResource.Error(message))
        }

    @Test
    fun `given error state for invalid credentials, when signInWithEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val message = R.string.invalid_credentials
            `when`(repository.signInWithEmail(anyString(), anyString()))
                .thenReturn(AuthResource.Error(message))

            //When
            viewModel.signInWithEmail("email@test.com", "1212")

            //Then
            verify(usersObserver).onChanged(AuthResource.Loading)
            verify(usersObserver).onChanged(AuthResource.Error(message))
        }

    @Test
    fun `given success state, when signInWithEmail called`() =
        testCoroutineRule.runBlockingTest {

            //Given
            val resource = UserModel("1")
            `when`(repository.signInWithEmail(anyString(), anyString()))
                .thenReturn(AuthResource.Authenticated(resource))

            //When
            viewModel.signInWithEmail("email@test.com", "1212")

            delay(1000)

            //Then
            verify(usersObserver).onChanged(AuthResource.Loading)
            verify(usersObserver).onChanged(AuthResource.Authenticated(resource))
        }

}