package org.wdt.wdtc.core.download.forge;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.utils.dependency.DefaultDependency;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.utils.io.FilenameUtils;
import org.wdt.wdtc.core.download.SpeedOfProgress;
import org.wdt.wdtc.core.download.game.DownloadGameClass;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.LibraryObject;
import org.wdt.wdtc.core.game.config.DefaultGameConfig;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.URLManger;
import org.wdt.wdtc.core.utils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgeInstallTask extends ForgeDownloadInfo implements InstallTaskInterface {
  private static final Logger logmaker = WdtcLogger.getLogger(ForgeInstallTask.class);
  private final DefaultGameConfig.Config config;

  public ForgeInstallTask(Launcher launcher, String forgeVersion) {
    super(launcher, forgeVersion);
    this.config = launcher.getGameConfig().getConfig();
  }

  public ForgeInstallTask(Launcher launcher, VersionJsonObjectInterface versionJsonObjectInterface) {
    this(launcher, versionJsonObjectInterface.getVersionNumber());
  }

  public String CommandLine(int index) throws IOException {
    JsonObject JsonObject = getInstallPrefileJSONObject().getAsJsonArray("processors").get(index).getAsJsonObject();
    StringBuilder CommandLine = new StringBuilder();
    CommandLine.append(config.getJavaPath()).append(" -cp ");
    JsonArray JarList = JsonObject.getAsJsonArray("classpath");
    for (int i = 0; i < JarList.size(); i++) {
      DependencyDownload Jar = new DependencyDownload(JarList.get(i).getAsString());
      Jar.setDownloadPath(launcher.getGameLibraryDirectory());
      CommandLine.append(FilenameUtils.separatorsToWindows(Jar.getLibraryFilePath())).append(";");
    }
    DependencyDownload MainJar = new DependencyDownload(JsonObject.get("jar").getAsString());
    MainJar.setDownloadPath(launcher.getGameLibraryDirectory());
    String MainClass = new JarInputStream(new FileInputStream(MainJar.getLibraryFile())).getManifest().getMainAttributes().getValue("Main-Class");
    CommandLine.append(MainJar.getLibraryFilePath()).append(" ").append(MainClass).append(" ");
    JsonArray ArgsList = JsonObject.getAsJsonArray("args");
    for (int i = 0; i < ArgsList.size(); i++) {
      if (i % 2 == 0) {
        CommandLine.append(ArgsList.get(i).getAsString()).append(" ");
      } else {
        String ArgeStr = ArgsList.get(i).getAsString();
        Matcher Middle = getMiddleBracket(ArgeStr);
        Matcher Large = getLargeBracket(ArgeStr);
        if (ArgeStr.equals("{MINECRAFT_JAR}")) {
          CommandLine.append(launcher.getVersionJar()).append(" ");
        } else if (ArgeStr.equals("{BINPATCH}")) {
          ZipUtils.unZipBySpecifyFile(getForgeInstallJarPath(), getClientLzmaFile());
          CommandLine.append(getClientLzmaFile()).append(" ");
        } else if (ArgeStr.equals("{SIDE}")) {
          CommandLine.append("client").append(" ");
        } else if (Large.find()) {
          DependencyDownload client = new DependencyDownload(Clean(getInstallPrefileJSONObject().getAsJsonObject("data")
              .getAsJsonObject(Large.group(1)).get("client").getAsString()));
          client.setDownloadPath(launcher.getGameLibraryDirectory());
          CommandLine.append(client.getLibraryFilePath()).append(" ");
        } else if (Middle.find()) {
          DependencyDownload arge = new DependencyDownload(Middle.group(1));
          arge.setDownloadPath(launcher.getGameLibraryDirectory());
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
    JsonObject DataObject = getInstallPrefileJSONObject().getAsJsonObject("data");
    if (DataObject.has("MOJMAPS")) {
      Matcher matcher = getMiddleBracket(DataObject.getAsJsonObject("MOJMAPS").get("client").getAsString());
      if (matcher.find()) {
        TxtPath = new DefaultDependency(matcher.group(1));
      }
    } else {
      Matcher matcher = getMiddleBracket(DataObject.getAsJsonObject("MAPPINGS").get("client").getAsString());
      if (matcher.find()) {
        TxtPath = new DefaultDependency(matcher.group(1));
      }
    }
    String TxtUrl = JsonUtils.getJsonObject(launcher.getVersionJson()).getAsJsonObject("downloads")
        .getAsJsonObject("client_mappings").get("url").getAsString();
    if (DownloadSourceManger.isNotOfficialDownloadSource()) {
      TxtUrl = URLUtils.getRedirectUrl(TxtUrl.replaceAll(URLManger.getPistonDataMojang(), source.getDataUrl()));
    }
    if (TxtPath != null) {
      DownloadUtils.StartDownloadTask(TxtUrl, launcher.getGameLibraryDirectory() + TxtPath.formJar());
    }

  }

  public void InstallForge() throws IOException {
    JsonArray objects = getInstallPrefileJSONObject().getAsJsonArray("processors");
    for (int i = 0; i < objects.size(); i++) {
      JsonObject TaskJson = objects.get(i).getAsJsonObject();
      if (TaskJson.has("sides")) {
        if (TaskJson.getAsJsonArray("sides").get(0).getAsString().equals("client")) {
          StartCommand(i);
        }
      } else {
        if (!TaskJson.getAsJsonArray("args").get(1).getAsString().equals("DOWNLOAD_MOJMAPS")) {
          StartCommand(i);
        }
      }
    }
  }

  public File getClientLzmaFile() {
    return new File(FileManger.getWdtcCache(), "/data/client.lzma");
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
  public void overwriteVersionJson() throws IOException {
    JsonObject ForgeVersionJsonObject = getForgeVersionJsonObject();
    JsonArray LibraryArray = ForgeVersionJsonObject.getAsJsonArray("libraries");
    GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
    GameVersionJsonObject.Arguments arguments = VersionJsonObject.getArguments();
    JsonArray GameList = arguments.getGameList();
    JsonObject Arguments = ForgeVersionJsonObject.getAsJsonObject("arguments");
    GameList.addAll(Arguments.getAsJsonArray("game"));
    arguments.setGameList(GameList);
    JsonArray JvmList = arguments.getJvmList();
    if (Arguments.has("jvm")) {
      JvmList.addAll(Arguments.getAsJsonArray("jvm"));
    }
    arguments.setJvmList(JvmList);
    VersionJsonObject.setArguments(arguments);
    for (int i = 0; i < LibraryArray.size(); i++) {
      LibraryObject libraryObject = JsonObjectUtils.parseObject(LibraryArray.get(i).getAsString(), LibraryObject.class);
      VersionJsonObject.getLibraries().add(libraryObject);
    }
    VersionJsonObject.setMainClass(ForgeVersionJsonObject.get("mainClass").getAsString());
    VersionJsonObject.setId(launcher.getVersionNumber() + "-forge-" + ForgeVersionNumber);
    launcher.putToVersionJson(VersionJsonObject);
  }

  @Override
  public void writeVersionJsonPatches() throws IOException {
    GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
    List<JsonObject> ObjectList = new ArrayList<>();
    ObjectList.add(JsonUtils.getJsonObject(launcher.getVersionJson()));
    ObjectList.add(JsonUtils.getJsonObject(getForgeVersionJsonPath()));
    Object.setJsonObject(ObjectList);
    launcher.putToVersionJson(Object);
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

  public void DownloadForgeLibraryFile(File file) throws IOException {
    JsonArray LibraryList = JsonUtils.getJsonObject(file).getAsJsonArray("libraries");
    SpeedOfProgress speed = new SpeedOfProgress(LibraryList.size());
    for (int i = 0; i < LibraryList.size(); i++) {
      LibraryObject object = LibraryObject.getLibraryObject(LibraryList.get(i).getAsJsonObject());
      DownloadGameClass task = new DownloadGameClass(launcher);
      task.StartDownloadLibraryTask(object, speed);
    }
    speed.await();
  }
}
