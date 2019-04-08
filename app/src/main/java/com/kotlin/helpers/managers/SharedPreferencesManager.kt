package com.kotlin.helpers.managers

import android.content.Context
import android.content.SharedPreferences

interface SharedPreferencesManagerInterface {
    fun putInt(map: HashMap<String, Int>)
    fun getInt(key: String): Int?

    fun putString(map: HashMap<String, String>)
    fun getString(key: String): String?

    fun putFloat(map: HashMap<String, Float>)
    fun getFloat(key: String): Float?

    fun putLong(map: HashMap<String, Long>)
    fun getLong(key: String): Long?

    fun putBoolean(key: String, checked: Boolean)
    fun getBoolean(key: String): Boolean?

    fun putStringSet(key: String, values: MutableSet<String>?)
    fun getStringSet(key: String): MutableSet<String>?
}

class SharedPreferencesManager(context: Context) : SharedPreferencesManagerInterface {

    private var sharedPreferences: SharedPreferences? =
        context.getSharedPreferences("preferencesData", Context.MODE_PRIVATE)

    private val editor = sharedPreferences?.edit()

    override fun putFloat(map: HashMap<String, Float>) {
        map.forEach { editor?.putFloat(it.key, it.value) }

        editor?.apply()
    }

    override fun getFloat(key: String): Float? {
        return sharedPreferences?.getFloat(key, 0f)
    }

    override fun putLong(map: HashMap<String, Long>) {
        map.forEach { editor?.putLong(it.key, it.value) }

        editor?.apply()
    }

    override fun getLong(key: String): Long? {
        return sharedPreferences?.getLong(key, 1L)
    }

    override fun putBoolean(key: String, checked: Boolean) {
        editor?.putBoolean(key, checked)
        editor?.apply()
    }

    override fun getBoolean(key: String): Boolean? {
        return sharedPreferences?.getBoolean(key, false)
    }

    override fun putStringSet(key: String, values: MutableSet<String>?) {
        editor?.putStringSet(key, values)
        editor?.apply()
    }

    override fun getStringSet(key: String): MutableSet<String>? {
        return sharedPreferences?.getStringSet(key, null)
    }

    override fun putInt(map: HashMap<String, Int>) {
        map.forEach { editor?.putInt(it.key, it.value) }

        editor?.apply()
    }

    override fun getInt(key: String): Int? {
        return sharedPreferences?.getInt(key, -1)
    }

    override fun putString(map: HashMap<String, String>) {
        map.forEach { editor?.putString(it.key, it.value) }

        editor?.apply()
    }

    override fun getString(key: String): String? {
        return sharedPreferences?.getString(key, null)
    }
}