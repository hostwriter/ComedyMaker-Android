package com.projektekis.gpt3comedymaker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

class MainActivityTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun onCreate() {

    }

    @Test
    fun checkList(){
        jokesListIsEmpty()
        jokesListHasNext()

    }

    private fun jokesListIsEmpty() {
        TODO("Not yet implemented")
    }

    private fun jokesListHasNext() {
        TODO("Not yet implemented")
    }

}