package org.wdt.platform;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LibraryDependencyDownload extends DefaultDependency {
    private final Logger logmaker = log4j.getLogger(LibraryDependencyDownload.class);
    private String path = System.getProperty("user.dir");
    private String DefaultUrl = "https://repo1.maven.org/maven2/";
    private boolean DeletePom = false;

    public LibraryDependencyDownload(String lib) {
        super(lib);
    }

    public LibraryDependencyDownload(String groupId, String artifactId, String version) {
        super(groupId, artifactId, version);
    }

    private static String aboutPath(String path) {
        if (Objects.isNull(path)) return "";
        else return path;
    }

    public boolean isDeletePom() {
        return DeletePom;
    }

    public void setDeletePom(boolean deletePom) {
        this.DeletePom = deletePom;
    }

    public String PomFilePath() throws IOException {
        return PomFile().getCanonicalPath();
    }

    public File PomFile() {
        return new File(aboutPath(path) + "/" + formPom());
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

    public void downloadPom() {
        try {
            logmaker.info("* " + getPomUrl() + " 开始下载");
            FileUtils.copyURLToFile(getPomUrl(), PomFile());
            logmaker.info("* " + PomFile() + " 下载完成");
        } catch (IOException e) {
            try {
                System.out.println(getPomUrl() + "找不到文件");
            } catch (MalformedURLException ex) {
                logmaker.error("* 错误:", ex);
            }
        }
    }

    public URL getPomUrl() throws MalformedURLException {
        return new URL(DefaultUrl + formPom().replaceAll("\\+", "%2B"));
    }


    public List<DependencyDownload> downloadDependency() throws DocumentException, IOException {
        if (!PomFile().exists()) {
            throw new IOException("The POM file not exists!");
        }
        List<DependencyDownload> dependencyList = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(PomFile());
        if (Objects.nonNull(document)) {
            Element element = document.getRootElement();
            Element dependencies = element.element("dependencies");
            if (Objects.nonNull(dependencies)) {
                List<Element> list = dependencies.elements("dependency");
                for (Element l : list) {
                    if (Objects.isNull(l.elementText("scope"))) {
                        dependencyList.add(new DependencyDownload(l.elementText("groupId"), l.elementText("artifactId"), l.elementText("version")));
                    } else {
                        dependencyList.add(new DependencyDownload(l.elementText("groupId") + ":" + l.elementText("artifactId") +
                                ":" + l.elementText("version") + ":" + l.elementText("scope")));
                    }

                }
            }
        }
        if (DeletePom) {
            FileUtils.delete(PomFile());
        }
        return dependencyList;
    }
}
