package org.wdt.wdtc.download;

import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.game.Launcher;

import java.io.IOException;

public class DownloadGameVersion {
    protected final Launcher launcher;
    protected final DownloadVersionGameFile DownloadGame;
    protected boolean Install;

    public DownloadGameVersion(Launcher launcher) {
        this(launcher, false);
    }

    public DownloadGameVersion(Launcher launcher, boolean Install) {
        this.launcher = launcher;
        this.Install = Install;
        this.DownloadGame = new DownloadVersionGameFile(launcher, Install);
    }

    public void DownloadGameFile() throws IOException {
        DownloadGame.DownloadGameVersionJson();
        DownloadGame.DownloadGameAssetsListJson();
        DownloadGame.DownloadVersionJar();
    }

    public void DownloadGameLibrary() {
        DownloadGame.DownloadGameLibraryFileTask().DownloadLibraryFile();
    }

    public void DownloadResourceFile() {
        DownloadGame.DownloadResourceFileTask().DownloadResourceFile();
    }

    public void DownloadGame() throws IOException {
        DownloadGameFile();
        DownloadGameLibrary();
        DownloadResourceFile();
    }
}
