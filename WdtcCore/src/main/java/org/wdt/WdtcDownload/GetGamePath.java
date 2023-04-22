package org.wdt.WdtcDownload;

public class GetGamePath {
    //此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
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
