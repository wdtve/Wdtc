package org.wdt.wdtc;


import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.UserList;
import org.wdt.wdtc.auth.yggdrasil.AuthlibInjector;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.Version;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.launch.GamePath;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.manger.SettingManger;
import org.wdt.wdtc.manger.VMManger;
import org.wdt.wdtc.ui.ErrorWindow;
import org.wdt.wdtc.utils.JavaUtils;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;

public class WdtcMain extends JavaFxUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(WdtcMain.class);

    public static void main(String[] args) {
        try {
            CkeckJavaFX();
            logmaker.info("===== Wdtc - " + VMManger.getLauncherVersion() + " =====");
            logmaker.info("Java Version:" + System.getProperty("java.version"));
            logmaker.info("Java VM Version:" + System.getProperty("java.vm.name"));
            logmaker.info("Java Home:" + System.getProperty("java.home"));
            logmaker.info("Wdtc User Path:" + FileManger.getWdtcConfig());
            logmaker.info("Setting File:" + FileManger.getSettingFile());
            logmaker.info("Here:" + GamePath.getDefaultHere());
            SettingManger.GenerateSettingFile();
            StartTask();
            Version.DownloadVersionManifestJsonFileTask();
            RemovePreferredVersion();
            UserList.printUserList();
            AuthlibInjector.UpdateAuthlibInjector();
            GameConfig.writeConfigJsonToAllVersion();
            ThreadUtils.StartThread(() -> JavaUtils.main(getRegistryKey())).setName("Found Java");
            AppMain.main(args);
        } catch (Throwable e) {
            ErrorWindow.setErrorWin(e);
        }
    }

    public static void StartTask() throws IOException {
        String LlbmpipeLoader = "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar";
        if (PlatformUtils.FileExistenceAndSize(FileManger.getLlbmpipeLoader())) {
            DownloadTask.StartDownloadTask(LlbmpipeLoader, FileManger.getLlbmpipeLoader());
        }
        if (PlatformUtils.FileExistenceAndSize(FileManger.getVersionManifestFile())) {
            DownloadVersionGameFile.DownloadVersionManifestJsonFile();
        }
    }

    public static void RemovePreferredVersion() throws IOException {
        SettingManger.Setting setting = SettingManger.getSetting();
        if (setting.getPreferredVersion() != null) {
            Launcher launcher = new Launcher(setting.getPreferredVersion());
            if (PlatformUtils.FileExistenceAndSize(launcher.getVersionJson())) {
                setting.setPreferredVersion(null);
                SettingManger.putSettingToFile(setting);
            }
        }
    }

    public static String[] getRegistryKey() {
        return new String[]{"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit",
                "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Update",
                "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Web Start Caps",
                "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JDK"};
    }
}
