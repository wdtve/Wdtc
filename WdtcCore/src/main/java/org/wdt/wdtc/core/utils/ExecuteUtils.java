package org.wdt.wdtc.core.utils;

import com.google.gson.JsonArray;
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.LibraryObject;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.utils.gson.JSONObject;

import java.io.IOException;
import java.util.List;

public class ExecuteUtils {
  private final Launcher launcher;
  private final JSONObject ModVersionJsonObject;
  private final DownloadSourceInterface source;

  public ExecuteUtils(Launcher launcher, JSONObject modVersionJsonObject) {
    this.launcher = launcher;
    ModVersionJsonObject = modVersionJsonObject;
    this.source = DownloadSourceManger.getDownloadSource();
  }

  public static GameVersionJsonObject.Arguments getArguments(GameVersionJsonObject VersionJsonObject, GameVersionJsonObject ModVersionJsonObject) {
    GameVersionJsonObject.Arguments GameArguments = VersionJsonObject.getArguments();
    GameVersionJsonObject.Arguments ModArguments = ModVersionJsonObject.getArguments();
    JsonArray ModGameList = ModArguments.getGameList();
    if (ModGameList != null) {
      JsonArray GameList = GameArguments.getGameList();
      GameList.add(ModGameList);
      GameArguments.setGameList(GameList);
    }
    JsonArray ModJvmList = ModArguments.getJvmList();
    if (ModJvmList != null) {
      JsonArray GameJvmList = GameArguments.getJvmList();
      GameJvmList.add(ModJvmList);
      GameArguments.setJvmList(GameJvmList);
    }
    return GameArguments;
  }

  public static List<LibraryObject> getLibrarys(GameVersionJsonObject VersionJsonObject, GameVersionJsonObject ModVersionJsonObject) {
    List<LibraryObject> libraryObjectList = VersionJsonObject.getLibraries();
    libraryObjectList.addAll(ModVersionJsonObject.getLibraries());
    return libraryObjectList;
  }

  public void execute(ModUtils.KindOfMod kind) throws IOException {
    ModDownloadInfoInterface info = ModUtils.getModDownloadInfo(launcher);
    GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
    GameVersionJsonObject ModVersionJsonObject = JSONObject.parseObject(this.ModVersionJsonObject, GameVersionJsonObject.class);
    VersionJsonObject.setMainClass(ModVersionJsonObject.getMainClass());
    VersionJsonObject.setArguments(getArguments(VersionJsonObject, ModVersionJsonObject));
    VersionJsonObject.setLibraries(getLibrarys(VersionJsonObject, ModVersionJsonObject));
    if (info != null) {
      VersionJsonObject.setId(launcher.getVersionNumber() + kind.toString().toLowerCase() + info.getModVersion());
    }
    launcher.putToVersionJson(VersionJsonObject);
  }
}
