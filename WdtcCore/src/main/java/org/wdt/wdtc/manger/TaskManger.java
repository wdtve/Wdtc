package org.wdt.wdtc.manger;

import com.google.gson.JsonObject;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.utils.DownloadUtils;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class TaskManger {
    public static void ckeckVMConfig() {
        if (VMManger.isDebug()) {
            System.setProperty(VMManger.CONFIG_PATH, ".");
        }
        if (System.getProperty("wdtc.config.path") == null) {
            System.setProperty("wdtc.config.path", System.getProperty("user.home"));
        }
    }

    public static void runStartUpTask() throws IOException {
        FileUtils.delete(DownloadUtils.StopProcess);
        FileUtils.writeStringToFile(new File(FileManger.getWdtcConfig(), "readme.txt"),
                IOUtils.toString(requireNonNull(SettingManger.class.getResourceAsStream("/readme.txt"))));
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
