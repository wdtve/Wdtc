package org.wdt.wdtc.download;

import java.util.concurrent.CountDownLatch;

public class SpeedOfProgress {
    private final CountDownLatch countDown;
    private int Spend;

    public SpeedOfProgress(int spend) {
        this.countDown = new CountDownLatch(spend);
        Spend = spend;
    }

    public void countDown() {
        Spend = Spend - 1;
        countDown.countDown();
    }

    public int getSpend() {
        return Spend;
    }

    public void await() {
        try {
            countDown.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean SpendZero() {
        return Spend != 0;
    }
}
