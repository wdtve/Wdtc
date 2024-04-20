package org.wdt.wdtc.core.openapi.game

import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO Using a method of comparing release times
@Throws(NumberFormatException::class)
fun determineSize(originalVersionNumber: String, compareVersionNumbers: String): Boolean {
  val matchOriginalVersionNumber = originalVersionNumber.matchVersionNumber
  val matchVersionNumber = compareVersionNumbers.matchVersionNumber
  if (matchOriginalVersionNumber.find() && matchVersionNumber.find()) {
    val bigOriginal = matchOriginalVersionNumber.group(1).toInt()
    val bigVersionNumber = matchVersionNumber.group(1).toInt()
    if (bigOriginal != bigVersionNumber) {
      return bigOriginal < bigVersionNumber
    } else {
      val middleOriginal = matchOriginalVersionNumber.group(2).toInt()
      val middleVersionNumber = matchVersionNumber.group(2).toInt()
      return if (middleOriginal != middleVersionNumber) {
        middleOriginal < middleVersionNumber
      } else {
        matchOriginalVersionNumber.group(3).toInt() <= matchVersionNumber.group(3).toInt()
      }
    }
  }
  return false
}

private val String.matchVersionNumber: Matcher
  get() = Pattern.compile("(.)\\.(.)\\.?(.*)?").matcher(this)

