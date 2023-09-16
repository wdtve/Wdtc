package org.wdt.utils.io;

public class FilenameUtils {
    private static final String WINDOWS_FILE_SEPARATOR = "\\";
    private static final String UNIX_FILE_SEPARATOR = "/";

    public static String separatorsToUnix(String path) {
        return path.replace(WINDOWS_FILE_SEPARATOR, UNIX_FILE_SEPARATOR);
    }

    public static String separatorsToWindows(String path) {
        return path.replace(UNIX_FILE_SEPARATOR, WINDOWS_FILE_SEPARATOR);
    }

    public static String getExtension(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }
}
