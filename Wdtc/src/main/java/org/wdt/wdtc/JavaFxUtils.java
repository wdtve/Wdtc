package org.wdt.wdtc;

import com.google.gson.JsonArray;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.utils.FilenameUtils;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

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
            File OpenJfxListFile = new File(FilePath.getWdtcImplementationPath() + "/openjfx-list.json");
            if (PlatformUtils.FileExistenceAndSize(OpenJfxListFile, 2393)) {
                List<String> MoudleList = List.of("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics", "javafx.media");
                JsonArray array = new JsonArray();
                for (String s : MoudleList) {
                    String path = "org/openjfx/" + s.replaceAll("\\.", "-") + "/17.0.6/" + s.replaceAll("\\.", "-") + "-17.0.6-win.jar";
                    URL url = new URL("https://maven.aliyun.com/repository/public/" + path);
                    LibraryObject.Artifact artifact = new LibraryObject.Artifact();
                    artifact.setUrl(url);
                    artifact.setPath(FilenameUtils.separatorsToUnix(path));
                    artifact.setSha1(PlatformUtils.getFileSha1(url.openStream()));
                    artifact.setSize(url.openConnection().getContentLength());
                    LibraryObject.Downloads downloads = new LibraryObject.Downloads();
                    downloads.setArtifact(artifact);
                    LibraryObject libraryObject = new LibraryObject();
                    libraryObject.setDownloads(downloads);
                    libraryObject.setLibraryName("org.openjfx:" + s + ":win:17.0.6");
                    array.add(JSONObject.GSON.toJsonTree(libraryObject, LibraryObject.class));
                }
                JSONUtils.ObjectToJsonFile(OpenJfxListFile, array);
            }
        } catch (IOException e) {
            logmaker.warn("", e);
        }
    }

    public static void DownloadDependencies() throws IOException {
        JSONArray array = getArrayObject();
        SpeedOfProgress speed = new SpeedOfProgress(array.size());
        for (int i = 0; i < array.size(); i++) {
            LibraryObject libraryObject = LibraryObject.getLibraryObject(array.getJSONObject(i));
            LibraryObject.Artifact artifact = libraryObject.getDownloads().getArtifact();
            File Library = new File(FilePath.getWtdcOpenJFXPath() + "/" + artifact.getPath());
            ThreadUtils.StartThread(() -> {
                try {
                    if (PlatformUtils.FileExistenceAndSize(Library, artifact.getSize())) {
                        DownloadTask.StartDownloadTask(artifact.getUrl(), Library);
                    }
                    speed.countDown();
                } catch (IOException e) {
                    logmaker.warn("", e);
                }
            });
        }
        speed.await();
    }

    private static JSONArray getArrayObject() throws IOException {
        return JSONArray.parseJSONArray(IOUtils.toString(Objects.requireNonNull(JavaFxUtils.class.getResourceAsStream("/openjfx-list.json"))));
    }

    public static void loadJavaFXPatch() {
        try {
            Class.forName("javafx.application.Application");
        } catch (ClassNotFoundException e) {
            try {
                logmaker.info("* Load JavaFX Dependencies");
                JSONArray array = getArrayObject();
                Set<Path> jarPaths = new HashSet<>();
                Set<String> modules = new HashSet<>();
                for (int i = 0; i < array.size(); i++) {
                    LibraryObject libraryObject = LibraryObject.getLibraryObject(array.getJSONObject(i));
                    LibraryObject.Artifact artifact = libraryObject.getDownloads().getArtifact();
                    File Library = new File(FilePath.getWtdcOpenJFXPath() + "/" + artifact.getPath());
                    jarPaths.add(Library.toPath());
                    modules.add(new DependencyDownload(libraryObject.getLibraryName()).getArtifactId());
                }
                // Form : HMCL3
                ModuleFinder finder = ModuleFinder.of(jarPaths.toArray(Path[]::new));
                for (ModuleReference mref : finder.findAll()) {
                    ((jdk.internal.loader.BuiltinClassLoader) ClassLoader.getSystemClassLoader()).loadModule(mref);
                }
                Configuration config = Configuration.resolveAndBind(finder, List.of(ModuleLayer.boot().configuration()), finder, modules);
                ModuleLayer.defineModules(config, List.of(ModuleLayer.boot()), name -> ClassLoader.getSystemClassLoader());
                logmaker.info("* Done");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void CkeckJavaFX() throws IOException {
        DownloadDependencies();
        loadJavaFXPatch();
    }
}

