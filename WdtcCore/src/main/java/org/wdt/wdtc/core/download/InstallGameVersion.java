package org.wdt.wdtc.core.download;

import lombok.Setter;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.download.infterface.TextInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.config.GameConfig;
import org.wdt.wdtc.core.utils.ModUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.IOException;

@Setter
public class InstallGameVersion extends DownloadGameVersion {
  private static final Logger logmaker = WdtcLogger.getLogger(InstallGameVersion.class);
  private TextInterface setTextFieldText;

  public InstallGameVersion(Launcher launcher, boolean Install) {
    super(launcher, Install);
  }

  public InstallGameVersion(Launcher launcher) {
    this(launcher, false);
  }


  public void InstallGame() {
    try {
      long startTime = System.currentTimeMillis();
      if (FileUtils.isFileNotExists(launcher.getVersionConfigFile())) {
        GameConfig.writeConfigJson(launcher);
      } else {
        GameConfig.ckeckVersionInfo(launcher);
      }
      DownloadGameFile();
      InstallTaskInterface task = ModUtils.getModInstallTask(launcher);
      if (install && task != null) {
        task.beforInstallTask();
        task.writeVersionJsonPatches();
        task.overwriteVersionJson();
      }
      DownloadGameLibrary();
      String LibraryFinishTime = "游戏所需类库下载完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms";
      logmaker.info(LibraryFinishTime);
      if (setTextFieldText != null) {
        setTextFieldText.setControl(LibraryFinishTime);
      }
      if (install && task != null) {
        task.afterDownloadTask();
      }
      DownloadResourceFile();
      String EndTime = "下载完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms";
      logmaker.info(EndTime);
      if (setTextFieldText != null) {
        setTextFieldText.setControl(EndTime);
      }
    } catch (IOException e) {
      logmaker.error("Download Game Error,", e);
    }
  }
}





