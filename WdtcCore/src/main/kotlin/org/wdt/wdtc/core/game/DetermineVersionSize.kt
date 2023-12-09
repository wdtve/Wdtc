package org.wdt.wdtc.core.game

import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO Using a method of comparing release times
fun determineSize(originalVersionNumber: String, launcher: Launcher): Boolean {
  val originalVersion = getVersionNumber(originalVersionNumber)
  val version = getVersionNumber(launcher.versionNumber)
  if (originalVersion.find() && version.find()) {
    val original = originalVersion.group(1).toInt()
    val target = version.group(1).toInt()
    return if (original == target) {
      if (originalVersion.group(2) != null && version.group(2) != null) {
        val originalMinorVersionNumber = originalVersion.group(2).toInt()
        val minorVersionNumber = version.group(2).toInt()
        if (originalMinorVersionNumber == minorVersionNumber) true else originalMinorVersionNumber < minorVersionNumber
      } else if (originalVersion.group(2) != null) {
        false
      } else if (version.group(2) != null) {
        true
      } else {
        true
      }
    } else original < target
  }
  return false
}

private fun getVersionNumber(str: String): Matcher {
  return Pattern.compile("1\\.([0-9][0-9])\\.?([0-9])?").matcher(str)
}

