package org.wdt.launch;

import org.apache.log4j.Logger;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.download.SelectGameVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class LauncherGame {
    private static final File m_t = FilePath.getStarterBat();
    private static final Logger logmaker = Logger.getLogger(LauncherGame.class);

    public static void bat(String file) throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start " + file);
    }

    public static void launchergame(Launcher launcher) {
        try {
            launcher.LaunchTask();
            logmaker.info("* 开始文件补全");
            SelectGameVersion gameVersion = new SelectGameVersion(launcher);
            try {
                gameVersion.DownloadGame();
            } catch (Exception e) {
                logmaker.error("错误:", e);
            }
            logmaker.info("* 文件补全完成");
            logmaker.info("* 开始写入启动脚本");
            GetJvm.GetJvmList(launcher);
            GetStartLibraryPath.getLibPath(launcher);
            GetGame.Getgame(launcher);
            launcher.writeStartScript();
            logmaker.info("* 启动脚本写入完成");
            if (launcher.log()) {
                logmaker.info("* 开始运行启动脚本,日志:显示");
                bat(m_t.getCanonicalPath());
            } else {
                logmaker.info("* 开始运行启动脚本,日志:不显示");
                executeBatFile(m_t.getCanonicalPath()).start();
            }
        } catch (Exception e) {
            logmaker.error("错误:", e);
        }
    }

    public static Thread executeBatFile(String file) {
        return new Thread(() -> {
            try {
                Process process = Runtime.getRuntime().exec("cmd.exe /c" + file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
                process.destroy();
            } catch (Exception e) {
                logmaker.error("错误:", e);
            }
        });
    }
}
