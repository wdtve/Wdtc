package org.wdt.wdtc.utils;

import org.apache.log4j.*;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.manger.VMManger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class WdtcLogger {


    public static <T> Logger getLogger(Class<T> clazz) {
        Logger logmaker = Logger.getLogger(clazz.getName());
        logmaker.addAppender(getFileAppender());
        logmaker.addAppender(getConsoleAppender());
        return logmaker;
    }

    private static RollingFileAppender getFileAppender() {
        RollingFileAppender FileAppender = new RollingFileAppender();
        FileAppender.setFile(FileManger.getWdtcConfig() + "/logs/Wdtc.log");
        FileAppender.setAppend(true);
        FileAppender.setLayout(new PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p]:%t * %m%n"));
        FileAppender.setMaxFileSize("10MB");
        FileAppender.setMaxBackupIndex(10);
        FileAppender.setThreshold(Level.DEBUG);
        FileAppender.activateOptions();
        return FileAppender;
    }

    private static ConsoleAppender getConsoleAppender() {
        ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p] * %m%n"));
        consoleAppender.setTarget("System.err");
        consoleAppender.setImmediateFlush(true);
        consoleAppender.setEncoding("UTF-8");
        consoleAppender.setThreshold(VMManger.isDebug() ? Level.DEBUG : Level.INFO);
        consoleAppender.activateOptions();
        return consoleAppender;
    }

    public static String getErrorMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
}
