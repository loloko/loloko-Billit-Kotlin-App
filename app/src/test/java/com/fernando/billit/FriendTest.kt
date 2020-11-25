package com.fernando.billit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fernando.billit.model.FriendModel
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.FriendRepository
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.ResultResource
import com.fernando.billit.util.TestCoroutineRule
import com.fernando.billit.viewmodel.FriendViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FriendTest {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var repository: FriendRepository

    private val mockedObserver = createObserver()
    private lateinit var viewModel: FriendViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FriendViewModel()
        viewModel.friendRepository = repository

        // Insert user into session
        viewModel.sessionManager = SessionManager()
        viewModel.sessionManager.authenticate(AuthResource.Authenticated(UserModel("12")))


        viewModel.friendResultObserver().observeForever(mockedObserver)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `given success state, when getAllFriends called`() {

        //Given
        val resource = ResultResource.Success<List<FriendModel>>(emptyList())
        coEvery { repository.getAllFriends(any()) } returns resource

        //When
        runBlocking { viewModel.getAllFriends() }

        //Then
        coVerifyOrder {
            mockedObserver.onChanged(ResultResource.Loading)
            mockedObserver.onChanged(resource)
        }
    }

    @Test
    fun `given error state, when getAllFriends called`() {

        //Given
        val resource = ResultResource.Error(R.string.no_friend_found)
        coEvery { repository.getAllFriends(any()) } returns resource

        //When
        runBlocking { viewModel.getAllFriends() }

        //Then
        coVerifyOrder {
            mockedObserver.onChanged(ResultResource.Loading)

            mockedObserver.onChanged(resource)

//            val state = viewModel.friendResultObserver().value
//            assertThat((state as ResultResource.Error).msg).isEqualTo(resource.msg)
        }
    }

    @Test
    fun `given error state for empty name, when addFriend called`() {

        //Given
        val message = R.string.required_name

        //When
        runBlocking { viewModel.addFriend(FriendModel("12", "", 0.0)) }

        //Then
        val slot = slot<ResultResource<List<FriendModel>>>()
        verify { mockedObserver.onChanged(capture(slot)) }

        assertThat((slot.captured as ResultResource.Error).msg).isEqualTo(message)
    }


    @Test
    fun `given success state, when addFriend called`() {
        //Given
        val resource = ResultResource.Success<List<FriendModel>>(emptyList())
        coEvery { repository.getAllFriends(any()) } returns resource
        coEvery { repository.insertFriendDatabase(any(), any()) } returns true

        //When
        runBlocking { viewModel.addFriend(FriendModel("12", "Name Test", 0.0)) }

        //Then
        coVerifyOrder {
            mockedObserver.onChanged(ResultResource.Loading)
            mockedObserver.onChanged(resource)
        }
    }

    @Test
    fun `given error state, when addFriend called`() {

        //Given
        val resource = ResultResource.Error(R.string.error_add_friend)
        coEvery { repository.insertFriendDatabase(any(), any()) } returns false

        //When
        runBlocking { viewModel.addFriend(FriendModel("12", "Name Test", 0.0)) }

        //Then
        coVerifyOrder {
            mockedObserver.onChanged(ResultResource.Loading)
            mockedObserver.onChanged(resource)
        }
    }

    @Test
    fun `given success state, when deleteFriend called`() {
        //Given
        val resource = ResultResource.Success<List<FriendModel>>(emptyList())
        coEvery { repository.getAllFriends(any()) } returns resource
        coEvery { repository.deleteFriend(any(), any()) } returns true

        //When
        runBlocking { viewModel.deleteFriend("friendID") }

        //Then
        coVerifyOrder {
            mockedObserver.onChanged(ResultResource.Loading)
            mockedObserver.onChanged(resource)
        }
    }

    @Test
    fun `given error state, when deleteFriend called`() {

        //Given
        val resource = ResultResource.Error(R.string.error_delete_friend)
        coEvery { repository.deleteFriend(any(), any()) } returns false

        //When
        runBlocking { viewModel.deleteFriend("friendID") }

        //Then
        coVerifyOrder {
            mockedObserver.onChanged(ResultResource.Loading)
            mockedObserver.onChanged(resource)
        }
    }

    private fun createObserver(): Observer<ResultResource<List<FriendModel>>> =
        spyk(Observer { })
}