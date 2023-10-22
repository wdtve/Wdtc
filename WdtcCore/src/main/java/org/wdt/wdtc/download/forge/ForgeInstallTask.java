package org.wdt.wdtc.download.forge;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.utils.dependency.DefaultDependency;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.utils.io.FilenameUtils;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.download.game.DownloadGameClass;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.manger.URLManger;
import org.wdt.wdtc.utils.*;
import org.wdt.wdtc.utils.gson.JSONArray;
import org.wdt.wdtc.utils.gson.JSONObject;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgeInstallTask extends ForgeDownloadInfo implements InstallTask {
    private static final Logger logmaker = WdtcLogger.getLogger(ForgeInstallTask.class);
    private final DefaultGameConfig.Config config;
    private final DownloadSource source;


    public ForgeInstallTask(Launcher launcher, String forgeVersion) {
        super(launcher, forgeVersion);
        this.config = launcher.getGameConfig().getConfig();
        this.source = Launcher.getDownloadSource();
    }


    public String CommandLine(int index) throws IOException {
        JSONObject JsonObject = getInstallPrefileJSONObject().getJSONArray("processors").getJSONObject(index);
        StringBuilder CommandLine = new StringBuilder();
        CommandLine.append(config.getJavaPath()).append(" -cp ");
        JSONArray JarList = JsonObject.getJSONArray("classpath");
        for (int i = 0; i < JarList.size(); i++) {
            DependencyDownload Jar = new DependencyDownload(JarList.getString(i));
            Jar.setDownloadPath(launcher.getGameLibraryPath());
            CommandLine.append(FilenameUtils.separatorsToWindows(Jar.getLibraryFilePath())).append(";");
        }
        DependencyDownload MainJar = new DependencyDownload(JsonObject.getString("jar"));
        MainJar.setDownloadPath(launcher.getGameLibraryPath());
        String MainClass = new JarInputStream(new FileInputStream(MainJar.getLibraryFile())).getManifest().getMainAttributes().getValue("Main-Class");
        CommandLine.append(MainJar.getLibraryFilePath()).append(" ").append(MainClass).append(" ");
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
                    ZipUtils.unZipBySpecifyFile(getForgeInstallJarPath(), ClientLzmaPath());
                    CommandLine.append(ClientLzmaPath()).append(" ");
                } else if (ArgeStr.equals("{SIDE}")) {
                    CommandLine.append("client").append(" ");
                } else if (Large.find()) {
                    DependencyDownload client = new DependencyDownload(Clean(getInstallPrefileJSONObject().getJSONObject("data")
                            .getJSONObject(Large.group(1)).getString("client")));
                    client.setDownloadPath(launcher.getGameLibraryPath());
                    CommandLine.append(client.getLibraryFilePath()).append(" ");
                } else if (Middle.find()) {
                    DependencyDownload arge = new DependencyDownload(Middle.group(1));
                    arge.setDownloadPath(launcher.getGameLibraryPath());
                    CommandLine.append(arge.getLibraryFilePath()).append(" ");
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
        String TxtUrl = JSONUtils.readJsonFiletoJSONObject(launcher.getVersionJson()).getJSONObject("downloads")
                .getJSONObject("client_mappings").getString("url");
        if (URLManger.DownloadSourceList.NoOfficialDownloadSource()) {
            TxtUrl = URLUtils.getRedirectUrl(TxtUrl.replaceAll(URLManger.getPistonDataMojang(), source.getDataUrl()));
        }
        if (TxtPath != null) {
            DownloadUtils.StartDownloadTask(TxtUrl, launcher.getGameLibraryPath() + TxtPath.formJar());
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
        return FilenameUtils.separatorsToWindows(FileManger.getWdtcCache() + "/data/client.lzma");
    }

    public String Clean(String str) {
        return StringUtils.cleanStrInString(str, "[", "]", "{", "}");
    }

    private void StartCommand(int i) throws IOException {
        String commmand = CommandLine(i);
        logmaker.info("Command Line:" + commmand);
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

    @Override
    public void execute() throws IOException {
        JSONObject ForgeVersionJsonObject = getForgeVersionJsonObject();
        JSONArray LibraryArray = ForgeVersionJsonObject.getJSONArray("libraries");
        GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
        GameVersionJsonObject.Arguments arguments = VersionJsonObject.getArguments();
        JsonArray GameList = arguments.getGameList();
        JSONObject Arguments = ForgeVersionJsonObject.getJSONObject("arguments");
        GameList.addAll(Arguments.getJSONArray("game").getJsonArrays());
        arguments.setGameList(GameList);
        JsonArray JvmList = arguments.getJvmList();
        if (Arguments.has("jvm")) {
            JvmList.addAll(Arguments.getJSONArray("jvm").getJsonArrays());
        }
        arguments.setJvmList(JvmList);
        VersionJsonObject.setArguments(arguments);
        for (int i = 0; i < LibraryArray.size(); i++) {
            LibraryObject libraryObject = JSONObject.parseObject(LibraryArray.getJSONObject(i).getJsonObjects(), LibraryObject.class);
            VersionJsonObject.getLibraries().add(libraryObject);
        }
        VersionJsonObject.setMainClass(ForgeVersionJsonObject.getString("mainClass"));
        VersionJsonObject.setId(launcher.getVersionNumber() + "-forge-" + ForgeVersionNumber);
        launcher.PutToVersionJson(VersionJsonObject);
    }

    @Override
    public void setPatches() throws IOException {
        GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
        List<JsonObject> ObjectList = new ArrayList<>();
        ObjectList.add(JSONUtils.readJsonFiletoJsonObject(launcher.getVersionJson()));
        ObjectList.add(JSONUtils.readJsonFiletoJsonObject(getForgeVersionJsonPath()));
        Object.setJsonObject(ObjectList);
        launcher.PutToVersionJson(Object);
    }

    @Override
    public void afterDownloadTask() throws IOException {
        DownloadForgeLibraryFile(getInstallProfilePath());
        DownloadClientText();
        InstallForge();
    }

    @Override
    public void beforInstallTask() {
        DownloadInstallJar();
        getInstallProfile();
        getForgeVersionJson();
    }

    public void DownloadForgeLibraryFile(String FilePath) throws IOException {
        JSONArray LibraryList = JSONUtils.readJsonFiletoJSONObject(FilePath).getJSONArray("libraries");
        SpeedOfProgress speed = new SpeedOfProgress(LibraryList.size());
        for (int i = 0; i < LibraryList.size(); i++) {
            LibraryObject object = LibraryObject.getLibraryObject(LibraryList.getJSONObject(i));
            DownloadGameClass task = new DownloadGameClass(launcher);
            task.StartDownloadLibraryTask(object, speed);
        }
        speed.await();
    }
}
