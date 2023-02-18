package org.WdtcLauncher.JavaHome;

public class GetJavaPath {
    public static String getjavapath() {
        return "\"" + System.getProperty("java.home") + "\\bin\\java.exe\"";
    }
}
