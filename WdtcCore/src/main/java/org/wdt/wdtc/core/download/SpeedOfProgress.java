package org.wdt.wdtc.core.download;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;

public class SpeedOfProgress {
  private final CountDownLatch countDown;
  @Getter
  private volatile int Spend;

  public SpeedOfProgress(int spend) {
    this.countDown = new CountDownLatch(spend);
    Spend = spend;
  }

  public synchronized void countDown() {
    Spend = Spend - 1;
    countDown.countDown();
  }


  @SneakyThrows
  public void await() {
    countDown.await();
  }

  public boolean isSpendZero() {
    return Spend == 0;
  }
}
