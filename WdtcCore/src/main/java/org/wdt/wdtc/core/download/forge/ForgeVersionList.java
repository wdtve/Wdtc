package org.wdt.wdtc.core.download.forge;


import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.wdt.utils.gson.JsonArrayUtils;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.download.infterface.VersionListInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.URLUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForgeVersionList implements VersionListInterface {
  private final Launcher launcher;


  public ForgeVersionList(Launcher launcher) {
    this.launcher = launcher;
  }

  public String getForgeListUrl() {
    return "https://bmclapi2.bangbang93.com/forge/minecraft/" + launcher.getVersionNumber();
  }

  @Override
  public List<VersionJsonObjectInterface> getVersionList() throws IOException {
    List<VersionJsonObjectInterface> VersionName = new ArrayList<>();
    JsonArray VersionList = JsonArrayUtils.parseJsonArray(URLUtils.getURLToString(getForgeListUrl()));
    for (int i = 0; i < VersionList.size(); i++) {
      VersionName.add(JsonObjectUtils.parseObject(VersionList.get(i).getAsJsonObject(), ForgeVersionJsonObjectImpl.class));
    }
    return VersionName;
  }

  @Getter
  public static class ForgeVersionJsonObjectImpl implements VersionJsonObjectInterface {
    @SerializedName("version")
    private String versionNumber;
    @SerializedName("modified")
    private String modified;
    @SerializedName("mcversion")
    private String mcversion;

    @Override
    public String getVersionNumber() {
      return versionNumber;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ForgeVersionJsonObjectImpl that = (ForgeVersionJsonObjectImpl) o;
      return Objects.equals(versionNumber, that.versionNumber) && Objects.equals(mcversion, that.mcversion);
    }

    @Override
    public int hashCode() {
      return Objects.hash(versionNumber, mcversion);
    }
  }
}
