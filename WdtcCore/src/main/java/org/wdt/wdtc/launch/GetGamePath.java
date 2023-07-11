package org.wdt.wdtc.launch;

public class GetGamePath {
    //此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
    private String here = System.getProperty("user.dir");

    public GetGamePath() {
    }

    public GetGamePath(String here) {
        this.here = here;
    }

    public static String getDefaultHere() {
        return System.getProperty("user.dir");
    }

    public String getGameObjects() {
        return getGameAssetsdir() + "objects\\";
    }

    public String getHere() {
        return here;
    }


    public String getGamePath() {
        return here + "\\.minecraft\\";
    }

    public String GetGameLibraryPath() {
        return here + "\\.minecraft\\libraries\\";
    }

    public String getGameVersionPath() {
        return here + "\\.minecraft\\versions\\";
    }

    public String getGameAssetsdir() {
        return here + "\\.minecraft\\assets\\";
    }
}
