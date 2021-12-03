package com.sliide.challenge.utils

import com.sliide.challenge.ext.getTimeAgo
import org.junit.Test
import java.util.*

class TimeAgoTest {

    @Test
    fun `should return null for future date`() {
        //given

        //when
        val result = Date().addSeconds(10).getTimeAgo()
        //then
        assert(result == null)
    }

    @Test
    fun `should show just now label`() {
        //given

        //when
        val result = Date().getTimeAgo()
        //then
        assert(result == "just now")
    }

    @Test
    fun `should show seconds ago`() {
        //given

        //when
        val result = Date().minusSeconds(10).getTimeAgo()
        //then
        assert(result == "10 seconds ago")
    }

    @Test
    fun `should show minute ago`() {
        //given

        //when
        val result = Date().minusSeconds(90).getTimeAgo()
        //then
        assert(result == "a minute ago")
    }

    @Test
    fun `should show minutes ago`() {
        //given

        //when
        val result = Date().minusMinutes(2).getTimeAgo()
        //then
        assert(result == "2 minutes ago")
    }

    @Test
    fun `should show an hour ago`() {
        //given

        //when
        val result = Date().minusMinutes(80).getTimeAgo()
        //then
        assert(result == "an hour ago")
    }

    @Test
    fun `should show an hours ago`() {
        //given

        //when
        val result = Date().minusHours(4).getTimeAgo()
        //then
        assert(result == "4 hours ago")
    }

    @Test
    fun `should show yesterday`() {
        //given

        //when
        val result = Date().minusHours(25).getTimeAgo()
        //then
        assert(result == "yesterday")
    }

    @Test
    fun `should show days ago`() {
        //given

        //when
        val result = Date().minusDays(5).getTimeAgo()
        //then
        assert(result == "5 days ago")
    }
}

private fun Date.addSeconds(seconds: Long): Date {
    return Date(this.time+seconds*1000)
}

private fun Date.minusSeconds(seconds: Long): Date {
    return Date(this.time-seconds*1000)
}

private fun Date.minusMinutes(minutes: Long): Date {
    return Date(this.time-minutes*60*1000)
}

private fun Date.minusHours(hours: Long): Date {
    return Date(this.time-hours*60*60*1000)
}

private fun Date.minusDays(days: Long): Date {
    return Date(this.time-days*24*60*60*1000)
}

