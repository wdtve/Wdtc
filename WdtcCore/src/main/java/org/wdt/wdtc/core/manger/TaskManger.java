package org.wdt.wdtc.core.manger;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class TaskManger {
  private static final Logger logmaker = WdtcLogger.getLogger(TaskManger.class);

  public static void ckeckVMConfig() {
    if (VMManger.isDebug()) {
      System.setProperty(VMManger.CONFIG_PATH, "./");
    }
    if (System.getProperty(VMManger.CONFIG_PATH) == null) {
      System.setProperty(VMManger.CONFIG_PATH, System.getProperty("user.home"));
    }
  }

  public static void runStartUpTask() throws IOException {
    FileUtils.delete(DownloadUtils.StopProcess);
    FileUtils.writeStringToFile(new File(FileManger.getWdtcConfig(), "assets/readme.txt"),
        IOUtils.toString(requireNonNull(SettingManger.class.getResourceAsStream("/assets/readme.txt"))));
    FileUtils.createDirectories(FileManger.getWdtcCache());
    if (FileUtils.isFileNotExists(FileManger.getUserListFile())) {
      JSONUtils.writeObjectToJsonFile(FileManger.getUserListFile(), new JsonObject());
    }
    if (FileUtils.isFileNotExists(FileManger.getSettingFile())) {
      JSONUtils.writeObjectToJsonFile(FileManger.getSettingFile(), new SettingManger.Setting());
    }
    String LlbmpipeLoader = "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar";
    if (FileUtils.isFileNotExists(FileManger.getLlbmpipeLoader())) {
      DownloadUtils.StartDownloadTask(LlbmpipeLoader, FileManger.getLlbmpipeLoader());
    }
    if (FileUtils.isFileNotExists(FileManger.getVersionManifestFile())) {
      DownloadVersionGameFile.DownloadVersionManifestJsonFile();
    }
  }
}
