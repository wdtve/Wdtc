package org.wdt.wdtc.console.options

import org.wdt.wdtc.core.manger.VMManger

class PrintVersionInfoOption {

    fun printInfo() {
        println("Launcher Version: ${VMManger.launcherVersion}")
    }
}