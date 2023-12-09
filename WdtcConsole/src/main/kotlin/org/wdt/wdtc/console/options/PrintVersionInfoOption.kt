package org.wdt.wdtc.console.options

import org.wdt.wdtc.core.manger.launcherVersion

class PrintVersionInfoOption {

  fun printInfo() {
    println("Launcher Version: $launcherVersion")
  }
}