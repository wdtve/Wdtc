package org.wdt.wdtc.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.utils.gson.JSON;
import org.wdt.utils.gson.JSONObject;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.net.URL;

@Setter
@Getter
public class LibraryObject {
    private static final Logger logger = WdtcLogger.getLogger(LibraryObject.class);
    @SerializedName("downloads")
    private Downloads downloads;
    @SerializedName("name")
    private String LibraryName;
    @SerializedName("rules")
    private JsonArray rules;
    @SerializedName("natives")
    private JsonObject natives;

    public static LibraryObject getLibraryObject(DependencyDownload dependency, String DefaultUrl) {

        try {
            LibraryObject.Artifact artifact = new LibraryObject.Artifact();
            URL url = dependency.getLibraryUrl();
            artifact.setSha1(PlatformUtils.getFileSha1(url.openStream()));
            artifact.setPath(dependency.formJar());
            artifact.setSize(url.openConnection().getContentLengthLong());
            dependency.setDefaultUrl(DefaultUrl);
            artifact.setUrl(dependency.getLibraryUrl());
            LibraryObject.Downloads downloads = new LibraryObject.Downloads();
            downloads.setArtifact(artifact);
            LibraryObject object = new LibraryObject();
            object.setLibraryName(dependency.getLibraryName());
            object.setDownloads(downloads);
            logger.info(object);
            return object;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LibraryObject getLibraryObject(JSONObject object) {
        return JSONObject.parseObject(object, LibraryObject.class);
    }

    public static LibraryObject getLibraryObject(String object) {
        return JSONObject.parseObject(object, LibraryObject.class);
    }

    @Override
    public String toString() {
        return JSON.GSON.toJson(this);
    }

    @Getter
    @Setter
    public static class Downloads {
        @SerializedName("artifact")
        private Artifact artifact;
        @SerializedName("classifiers")
        private Classifiers classifiers;

    }

    @Getter
    @Setter
    public static class Artifact {
        private String path;
        private String sha1;
        private long size;
        private URL url;
    }

    @Getter
    @Setter
    public static class Classifiers {
        @SerializedName("natives-macos")
        private NativesOs NativesMacos;
        @SerializedName("natives-linux")
        private NativesOs NativesLinux;
        @SerializedName("natives-windows")
        private NativesOs Nativesindows;

    }

    @Getter
    @Setter
    public static class NativesOs {
        private String path;
        private String sha1;
        private int size;
        private URL url;

    }
}
