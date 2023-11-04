package org.wdt.wdtc.ui;

import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.auth.UsersList;
import org.wdt.wdtc.core.auth.yggdrasil.AuthlibInjector;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.config.GameConfig;
import org.wdt.wdtc.core.manger.*;
import org.wdt.wdtc.core.utils.JavaUtils;
import org.wdt.wdtc.core.utils.ThreadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.ui.window.ExceptionWindow;

import java.io.IOException;

public class WdtcMain extends JavaFxUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(WdtcMain.class);

    public static void main(String[] args) {
        try {
            TaskManger.ckeckVMConfig();
            ckeckJavaFX();
            logmaker.info(String.format("===== Wdtc - %s - =====", VMManger.getLauncherVersion()));
            logmaker.info(String.format("Java Version:%s", System.getProperty("java.version")));
            logmaker.info(String.format("Java VM Version:%s", System.getProperty("java.vm.name")));
            logmaker.info(String.format("Java Home:%s", System.getProperty("java.home")));
            logmaker.info(String.format("Wdtc Debug Mode:%b", VMManger.isDebug()));
            logmaker.info("Wdtc Application Type:" + VMManger.getApplicationType());
            logmaker.info("Wdtc Config Path:" + FileManger.getWdtcConfig());
            logmaker.info("Setting File:" + FileManger.getSettingFile());
            logmaker.info("Here:" + GameDirectoryManger.getDefaultHere());
            TaskManger.runStartUpTask();
            GameFileManger.downloadVersionManifestJsonFileTask();
            removePreferredVersion();
            UsersList.printUserList();
            AuthlibInjector.updateAuthlibInjector();
            GameConfig.writeConfigJsonToAllVersion();
            ThreadUtils.startThread(() -> JavaUtils.main(getRegistryKey())).setName("Found Java");
            AppMain.main(args);
        } catch (Throwable e) {
            ExceptionWindow.setErrorWin(e);
        }
    }

    public static void removePreferredVersion() throws IOException {
        SettingManger.Setting setting = SettingManger.getSetting();
        if (setting.getPreferredVersion() != null) {
            Launcher launcher = new Launcher(setting.getPreferredVersion());
            if (FileUtils.isFileNotExists(launcher.getVersionJson())) {
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
