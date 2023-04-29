package org.wdt.WdtcLauncher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetJavaPath {
    private static final List<String> AllJavaHomePath = new ArrayList<>();

    public static String GetRunJavaHome() {
        return "\"" + System.getProperty("java.home") + "\\bin\\java.exe\"";
    }

    public static List<String> GetAllJavaHome() {
        File[] dishes = File.listRoots();
        for (File file : dishes) {
            function("java.exe", file);
        }
        return AllJavaHomePath;
    }

    private static void function(String filename, File dir) {
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    function(filename, file.getAbsoluteFile());
                }
                if (file.isFile() && filename.equals(file.getName())) {
                    AllJavaHomePath.add(file.getAbsolutePath());
                    break;
                }
            }
        }
    }
}
