package org.WdtcUI;

import javafx.application.Application;
import org.apache.log4j.Logger;

public class WdtcMain {
    private static final Logger logger = Logger.getLogger(WdtcMain.class);
    public static void main(String[] args) {
        logger.info("* Java Version:" + System.getProperty("java.version"));
        logger.info("* Java VM Version:" + System.getProperty("java.vm.name"));
        logger.info("* Java Home:" + System.getProperty("java.home"));
        logger.info("* 程序开始运行");
        logger.info("* 栓Q,JavaFX ↓");
        Application.launch(AppMain.class);
    }
}
