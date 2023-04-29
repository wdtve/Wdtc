package org.wdt.WdtcLauncher;

import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.FilePath;
import org.wdt.WdtcDownload.CompleteDocument;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Launcher {
    private static final File m_t = FilePath.getStarterBat();
    private static final Logger logmaker = Logger.getLogger(Launcher.class);
    private static String Version_number;
    //    private static String xmx = "1024";


    public Launcher(String version_number) throws IOException, InterruptedException {
        boolean BMCLAPI = AboutSetting.GetBmclSwitch();
        boolean log = AboutSetting.GetLogSwitch();
        Launcher.Version_number = version_number;
        logmaker.info("* 开始文件补全");
        CompleteDocument completeDocument = new CompleteDocument(version_number, BMCLAPI);
        completeDocument.readdown();
        completeDocument.gethash();
        logmaker.info("* 文件补全完成");
        logmaker.info("* 开始写入启动脚本");
        GetJvm.read_jvm(Version_number);
        GetStartGameLibPath.getLibPath(Version_number);
        GetGame.Getgame(Version_number);
        logmaker.info("* 启动脚本写入完成");
        if (log) {
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
