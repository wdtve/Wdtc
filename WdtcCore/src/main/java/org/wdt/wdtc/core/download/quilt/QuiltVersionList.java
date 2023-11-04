package org.wdt.wdtc.core.download.quilt;

import com.google.gson.annotations.SerializedName;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.download.infterface.VersionListInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.gson.JSONArray;
import org.wdt.wdtc.core.utils.gson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuiltVersionList implements VersionListInterface {
    private static final String QuiltVersionListUrl = "https://meta.quiltmc.org//v3/versions/loader/%s";
    private final Launcher launcher;

    public QuiltVersionList(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public List<VersionJsonObjectInterface> getVersionList() throws IOException {
        List<VersionJsonObjectInterface> List = new ArrayList<>();
        JSONArray VersionArray = JSONArray.parseJSONArray(URLUtils.getURLToString(String.format(QuiltVersionListUrl, launcher.getVersionNumber())));
        for (int i = 0; i < VersionArray.size(); i++) {
            JSONObject VersionObject = VersionArray.getJSONObject(i);
            List.add(JSONObject.parseObject(VersionObject.getJSONObject("loader"), QuiltVersionJsonObjectImpl.class));
        }
        return List;
    }

    public static class QuiltVersionJsonObjectImpl implements VersionJsonObjectInterface {
        @SerializedName("version")
        private String versionNumber;
        @SerializedName("build")
        private int buildNumber;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QuiltVersionJsonObjectImpl that = (QuiltVersionJsonObjectImpl) o;
            return buildNumber == that.buildNumber && Objects.equals(versionNumber, that.versionNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(versionNumber, buildNumber);
        }

        @Override
        public String getVersionNumber() {
            return versionNumber;
        }

        @Override
        public boolean isInstanceofThis(Object o) {
            return o instanceof QuiltVersionJsonObjectImpl;
        }
    }
}
