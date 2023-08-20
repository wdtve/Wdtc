package org.wdt.wdtc.ui;


import org.apache.log4j.Logger;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LauncherGameWindow {
    private static final Logger logmaker = WdtcLogger.getLogger(LauncherGameWindow.class);
    private final Process process;

    public LauncherGameWindow(Process process) {
        this.process = process;
    }

    public void startGame() throws IOException {
        try {
            ThreadUtils.StartThread(() -> getRunInfo(process.getInputStream()));
            ThreadUtils.StartThread(() -> getRunInfo(process.getErrorStream())).join();
            logmaker.info("* 游戏已退出");
        } catch (InterruptedException e) {
            logmaker.error("* Run Command Error,", e);
        }

    }

    private void getRunInfo(InputStream inputStream) {
        try {
            BufferedReader ErrorReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;
            while ((line = ErrorReader.readLine()) != null) {
                if (Thread.currentThread().isInterrupted()) {
                    showErrorWin();
                    break;
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
            logmaker.error("* Run Command Error,", e);
        }
    }

    private void showErrorWin() throws IOException {
        ErrorWin.setWin("启动失败:\n" + IOUtils.toString(process.getErrorStream()) + IOUtils.toString(process.getInputStream()), "启动失败");
    }
}
