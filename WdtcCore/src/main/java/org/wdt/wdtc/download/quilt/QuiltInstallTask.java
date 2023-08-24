package org.wdt.wdtc.download.quilt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuiltInstallTask extends QuiltDownloadInfo implements InstallTask {

    private static final Logger logmaker = WdtcLogger.getLogger(QuiltInstallTask.class);
    private final DownloadSource source;

    public QuiltInstallTask(Launcher launcher, String quiltVersionNumber) {
        super(launcher, quiltVersionNumber);
        this.source = Launcher.getDownloadSource();
    }


    public void DownloadQuiltGameVersionJson() {
        DownloadTask.StartDownloadTask(getQuiltVersionJsonUrl(), getQuiltVersionJson());
    }


    @Override
    public void execute() throws IOException {
        GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
        JSONObject QuiltVersionJsonObject = getQuiltGameVersionJsonObject();
        JSONObject Arguments = QuiltVersionJsonObject.getJSONObject("arguments");
        GameVersionJsonObject.Arguments arguments = VersionJsonObject.getArguments();
        JsonArray GameList = arguments.getGameList();
        VersionJsonObject.setMainClass(QuiltVersionJsonObject.getString("mainClass"));
        GameList.addAll(Arguments.getJSONArray("game").getJsonArrays());
        arguments.setGameList(GameList);
        VersionJsonObject.setArguments(arguments);
        JSONArray QuiltLibraryList = QuiltVersionJsonObject.getJSONArray("library");
        List<LibraryObject> LibraryList = VersionJsonObject.getLibraries();
        for (int i = 0; i < QuiltLibraryList.size(); i++) {
            JSONObject QuiltLibraryObject = QuiltLibraryList.getJSONObject(i);
            DependencyDownload download = new DependencyDownload(QuiltLibraryObject.getString("name"));
            String LibraryDefaultUrl = QuiltLibraryObject.getString("url");
            if (LibraryDefaultUrl.equals("https://maven.fabricmc.net/")) {
                download.setDefaultUrl(source.getFabricLibraryUrl());
            } else if (LibraryDefaultUrl.equals("https://maven.quiltmc.org/repository/release/")) {
                download.setDefaultUrl("https://maven.quiltmc.org/repository/release/");
            }
            LibraryList.add(LibraryObject.getLibraryObject(download, LibraryDefaultUrl));
        }
        VersionJsonObject.setLibraries(LibraryList);
        VersionJsonObject.setId(launcher.getVersionNumber() + "-quilt-" + QuiltVersionNumber);
        launcher.PutToVersionJson(VersionJsonObject);
    }

    @Override
    public void setPatches() throws IOException {
        GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
        List<JsonObject> ObjectList = new ArrayList<>();
        ObjectList.add(JSONUtils.getJsonObject(launcher.getVersionJson()));
        ObjectList.add(JSONUtils.getJsonObject(getQuiltVersionJson()));
        Object.setJsonObject(ObjectList);
        launcher.PutToVersionJson(Object);
    }

    @Override
    public void AfterDownloadTask() {

    }

    @Override
    public void BeforInstallTask() {
        DownloadQuiltGameVersionJson();
    }
}
