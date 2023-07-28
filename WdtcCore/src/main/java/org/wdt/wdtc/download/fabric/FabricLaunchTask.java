package org.wdt.wdtc.download.fabric;

import org.wdt.platform.DependencyDownload;
import org.wdt.wdtc.game.Launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricLaunchTask extends FabricFileList {
    private final Launcher launcher;


    public FabricLaunchTask(Launcher launcher, String FabricVersionNumber) {
        super(launcher, FabricVersionNumber);
        this.launcher = launcher;
    }

    public List<String> getFabricLibraryList() throws IOException {
        List<String> LibraryList = new ArrayList<>();
        for (String name : getFabricFileName()) {
            DependencyDownload download = new DependencyDownload(name);
            download.setDownloadPath(launcher.GetGameLibraryPath());
            LibraryList.add(download.getLibraryFilePath());
        }
        return LibraryList;
    }

    public String getJvm() {
        return "-DFabricMcEmu=net.minecraft.client.main.Main";
    }
}
