package org.wdt.wdtc.core.download.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricInstallTask extends FabricDonwloadInfo implements InstallTaskInterface {
  private final DownloadSourceInterface source;


  public FabricInstallTask(Launcher launcher, String FabricVersionNumber) {
    super(launcher, FabricVersionNumber);
    this.source = DownloadSourceManger.getDownloadSource();
  }

  public FabricInstallTask(Launcher launcher, VersionJsonObjectInterface versionJsonObjectInterface) {
    this(launcher, versionJsonObjectInterface.getVersionNumber());
  }

  @Override
  public void overwriteVersionJson() throws IOException {
    GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
    List<LibraryObject> libraryObjectList = VersionJsonObject.getLibraries();
    JsonObject FabricVersionJsonObject = getFabricVersionJsonObject();
    JsonArray FabricLibraryList = FabricVersionJsonObject.getAsJsonArray("libraries");
    for (int i = 0; i < FabricLibraryList.size(); i++) {
      JsonObject object = FabricLibraryList.get(i).getAsJsonObject();
      DependencyDownload dependency = new DependencyDownload(object.get("name").getAsString());
      dependency.setDefaultUrl(source.getFabricLibraryUrl());
      dependency.setDownloadPath(launcher.getGameLibraryDirectory());
      libraryObjectList.add(LibraryObject.getLibraryObject(dependency, object.get("url").getAsString()));
    }
    VersionJsonObject.setLibraries(libraryObjectList);
    VersionJsonObject.setMainClass(FabricVersionJsonObject.get("mainClass").getAsString());
    GameVersionJsonObject.Arguments arguments = VersionJsonObject.getArguments();
    JsonObject Arguments = FabricVersionJsonObject.get("arguments").getAsJsonObject();
    JsonArray JvmList = arguments.getJvmList();
    JvmList.addAll(Arguments.getAsJsonArray("jvm"));
    arguments.setJvmList(JvmList);
    JsonArray GameList = arguments.getGameList();
    GameList.addAll(Arguments.getAsJsonArray("game"));
    arguments.setGameList(GameList);
    VersionJsonObject.setArguments(arguments);
    VersionJsonObject.setId(launcher.getVersionNumber() + "-fabric-" + FabricVersionNumber);
    launcher.putToVersionJson(VersionJsonObject);
  }


  @Override
  public void writeVersionJsonPatches() throws IOException {
    GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
    List<JsonObject> ObjectList = new ArrayList<>();
    ObjectList.add(JsonUtils.getJsonObject(launcher.getVersionJson()));
    ObjectList.add(JsonUtils.getJsonObject(getFabricVersionJson()));
    Object.setJsonObject(ObjectList);
    launcher.putToVersionJson(Object);
  }

  @Override
  public void afterDownloadTask() throws IOException {
    if (isAPIDownloadTaskNoNull()) {
      getApiDownloadTask().startDownloadFabricAPI();
    }
  }

  @Override
  public void beforInstallTask() {
    DownloadUtils.StartDownloadTask(String.format(getFabricVersionFileUrl(), launcher.getVersionNumber(), FabricVersionNumber), getFabricVersionJson());
  }
}