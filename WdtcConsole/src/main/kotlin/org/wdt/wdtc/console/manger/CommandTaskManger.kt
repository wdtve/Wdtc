package org.wdt.wdtc.console.manger

import org.apache.commons.cli.*
import org.wdt.wdtc.console.options.PrintVersionInfo

class CommandTaskManger(private val options: Options, private val args: Array<String>) {

    fun startTask() {
        val printInfo = PrintVersionInfo(options)
        val helps = HelpFormatter()
        if (args.isEmpty()) {
            helps.printHelp("-(commnad) (args)", options)
        }
        val line = initCommandLine()
        if (line.hasOption(printInfo.option)) {
            printInfo.printInfo()
        }
    }


    @Throws(ParseException::class)
    fun initCommandLine(): CommandLine {
        return DefaultParser().parse(options, args)
    }
}
