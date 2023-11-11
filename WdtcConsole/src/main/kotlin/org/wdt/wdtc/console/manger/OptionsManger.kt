package org.wdt.wdtc.console.manger

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.wdt.wdtc.console.utils.OptionUtils

object OptionsManger {
	val printVersionInfoOption: Option = OptionUtils.getOption("version", "v", "Print version number")
	val downloadGameOption = OptionUtils.getOption("download", "d", "Download a game version")
	val chooseVersionNumber =
		OptionUtils.getOption("chooseversion", "cv", true, "Choose a version to download(Need '-d' command)")

	fun addAllOptions(options: Options) {
		options.addOption(printVersionInfoOption)
		options.addOption(downloadGameOption)
		options.addOption(chooseVersionNumber)
	}
}