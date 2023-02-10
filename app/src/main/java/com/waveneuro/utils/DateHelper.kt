package com.waveneuro.utils

import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object DateHelper {

    private const val BIRTHDAY_PATTERN = "MM/dd/yyyy"
    private const val PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz"

    private var birthdayFormatter = SimpleDateFormat(BIRTHDAY_PATTERN, Locale.getDefault())
    private var dateFormatter = SimpleDateFormat(PATTERN_RFC1123, Locale.getDefault())

    fun birthdayFormat(date: Date?): String =
        date?.let { birthdayFormatter.format(it) } ?: ""

    fun getBirthdayDate(birthday: String?): Date? = try {
            dateFormatter.parse(birthday ?: "")
        } catch (e: Exception) {
            null
        }

    fun birthdayFormat(stringDate: String?): String =
        getDate(stringDate)?.let { birthdayFormatter.format(it) } ?: ""

    private fun getDate(stringDate: String?): Date? = try {
        dateFormatter.parse(stringDate ?: "")
    } catch (e: Exception) {
        Timber.e("e = $e")
        null
    }
}