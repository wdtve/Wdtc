package org.wdt.wdtc.launch;


import org.wdt.platform.DependencyDownload;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.manger.UrlManger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GameLibraryPathAndUrl {
    private final Launcher launcher;
    private final DownloadSource source;


    public GameLibraryPathAndUrl(Launcher launcher) {
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    public File GetNativesLibraryFile(LibraryObject.NativesOs nativesOs) {
        return new File(launcher.getGameLibraryPath(), nativesOs.getPath());
    }

    public URL GetNativesLibraryUrl(LibraryObject libraryObject) throws IOException {
        LibraryObject.NativesOs Nativesindows = libraryObject.getDownloads().getClassifiers().getNativesindows();
        if (UrlManger.DownloadSourceList.NoOfficialDownloadSource()) {
            return new URL(source.getLibraryUrl() + Nativesindows.getPath());
        } else {

            return Nativesindows.getUrl();
        }
    }

    public File GetLibraryFile(LibraryObject object) {
        File LibraryPath = launcher.getGameLibraryPath();
        DependencyDownload dependency = new DependencyDownload(object.getLibraryName());
        dependency.setDownloadPath(LibraryPath);
        return dependency.getLibraryFile();

    }

    public URL GetLibraryUrl(LibraryObject libraryObject) throws MalformedURLException {
        if (UrlManger.DownloadSourceList.NoOfficialDownloadSource()) {
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
