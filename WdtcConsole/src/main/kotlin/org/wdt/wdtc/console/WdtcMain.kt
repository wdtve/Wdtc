package org.wdt.wdtc.console

import org.apache.commons.cli.Options
import org.wdt.wdtc.console.manger.CommandTaskManger
import org.wdt.wdtc.core.auth.yggdrasil.AuthlibInjector
import org.wdt.wdtc.core.manger.GameFileManger
import org.wdt.wdtc.core.manger.TaskManger
import org.wdt.wdtc.core.utils.WdtcLogger
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage

object WdtcMain {
  private val logmaker = WdtcLogger.getLogger()

  init {
    TaskManger.ckeckVMConfig()
  }

  @JvmStatic
  fun main(args: Array<String>) {
    try {
      TaskManger.runStartUpTask()
      AuthlibInjector.updateAuthlibInjector()
      GameFileManger.downloadVersionManifestJsonFileTask()
      val options = Options()
      logmaker.info("Args: " + args.contentToString())
      val commandTaskManger = CommandTaskManger(options, args)
      commandTaskManger.startTask()
    } catch (e: Throwable) {
      logmaker.error(e.getExceptionMessage())
      throw RuntimeException(e)
    }
  }
}
