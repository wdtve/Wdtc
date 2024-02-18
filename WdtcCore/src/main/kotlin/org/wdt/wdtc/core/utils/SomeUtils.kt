package org.wdt.wdtc.core.utils

inline fun <T> T?.runIfNoNull(block: T.() -> Unit) {
  if (this != null) {
    block(this)
  }
}

inline fun <reified T> T?.ckeckIsNull(): T {
  return this ?: throw NullPointerException("${T::class.simpleName} is null")
}

inline fun <T> Collection<T>.forEachWhenIsNotEmpty(action: (T) -> Unit) {
  if (isNotEmpty()) {
    for (element in this) action(element)
  }
}
