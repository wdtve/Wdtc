package org.wdt.wdtc.core.download.game;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.SpeedOfProgress;
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.ThreadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class DownloadGameAssetsFile {
  private static final Logger logmaker = WdtcLogger.getLogger(DownloadGameAssetsFileTask.class);
  protected final Launcher launcher;

  public DownloadGameAssetsFile(Launcher launcher) {
    this.launcher = launcher;
  }

  @SneakyThrows(IOException.class)
  public void DownloadAssetsFiles() {
    Map<String, JsonElement> list = JsonUtils.readFileToJsonObject(launcher.getGameAssetsListJson()).getAsJsonObject("objects").asMap();
    SpeedOfProgress progress = new SpeedOfProgress(list.size());
    for (String key : list.keySet()) {
      AssetsFileData data = JsonObjectUtils.parseObject(list.get(key).getAsJsonObject(), AssetsFileData.class);
      if (DownloadUtils.isDownloadProcess()) return;
      if (FileUtils.isFileNotExistsAndIsNotSameSize(new File(launcher.getGameObjects(), data.getHashSplicing()), data.getSize())) {
        DownloadGameAssetsFileTask task = new DownloadGameAssetsFileTask(launcher, data, progress);
        ThreadUtils.startThread(task);
      } else {
        progress.countDown();
      }
    }
    progress.await();
  }

  @EqualsAndHashCode
  @Getter
  public static class AssetsFileData {
    @SerializedName("hash")
    private String hash;
    @SerializedName("size")
    private int size;

    public String getHashSplicing() {
      return getHashHead() + "/" + hash;
    }

    public String getHashHead() {
      return hash.substring(0, 2);
    }
  }

  public static class DownloadGameAssetsFileTask extends Thread {
    private final Launcher launcher;
    private final AssetsFileData data;
    private final SpeedOfProgress progress;
    private final DownloadSourceInterface source;

    public DownloadGameAssetsFileTask(Launcher launcher, AssetsFileData data, SpeedOfProgress progress) {
      this.launcher = launcher;
      this.data = data;
      this.progress = progress;
      this.source = DownloadSourceManger.getDownloadSource();
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
      File HashFile = new File(launcher.getGameObjects(), data.getHashSplicing());
      URL HashUrl = new URL(source.getAssetsUrl() + data.getHashSplicing());
      DownloadUtils.StartDownloadTask(HashUrl, HashFile);
      synchronized (this) {
        progress.countDown();
      }
    }
  }
}
