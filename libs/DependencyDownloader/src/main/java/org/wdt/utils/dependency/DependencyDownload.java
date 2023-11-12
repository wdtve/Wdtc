package org.wdt.utils.dependency;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DependencyDownload extends DefaultDependency {
  private File DownloadPath;
  private String DefaultUrl = "https://repo1.maven.org/maven2/";

  public DependencyDownload(String lib) {
    super(lib);
  }

  public DependencyDownload(String groupId, String artifactId, String version) {
    super(groupId, artifactId, version);
  }

  public String getLibraryFilePath() throws IOException {
    return getLibraryFile().getCanonicalPath();
  }

  public File getLibraryFile() {
    return new File(DownloadPath, formJar());
  }


  public String getDefaultUrl() {
    return DefaultUrl;
  }

  public void setDefaultUrl(String defaultUrl) {
    DefaultUrl = defaultUrl;
  }

  public File getDownloadPath() {
    return DownloadPath;
  }

  public void setDownloadPath(File path) {
    this.DownloadPath = path;
  }


  public URL getLibraryUrl() throws MalformedURLException {
    return new URL(DefaultUrl + formJar());
  }

  public LibraryDependencyDownload getLibraryDependencyDownload() {
    LibraryDependencyDownload libraryDependencyDownload = GetLibraryDependencyDownload();
    libraryDependencyDownload.setDefaultUrl(DefaultUrl);
    return libraryDependencyDownload;
  }
}
