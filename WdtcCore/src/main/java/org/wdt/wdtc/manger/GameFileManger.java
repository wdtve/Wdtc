package org.wdt.wdtc.manger;


import lombok.Getter;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Getter
public class GameFileManger extends GameFolderManger {
    protected final String VersionNumber;

    public GameFileManger(String VersionNumber) {
        this.VersionNumber = VersionNumber;
    }

    public GameFileManger(String VersionNumber, File here) {
        super(here);
        this.VersionNumber = VersionNumber;
    }

    public static void downloadVersionManifestJsonFileTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        try {
            if (FileUtils.isFileNotExists(FileManger.getVersionManifestFile()) ||
                    FileUtils.isFileOlder(FileManger.getVersionManifestFile(), calendar.getTime())) {
                DownloadVersionGameFile.DownloadVersionManifestJsonFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public File getVersionPath() {
        return new File(getGameVersionsPath(), VersionNumber);
    }

    public File getVersionJson() {
        return new File(getVersionPath(), VersionNumber + ".json");
    }

    public File getVersionJar() {
        return new File(getVersionPath(), VersionNumber + ".jar");
    }

    public File getVersionLog4j2() {
        return new File(getVersionPath(), "log4j2.xml");
    }

    public File getVersionNativesPath() {
        return new File(getVersionPath(), "natives-windows-x86_64");
    }

    public File getGameAssetsListJson() throws IOException {
        return new File(getGameAssetsdir(), "indexes/" + getGameVersionJsonObject().getAssets() + ".json");
    }

    public File getGameOptionsFile() {
        return new File(getVersionPath(), "options.txt");
    }

    public File getGameModsPath() {
        return new File(getVersionPath(), "mods");
    }

    public File getGameLogDir() {
        return new File(getVersionPath(), "logs");
    }

    public void PutToVersionJson(GameVersionJsonObject o) {
        JSONUtils.ObjectToJsonFile(getVersionJson(), o);
    }

    public GameVersionJsonObject getGameVersionJsonObject() throws IOException {
        return JSONUtils.JsonFileToClass(getVersionJson(), GameVersionJsonObject.class);
    }

    public File getVersionConfigFile() {
        return new File(getVersionPath(), "config.json");
    }
}
