package org.wdt.wdtc.manger;

import lombok.Getter;
import lombok.ToString;

import java.io.File;

@Getter
@ToString
public class GameFolderManger {
    //此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
    protected File here;

    public GameFolderManger() {
        this.here = SettingManger.getSetting().getDefaultGamePath();
    }

    public GameFolderManger(File here) {
        this.here = here;
    }

    public static File getDefaultHere() {
        return new File(System.getProperty("user.dir"));
    }

    public File getGameObjects() {
        return new File(getGameAssetsdir(), "objects");
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

}
