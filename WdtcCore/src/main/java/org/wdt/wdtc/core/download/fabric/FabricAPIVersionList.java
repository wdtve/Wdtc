package org.wdt.wdtc.core.download.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.wdt.utils.gson.JsonArrayUtils;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.download.infterface.VersionListInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.URLUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FabricAPIVersionList implements VersionListInterface {
  private static final String VersionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version";
  private final Launcher launcher;

  public FabricAPIVersionList(Launcher launcher) {
    this.launcher = launcher;
  }

  @Override
  public List<VersionJsonObjectInterface> getVersionList() throws IOException {
    List<VersionJsonObjectInterface> VersionList = new ArrayList<>();
    JsonArray VersionListArray = JsonArrayUtils.parseJsonArray(URLUtils.getURLToString(VersionListUrl));
    for (int i = 0; i < VersionListArray.size(); i++) {
      JsonObject VersionObject = VersionListArray.get(i).getAsJsonObject();
      FabricAPIVersionJsonObjectImpl versionJsonObject = JsonObjectUtils.parseObject(VersionObject, FabricAPIVersionJsonObjectImpl.class);
      if (launcher.getVersionNumber().equals(versionJsonObject.gameVersion.get(0))) {
        VersionList.add(versionJsonObject);
      }
    }
    return VersionList;
  }

  @Getter
  public static class FabricAPIVersionJsonObjectImpl implements VersionJsonObjectInterface {
    @SerializedName("version_number")
    private String versionNumber;
    @SerializedName("files")
    private List<FilesObject> filesObjectList;
    @SerializedName("game_versions")
    private List<String> gameVersion;

    @Override
    public String getVersionNumber() {
      return versionNumber;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      FabricAPIVersionJsonObjectImpl that = (FabricAPIVersionJsonObjectImpl) o;
      return Objects.equals(versionNumber, that.versionNumber);
    }

    @Override
    public int hashCode() {
      return Objects.hash(versionNumber);
    }

    @Getter
    public static class FilesObject {
      @SerializedName("url")
      private URL jarDownloadURL;
      @SerializedName("size")
      private int fileSize;
      @SerializedName("filename")
      private String jarFileName;
    }
  }
}
