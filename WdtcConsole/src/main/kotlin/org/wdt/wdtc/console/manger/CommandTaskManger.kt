package org.wdt.wdtc.console.manger

import org.apache.commons.cli.*
import org.wdt.wdtc.console.options.DownloadGameOption
import org.wdt.wdtc.console.options.PrintVersionInfoOption
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage

class CommandTaskManger(private val options: Options, private val args: Array<String>) {

    fun startTask() {
        OptionsManger.addAllOptions(options)
        val printInfo = PrintVersionInfoOption()
        val downloadGame = DownloadGameOption()
        if (args.isEmpty()) {
            printHelp()
        } else {
            try {
                val line = initCommandLine()
                if (line.hasOption(OptionsManger.printVersionInfoOption)) {
                    printInfo.printInfo()
                }
                if (line.hasOption(OptionsManger.downloadGameOption)) {
                    downloadGame.donwloadGame(line)
                }
            } catch (e: ParseException) {
                println(e.getExceptionMessage())
                printHelp()
            }
        }
    }

    @Throws(ParseException::class)
    fun initCommandLine(): CommandLine {
        return DefaultParser().parse(options, args)
    }

    private fun printHelp() {
        val helps = HelpFormatter()
        helps.printHelp("-(commnad) (args)", options)
    }
}
