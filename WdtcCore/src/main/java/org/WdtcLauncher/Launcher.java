package org.WdtcLauncher;

import org.WdtcDownload.CompleteDocument.CompleteDocument;
import org.WdtcLauncher.ClassPath.ReadClass;
import org.WdtcLauncher.GameSet.GetGame;
import org.WdtcLauncher.JavaHome.GetJavaPath;
import org.WdtcLauncher.JvmSet.GetJvm;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Launcher {
    private static final String java_home = GetJavaPath.getjavapath();
    private static final File m_t = new File("WdtcCore/ResourceFile/Launcher/starter.bat");
    private static final Logger logmaker = Logger.getLogger(Launcher.class);
    private static String Version_number;
    private static String xmx = "1024";
    private static boolean log = false;
    private static boolean BMCLAPI;


    public Launcher() throws IOException, InterruptedException {
        this(Version_number, xmx, log, BMCLAPI);
    }

    public Launcher(String version_number, String xmx, boolean log, boolean BMCLAPI) throws IOException, InterruptedException {
        Launcher.log = log;
        Launcher.Version_number = version_number;
        Launcher.xmx = xmx;
        Launcher.BMCLAPI = BMCLAPI;
        Version version = new Version(version_number);
        File v_j = new File(version.getVersion_json());
        String v_path = version.getVersion_path();
        logmaker.info("* 开始文件补全");
        CompleteDocument completeDocument = new CompleteDocument(v_j, v_path, version_number, BMCLAPI);
        completeDocument.readdown();
        completeDocument.gethash();
        logmaker.info("* 文件补全完成");
        logmaker.info("* 开始写入启动脚本");
        GetJvm.read_jvm(xmx, v_j, java_home, v_path, Version_number);
        ReadClass.readdown(v_j, v_path, version_number);
        GetGame.Getgame(Version_number, v_j);
        logmaker.info("* 启动脚本写入完成");
        if (log) {
            logmaker.info("* 开始运行启动脚本,日志:显示");
            bat(m_t.getCanonicalPath());
        } else {
            logmaker.info("* 开始运行启动脚本,日志:不显示");
            executeBatFile(m_t.getCanonicalPath(), true);
        }
    }

    public Launcher(String version_number, String xmx) throws IOException, InterruptedException {
        this(version_number, xmx, log, BMCLAPI);
    }

    public Launcher(String version_number, boolean log, boolean BMCLAPI) throws IOException, InterruptedException {
        this(version_number, xmx, log, BMCLAPI);
    }

    public static void bat(String file) throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start " + file);
    }

    public StringBuilder executeBatFile(String file, boolean isCloseWindow) throws IOException {
        Thread thread = new Thread(() -> {

            String cmdCommand;
            if (isCloseWindow) {
                cmdCommand = "cmd.exe /c " + file;
            } else {
                cmdCommand = "cmd.exe /k " + file;
            }
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
        return null;
    }
}
