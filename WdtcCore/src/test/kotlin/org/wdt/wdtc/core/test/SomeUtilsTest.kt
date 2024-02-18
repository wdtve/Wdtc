package org.wdt.wdtc.core.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class SomeUtilsTest {
  @Test
  fun keepRun(): Unit = runBlocking {
    val job = launch(Dispatchers.IO) {
      while (false) {
      }
      println("hello")
    }
    launch {
      job.cancel()
    }
  }
}