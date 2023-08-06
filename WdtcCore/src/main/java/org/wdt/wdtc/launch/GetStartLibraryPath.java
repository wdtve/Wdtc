package org.wdt.wdtc.launch;


import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.GetGameNeedLibraryFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.ExtractFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetStartLibraryPath {
    private static StringBuilder ClassPathBuilder;

    public static void getLibraryPath(Launcher launcher) throws IOException {
        ClassPathBuilder = new StringBuilder();
        GameLibraryPathAndUrl gameLibraryPathAndUrl = new GameLibraryPathAndUrl(launcher);
        Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
        GetGameNeedLibraryFile FileList = new GetGameNeedLibraryFile(launcher);
        for (GetGameNeedLibraryFile.LibraryFile LibraryFile : FileList.getFileList()) {
            if (LibraryFile.isNativesLibrary()) {
                ExtractFile.unzipByFile(gameLibraryPathAndUrl.GetNativesLibraryFile(LibraryFile.getLibraryObject().getDownloads().getClassifiers().getNativesindows()), launcher.getVersionNativesPath());
            } else {
                Space(gameLibraryPathAndUrl.GetLibraryFile(LibraryFile.getLibraryObject()));
            }
        }
        ClassPathBuilder.append(AdditionalCommand.AdditionalLibrary(launcher));
        Add(launcher.getVersionJar());
        ClassPathBuilder.append(AdditionalCommand.AdditionalJvm(launcher));
        Add(launcher.GetAccounts().getJvm());
        if (AboutSetting.getSetting().isLlvmpipeLoader()) {
            Add(LlbmpipeLoader());
        }
        ClassPathBuilder.append(AdditionalCommand.GameMainClass(launcher));
        launcher.setLibrartattribute(ClassPathBuilder);
    }


    private static String LlbmpipeLoader() {
        return "-javaagent:" + FilePath.getLlbmpipeLoader();
    }

    private static void Space(String str) {
        ClassPathBuilder.append(str).append(";");
    }

    private static void Space(File file) {
        Space(file.getAbsolutePath());
    }

    private static void Add(String str) {
        ClassPathBuilder.append(str).append(" ");
    }
}
