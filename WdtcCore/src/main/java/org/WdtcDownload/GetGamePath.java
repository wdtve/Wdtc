package org.WdtcDownload;

public class GetGamePath {
    private static final String here = System.getProperty("user.dir");

    public static String getGameObjects() {
        return getGameAssetsdir() + "objects\\";
    }

    public static String getHere() {
        return here;
    }

    public static String getGamePath() {
        return here + "\\.minecraft\\";
    }

    public static String GetGameLibPath() {
        return here + "\\.minecraft\\libraries\\";
    }

    public static String getGameVersionPath() {
        return here + "\\.minecraft\\versions\\";
    }

    public static String getGameAssetsdir() {
        return here + "\\.minecraft\\assets\\";
    }
}
