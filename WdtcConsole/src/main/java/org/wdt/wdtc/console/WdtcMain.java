package org.wdt.wdtc.console;

import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.wdt.wdtc.console.manger.CommandTaskManger;
import org.wdt.wdtc.core.auth.yggdrasil.AuthlibInjector;
import org.wdt.wdtc.core.manger.GameFileManger;
import org.wdt.wdtc.core.manger.TaskManger;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.util.Arrays;

public class WdtcMain {

  private static final Logger logmaker = WdtcLogger.getLogger(WdtcMain.class);

  static {
    TaskManger.ckeckVMConfig();
  }

  public static void main(String[] args) {
    try {
      TaskManger.runStartUpTask();
      AuthlibInjector.updateAuthlibInjector();
      GameFileManger.downloadVersionManifestJsonFileTask();
      Options options = new Options();
      logmaker.info("Args: " + Arrays.toString(args));
      CommandTaskManger commandTaskManger = new CommandTaskManger(options, args);
      commandTaskManger.startTask();
    } catch (Throwable e) {
      logmaker.error(WdtcLogger.getExceptionMessage(e));
      throw new RuntimeException(e);
    }
  }
}
