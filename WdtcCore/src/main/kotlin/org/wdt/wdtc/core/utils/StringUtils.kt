package org.wdt.wdtc.core.utils

import java.nio.charset.StandardCharsets
import java.util.*

object StringUtils {
    const val STRING_EMPTY = ""
    const val STRING_SPACE = " "

    @JvmStatic
    fun String.cleanStrInString(strs: String): String {
        return this.replace(strs, STRING_EMPTY)
    }

    @JvmStatic
    fun String.appendForString(vararg strings: Any?): String {
        val string = StringBuilder(this)
        for (str in strings) {
            string.append(str)
        }
        return string.toString()
    }

    @JvmStatic
    fun String.toBase64(): String {
        return Base64.getEncoder().encodeToString(this.toByteArray(StandardCharsets.UTF_8))
    }
}
