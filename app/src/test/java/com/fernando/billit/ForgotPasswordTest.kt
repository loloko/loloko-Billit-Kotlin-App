package com.fernando.billit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.ForgotPasswordViewModel
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