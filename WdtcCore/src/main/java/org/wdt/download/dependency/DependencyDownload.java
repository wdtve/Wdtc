package org.wdt.download.dependency;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class DependencyDownload extends DefaultDependency {
    private final Logger logmaker = Logger.getLogger(getClass());
    private String path;
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

    public String libFilePath() throws IOException {
        return libPath().getCanonicalPath();
    }

    public File libPath() {
        return new File(aboutPath(path) + formJar());
    }


    public String getDefaultUrl() {
        return DefaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        DefaultUrl = defaultUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = aboutPath(path) + "/";
    }


    public void download() throws IOException {
        logmaker.info("* " + getLibUrl() + " 开始下载");
        FileUtils.copyURLToFile(getLibUrl(), libPath());
        logmaker.info("* " + libPath() + " 下载完成");
        if (downloadPom) {
            getLibraryDependencyDownload().downloadPom();
        }

    }

    public URL getLibUrl() throws MalformedURLException {
        return new URL(DefaultUrl + formJar());
    }

    public LibraryDependencyDownload getLibraryDependencyDownload() {
        LibraryDependencyDownload libraryDependencyDownload = GetLibraryDependencyDownload();
        libraryDependencyDownload.setDefaultUrl(DefaultUrl);
        return libraryDependencyDownload;
    }
}
