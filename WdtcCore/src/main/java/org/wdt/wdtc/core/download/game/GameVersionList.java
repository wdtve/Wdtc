package org.wdt.wdtc.core.download.game;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.download.infterface.VersionListInterface;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONArray;
import org.wdt.wdtc.core.utils.gson.JSONObject;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameVersionList implements VersionListInterface {
  private static final Logger logmaker = WdtcLogger.getLogger(GameVersionList.class);

  @SneakyThrows
  public GameVersionList() {
    if (FileUtils.isFileNotExists(FileManger.getVersionManifestFile())) {
      DownloadVersionGameFile.DownloadVersionManifestJsonFile();
    }
  }


  public List<VersionJsonObjectInterface> getVersionList() {
    List<VersionJsonObjectInterface> VersionList = new ArrayList<>();
    try {
      JSONArray version_list = JSONUtils.readJsonFiletoJSONObject(FileManger.getVersionManifestFile()).getJSONArray("versions");
      for (int i = 0; i < version_list.size(); i++) {
        JSONObject VersionObject = version_list.getJSONObject(i);
        GameVersionJsonObjectImpl versionJsonObject = JSONObject.parseObject(VersionObject, GameVersionJsonObjectImpl.class);
        if (versionJsonObject.gameType.equals("release")) {
          VersionList.add(versionJsonObject);
        }
      }
    } catch (IOException e) {
      logmaker.error(WdtcLogger.getExceptionMessage(e));
    }
    return VersionList;
  }

  @Getter
  public static class GameVersionJsonObjectImpl implements VersionJsonObjectInterface {
    @SerializedName("id")
    private String versionNumber;
    @SerializedName("type")
    private String gameType;
    @SerializedName("url")
    private URL versionJsonURL;
    @SerializedName("time")
    private String time;
    @SerializedName("releaseTime")
    private String releaseTime;

    @Override
    public String getVersionNumber() {
      return versionNumber;
    }

    @Override
    public boolean isInstanceofThis(Object o) {
      return o instanceof GameVersionJsonObjectImpl;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      GameVersionJsonObjectImpl that = (GameVersionJsonObjectImpl) o;
      return Objects.equals(versionNumber, that.versionNumber) && Objects.equals(gameType, that.gameType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(versionNumber, gameType);
    }
  }
}
