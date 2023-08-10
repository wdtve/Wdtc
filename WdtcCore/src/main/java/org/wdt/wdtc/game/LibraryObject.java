package org.wdt.wdtc.game;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.net.URL;

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
            return object;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LibraryObject getLibraryObject(JSONObject object) {
        return JSONObject.getGson().fromJson(object.getJsonObjects(), LibraryObject.class);
    }

    public static LibraryObject getLibraryObject(String object) {
        return JSONObject.parseObject(object, LibraryObject.class);
    }

    public JsonObject getNatives() {
        return natives;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public JsonArray getRules() {
        return rules;
    }

    public String getLibraryName() {
        return LibraryName;
    }

    public void setLibraryName(String libraryName) {
        LibraryName = libraryName;
    }

    public Downloads getDownloads() {
        return downloads;
    }

    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    public static class Downloads {
        @SerializedName("artifact")
        private Artifact artifact;
        @SerializedName("classifiers")
        private Classifiers classifiers;

        public Classifiers getClassifiers() {
            return classifiers;
        }


        public Artifact getArtifact() {
            return artifact;
        }

        public void setArtifact(Artifact artifact) {
            this.artifact = artifact;
        }
    }

    public static class Artifact {
        private String path;
        private String sha1;
        private long size;
        private URL url;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }
    }

    public static class Classifiers {
        @SerializedName("natives-macos")
        private NativesOs NativesMacos;
        @SerializedName("natives-linux")
        private NativesOs NativesLinux;
        @SerializedName("natives-windows")
        private NativesOs Nativesindows;

        public NativesOs getNativesMacos() {
            return NativesMacos;
        }

        public NativesOs getNativesLinux() {
            return NativesLinux;
        }

        public NativesOs getNativesindows() {
            return Nativesindows;
        }
    }

    public static class NativesOs {
        private String path;
        private String sha1;
        private int size;
        private URL url;

        public String getPath() {
            return path;
        }

        public String getSha1() {
            return sha1;
        }

        public int getSize() {
            return size;
        }

        public URL getUrl() {
            return url;
        }
    }
}
