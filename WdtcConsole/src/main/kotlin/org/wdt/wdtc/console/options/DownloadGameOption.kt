package org.wdt.wdtc.console.options

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.wdt.wdtc.console.utils.OptionUtils
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.game.Launcher

class DownloadGameOption(options: Options) {
    val downloadGameOption = OptionUtils.getOption("d", "download", "Download a game version")
    val chooseVersionNumber =
        OptionUtils.getOption("cv", "chooseversion", true, "Choose a version to download(Need '-d' command)")

    init {
        options.addOption(chooseVersionNumber)
        options.addOption(downloadGameOption)
    }

    fun donwloadGame(commandLine: CommandLine) {
        if (commandLine.hasOption(chooseVersionNumber)) {
            val launcher = Launcher(commandLine.getOptionValue(chooseVersionNumber))
            val installGameVersion = InstallGameVersion(launcher, true)
            installGameVersion.setSetTextFieldText { string -> println(string) }
            installGameVersion.InstallGame()
        }
    }
}