package org.wdt.platform;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class DependencyDownload extends DefaultDependency {
    private final Logger logmaker = Logger.getLogger(getClass());
    private String DownloadPath;
    private String DefaultUrl = "https://repo1.maven.org/maven2/";
    private Boolean downloadPom = false;

    public DependencyDownload(String lib) {
        super(lib);
    }

    public DependencyDownload(String groupId, String artifactId, String version) {
        super(groupId, artifactId, version);
    }

    public void setDownloadPom(Boolean downloadPom) {
        this.downloadPom = downloadPom;
    }

    private String aboutPath(String path) {
        if (Objects.isNull(path)) return "";
        else return path;
    }

    public String getLibraryFilePath() throws IOException {
        return getLibraryFile().getCanonicalPath();
    }

    public File getLibraryFile() {
        return new File(aboutPath(DownloadPath) + formJar());
    }


    public String getDefaultUrl() {
        return DefaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        DefaultUrl = defaultUrl;
    }

    public String getDownloadPath() {
        return DownloadPath;
    }

    public void setDownloadPath(String path) {
        this.DownloadPath = aboutPath(path) + "/";
    }


    public void download() throws IOException {
        logmaker.info("* " + getLibraryUrl() + " 开始下载");
        FileUtils.copyURLToFile(getLibraryUrl(), getLibraryFile());
        logmaker.info("* " + getLibraryFile() + " 下载完成");
        if (downloadPom) {
            getLibraryDependencyDownload().downloadPom();
        }

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
