package org.wdt.wdtc.download;

import java.util.concurrent.CountDownLatch;

public class SpeedOfProgress {
    private final CountDownLatch countDown;
    private double Spend;

    public SpeedOfProgress(Double spend) {
        this.countDown = new CountDownLatch(spend.intValue());
        Spend = spend;
    }

    public void countDown() {
        Spend = Spend - 1;
        countDown.countDown();
    }

    public double getSpend() {
        return Spend;
    }

    public void await() throws InterruptedException {
        countDown.await();
    }

    public boolean SpendZero() {
        return Spend != 0;
    }
}
