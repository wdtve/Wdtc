package org.WdtcDownload.SetFilePath;

public class SetPath {
    private static final String here = System.getProperty("user.dir");

    public static String getGameObjects() {
        return getGame_assetsdir() + "objects\\";
    }

    public static String getHere() {
        return here;
    }

    public static String getGame_path() {
        return here + "\\.minecraft\\";
    }

    public static String getGame_lib_path() {
        return here + "\\.minecraft\\libraries\\";
    }

    public static String getV_lib_path() {
        return here + "\\.minecraft\\versions\\";
    }

    public static String getGame_assetsdir() {
        return here + "\\.minecraft\\assets\\";
    }
}
