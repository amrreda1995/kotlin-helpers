package com.kotlin.helpers.managers

import java.util.regex.Pattern

interface ValidatorInterface {
    fun isPasswordStrong(password: String): Boolean
    fun isEmailValid(email: String): Boolean
    fun isPhoneValid(thereIsPhoneCode: Boolean, phone: String): Boolean
}

class Validator : ValidatorInterface {
    override fun isPasswordStrong(password: String): Boolean {
        var hasLetter = false
        var hasDigit = false

        if (password.length >= 8) {
            for (i in 0 until password.length) {
                val x = password[i]
                if (Character.isLetter(x)) {
                    hasLetter = true
                } else if (Character.isDigit(x)) {
                    hasDigit = true
                }

                // no need to check further, break the loop
                if (hasLetter && hasDigit) {
                    break
                }
            }
            return hasLetter && hasDigit
        } else {
            return false
        }
    }

    override fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun isPhoneValid(thereIsPhoneCode: Boolean, phone: String): Boolean {
        val pattern = if (thereIsPhoneCode) {
            Pattern.compile("\\+[0-9]+")
        } else {
            Pattern.compile("[0-9]+")
        }

        return pattern.matcher(phone).matches()
    }
}