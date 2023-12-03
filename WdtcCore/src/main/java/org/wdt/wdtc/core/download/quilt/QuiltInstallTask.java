package org.wdt.wdtc.core.download.quilt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.LibraryObject;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.IOException;
import java.util.List;

public class QuiltInstallTask extends QuiltDownloadInfo implements InstallTaskInterface {

  private static final Logger logmaker = WdtcLogger.getLogger(QuiltInstallTask.class);
  private final DownloadSourceInterface source;

  public QuiltInstallTask(Launcher launcher, String quiltVersionNumber) {
    super(launcher, quiltVersionNumber);
    this.source = DownloadSourceManger.getDownloadSource();
  }

  public QuiltInstallTask(Launcher launcher, VersionJsonObjectInterface versionJsonObjectInterface) {
    super(launcher, versionJsonObjectInterface);
    this.source = DownloadSourceManger.getDownloadSource();
  }

  public void DownloadQuiltGameVersionJson() {
    DownloadUtils.StartDownloadTask(getQuiltVersionJsonUrl(), getQuiltVersionJson());
  }


  @Override
  public void overwriteVersionJson() throws IOException {
    GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
    JsonObject QuiltVersionJsonObject = getQuiltGameVersionJsonObject();
    JsonObject Arguments = QuiltVersionJsonObject.getAsJsonObject("arguments");
    GameVersionJsonObject.Arguments arguments = VersionJsonObject.getArguments();
    JsonArray GameList = arguments.getGameList();
    VersionJsonObject.setMainClass(QuiltVersionJsonObject.get("mainClass").getAsString());
    GameList.addAll(Arguments.getAsJsonArray("game"));
    arguments.setGameList(GameList);
    VersionJsonObject.setArguments(arguments);
    JsonArray QuiltLibraryList = QuiltVersionJsonObject.getAsJsonArray("library");
    List<LibraryObject> LibraryList = VersionJsonObject.getLibraries();
    for (int i = 0; i < QuiltLibraryList.size(); i++) {
      JsonObject QuiltLibraryObject = QuiltLibraryList.get(i).getAsJsonObject();
      DependencyDownload download = new DependencyDownload(QuiltLibraryObject.get("name").getAsString());
      String LibraryDefaultUrl = QuiltLibraryObject.get("url").getAsString();
      if (LibraryDefaultUrl.equals("https://maven.fabricmc.net/")) {
        download.setDefaultUrl(source.getFabricLibraryUrl());
      } else if (LibraryDefaultUrl.equals("https://maven.quiltmc.org/repository/release/")) {
        download.setDefaultUrl("https://maven.quiltmc.org/repository/release/");
      }
      LibraryList.add(LibraryObject.getLibraryObject(download, LibraryDefaultUrl));
    }
    VersionJsonObject.setLibraries(LibraryList);
    VersionJsonObject.setId(launcher.getVersionNumber() + "-quilt-" + QuiltVersionNumber);
    launcher.putToVersionJson(VersionJsonObject);
  }

  @Override
  public void writeVersionJsonPatches() throws IOException {
    GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
    Object.setJsonObject(List.of(
        JsonUtils.readFileToJsonObject(launcher.getVersionJson()),
        JsonUtils.readFileToJsonObject(getQuiltVersionJson())
    ));
    launcher.putToVersionJson(Object);
  }

  @Override
  public void afterDownloadTask() {

  }

  @Override
  public void beforInstallTask() {
    DownloadQuiltGameVersionJson();
  }
}
