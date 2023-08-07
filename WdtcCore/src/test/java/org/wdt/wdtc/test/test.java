package org.wdt.wdtc.test;

import org.junit.jupiter.api.Test;
import org.wdt.platform.DependencyDownload;
import org.wdt.wdtc.game.LibraryObject;

import java.io.IOException;

public class test {
    @Test
    public void getVersion() throws IOException {
        DependencyDownload dependencyDownload = new DependencyDownload("org.lwjgl:lwjgl-openal:3.3.1");
        LibraryObject object = new LibraryObject();
        LibraryObject.Downloads downloads = new LibraryObject.Downloads();
        LibraryObject.Artifact artifact = new LibraryObject.Artifact();
        artifact.setPath(dependencyDownload.formJar());
        artifact.setSize(88237);
        artifact.setSha1("2623a6b8ae1dfcd880738656a9f0243d2e6840bd");
        dependencyDownload.setDefaultUrl("https://libraries.minecraft.net/");
        artifact.setUrl(dependencyDownload.getLibraryUrl());
        downloads.setArtifact(artifact);
        object.setDownloads(downloads);
        object.setLibraryName(dependencyDownload.getLibraryName());
        System.out.println(object);
    }

    @Test
    public void StringToClss() throws IOException {

    }
}
