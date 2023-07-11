package org.wdt.wdtc.launch;

import org.apache.log4j.Logger;
import org.wdt.wdtc.download.SelectGameVersion;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;
import org.wdt.wdtc.platform.Starter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class LauncherGame {
    private static final File StartBat = FilePath.getStarterBat();
    private static final Logger logmaker = Logger.getLogger(LauncherGame.class);
    private final Launcher launcher;

    public LauncherGame(Launcher launcher) {
        this.launcher = launcher;
    }

    public static void bat() throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start " + StartBat.getCanonicalPath());
    }

    public void LauncherVersion() {
        try {
            logmaker.info("Launch Version: " + launcher.getVersion() + "-" + launcher.getKind());
            launcher.LaunchTask();
            logmaker.info("* 开始文件补全");
            SelectGameVersion gameVersion = new SelectGameVersion(launcher);
            try {
                if (!ModList.GameModIsForge(launcher) || !Starter.getForgeSwitch()) {
                    gameVersion.DownloadGame();
                }
            } catch (Exception e) {
                logmaker.error("错误:", e);
            }
            logmaker.info("* 文件补全完成");
            logmaker.info("* 开始写入启动脚本");
            GetJvm.GetJvmList(launcher);
            GetStartLibraryPath.getLibraryPath(launcher);
            GetGame.Getgame(launcher);
            launcher.writeStartScript();
            logmaker.info("* 启动脚本写入完成");
            if (launcher.log()) {
                logmaker.info("* 开始运行启动脚本,日志:显示");
                bat();
            } else {
                logmaker.info("* 开始运行启动脚本,日志:不显示");
                executeBatFile().start();
            }
        } catch (Exception e) {
            logmaker.error("错误:", e);
        }
    }

    public Thread executeBatFile() {
        return new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(StartBat.getCanonicalPath());
                pb.directory(new File(launcher.getVersionPath()));
                Process process = pb.start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                BufferedReader ErrorReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                while (bufferedReader.readLine() != null || ErrorReader.readLine() != null) {
                    if (bufferedReader.readLine() != null) {
                        System.out.println(bufferedReader.readLine());
                    } else {
                        System.err.println(ErrorReader.readLine());
                    }
                }
                process.destroy();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
