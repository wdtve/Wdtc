package org.wdt.wdtc.download.forge;


import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.DefaultDependency;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.platform.ExtractFile;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgeInstallTask extends ForgeDownloadTask {
    private static final Logger logmaker = WdtcLogger.getLogger(ForgeInstallTask.class);
    private final DefaultGameConfig config;
    private final DownloadSource source;


    public ForgeInstallTask(Launcher launcher, String forgeVersion) {
        super(launcher, forgeVersion);
        this.config = launcher.getGameConfig().getGameConfig();
        this.source = Launcher.getDownloadSource();
    }


    public String CommandLine(int index) throws IOException {
        JSONObject JsonObject = getInstallPrefileJSONObject().getJSONArray("processors").getJSONObject(index);
        StringBuilder CommandLine = new StringBuilder();
        CommandLine.append("\"").append(config.getJavaPath()).append("\"").append(" -cp ");
        JSONArray JarList = JsonObject.getJSONArray("classpath");
        for (int i = 0; i < JarList.size(); i++) {
            DefaultDependency Jar = new DefaultDependency(JarList.getString(i));
            CommandLine.append(FilenameUtils.separatorsToWindows(launcher.GetGameLibraryPath() + Jar.formJar())).append(";");
        }
        if (Pattern.compile("net.minecraftforge:ForgeAutoRenamingTool").matcher(JsonObject.getString("jar")).find()) {
            DefaultDependency Jar = new DependencyDownload(JsonObject.getString("jar"));
            CommandLine.append(launcher.GetGameLibraryPath()).append(FilenameUtils.separatorsToWindows(Jar.formJar()));
            CommandLine.append(" ").append("net.minecraftforge.fart.Main").append(" ");
        } else {
            DefaultDependency MainJar = new DependencyDownload(JsonObject.getString("jar"));
            CommandLine.append(launcher.GetGameLibraryPath()).append(MainJar.formJar()).append(" ").append(MainJar.getGroupId())
                    .append(".").append(MainJar.getArtifactId()).append(".ConsoleTool ");
        }
        JSONArray ArgsList = JsonObject.getJSONArray("args");
        for (int i = 0; i < ArgsList.size(); i++) {
            if (i % 2 == 0) {
                CommandLine.append(ArgsList.getString(i)).append(" ");
            } else {
                String ArgeStr = ArgsList.getString(i);
                Matcher Middle = getMiddleBracket(ArgeStr);
                Matcher Large = getLargeBracket(ArgeStr);
                if (ArgeStr.equals("{MINECRAFT_JAR}")) {
                    CommandLine.append(launcher.getVersionJar()).append(" ");
                } else if (ArgeStr.equals("{BINPATCH}")) {
                    ExtractFile.unZipBySpecifyFile(getForgeInstallJarPath(), ClientLzmaPath());
                    CommandLine.append(ClientLzmaPath()).append(" ");
                } else if (ArgeStr.equals("{SIDE}")) {
                    CommandLine.append("client").append(" ");
                } else if (Large.find()) {
                    DefaultDependency client = new DefaultDependency(Clean(getInstallPrefileJSONObject().getJSONObject("data")
                            .getJSONObject(Large.group(1)).getString("client")));
                    CommandLine.append(launcher.GetGameLibraryPath()).append(client.formJar()).append(" ");
                } else if (Middle.find()) {
                    DefaultDependency arge = new DefaultDependency(Middle.group(1));
                    CommandLine.append(launcher.GetGameLibraryPath()).append(arge.formJar()).append(" ");
                } else {
                    CommandLine.append(ArgeStr).append(" ");
                }
            }
        }
        return FilenameUtils.separatorsToWindows(CommandLine.toString());
    }

    public void DownloadClientText() throws IOException {
        DefaultDependency TxtPath = null;
        JSONObject DataObject = getInstallPrefileJSONObject().getJSONObject("data");
        if (DataObject.has("MOJMAPS")) {
            Matcher matcher = getMiddleBracket(DataObject.getJSONObject("MOJMAPS").getString("client"));
            if (matcher.find()) {
                TxtPath = new DefaultDependency(matcher.group(1));
            }
        } else {
            Matcher matcher = getMiddleBracket(DataObject.getJSONObject("MAPPINGS").getString("client"));
            if (matcher.find()) {
                TxtPath = new DefaultDependency(matcher.group(1));
            }
        }
        String TxtUrl = JSONUtils.getJSONObject(launcher.getVersionJson()).getJSONObject("downloads")
                .getJSONObject("client_mappings").getString("url");
        if (FileUrl.DownloadSourceList.NoOfficialDownloadSource()) {
            TxtUrl = PlatformUtils.getRedirectUrl(TxtUrl.replaceAll(FileUrl.getPistonDataMojang(), source.getDataUrl()));
        }
        if (TxtPath != null) {
            DownloadTask.StartDownloadTask(TxtUrl, launcher.GetGameLibraryPath() + TxtPath.formJar());
        }

    }

    public void InstallForge() throws IOException {
        JSONArray objects = getInstallPrefileJSONObject().getJSONArray("processors");
        for (int i = 0; i < objects.size(); i++) {
            JSONObject TaskJson = objects.getJSONObject(i);
            if (TaskJson.has("sides")) {
                if (TaskJson.getJSONArray("sides").getString(0).equals("client")) {
                    StartCommand(i);
                }
            } else {
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
        String commmand = CommandLine(i);
        logmaker.info("* Command Line:" + commmand);
        Process process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", commmand});
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            logmaker.info(line);
        }
        process.destroy();
    }

    public Matcher getMiddleBracket(String args) {
        return Pattern.compile("\\[(.+)]").matcher(args);
    }

    public Matcher getLargeBracket(String args) {
        return Pattern.compile("\\{(.+)}").matcher(args);
    }
}
