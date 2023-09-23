package org.wdt.wdtc;


import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.UserList;
import org.wdt.wdtc.auth.yggdrasil.AuthlibInjector;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.manger.*;
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
            CkeckWdtcConfigPath();
            logmaker.info(String.format("===== Wdtc - %s - =====", VMManger.getLauncherVersion()));
            logmaker.info(String.format("Java Version:%s", System.getProperty("java.version")));
            logmaker.info(String.format("Java VM Version:%s", System.getProperty("java.vm.name")));
            logmaker.info(String.format("Java Home:%s", System.getProperty("java.home")));
            logmaker.info("Wdtc Config Path:" + FileManger.getWdtcConfig());
            logmaker.info("Setting File:" + FileManger.getSettingFile());
            logmaker.info("Here:" + GameFolderManger.getDefaultHere());
            TaskManger.StartUpTask();
            GameFileManger.DownloadVersionManifestJsonFileTask();
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

    public static void CkeckWdtcConfigPath() {
        if (System.getProperty("wdtc.config.path") == null) {
            System.setProperty("wdtc.config.path", System.getProperty("user.home"));
        }
    }
}
