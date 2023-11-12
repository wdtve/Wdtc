package org.wdt.wdtc.core.launch;

import lombok.Setter;
import org.apache.log4j.Logger;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.download.infterface.TextInterface;
import org.wdt.wdtc.core.utils.ThreadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
public class LaunchProcess {
  private static final Logger logmaker = WdtcLogger.getLogger(LaunchProcess.class);
  private final Process process;
  private TextInterface setUIText;

  public LaunchProcess(Process process) {
    this.process = process;
  }

  public void startLaunchGame() throws IOException {
    try {
      ThreadUtils.startThread(() -> getRunInfo(process.getInputStream()));
      ThreadUtils.startThread(() -> getRunInfo(process.getErrorStream())).join();
      logmaker.info("Game Stop");
    } catch (InterruptedException e) {
      logmaker.error("Run Command Error,", e);
    }

  }

  private void getRunInfo(InputStream inputStream) {
    try {
      BufferedReader Reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
      String line;
      while ((line = Reader.readLine()) != null) {
        if (Thread.currentThread().isInterrupted()) {
          launchErrorTask();
          return;
        } else {
          Matcher ErrorWarn = Pattern.compile("FATAL").matcher(line);
          if (ErrorWarn.find()) {
            System.out.println(line);
            Thread.currentThread().interrupt();
          } else {
            System.out.println(line);
          }
        }
      }

    } catch (IOException e) {
      logmaker.error("Run Command Error,", e);
    }
  }

  private void launchErrorTask() throws IOException {
    setUIText.setControl("启动失败:\n" + IOUtils.toString(process.getErrorStream()) +
        IOUtils.toString(process.getInputStream()));
  }
}
