package org.wdt.WdtcLauncher;

import org.apache.log4j.Logger;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.WdtcDownload.CompleteDocument;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class LauncherGame {
    private static final File m_t = FilePath.getStarterBat();
    private static final Logger logmaker = Logger.getLogger(LauncherGame.class);
    //    private static String xmx = "1024";


    public LauncherGame(Launcher launcher) throws IOException, InterruptedException {
        GetStartGameLibPath getStartGameLibPath = new GetStartGameLibPath(launcher);
        logmaker.info("* 开始文件补全");
        CompleteDocument completeDocument = new CompleteDocument(launcher);
        completeDocument.readdown();
        completeDocument.gethash();
        logmaker.info("* 文件补全完成");
        logmaker.info("* 开始写入启动脚本");
        GetJvm.readJvm(launcher);
        getStartGameLibPath.getLibPath();
        GetGame.Getgame(launcher);
        logmaker.info("* 启动脚本写入完成");
        if (launcher.log()) {
            logmaker.info("* 开始运行启动脚本,日志:显示");
            bat(m_t.getCanonicalPath());
        } else {
            logmaker.info("* 开始运行启动脚本,日志:不显示");
            executeBatFile(m_t.getCanonicalPath());
        }
    }


    public static void bat(String file) throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start " + file);
    }

    public void executeBatFile(String file) {
        Thread thread = new Thread(() -> {
            String cmdCommand;
            cmdCommand = "cmd.exe /k " + file;
            StringBuilder stringBuilder = new StringBuilder();
            Process process;
            try {
                process = Runtime.getRuntime().exec(cmdCommand);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append(" ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
