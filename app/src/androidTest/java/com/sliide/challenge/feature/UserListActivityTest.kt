package com.sliide.challenge.feature

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.sliide.challenge.R
import com.sliide.challenge.actions.RecyclerViewActions.Companion.atListPosition
import com.sliide.challenge.di.TestServiceLocator
import com.sliide.challenge.feature.userList.UserListActivity
import com.sliide.challenge.feature.userList.UserViewHolder
import com.sliide.challenge.feature.userList.repository.AddUserResult
import com.sliide.challenge.feature.userList.repository.UserListRepository
import com.sliide.challenge.feature.userList.repository.UserListResult
import com.sliide.challenge.feature.userList.repository.UserResult
import com.sliide.challenge.matchers.RecyclerViewMatchers.Companion.withItemCount
import com.sliide.challenge.matchers.RecyclerViewMatchers.Companion.withListPosition
import com.sliide.challenge.model.responses.Gender
import com.sliide.challenge.model.responses.Status
import com.sliide.challenge.model.responses.User
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Test

class UserListActivityTest {

    lateinit var scenario: ActivityScenario<UserListActivity>

    @After
    fun tearDown() {
        scenario.close()
        TestServiceLocator.clear()
    }

    @Test
    fun testRecyclerItemCount() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()

        //then
        onView(withId(R.id.user_list_rv)).check(matches(withItemCount(SAMPLE_USER_LIST.size)))
    }

    @Test
    fun testCorrectNameDisplaying() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()

        //then
        onView(withId(R.id.user_list_rv)).check(
            matches(
                withListPosition(
                    0, R.id.user_name_tv,
                    withText(SAMPLE_NAME_1)
                )
            )
        )
    }

    @Test
    fun testCorrectEmailDisplaying() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()

        //then
        onView(withId(R.id.user_list_rv)).check(
            matches(
                withListPosition(
                    1, R.id.user_email_tv,
                    withText(SAMPLE_EMAIL_2)
                )
            )
        )
    }

    @Test
    fun testShowConfirmationOnLongClick() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()
        onView(withId(R.id.user_list_rv)).perform(atListPosition(0, longClick()))

        //then
        onView(withText(R.string.user_removing_confirmation)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun testRemoveItemFromList() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()
        onView(withId(R.id.user_list_rv)).perform(actionOnItemAtPosition<UserViewHolder>(0, longClick()))
        onView(withText(R.string.label_yes))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        //then
        onView(withId(R.id.user_list_rv)).check(matches(withItemCount(SAMPLE_USER_LIST.size - 1)))
    }

    @Test
    fun testRemoveItemFromListError() {
        //given
        mockUnsuccessfulRemoveUserResponse()

        //when
        launchUserListActivity()
        onView(withId(R.id.user_list_rv)).perform(actionOnItemAtPosition<UserViewHolder>(0, longClick()))
        onView(withText(R.string.label_yes))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        //then
        onView(withText(R.string.error_user_remove)).check(matches(isDisplayed()))
    }

    @Test
    fun testShowAddUserDialog() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()
        onView(withId(R.id.add_user_fab)).perform(click())

        //then
        onView(withText(R.string.add_user_description)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun testAddUserToList() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()
        onView(withId(R.id.add_user_fab)).perform(click())
        onView(withId(R.id.name_tiet)).perform(typeText("jacek"))
        onView(withId(R.id.email_tiet)).perform(typeText("jacek.chronowski@gmail.com"))
        onView(withText(R.string.label_add_user))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        //then
        onView(withId(R.id.user_list_rv)).check(matches(withItemCount(SAMPLE_USER_LIST.size + 1)))
    }

    @Test
    fun testAddUserDialogDismiss() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()
        onView(withId(R.id.add_user_fab)).perform(click())
        onView(withText(R.string.label_cancel))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        //then
        onView(withText(R.string.add_user_description)).check(doesNotExist())
    }

    @Test
    fun testRemoveUserDialogDismiss() {
        //given
        mockSuccessfulResponse()

        //when
        launchUserListActivity()
        onView(withId(R.id.user_list_rv)).perform(actionOnItemAtPosition<UserViewHolder>(0, longClick()))
        onView(withText(R.string.label_no))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        //then
        onView(withText(R.string.user_removing_confirmation)).check(doesNotExist())
    }

    private fun mockSuccessfulResponse() {
        TestServiceLocator.userListRepository = object : UserListRepository {
            override suspend fun getUsersList(): UserListResult {
                return UserListResult.Success(SAMPLE_USER_LIST)
            }

            override suspend fun removeUser(userId: Long): UserResult {
                return UserResult.Success(SAMPLE_ID_2)
            }

            override suspend fun addUser(name: String, email: String): AddUserResult {
                return AddUserResult.Success(SAMPLE_USER)
            }

        }
    }

    private fun mockUnsuccessfulRemoveUserResponse() {
        TestServiceLocator.userListRepository = object : UserListRepository {
            override suspend fun getUsersList(): UserListResult {
                return UserListResult.Success(SAMPLE_USER_LIST)
            }

            override suspend fun removeUser(userId: Long): UserResult {
                return UserResult.Error(R.string.error_user_remove)
            }

            override suspend fun addUser(name: String, email: String): AddUserResult {
                return AddUserResult.Success(SAMPLE_USER)
            }
        }
    }

    private fun launchUserListActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), UserListActivity::class.java)
        scenario = ActivityScenario.launch(intent)
    }

    companion object {

        private const val SAMPLE_ID_1 = 1L
        private const val SAMPLE_NAME_1 = "Sample Name1"
        private const val SAMPLE_EMAIL_1 = "Sample Email1"
        private val SAMPLE_GENDER_1 = Gender.FEMALE
        private val SAMPLE_STATUS_1 = Status.ACTIVE

        private const val SAMPLE_ID_2 = 2L
        private const val SAMPLE_NAME_2 = "Sample Name2"
        private const val SAMPLE_EMAIL_2 = "Sample Email2"
        private val SAMPLE_GENDER_2 = Gender.MALE
        private val SAMPLE_STATUS_2 = Status.INACTIVE

        private const val SAMPLE_ID_3 = 3L
        private const val SAMPLE_NAME_3 = "Sample Name3"
        private const val SAMPLE_EMAIL_3 = "Sample Email3"
        private val SAMPLE_GENDER_3 = Gender.MALE
        private val SAMPLE_STATUS_3 = Status.INACTIVE

        private val SAMPLE_USER = User(
            id = SAMPLE_ID_3,
            name = SAMPLE_NAME_3,
            email = SAMPLE_EMAIL_3,
            gender = SAMPLE_GENDER_3,
            status = SAMPLE_STATUS_3
        )

        val SAMPLE_USER_LIST = listOf(
            User(
                id = SAMPLE_ID_1,
                name = SAMPLE_NAME_1,
                email = SAMPLE_EMAIL_1,
                gender = SAMPLE_GENDER_1,
                status = SAMPLE_STATUS_1
            ),
            User(
                id = SAMPLE_ID_2,
                name = SAMPLE_NAME_2,
                email = SAMPLE_EMAIL_2,
                gender = SAMPLE_GENDER_2,
                status = SAMPLE_STATUS_2
            )
        )
    }
}