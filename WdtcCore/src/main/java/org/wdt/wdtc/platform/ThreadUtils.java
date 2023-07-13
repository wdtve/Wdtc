package org.wdt.wdtc.platform;

public class ThreadUtils {
    public static void StartThread(Runnable runnable) {
        new Thread(runnable).start();
    }
}
