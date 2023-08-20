package org.wdt.wdtc.utils;

import org.apache.log4j.*;
import org.wdt.wdtc.game.FilePath;

public class WdtcLogger {
    private static final Layout layout = new PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p] %m%n");

    public static <T> Logger getLogger(Class<T> clazz) {
        Logger logmaker = Logger.getLogger(clazz.getName());
        logmaker.addAppender(getFileAppender());
        logmaker.addAppender(getConsoleAppender());
        return logmaker;
    }

    private static RollingFileAppender getFileAppender() {
        RollingFileAppender FileAppender = new RollingFileAppender();
        FileAppender.setFile(FilePath.getWdtcConfig() + "/logs/Wdtc.log");
        FileAppender.setAppend(true);
        FileAppender.setLayout(layout);
        FileAppender.setMaxFileSize("10MB");
        FileAppender.setMaxBackupIndex(10);
        FileAppender.setThreshold(Level.INFO);
        FileAppender.activateOptions();
        return FileAppender;
    }

    private static ConsoleAppender getConsoleAppender() {
        ConsoleAppender consoleAppender = new ConsoleAppender(layout);
        consoleAppender.setTarget("System.err");
        consoleAppender.setImmediateFlush(true);
        consoleAppender.setEncoding("UTF-8");
        consoleAppender.setThreshold(Level.INFO);
        consoleAppender.activateOptions();
        return consoleAppender;
    }
}
