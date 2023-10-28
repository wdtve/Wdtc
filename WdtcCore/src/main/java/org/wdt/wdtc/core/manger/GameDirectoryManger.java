package org.wdt.wdtc.core.manger;

import lombok.Getter;
import lombok.ToString;

import java.io.File;

@Getter
@ToString
public class GameDirectoryManger {
    //此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
    protected File here;

    public GameDirectoryManger() {
        this.here = SettingManger.getSetting().getDefaultGamePath();
    }

    public GameDirectoryManger(File here) {
        this.here = here;
    }

    public static File getDefaultHere() {
        return new File(System.getProperty("user.dir"));
    }

    public File getGameObjects() {
        return new File(getGameAssetsDirectory(), "objects");
    }


    public File getGameDirectory() {
        return new File(here, ".minecraft");
    }

    public File getGameLibraryDirectory() {
        return new File(getGameDirectory(), "libraries");
    }

    public File getGameVersionsDirectory() {
        return new File(getGameDirectory(), "versions");
    }

    public File getGameAssetsDirectory() {
        return new File(getGameDirectory(), "assets");
    }

}
