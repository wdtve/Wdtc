@file:JvmName("StringUtils")

package org.wdt.wdtc.core.utils

import java.nio.charset.StandardCharsets
import java.util.*

const val STRING_EMPTY = ""
const val STRING_SPACE = " "

fun String.cleanStrInString(strs: String): String {
  return this.replace(strs, STRING_EMPTY)
}

fun String.appendForString(vararg strings: Any?): String {
  return buildString {
    append(this@appendForString)
    strings.forEach {
      append(it)
    }
  }
}

fun String.toBase64(): String {
  return Base64.getEncoder().encodeToString(toByteArray(StandardCharsets.UTF_8))
}

fun String.startendWith(start: String, end: String): Boolean {
  return this.startsWith(start) && this.endsWith(end)
}