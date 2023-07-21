package org.wdt.wdtc.utils;

import org.apache.log4j.*;
import org.wdt.wdtc.game.FilePath;

import java.io.IOException;

public class getWdtcLogger {
    private static final Layout layout = new PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p] : %m%n");

    public static Logger getLogger(Class clazz) {
        Logger logmaker = Logger.getLogger(clazz);
        try {
            logmaker.addAppender(getFileAppender());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logmaker;
    }

    private static RollingFileAppender getFileAppender() throws IOException {
        RollingFileAppender FileAppender = new RollingFileAppender();
        FileAppender.setFile(FilePath.getWdtcConfig() + "/logs/Wdtc.log");
        FileAppender.setAppend(true);
        FileAppender.setLayout(layout);
        FileAppender.setMaxFileSize("10mb");
        FileAppender.setMaxBackupIndex(10);
        FileAppender.setThreshold(Level.INFO);
        FileAppender.activateOptions();
        return FileAppender;
    }
}
