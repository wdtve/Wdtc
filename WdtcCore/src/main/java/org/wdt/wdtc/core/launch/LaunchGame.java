package org.wdt.wdtc.core.launch;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.DownloadGameVersion;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.SettingManger;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.File;

public class LaunchGame {
    private static final File StartBat = FileManger.getStarterBat();
    private static final Logger logmaker = WdtcLogger.getLogger(LaunchGame.class);
    private final Launcher launcher;
    private final SettingManger.Setting setting;


    private LaunchGame(Launcher launcher, SettingManger.Setting setting) {
        this.launcher = launcher;
        this.setting = setting;
    }

    public static LaunchGame init(Launcher launcher) {
        SettingManger.Setting setting = SettingManger.getSetting();
        try {
            launcher.beforLaunchTask();
            logmaker.info("Start Download");
            DownloadGameVersion gameVersion = new DownloadGameVersion(launcher);
            try {
                gameVersion.DownloadGame();
            } catch (Throwable e) {
                logmaker.error(WdtcLogger.getExceptionMessage(e));
            }
            logmaker.info("Downloaded Finish");
            logmaker.info("Write Start Script");
            String script = new GameJvmCommand(launcher).getCommand().append(new GameCLICommand(launcher).getCommand()).toString();
            logmaker.info(script);
            FileUtils.writeStringToFile(FileManger.getStarterBat(), script);
            logmaker.info("Write Start Script Finish");
            logmaker.info("Start Run Start Script,Console:" + setting.isConsole());
            logmaker.info(String.format("Launch Version: %s - %s", launcher.getVersionNumber(), launcher.getKind().toString()));
            logmaker.info(launcher.getGameConfig().getConfig());
        } catch (Exception e) {
            logmaker.error(WdtcLogger.getExceptionMessage(e));
        }
        return new LaunchGame(launcher, setting);
    }


    @SneakyThrows
    public Process getProcess() {
        return setting.isConsole()
                ? Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "start", StartBat.getCanonicalPath()})
                : new ProcessBuilder(StartBat.getCanonicalPath()).directory(launcher.getVersionPath()).start();

    }

    public LaunchProcess getLaunchProcess() {
        return new LaunchProcess(getProcess());
    }
}
