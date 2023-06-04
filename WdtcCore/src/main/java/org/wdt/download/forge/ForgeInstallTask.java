package org.wdt.download.forge;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.download.DownloadTask;
import org.wdt.download.FileUrl;
import org.wdt.download.dependency.DefaultDependency;
import org.wdt.download.dependency.DependencyDownload;
import org.wdt.launch.GetJavaPath;
import org.wdt.platform.PlatformUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Pattern;

public class ForgeInstallTask extends ForgeDownloadTask {
    private final Logger logmaker = Logger.getLogger(getClass());

    public ForgeInstallTask(String mcVersion, String forgeVersion) throws IOException {
        super(mcVersion, forgeVersion);
    }

    public ForgeInstallTask(Launcher launcher, String forgeVersion) {
        super(launcher, forgeVersion);
    }


    public String CommandLine(int index) throws IOException {
        JSONObject JsonObject = getInstallPrefileJSONObject().getJSONArray("processors").getJSONObject(index);
        StringBuilder CommandLine = new StringBuilder();
        CommandLine.append(GetJavaPath.GetRunJavaHome()).append(" -cp ");
        JSONArray JarList = JsonObject.getJSONArray("classpath");
        for (int i = 0; i < JarList.size(); i++) {
            DefaultDependency Jar = new DefaultDependency(JarList.getString(i));
            CommandLine.append(FilenameUtils.separatorsToWindows(launcher.GetGameLibPath() + Jar.formJar())).append(";");
        }
        if (JsonObject.getString("jar").equals("net.minecraftforge:ForgeAutoRenamingTool:0.1.22:all")) {
            CommandLine.append(launcher.GetGameLibPath()).append(FilenameUtils.separatorsToWindows("net/minecraftforge/ForgeAutoRenamingTool/0.1.22/ForgeAutoRenamingTool-0.1.22-all.jar"));
            CommandLine.append(" ").append("net.minecraftforge.fart.Main").append(" ");
        } else {

            DefaultDependency MainJar = new DependencyDownload(JsonObject.getString("jar"));
            CommandLine.append(launcher.GetGameLibPath()).append(MainJar.formJar()).append(" ").append(MainJar.getGroupId())
                    .append(".").append(MainJar.getArtifactId()).append(".ConsoleTool ");
        }
        JSONArray ArgsList = JsonObject.getJSONArray("args");
        for (int i = 0; i < ArgsList.size(); i++) {
            if (i % 2 == 0) {
                CommandLine.append(ArgsList.getString(i)).append(" ");
            } else {
                String ArgeStr = ArgsList.getString(i);
                if (ArgeStr.equals("{MINECRAFT_JAR}")) {
                    CommandLine.append(launcher.getVersionJar()).append(" ");
                } else if (ArgeStr.equals("{BINPATCH}")) {
                    unzipByInstallProfile(getForgeInstallJarPath(), ClientLzmaPath());
                    CommandLine.append(ClientLzmaPath()).append(" ");
                } else if (ArgeStr.equals("{SIDE}")) {
                    CommandLine.append("client").append(" ");
                } else if (Pattern.compile("\\{").matcher(ArgeStr).find()) {
                    DefaultDependency client = new DefaultDependency(Clean(getInstallPrefileJSONObject().getJSONObject("data")
                            .getJSONObject(Clean(ArgeStr)).getString("client")));
                    CommandLine.append(launcher.GetGameLibPath()).append(client.formJar()).append(" ");
                } else if (Pattern.compile("\\[").matcher(ArgeStr).find()) {
                    DefaultDependency arge = new DefaultDependency(Clean(ArgeStr));
                    CommandLine.append(launcher.GetGameLibPath()).append(arge.formJar()).append(" ");
                } else {
                    CommandLine.append(ArgeStr).append(" ");
                }
            }
        }
        return FilenameUtils.separatorsToWindows(CommandLine.toString());
    }

    public void DownloadClientText() throws IOException {
        DefaultDependency TxtPath = new DefaultDependency(getInstallPrefileJSONObject().getJSONObject("data")
                .getJSONObject("MOJMAPS").getString("client").replace("[", "").replace("]", ""));
        String TxtUrl = PlatformUtils.FileToJSONObject(launcher.getVersionJson()).getJSONObject("downloads")
                .getJSONObject("client_mappings").getString("url");
        if (launcher.bmclapi()) {
            TxtUrl = TxtUrl.replaceAll(FileUrl.getPistonDataMojang(), FileUrl.getBmcalapiCom());
        }
        DownloadTask.StartWGetDownloadTask(TxtUrl, launcher.GetGameLibPath() + TxtPath.formJar());

    }

    public void InstallForge() throws IOException {
        JSONArray objects = getInstallPrefileJSONObject().getJSONArray("processors");
        for (int i = 0; i < objects.size(); i++) {
            JSONObject TaskJson = objects.getJSONObject(i);
            if (Objects.isNull(TaskJson.getJSONArray("sides")) || TaskJson.getJSONArray("sides").getString(0).equals("client")) {
                if (!TaskJson.getJSONArray("args").getString(1).equals("DOWNLOAD_MOJMAPS")) {
                    StartCommand(i);
                }

            }

        }
    }

    public String ClientLzmaPath() {
        return FilenameUtils.separatorsToWindows(FilePath.getWdtcCache() + "/data/client.lzma");
    }

    public String Clean(String str) {
        return str.replace("[", "").replace("]", "").replace("{", "").replace("}", "");
    }

    private void StartCommand(int i) throws IOException {
        logmaker.info("* Command Line:" + CommandLine(i));
        Process process = Runtime.getRuntime().exec("cmd.exe /c" + CommandLine(i));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            logmaker.info(line);
        }
    }
}
