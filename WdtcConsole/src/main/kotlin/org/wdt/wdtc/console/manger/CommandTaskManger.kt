package org.wdt.wdtc.console.manger

import org.apache.commons.cli.*
import org.wdt.wdtc.console.options.DownloadGameOption
import org.wdt.wdtc.console.options.PrintVersionInfoOption

class CommandTaskManger(private val options: Options, private val args: Array<String>) {

  fun startTask() {
    val printInfo = PrintVersionInfoOption(options)
    val downloadGame = DownloadGameOption(options)
    if (args.isEmpty()) {
      printHelp()
    } else {
      val line = initCommandLine()
      if (line.hasOption(printInfo.option)) {
        printInfo.printInfo()
      } else {
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
