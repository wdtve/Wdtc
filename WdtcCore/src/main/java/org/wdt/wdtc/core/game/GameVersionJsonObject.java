package org.wdt.wdtc.core.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class GameVersionJsonObject {
    @SerializedName("arguments")
    private Arguments arguments;
    @SerializedName("assetIndex")
    private FileDataObject assetIndex;
    @SerializedName("assets")
    private String assets;
    @SerializedName("complianceLevel")
    private int complianceLevel;
    @SerializedName("downloads")
    private Downloads downloads;
    @SerializedName("id")
    private String id;
    @SerializedName("javaVersion")
    private JsonObject javaVersion;
    @SerializedName("libraries")
    private List<LibraryObject> libraries;
    @SerializedName("logging")
    private JsonObject logging;
    @SerializedName("mainClass")
    private String mainClass;
    @SerializedName("patches")
    private List<JsonObject> JsonObject;
    @SerializedName("minimumLauncherVersion")
    private int minimumLauncherVersion;
    @SerializedName("releaseTime")
    private String releaseTime;
    @SerializedName("time")
    private String time;
    @SerializedName("type")
    private String type;

    @Setter
    @Getter
    @EqualsAndHashCode
    public static class Arguments {
        @SerializedName("game")
        private JsonArray GameList;
        @SerializedName("jvm")
        private JsonArray JvmList;
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class FileDataObject {
        @SerializedName("sha1")
        private String fileSha1;
        @SerializedName("size")
        private int fileSize;
        @SerializedName("url")
        private URL listJsonURL;
    }

    @Getter
    @EqualsAndHashCode
    public static class Downloads {
        @SerializedName("client")
        private FileDataObject client;
        @SerializedName("client_mappings")
        private FileDataObject clientMappings;
        @SerializedName("server")
        private FileDataObject server;
        @SerializedName("server_mappings")
        private FileDataObject serverMappings;
    }
}
