package org.wdt.wdtc.console.options

import org.apache.commons.cli.CommandLine
import org.wdt.wdtc.console.manger.OptionsManger.chooseVersionNumber
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.download.game.GameVersionList
import org.wdt.wdtc.core.download.game.GameVersionList.GameVersionJsonObjectImpl
import org.wdt.wdtc.core.game.Launcher
import java.util.*
import kotlin.streams.toList

class DownloadGameOption {

  fun donwloadGame(commandLine: CommandLine) {
    lateinit var launcher: Launcher
    if (commandLine.hasOption(chooseVersionNumber)) {
      launcher = Launcher(commandLine.getOptionValue(chooseVersionNumber))

    } else {
      val versionList = GameVersionList().versionList.stream().toList()
      for (i in versionList.indices) {
        val versionObject = versionList as GameVersionJsonObjectImpl
        println("$i. Version: ${versionObject.versionNumber}, Type: ${versionObject.gameType}")
      }
      val input = Scanner(System.`in`)
      print("Choose a number:")
      if (input.hasNext()) {
        val index = input.nextInt()
        launcher = Launcher(versionList[index].versionNumber!!)
      }
    }
    val installGameVersion = InstallGameVersion(launcher, true) {
      println(it)
    }
    installGameVersion.startInstallGame()
  }
}