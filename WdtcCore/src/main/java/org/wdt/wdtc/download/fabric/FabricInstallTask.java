package org.wdt.wdtc.download.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricInstallTask extends FabricFileList implements InstallTask {
    private static final Logger logger = WdtcLogger.getLogger(FabricInstallTask.class);
    private final Launcher launcher;
    private final DownloadSource source;
    private FabricAPIDownloadTask APIDownloadTask = null;


    public FabricInstallTask(Launcher launcher, String FabricVersionNumber) {
        super(launcher, FabricVersionNumber);
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();

    }

    public FabricAPIDownloadTask getAPIDownloadTask() {
        return APIDownloadTask;
    }

    public void setAPIDownloadTask(FabricAPIDownloadTask APIDownloadTask) {
        this.APIDownloadTask = APIDownloadTask;
    }

    public boolean getAPIDownloadTaskNoNull() {
        return APIDownloadTask != null;
    }

    @Override
    public void execute() throws IOException {
        GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
        List<LibraryObject> libraryObjectList = Object.getLibraries();
        for (String name : getFabricFileName()) {
            DependencyDownload dependency = new DependencyDownload(name);
            dependency.setDefaultUrl(source.getFabricLibraryUrl());
            dependency.setDownloadPath(launcher.GetGameLibraryPath());
            libraryObjectList.add(LibraryObject.getLibraryObject(dependency));
        }
        Object.setLibraries(libraryObjectList);
        Object.setMainClass("net.fabricmc.loader.impl.launch.knot.KnotClient");
        GameVersionJsonObject.Arguments arguments = Object.getArguments();
        JsonArray JvmList = arguments.getJvmList();
        JvmList.add("-DFabricMcEmu=net.minecraft.client.main.Main");
        arguments.setJvmList(JvmList);
        Object.setArguments(arguments);
        launcher.PutToVersionJson(Object);
    }

    @Override
    public void setPatches() throws IOException {
        writeCacheVersionJson();
        GameVersionJsonObject Object = launcher.getGameVersionJsonObject();
        List<JsonObject> ObjectList = new ArrayList<>();
        ObjectList.add(JSONUtils.getJsonObject(launcher.getVersionJson()));
        ObjectList.add(JSONUtils.getJsonObject(getCacheVersionJson()));
        Object.setJsonObject(ObjectList);
        launcher.PutToVersionJson(Object);
    }

    @Override
    public void AfterDownloadTask() {

    }
}