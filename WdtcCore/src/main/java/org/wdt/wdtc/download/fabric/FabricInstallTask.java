package org.wdt.wdtc.download.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.utils.gson.JSONArray;
import org.wdt.wdtc.utils.gson.JSONObject;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricInstallTask extends FabricDonwloadInfo implements InstallTask {
    private final DownloadSource source;



    public FabricInstallTask(Launcher launcher, String FabricVersionNumber) {
        super(launcher, FabricVersionNumber);
        this.source = Launcher.getDownloadSource();

    }



    @Override
    public void execute() throws IOException {
        GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
        List<LibraryObject> libraryObjectList = VersionJsonObject.getLibraries();
        JSONObject FabricVersionJsonObject = getFabricVersionJsonObject();
        JSONArray FabricLibraryList = FabricVersionJsonObject.getJSONArray("libraries");
        for (int i = 0; i < FabricLibraryList.size(); i++) {
            JSONObject object = FabricLibraryList.getJSONObject(i);
            DependencyDownload dependency = new DependencyDownload(object.getString("name"));
            dependency.setDefaultUrl(source.getFabricLibraryUrl());
            dependency.setDownloadPath(launcher.getGameLibraryPath());
            libraryObjectList.add(LibraryObject.getLibraryObject(dependency, object.getString("url")));
        }
        VersionJsonObject.setLibraries(libraryObjectList);
        VersionJsonObject.setMainClass(FabricVersionJsonObject.getString("mainClass"));
        GameVersionJsonObject.Arguments arguments = VersionJsonObject.getArguments();
        JSONObject Arguments = FabricVersionJsonObject.getJSONObject("arguments");
        JsonArray JvmList = arguments.getJvmList();
        JvmList.addAll(Arguments.getJSONArray("jvm").getJsonArrays());
        arguments.setJvmList(JvmList);
        JsonArray GameList = arguments.getGameList();
        GameList.addAll(Arguments.getJSONArray("game").getJsonArrays());
        arguments.setGameList(GameList);
        VersionJsonObject.setArguments(arguments);
        VersionJsonObject.setId(launcher.getVersionNumber() + "-fabric-" + FabricVersionNumber);
        launcher.PutToVersionJson(VersionJsonObject);
    }


    @Override
    public void setPatches() throws IOException {
        GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
        List<JsonObject> ObjectList = new ArrayList<>();
        ObjectList.add(JSONUtils.getJsonObject(launcher.getVersionJson()));
        ObjectList.add(JSONUtils.getJsonObject(getFabricVersionJson()));
        Object.setJsonObject(ObjectList);
        launcher.PutToVersionJson(Object);
    }

    @Override
    public void AfterDownloadTask() throws IOException {
        if (getAPIDownloadTaskNoNull()) {
            getAPIDownloadTask().DownloadFabricAPI();
        }
    }

    @Override
    public void BeforInstallTask() {
        DownloadTask.StartDownloadTask(String.format(getFabricVersionFileUrl(), launcher.getVersionNumber(), FabricVersionNumber), getFabricVersionJson());
    }
}