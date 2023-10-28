package org.wdt.wdtc.console.options

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.wdt.wdtc.console.utils.OptionUtils
import org.wdt.wdtc.core.manger.VMManger

class PrintVersionInfo(options: Options) {
    val option: Option = OptionUtils.getOption("version", "v", "Print Version Number")

    init {
        options.addOption(option)
    }

    fun printInfo() {
        println("Launcher Version: ${VMManger.getLauncherVersion()}")
    }
}