package org.wdt.platform;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class log4j {
    private static final PatternLayout layout = new PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p] %m%n");

    public static <T> Logger getLogger(Class<T> clazz) {
        Logger logger = Logger.getLogger(clazz);
        ConsoleAppender consoleAppender = new ConsoleAppender(layout);
        consoleAppender.setThreshold(Level.DEBUG);
        consoleAppender.setTarget("System.out");
        consoleAppender.setImmediateFlush(true);
        consoleAppender.setEncoding("UTF-8");
        consoleAppender.activateOptions();
        logger.addAppender(consoleAppender);
        return logger;
    }
}
