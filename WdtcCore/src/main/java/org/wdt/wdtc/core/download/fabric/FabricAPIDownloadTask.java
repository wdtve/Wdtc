package org.wdt.wdtc.core.download.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.wdt.utils.gson.JsonArrayUtils;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.URLUtils;

import java.io.IOException;

public class FabricAPIDownloadTask {
  private static final String VersionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version";
  private final Launcher launcher;
  @Getter
  private final String FabricAPIVersionNumber;
  @Getter
  private VersionJsonObjectInterface versionJsonObjectInterface;

  public FabricAPIDownloadTask(Launcher launcher, String FabricAPIVersionNumber) {
    this.launcher = launcher;
    this.FabricAPIVersionNumber = FabricAPIVersionNumber;
  }

  public FabricAPIDownloadTask(Launcher launcher, VersionJsonObjectInterface versionJsonObjectInterface) {
    this(launcher, versionJsonObjectInterface.getVersionNumber());
    this.versionJsonObjectInterface = versionJsonObjectInterface;
  }

  public void startDownloadFabricAPI() throws IOException {
    FabricAPIVersionList.FabricAPIVersionJsonObjectImpl versionJsonObject = (FabricAPIVersionList.FabricAPIVersionJsonObjectImpl) versionJsonObjectInterface;
    if (versionJsonObject == null) {
      JsonArray VersionListArray = JsonArrayUtils.parseJsonArray(URLUtils.getURLToString(VersionListUrl));
      for (int i = 0; i < VersionListArray.size(); i++) {
        JsonObject VersionObject = VersionListArray.get(i).getAsJsonObject();
        FabricAPIVersionList.FabricAPIVersionJsonObjectImpl newVersionJsonObject =
            JsonObjectUtils.parseObject(VersionObject, FabricAPIVersionList.FabricAPIVersionJsonObjectImpl.class);
        if (newVersionJsonObject.getVersionNumber().equals(FabricAPIVersionNumber)) {
          downloadFabricAPITask(newVersionJsonObject.getFilesObjectList().get(0));
        }
      }
    } else {
      downloadFabricAPITask(versionJsonObject.getFilesObjectList().get(0));
    }
  }

  public void downloadFabricAPITask(FabricAPIVersionList.FabricAPIVersionJsonObjectImpl.FilesObject filesObject) {
    DownloadUtils.StartDownloadTask(filesObject.getJarDownloadURL(), FileUtils.toFile(launcher.getGameModsPath(), filesObject.getJarFileName()));
  }
}