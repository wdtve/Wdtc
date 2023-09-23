package org.wdt.wdtc.download;

import lombok.Getter;

import java.util.concurrent.CountDownLatch;

public class SpeedOfProgress {
    private final CountDownLatch countDown;
    @Getter
    private int Spend;

    public SpeedOfProgress(int spend) {
        this.countDown = new CountDownLatch(spend);
        Spend = spend;
    }

    public void countDown() {
        Spend = Spend - 1;
        countDown.countDown();
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
