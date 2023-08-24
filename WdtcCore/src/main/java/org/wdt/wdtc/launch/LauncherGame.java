package org.wdt.wdtc.launch;

import org.apache.log4j.Logger;
import org.wdt.wdtc.download.InstallGameVersion;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;

public class LauncherGame {
    private static final File StartBat = FilePath.getStarterBat();
    private static final Logger logmaker = WdtcLogger.getLogger(LauncherGame.class);
    private final Launcher launcher;


    public LauncherGame(Launcher launcher) {
        this.launcher = launcher;
        try {
            launcher.LaunchTask();
            logmaker.info("* Start Download");
            InstallGameVersion gameVersion = new InstallGameVersion(launcher, false);
            try {
                gameVersion.DownloadGame();
            } catch (Throwable e) {
                logmaker.error("错误:", e);
            }
            logmaker.info("* Downloaded Finish");
            logmaker.info("* Write Start Script");
            launcher.setJvmattribute(new GameJvmCommand(launcher).GetJvmList());
            launcher.setGameattribute(new GameCommand(launcher).Getgame());
            launcher.writeStartScript();
            logmaker.info("* Write Start Script Finish");
            if (launcher.Console()) {
                logmaker.info("* Start Run Start Script,Console: true");
            } else {
                logmaker.info("* Start Run Start Script,Console: false");
            }
            logmaker.info("Launch Version: " + launcher.getVersionNumber() + "-" + launcher.getKind());
            logmaker.info(launcher.getGameConfig().getConfig());
        } catch (Exception e) {
            logmaker.error("错误:", e);
        }
    }

    private Process RunBatFile() throws IOException {
        return Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "start", StartBat.getCanonicalPath()});
    }

    private Process executeBatFile() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(StartBat.getCanonicalPath());
        pb.directory(launcher.getVersionPath());
        return pb.start();

    }

    public Process getStart() throws IOException {
        if (launcher.Console()) {
            return RunBatFile();
        } else {
            return executeBatFile();
        }
    }
}
