package com.capstone.pawcheck.utils

import android.util.Patterns

object ValidationUtils {
    private const val MIN_NAME_LENGTH = 3
    private const val MAX_NAME_LENGTH = 10
    private const val MIN_PASSWORD_LENGTH = 8

    fun isValidName(name: String): Boolean {
        return name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH &&
                name.matches(Regex("^[A-Za-z\\s]+$"))
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getPasswordValidationErrors(password: String): List<String> {
        val errors = mutableListOf<String>()

        if (password.length < MIN_PASSWORD_LENGTH) {
            errors.add("Password must be at least $MIN_PASSWORD_LENGTH characters")
        }

        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        if (!hasUpperCase) errors.add("Password must contain an uppercase letter")
        if (!hasLowerCase) errors.add("Password must contain a lowercase letter")
        if (!hasDigit) errors.add("Password must contain a number")

        return errors
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}