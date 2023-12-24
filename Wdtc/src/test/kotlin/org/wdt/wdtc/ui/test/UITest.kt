package org.wdt.wdtc.ui.test

import org.wdt.wdtc.ui.window.setErrorWin
import java.io.IOException

fun main() {
  try {
    throw IOException()
  } catch (e: Throwable) {
    setErrorWin(e)
  }
}