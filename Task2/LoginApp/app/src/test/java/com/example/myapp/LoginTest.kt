package com.example.myapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Local unit tests for the login check in MainActivity.kt
 */
class LoginTest {

    @Test
    fun correctCredentials() {
        assertTrue(isValidLogin("admin", "admin"))
    }

    @Test
    fun wrongPassword() {
        assertFalse(isValidLogin("admin", "1234"))
    }

    @Test
    fun wrongLogin() {
        assertFalse(isValidLogin("user", "admin"))
    }

    @Test
    fun emptyFields() {
        assertFalse(isValidLogin("", ""))
    }

    @Test
    fun isCaseSensitive() {
        assertFalse(isValidLogin("Admin", "admin"))
        assertFalse(isValidLogin("admin", "ADMIN"))
    }
}
