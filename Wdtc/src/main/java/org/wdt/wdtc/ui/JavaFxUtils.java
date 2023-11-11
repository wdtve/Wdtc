package org.wdt.wdtc.ui;

import com.google.gson.JsonArray;
import org.apache.log4j.Logger;
import org.wdt.utils.dependency.DependencyDownload;
import org.wdt.utils.gson.Json;
import org.wdt.utils.gson.JsonArrayUtils;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.FilenameUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.download.SpeedOfProgress;
import org.wdt.wdtc.core.game.LibraryObject;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.ThreadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class JavaFxUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(JavaFxUtils.class);


    public static void setJavaFXListJson() {
        try {
            File OpenJfxListFile = new File(FileManger.getWdtcImplementationPath(), "openjfx-list.json");
            if (FileUtils.isFileNotExistsAndIsNotSameSize(OpenJfxListFile, 2393)) {
                List<String> MoudleList = List.of("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics", "javafx.media");
                JsonArray array = new JsonArray();
                for (String s : MoudleList) {
                    String path = "org/openjfx/" + s.replaceAll("\\.", "-") + "/17.0.6/" + s.replaceAll("\\.", "-") + "-17.0.6-win.jar";
                    URL url = new URL("https://maven.aliyun.com/repository/public/" + path);
                    LibraryObject.Artifact artifact = new LibraryObject.Artifact();
                    artifact.setUrl(url);
                    artifact.setPath(FilenameUtils.separatorsToUnix(path));
                    artifact.setSha1(IOUtils.getInputStreamSha1(url.openStream()));
                    artifact.setSize(url.openConnection().getContentLength());
                    LibraryObject.Downloads downloads = new LibraryObject.Downloads();
                    downloads.setArtifact(artifact);
                    LibraryObject libraryObject = new LibraryObject();
                    libraryObject.setDownloads(downloads);
                    libraryObject.setLibraryName("org.openjfx:" + s + ":win:17.0.6");
                    array.add(Json.GSON.toJsonTree(libraryObject, LibraryObject.class));
                }
                JsonUtils.writeObjectToFile(OpenJfxListFile, array);
            }
        } catch (IOException e) {
            logmaker.warn(WdtcLogger.getExceptionMessage(e));
        }
    }

    public static void downloadDependencies() throws IOException {
        JsonArray array = getArrayObject();
        SpeedOfProgress speed = new SpeedOfProgress(array.size());
        for (int i = 0; i < array.size(); i++) {
            LibraryObject libraryObject = LibraryObject.getLibraryObject(array.get(i).getAsJsonObject());
            LibraryObject.Artifact artifact = libraryObject.getDownloads().getArtifact();
            File Library = new File(FileManger.getWtdcOpenJFXPath(), artifact.getPath());
            ThreadUtils.startThread(() -> {
                try {
                    if (FileUtils.isFileNotExistsAndIsNotSameSize(Library, artifact.getSize())) {
                        DownloadUtils.startDownloadTask(artifact.getUrl(), Library);
                    }
                    speed.countDown();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        speed.await();
    }

    private static JsonArray getArrayObject() throws IOException {
        return JsonArrayUtils.parseJsonArray(IOUtils.toString(Objects.requireNonNull(JavaFxUtils.class.getResourceAsStream("/openjfx-list.json"))));
    }

    public static void loadJavaFXPatch() {
        try {
            Class.forName("javafx.application.Application");
        } catch (ClassNotFoundException e) {
            try {
                logmaker.info("Load JavaFX Dependencies");
                JsonArray array = getArrayObject();
                Set<Path> jarPaths = new HashSet<>();
                Set<String> modules = new HashSet<>();
                for (int i = 0; i < array.size(); i++) {
                    LibraryObject libraryObject = LibraryObject.getLibraryObject(array.get(i).getAsJsonObject());
                    LibraryObject.Artifact artifact = libraryObject.getDownloads().getArtifact();
                    File library = new File(FileManger.getWtdcOpenJFXPath(), artifact.getPath());
                    jarPaths.add(library.toPath());
                    modules.add(new DependencyDownload(libraryObject.getLibraryName()).getArtifactId());
                }
                // Form : HMCL3
                ModuleFinder finder = ModuleFinder.of(jarPaths.toArray(Path[]::new));
                for (ModuleReference mref : finder.findAll()) {
                    ((jdk.internal.loader.BuiltinClassLoader) ClassLoader.getSystemClassLoader()).loadModule(mref);
                }
                Configuration config = Configuration.resolveAndBind(finder, List.of(ModuleLayer.boot().configuration()), finder, modules);
                ModuleLayer.defineModules(config, List.of(ModuleLayer.boot()), name -> ClassLoader.getSystemClassLoader());
                logmaker.info("Done");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void ckeckJavaFX() throws IOException {
        downloadDependencies();
        loadJavaFXPatch();
    }
}

