package com.kotlin.helpers.managers

import java.util.regex.Pattern

/**
 * @Validator helps you check if password is strong or not, if email address is valid or not or even
 * if phone number is valid or not
 * @Note that you can change the methods implementation according to your needs
 * */

interface ValidatorInterface {

    fun isPasswordStrong(password: String): Boolean
    fun isEmailValid(email: String): Boolean
    fun isPhoneValid(thereIsPhoneCode: Boolean, phone: String): Boolean
}

class Validator : ValidatorInterface {

    override fun isPasswordStrong(password: String): Boolean {
        return password.matches("^(?=.*[A-Za-z])(?=.*[0-9]).{8,}\$".toRegex())
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