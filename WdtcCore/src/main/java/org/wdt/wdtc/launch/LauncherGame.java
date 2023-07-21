package org.wdt.wdtc.launch;

import org.apache.log4j.Logger;
import org.wdt.wdtc.download.SelectGameVersion;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;
import org.wdt.wdtc.platform.Starter;

import java.io.File;
import java.io.IOException;

public class LauncherGame {
    private static final File StartBat = FilePath.getStarterBat();
    private static final Logger logmaker = Logger.getLogger(LauncherGame.class);
    private final Launcher launcher;


    public LauncherGame(Launcher launcher) {
        this.launcher = launcher;
        try {
            launcher.LaunchTask();
            logmaker.info("* 开始文件补全");
            SelectGameVersion gameVersion = new SelectGameVersion(launcher);
            try {
                if (!ModList.GameModIsForge(launcher) && !Starter.getForgeSwitch()) {
                    gameVersion.DownloadGame();
                }
            } catch (Exception e) {
                logmaker.error("错误:", e);
            }
            logmaker.info("* 文件补全完成");
            logmaker.info("* 开始写入启动脚本");
            GameJvmCommand.GetJvmList(launcher);
            GetStartLibraryPath.getLibraryPath(launcher);
            GameCommand.Getgame(launcher);
            launcher.writeStartScript();
            logmaker.info("* 启动脚本写入完成");
            if (launcher.log()) {
                logmaker.info("* 开始运行启动脚本,日志:显示");
            } else {
                logmaker.info("* 开始运行启动脚本,日志:不显示");
            }
            logmaker.info("Launch Version: " + launcher.getVersion() + "-" + launcher.getKind());
        } catch (Exception e) {
            logmaker.error("错误:", e);
        }
    }

    private Process bat() throws IOException {
        return Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "start", StartBat.getCanonicalPath()});
    }

    private Process executeBatFile() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(StartBat.getCanonicalPath());
        pb.directory(new File(launcher.getVersionPath()));
        return pb.start();

    }

    public Process getStart() throws IOException {
        if (launcher.log()) {
            return bat();
        } else {
            return executeBatFile();
        }
    }
}
