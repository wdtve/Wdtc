package org.wdt.wdtc.utils;

public class ThreadUtils {

    public static Thread startThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }
}
