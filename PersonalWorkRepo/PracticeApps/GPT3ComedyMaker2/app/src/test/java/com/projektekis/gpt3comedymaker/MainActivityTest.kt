package com.projektekis.gpt3comedymaker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule



class MainActivityTest {
    var testAct = MainActivity()
    val emptyList = arrayListOf<String>()
    var testList = arrayListOf("joke1", "joke2", "joke3")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Test
    fun onCreate() {
        //Assert main activity is the view
    }

    @Test
    fun checkButtons(){
        upvoteButtonWorking()
        downvoteButtonWorking()

    }

    private fun downvoteButtonWorking() {
        //TODO("Not yet implemented")
    }

    private fun upvoteButtonWorking() {
        TODO("Not yet implemented")
    }

    @Test
    fun checkList(){
        jokesListIsEmpty()
        //jokesListHasNext()
    }

    private fun jokesListIsEmpty() {
        //TODO("Not yet implemented")
        Assert.assertFalse(testAct.talkToApi(emptyList))
    }

    private fun jokesListHasNext() {
        //TODO("Not yet implemented")

    }

    @Test
    fun checkTextView(){
        emptyJokeText()
        textIsTooLong()
    }

    private fun emptyJokeText() {
        //TODO("Not yet implemented")
    }

    private fun textIsTooLong() {
        //TODO("Not yet implemented")
    }

    @Test
    fun checkApiCommunication(){
        noDataRetrieved()
        apiDown()
    }

    private fun noDataRetrieved() {
        //TODO("Not yet implemented")
    }

    private fun apiDown() {
        //TODO("Not yet implemented")
    }

    @Test
    fun checkIterator(){
        iteratorHasNext()
        listIsEmpty()

    }

    private fun iteratorHasNext() {
        val testIter = testAct.setJokeIterator(testList)
        Assert.assertEquals("joke1", testIter.next())
        Assert.assertEquals("joke2", testIter.next())
    }


    private fun listIsEmpty() {
        val testIter = testAct.setJokeIterator(emptyList)
        val defaultIter = testAct.defaultList.iterator()
        Assert.assertEquals(defaultIter.next(), testIter.next())
        Assert.assertEquals(defaultIter.next(), testIter.next())

    }
}