package org.wdt.wdtc.download;

import java.util.concurrent.CountDownLatch;

public class SpeedOfProgress extends CountDownLatch {
    private int Spend;

    public SpeedOfProgress(int spend) {
        super(spend);
        Spend = spend;
    }

    public void countDown() {
        Spend = Spend - 1;
        super.countDown();
    }

    public int getSpend() {
        return Spend;
    }

    public void await() throws InterruptedException {
        super.await();
    }

    public boolean SpendZero() {
        return Spend != 0;
    }
}
