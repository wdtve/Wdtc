package org.wdt.wdtc.manger;

import com.google.gson.JsonObject;
import org.wdt.utils.gson.JSONUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.utils.DownloadUtils;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class TaskManger {
    public static void CkeckVMConfig() {
        if (VMManger.isDebug()) {
            System.setProperty(VMManger.CHANGE_WINDOWS_SIZE, Boolean.toString(true));
            System.setProperty(VMManger.FORGE_SWITCH, Boolean.toString(true));
            System.setProperty(VMManger.CONFIG_PATH, ".");
        }
        if (System.getProperty("wdtc.config.path") == null) {
            System.setProperty("wdtc.config.path", System.getProperty("user.home"));
        }
    }
    public static void StartUpTask() throws IOException {
        FileUtils.writeStringToFile(new File(FileManger.getWdtcConfig(), "readme.txt"),
                IOUtils.toString(requireNonNull(SettingManger.class.getResourceAsStream("/readme.txt"))));
        FileUtils.createDirectories(FileManger.getWdtcCache());
        if (PlatformUtils.FileExistenceAndSize(FileManger.getUserListFile())) {
            JSONUtils.ObjectToJsonFile(FileManger.getUserListFile(), new JsonObject());
        }
        if (PlatformUtils.FileExistenceAndSize(FileManger.getSettingFile())) {
            JSONUtils.ObjectToJsonFile(FileManger.getSettingFile(), new SettingManger.Setting());
        }
        String LlbmpipeLoader = "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar";
        if (PlatformUtils.FileExistenceAndSize(FileManger.getLlbmpipeLoader())) {
            DownloadTask.StartDownloadTask(LlbmpipeLoader, FileManger.getLlbmpipeLoader());
        }
        if (PlatformUtils.FileExistenceAndSize(FileManger.getVersionManifestFile())) {
            DownloadVersionGameFile.DownloadVersionManifestJsonFile();
        }
        FileUtils.delete(DownloadUtils.StopProcess);
    }
}
