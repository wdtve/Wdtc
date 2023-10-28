package org.wdt.wdtc.core.launch;


import lombok.SneakyThrows;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.LibraryObject;
import org.wdt.wdtc.core.manger.URLManger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class GameLibraryData {
    private final Launcher launcher;
    private final DownloadSourceInterface source;


    public GameLibraryData(Launcher launcher) {
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    public File GetNativesLibraryFile(LibraryObject.NativesOs nativesOs) {
        return new File(launcher.getGameLibraryDirectory(), nativesOs.getPath());
    }

    @SneakyThrows(MalformedURLException.class)
    public URL GetNativesLibraryUrl(LibraryObject libraryObject) {
        LibraryObject.NativesOs Nativesindows = libraryObject.getDownloads().getClassifiers().getNativesindows();
        if (URLManger.DownloadSourceList.NoOfficialDownloadSource()) {
            return new URL(source.getLibraryUrl() + Nativesindows.getPath());
        } else {
            return Nativesindows.getUrl();
        }
    }

    public File GetLibraryFile(LibraryObject object) {
        File LibraryPath = launcher.getGameLibraryDirectory();
        DependencyDownload dependency = new DependencyDownload(object.getLibraryName());
        dependency.setDownloadPath(LibraryPath);
        return dependency.getLibraryFile();

    }

    @SneakyThrows(MalformedURLException.class)
    public URL GetLibraryUrl(LibraryObject libraryObject) {
        if (URLManger.DownloadSourceList.NoOfficialDownloadSource()) {
            DependencyDownload dependency = new DependencyDownload(libraryObject.getLibraryName());
            dependency.setDefaultUrl(source.getLibraryUrl());
            return dependency.getLibraryUrl();
        } else {
            return GetOfficialLibraryUrl(libraryObject);
        }
    }

    public URL GetOfficialLibraryUrl(LibraryObject object) {
        return object.getDownloads().getArtifact().getUrl();
    }

}
