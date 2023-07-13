package org.wdt.wdtc.WdtcUI;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LauncherErrorWin {
    private static final Logger logmaker = getWdtcLogger.getLogger(LauncherErrorWin.class);
    private final Process process;

    public LauncherErrorWin(Process process) {
        this.process = process;
    }

    public void startGame() throws IOException {
        BufferedReader Reader = process.inputReader();
        BufferedReader ErrorReader = process.errorReader();
        String line, error;
        while (Reader.readLine() != null || ErrorReader.readLine() != null) {
            line = Reader.readLine();
            error = ErrorReader.readLine();
            if (line != null) {
                Matcher info = Pattern.compile("INFO").matcher(line);
                Matcher warn = Pattern.compile("WARN").matcher(line);
                if (info.find() || warn.find()) {
                    System.out.println(line);
                } else {
                    showErrorWin();
                    break;
                }
            }
            if (error != null) {
                Matcher info = Pattern.compile("ERROR").matcher(error);
                Matcher warn = Pattern.compile("FATAL").matcher(error);
                if (info.find() || warn.find()) {
                    showErrorWin();
                    break;
                } else {
                    System.out.println(error);
                }
            }
        }
        process.destroy();
        logmaker.info("* 游戏已退出");
    }

    private void showErrorWin() throws IOException {
        ErrorWin.setWin("启动失败:\n" + IOUtils.toString(process.getInputStream()), "启动失败");
    }
}
