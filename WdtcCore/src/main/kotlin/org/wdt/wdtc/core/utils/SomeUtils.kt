package org.wdt.wdtc.core.utils

inline fun <T> T?.runIfNoNull(block: T.() -> Unit) {
  if (this != null) {
    block(this)
  }
}

inline fun <reified T> T?.noNull(): T {
  return this ?: throw NullPointerException("${T::class.simpleName} is null")
}

