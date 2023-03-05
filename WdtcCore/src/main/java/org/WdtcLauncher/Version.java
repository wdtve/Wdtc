package org.WdtcLauncher;

import org.WdtcDownload.SetFilePath.SetPath;

public class Version {
    private static String version;

    public Version(String version) {
        Version.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getVersion_lib_path() {
        return SetPath.getV_lib_path();
    }

    public String getVersion_path() {
        return getVersion_lib_path() + version + "\\";
    }

    public String getVersion_json() {
        return getVersion_path() + version + ".json";
    }
    public String getVersionJar() {
        return getVersion_path() + version + ".jar";
    }
    public String getVersionLog4j2() {
        return getVersion_path() + "log4j2.xml";
    }
    public String getVersionNativesPath() {
        return getVersion_path() + "natives-windows-x86_64";
    }
}
