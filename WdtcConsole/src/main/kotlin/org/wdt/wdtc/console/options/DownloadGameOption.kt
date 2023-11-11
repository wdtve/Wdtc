package org.wdt.wdtc.console.options

import org.apache.commons.cli.CommandLine
import org.wdt.wdtc.console.manger.OptionsManger.chooseVersionNumber
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.download.game.GameVersionList
import org.wdt.wdtc.core.download.game.GameVersionList.GameVersionJsonObjectImpl
import org.wdt.wdtc.core.download.infterface.TextInterface
import org.wdt.wdtc.core.game.Launcher
import java.util.*

class DownloadGameOption {

    fun donwloadGame(commandLine: CommandLine) {
        var launcher: Launcher? = null;
        if (commandLine.hasOption(chooseVersionNumber)) {
            launcher = Launcher(commandLine.getOptionValue(chooseVersionNumber))

        } else {
            val versionList = GameVersionList().versionList
            for (i in 0 until versionList.size) {
                val versionObejct = versionList[i] as GameVersionJsonObjectImpl
                println("$i. Version: ${versionObejct.versionNumber}, Type: ${versionObejct.gameType}")
            }
            val input = Scanner(System.`in`)
            print("Choose a number:")
            if (input.hasNext()) {
                val index = input.nextInt()
                launcher = Launcher(versionList[index].versionNumber)
            }
        }
        val installGameVersion = InstallGameVersion(launcher!!, true)
        installGameVersion.setTextFieldText = TextInterface { string -> println(string) }
        installGameVersion.startInstallGame()
    }
}