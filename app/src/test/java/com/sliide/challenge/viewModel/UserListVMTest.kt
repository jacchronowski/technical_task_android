package com.sliide.challenge.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sliide.challenge.R
import com.sliide.challenge.feature.userList.repository.AddUserResult
import com.sliide.challenge.feature.userList.repository.UserListRepository
import com.sliide.challenge.feature.userList.repository.UserListResult
import com.sliide.challenge.feature.userList.repository.UserResult
import com.sliide.challenge.feature.userList.utils.state
import com.sliide.challenge.feature.userList.utils.stateAddedItem
import com.sliide.challenge.feature.userList.utils.stateRemovedItem
import com.sliide.challenge.feature.userList.viewModel.UserListVM
import com.sliide.challenge.feature.userList.viewModel.UserListVMImpl
import com.sliide.challenge.model.responses.Gender
import com.sliide.challenge.model.responses.Status
import com.sliide.challenge.model.responses.User
import com.sliide.challenge.model.state.UserListUIState
import com.sliide.challenge.model.ui.UserUI
import com.sliide.challenge.ext.MainCoroutineRule
import com.sliide.challenge.ext.isEmailValid
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserListVMTest {

    @get:Rule
    val rule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var sut: UserListVM

    private val repository = mockk<UserListRepository>(relaxed = true)
    private val addUserDialogObserver = mockk<Observer<Unit>>(relaxed = true)
    private val removeUserDialogObserver = mockk<Observer<Long>>(relaxed = true)
    private val listStateObserver = mockk<Observer<UserListUIState>>(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic(STATE_EXTENSIONS)
        mockkStatic(VALIDATION_EXTENSIONS)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should fetch users on init`() {
        //given
        mockSuccessfulUsersResponse()
        createSut()

        //when

        //then
        verify { listStateObserver.onChanged(SAMPLE_USER_LIST_UI_STATE) }
    }

    @Test
    fun `should show error after fetch users failure`() {
        //given
        mockUnsuccessfulUsersResponse()
        createSut()

        //when

        //then
        verify { listStateObserver.onChanged(SAMPLE_ERROR_UI_STATE) }
    }

    @Test
    fun `should show add user dialog`() {
        //given
        createSut()

        //when
        sut.onAddUserClick()

        //then
        verify { addUserDialogObserver.onChanged(null) }
    }

    @Test
    fun `should show confirm user removing dialog`() {
        //given
        createSut()

        //when
        sut.onRemoveUserClick(SAMPLE_ID)

        //then
        verify { removeUserDialogObserver.onChanged(SAMPLE_ID) }
    }

    @Test
    fun `should remove user with given id after confirmation`() = runBlockingTest{
        //given
        mockSuccessfulUsersResponse()
        mockSuccessfulRemoveUserResponse()
        createSut()

        //when
        sut.onRemoveUserConfirmClick(SAMPLE_ID)

        //then
        verify { listStateObserver.onChanged(UserListUIState.DataItems(listOf())) }
    }

    @Test
    fun `should add user with given data after confirmation`() = runBlockingTest{
        //given
        mockSuccessfulUsersResponse()
        mockSuccessfulAddUserResponse()
        mockEmailValid()
        createSut()

        //when
        sut.onAddUserConfirmClick(SAMPLE_NAME, SAMPLE_EMAIL)

        //then
        verify { listStateObserver.onChanged(SAMPLE_USER_LIST_UI_STATE_AFTER_ADD) }
    }

    private fun createSut() {
        sut = UserListVMImpl(repository)
        sut.listState.observeForever(listStateObserver)
        sut.showAddUserDialogEvent.observeForever(addUserDialogObserver)
        sut.showRemoveUserDialogEvent.observeForever(removeUserDialogObserver)
    }

    private fun mockEmailValid() {
        every { SAMPLE_EMAIL.isEmailValid() } returns true
    }

    private fun mockSuccessfulUsersResponse() {
        coEvery { repository.getUsersList() } returns SAMPLE_SUCCESS_USER_LIST_RESULT
        coEvery { SAMPLE_SUCCESS_USER_LIST_RESULT.state(any()) } returns SAMPLE_USER_LIST_UI_STATE
    }

    private fun mockSuccessfulRemoveUserResponse() {
        coEvery { repository.removeUser(SAMPLE_ID) } returns SAMPLE_SUCCESS_USER_RESULT
        coEvery { SAMPLE_SUCCESS_USER_RESULT.stateRemovedItem(any()) } returns SAMPLE_USER_LIST_UI_STATE_AFTER_REMOVE
    }

    private fun mockSuccessfulAddUserResponse() {
        coEvery { repository.addUser(SAMPLE_NAME, SAMPLE_EMAIL) } returns SAMPLE_SUCCESS_ADD_USER_RESULT
        coEvery { SAMPLE_SUCCESS_ADD_USER_RESULT.stateAddedItem(any()) } returns SAMPLE_USER_LIST_UI_STATE_AFTER_ADD
    }

    private fun mockUnsuccessfulUsersResponse() {
        coEvery { repository.getUsersList() } returns SAMPLE_ERROR_USER_LIST_RESULT
        coEvery { SAMPLE_SUCCESS_USER_LIST_RESULT.state(any()) } returns SAMPLE_ERROR_UI_STATE
    }

    companion object {

        private const val STATE_EXTENSIONS =
            "com.sliide.challenge.feature.userList.utils.UserListUiStateExtKt"

        private const val VALIDATION_EXTENSIONS =
            "com.sliide.challenge.ext.ValidationExtKt"

        private const val SAMPLE_ID = 1L
        private const val SAMPLE_ID_2 = 2L

        private const val SAMPLE_NAME = "Sample Name"
        private const val SAMPLE_EMAIL = "Sample Email"
        private val SAMPLE_GENDER = Gender.FEMALE
        private val SAMPLE_STATUS = Status.ACTIVE

        private val SAMPLE_USER_LIST = listOf(
            User(
                id = SAMPLE_ID,
                name = SAMPLE_NAME,
                email = SAMPLE_EMAIL,
                gender = SAMPLE_GENDER,
                status = SAMPLE_STATUS,
            )
        )

        private val SAMPLE_SUCCESS_USER_LIST_RESULT = UserListResult.Success(SAMPLE_USER_LIST)
        private val SAMPLE_ERROR_USER_LIST_RESULT = UserListResult.Error(R.string.error_user_list)

        private val SAMPLE_USER_UI_LIST = listOf(
            UserUI(
                id = SAMPLE_ID,
                name = SAMPLE_NAME,
                email = SAMPLE_EMAIL,
                gender = SAMPLE_GENDER,
                status = SAMPLE_STATUS
            )
        )
        private val SAMPLE_USER_LIST_UI_STATE = UserListUIState.DataItems(SAMPLE_USER_UI_LIST)
        private val SAMPLE_ERROR_UI_STATE = UserListUIState.Error(R.string.error_user_list)

        private val SAMPLE_SUCCESS_USER_RESULT = UserResult.Success(SAMPLE_ID)
        private val SAMPLE_USER_LIST_UI_STATE_AFTER_REMOVE = UserListUIState.DataItems(listOf())

        private val SAMPLE_SUCCESS_ADD_USER_RESULT = AddUserResult.Success(
            User(
                id = SAMPLE_ID_2,
                name = SAMPLE_NAME,
                email = SAMPLE_EMAIL,
                gender = SAMPLE_GENDER,
                status = SAMPLE_STATUS
            )
        )

        private val SAMPLE_USER_LIST_UI_STATE_AFTER_ADD = UserListUIState.DataItems(listOf(
            UserUI(
                id = SAMPLE_ID,
                name = SAMPLE_NAME,
                email = SAMPLE_EMAIL,
                gender = SAMPLE_GENDER,
                status = SAMPLE_STATUS
            ),
            UserUI(
                id = SAMPLE_ID_2,
                name = SAMPLE_NAME,
                email = SAMPLE_EMAIL,
                gender = SAMPLE_GENDER,
                status = SAMPLE_STATUS
            )
        ))
    }
}
