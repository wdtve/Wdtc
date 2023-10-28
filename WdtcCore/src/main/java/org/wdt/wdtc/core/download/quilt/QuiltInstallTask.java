package org.wdt.wdtc.core.download.quilt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.LibraryObject;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONArray;
import org.wdt.wdtc.core.utils.gson.JSONObject;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuiltInstallTask extends QuiltDownloadInfo implements InstallTaskInterface {

    private static final Logger logmaker = WdtcLogger.getLogger(QuiltInstallTask.class);
    private final DownloadSourceInterface source;

    public QuiltInstallTask(Launcher launcher, String quiltVersionNumber) {
        super(launcher, quiltVersionNumber);
        this.source = Launcher.getDownloadSource();
    }


    public void DownloadQuiltGameVersionJson() {
        DownloadUtils.StartDownloadTask(getQuiltVersionJsonUrl(), getQuiltVersionJson());
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
        launcher.putToVersionJson(VersionJsonObject);
    }

    @Override
    public void setPatches() throws IOException {
        GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
        List<JsonObject> ObjectList = new ArrayList<>();
        ObjectList.add(JSONUtils.readJsonFiletoJsonObject(launcher.getVersionJson()));
        ObjectList.add(JSONUtils.readJsonFiletoJsonObject(getQuiltVersionJson()));
        Object.setJsonObject(ObjectList);
        launcher.putToVersionJson(Object);
    }

    @Override
    public void afterDownloadTask() {

    }

    @Override
    public void beforInstallTask() {
        DownloadQuiltGameVersionJson();
    }
}
