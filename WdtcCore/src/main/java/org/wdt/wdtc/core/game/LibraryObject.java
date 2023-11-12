package org.wdt.wdtc.core.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONObject;

import java.io.IOException;
import java.net.URL;

@Data
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

  @SneakyThrows(IOException.class)
  public static LibraryObject getLibraryObject(DependencyDownload dependency, String DefaultUrl) {
    LibraryObject.Artifact artifact = new LibraryObject.Artifact();
    URL url = dependency.getLibraryUrl();
    artifact.setSha1(IOUtils.getInputStreamSha1(url.openStream()));
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
  }

  public static LibraryObject getLibraryObject(JSONObject object) {
    return JSONObject.parseObject(object, LibraryObject.class);
  }

  public static LibraryObject getLibraryObject(String object) {
    return JSONObject.parseObject(object, LibraryObject.class);
  }


  @Data
  public static class Downloads {
    @SerializedName("artifact")
    private Artifact artifact;
    @SerializedName("classifiers")
    private Classifiers classifiers;

  }

  @Data
  public static class Artifact {
    private String path;
    private String sha1;
    private long size;
    private URL url;
  }

  @Data
  public static class Classifiers {
    @SerializedName("natives-macos")
    private NativesOs NativesMacos;
    @SerializedName("natives-linux")
    private NativesOs NativesLinux;
    @SerializedName("natives-windows")
    private NativesOs Nativesindows;

  }

  @Data
  public static class NativesOs {
    private String path;
    private String sha1;
    private int size;
    private URL url;

  }
}
