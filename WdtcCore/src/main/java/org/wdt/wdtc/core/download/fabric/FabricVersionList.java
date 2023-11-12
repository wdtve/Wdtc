package org.wdt.wdtc.core.download.fabric;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.download.infterface.VersionListInterface;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.gson.JSONArray;
import org.wdt.wdtc.core.utils.gson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FabricVersionList implements VersionListInterface {

  @Override
  public List<VersionJsonObjectInterface> getVersionList() throws IOException {
    List<VersionJsonObjectInterface> FabricVersionList = new ArrayList<>();
    JSONArray list = JSONArray.parseJSONArray(URLUtils.getURLToString(DownloadSourceManger.getDownloadSource().getFabricMetaUrl() + "v2/versions/loader"));
    for (int i = 0; i < list.size(); i++) {
      JSONObject fabricObject = list.getJSONObject(i);
      FabricVersionList.add(JSONObject.parseObject(fabricObject, FabricVersionJsonObjectImpl.class));
    }
    return FabricVersionList;
  }

  @Data
  public static class FabricVersionJsonObjectImpl implements VersionJsonObjectInterface {
    @SerializedName("version")
    private String versionNumber;
    @SerializedName("build")
    private int buildNumber;

    @Override
    public String getVersionNumber() {
      return versionNumber;
    }

    @Override
    public boolean isInstanceofThis(Object o) {
      return o instanceof FabricVersionJsonObjectImpl;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      FabricVersionJsonObjectImpl that = (FabricVersionJsonObjectImpl) o;
      return buildNumber == that.buildNumber && Objects.equals(versionNumber, that.versionNumber);
    }

    @Override
    public int hashCode() {
      return Objects.hash(versionNumber, buildNumber);
    }
  }
}
