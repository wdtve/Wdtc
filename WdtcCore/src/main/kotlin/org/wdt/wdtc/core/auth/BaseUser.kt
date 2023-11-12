package org.wdt.wdtc.core.auth

import java.io.IOException

abstract class BaseUser {
  @get:Throws(IOException::class)
  abstract val user: User
}
