package org.wdt.wdtc.launch;


import org.wdt.wdtc.platform.SettingManger;

import java.io.File;

public class GamePath {
    //此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
    protected File here = SettingManger.getSetting().getDefaultGamePath();

    public GamePath() {
    }

    public GamePath(File here) {
        this.here = here;
    }

    public static File getDefaultHere() {
        return new File(System.getProperty("user.dir"));
    }

    public File getGameObjects() {
        return new File(getGameAssetsdir(), "objects");
    }

    public File getHere() {
        return here;
    }


    public File getGamePath() {
        return new File(here, ".minecraft");
    }

    public File getGameLibraryPath() {
        return new File(getGamePath(), "libraries");
    }

    public File getGameVersionsPath() {
        return new File(getGamePath(), "versions");
    }

    public File getGameAssetsdir() {
        return new File(getGamePath(), "assets");
    }

    @Override
    public String toString() {
        return "GamePath{" +
                "here=" + here +
                '}';
    }
}
