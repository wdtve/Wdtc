package org.wdt.wdtc.utils;

import com.google.gson.JsonArray;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.download.infterface.DownloadInfo;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;

import java.io.IOException;
import java.util.List;

public class ExecuteUtils {
    private final Launcher launcher;
    private final JSONObject ModVersionJsonObject;
    private final DownloadSource source;

    public ExecuteUtils(Launcher launcher, JSONObject modVersionJsonObject) {
        this.launcher = launcher;
        ModVersionJsonObject = modVersionJsonObject;
        this.source = Launcher.getDownloadSource();
    }

    public void execute(ModUtils.KindOfMod kind) throws IOException {
        DownloadInfo info = ModUtils.getModDownloadInfo(launcher);
        GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
        List<LibraryObject> LibraryObjectList = VersionJsonObject.getLibraries();
        JSONArray FabricLibraryList = ModVersionJsonObject.getJSONArray("libraries");
        for (int i = 0; i < FabricLibraryList.size(); i++) {
            JSONObject object = FabricLibraryList.getJSONObject(i);
            DependencyDownload dependency = new DependencyDownload(object.getString("name"));
            dependency.setDefaultUrl(source.getFabricLibraryUrl());
            dependency.setDownloadPath(launcher.getGameLibraryPath());
            LibraryObjectList.add(LibraryObject.getLibraryObject(dependency, object.getString("url")));
        }
        VersionJsonObject.setLibraries(LibraryObjectList);
        VersionJsonObject.setMainClass(ModVersionJsonObject.getString("mainClass"));
        GameVersionJsonObject.Arguments arguments = VersionJsonObject.getArguments();
        JSONObject Arguments = ModVersionJsonObject.getJSONObject("arguments");
        JsonArray JvmList = arguments.getJvmList();
        JvmList.addAll(Arguments.getJSONArray("jvm").getJsonArrays());
        arguments.setJvmList(JvmList);
        JsonArray GameList = arguments.getGameList();
        GameList.addAll(Arguments.getJSONArray("game").getJsonArrays());
        arguments.setGameList(GameList);
        VersionJsonObject.setArguments(arguments);
        if (info != null) {
            VersionJsonObject.setId(launcher.getVersionNumber() + kind.toString().toLowerCase() + info.getModVersion());
        }

        launcher.PutToVersionJson(VersionJsonObject);
    }
}
