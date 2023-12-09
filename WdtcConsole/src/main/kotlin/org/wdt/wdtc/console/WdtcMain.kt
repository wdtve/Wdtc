package org.wdt.wdtc.console

import org.apache.commons.cli.Options
import org.wdt.wdtc.console.manger.CommandTaskManger
import org.wdt.wdtc.core.auth.yggdrasil.updateAuthlibInjector
import org.wdt.wdtc.core.manger.ckeckVMConfig
import org.wdt.wdtc.core.manger.downloadVersionManifestJsonFileTask
import org.wdt.wdtc.core.manger.runStartUpTask
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.logmaker

fun main(args: Array<String>) {
  try {
    ckeckVMConfig()

    runStartUpTask()
    updateAuthlibInjector()
    downloadVersionManifestJsonFileTask()

    val options = Options()
    logmaker.info("Args: " + args.contentToString())
    val commandTaskManger = CommandTaskManger(options, args)
    commandTaskManger.startTask()
  } catch (e: Throwable) {
    logmaker.error(e.getExceptionMessage())
    throw RuntimeException(e)
  }
}

