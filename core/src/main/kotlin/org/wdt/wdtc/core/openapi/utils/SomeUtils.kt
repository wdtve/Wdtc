package org.wdt.wdtc.core.openapi.utils

inline fun <reified T> T?.noNull(): T {
	return requireNotNull(this)
}

inline fun <T> T.runWhen(block: T.() -> Boolean, task: T.() -> Unit) {
	if (block()) {
		task()
	}
}