package org.wdt.wdtc.core.utils

inline fun <T> T?.runIfNoNull(block: T.() -> Unit) {
	if (this != null) {
		block(this)
	}
}

fun <T> T?.noNull(): T {
	return requireNotNull(this)
}