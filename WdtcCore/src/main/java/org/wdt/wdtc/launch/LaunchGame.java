package org.wdt.wdtc.launch;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.download.DownloadGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;

public class LaunchGame {
    private static final File StartBat = FileManger.getStarterBat();
    private static final Logger logmaker = WdtcLogger.getLogger(LaunchGame.class);
    private final Launcher launcher;


    public LaunchGame(Launcher launcher) {
        this.launcher = launcher;
        try {
            launcher.LaunchTask();
            logmaker.info("Start Download");
            DownloadGameVersion gameVersion = new DownloadGameVersion(launcher);
            try {
                gameVersion.DownloadGame();
            } catch (Throwable e) {
                logmaker.error("", e);
            }
            logmaker.info("Downloaded Finish");
            logmaker.info("Write Start Script");
            writeStartScript();
            logmaker.info("Write Start Script Finish");
            logmaker.info("Start Run Start Script,Console:" + launcher.Console());
            logmaker.info(String.format("Launch Version: %s - %s", launcher.getVersionNumber(), launcher.getKind().toString()));
            logmaker.info(launcher.getGameConfig().getConfig());
        } catch (Exception e) {
            logmaker.error(e);
        }
    }

    public Process getProcess() throws IOException {
        return launcher.Console()
                ? Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "start", StartBat.getCanonicalPath()})
                : new ProcessBuilder(StartBat.getCanonicalPath()).directory(launcher.getVersionPath()).start();

    }

    @SneakyThrows(IOException.class)
    public void writeStartScript() {
        StringBuilder Script = new StringBuilder().append(new GameJvmCommand(launcher).getCommand())
                .append(new GameCLICommand(launcher).getCommand());
        logmaker.info(Script);
        FileUtils.writeStringToFile(FileManger.getStarterBat(), Script.toString());
    }
}
