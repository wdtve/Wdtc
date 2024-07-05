@file:JvmName("StringUtils")

package org.wdt.wdtc.core.openapi.utils

import com.google.gson.GsonBuilder
import org.wdt.wdtc.core.openapi.utils.gson.defaultGsonBuilder

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

fun String.startendWith(start: String, end: String): Boolean {
	return this.startsWith(start) && this.endsWith(end)
}

fun Any.toJsonStringBuilder(builder: GsonBuilder = defaultGsonBuilder): String {
	let { any ->
		return buildString {
			append(any::class.java.simpleName).append("(").append(builder.create().toJson(any)).append(")")
		}
	}
}