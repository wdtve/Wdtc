package org.wdt.wdtc.console.utils

import org.apache.commons.cli.Option

object OptionUtils {
    fun getOption(longAge: String?, arg: String?, hasArgs: Boolean, desc: String?): Option {
        return Option.builder(arg).longOpt(longAge).hasArg(hasArgs).desc(desc).required(false).build()
    }

    fun getOption(longAge: String?, arg: String?, desc: String?): Option {
        return getOption(longAge, arg, false, desc)
    }

    fun getOption(longAge: String?, arg: String?): Option {
        return getOption(longAge, arg, false, "No Description")
    }

    fun getOption(longAge: String?, arg: String?, hasArg: Boolean): Option {
        return getOption(longAge, arg, hasArg, "No Description")
    }
}
