package org.wdt.download.dependency;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultDependency {
    private final String lib;
    private String groupId;
    private String artifactId;
    private String version;
    private String spaec;


    public DefaultDependency(String lib) throws IllegalArgumentException {
        Pattern p = Pattern.compile("([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)");
        Matcher m = p.matcher(Objects.requireNonNull(lib));
        if (!m.matches()) {
            throw new IllegalArgumentException("Bad artifact coordinates " + lib + ", expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>");
        }
        this.lib = lib;
        groupId = Clean(m.group(1));
        artifactId = Clean(m.group(2));
        if (Objects.nonNull(m.group(3))) {
            spaec = Clean(m.group(3));
            version = Clean(m.group(7));
        } else {
            spaec = null;
            version = Clean(m.group(7));
        }
    }

    public DefaultDependency(String groupId, String artifactId, String version) {
        this(groupId + ":" + artifactId + ":" + version);
    }

    public String getLib() {
        return lib;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String formPom() {
        return getGroupId().replaceAll("\\.", "/") + "/" + getArtifactId() + "/" + getVersion() + "/" + getArtifactId() + "-" + getVersion() + ".pom";
    }

    public String formJar() {
        Pattern p = Pattern.compile("@");
        Matcher m = p.matcher(getVersion());
        //net.minecraft:client:1.19.4-20230314.122934:slim net\minecraft\client\1.19.4-20230314.122934\client-1.19.4-20230314.122934-srg.jar
        if (Objects.isNull(spaec)) {
            if (m.find()) {
                return getGroupId().replaceAll("\\.", "/") + "/" +
                        getArtifactId() + "/" + getVersion().substring(0, getVersion().indexOf("@"))
                        + "/" + getArtifactId() + "-" + getVersion().replaceAll("@", ".");
            } else {

                return getGroupId().replaceAll("\\.", "/") + "/"
                        + getArtifactId() + "/" + getVersion() + "/" + getArtifactId() + "-" + getVersion() + ".jar";
            }
        } else {
            if (m.find()) {
                return getGroupId().replaceAll("\\.", "/") + "/" + getArtifactId()
                        + "/" + getSpaec() + "/" + getArtifactId() + "-" + getSpaec() + "-" + getVersion().replaceAll("@", ".");
            } else {

                return getGroupId().replaceAll("\\.", "/") + "/" + getArtifactId()
                        + "/" + getSpaec() + "/" + getArtifactId() + "-" + getSpaec() + "-" + getVersion() + ".jar";
            }
        }
    }

    public String getSpaec() {
        return spaec;
    }

    public void setSpaec(String spaec) {
        this.spaec = spaec;
    }

    public DependencyDownload GetDependencyDownload() {
        return new DependencyDownload(lib);
    }

    public LibraryDependencyDownload GetLibraryDependencyDownload() {
        return new LibraryDependencyDownload(lib);
    }

    public String Clean(String str) {
        return str.replace(":", "").replace("[", "").replace("]", "");
    }
}
