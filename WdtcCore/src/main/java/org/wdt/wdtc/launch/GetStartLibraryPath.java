package org.wdt.wdtc.launch;


import org.wdt.utils.FilenameUtils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.GetGameNeedLibraryFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetStartLibraryPath {
    private final StringBuilder ClassPathBuilder;
    private final Launcher launcher;

    public GetStartLibraryPath(Launcher launcher) {
        ClassPathBuilder = new StringBuilder();
        this.launcher = launcher;
    }

    public StringBuilder getLibraryPath() {
        try {
            GameLibraryPathAndUrl gameLibraryPathAndUrl = new GameLibraryPathAndUrl(launcher);
            Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
            GetGameNeedLibraryFile FileList = new GetGameNeedLibraryFile(launcher);
            for (GetGameNeedLibraryFile.LibraryFile LibraryFile : FileList.getFileList()) {
                if (LibraryFile.isNativesLibrary()) {
                    ZipUtils.unzipByFile(gameLibraryPathAndUrl.GetNativesLibraryFile(LibraryFile.getLibraryObject().getDownloads().getClassifiers().getNativesindows()), launcher.getVersionNativesPath());
                } else {
                    InsertclasspathSeparator(gameLibraryPathAndUrl.GetLibraryFile(LibraryFile.getLibraryObject()));
                }
            }
            ClassPathBuilder.append(FilenameUtils.separatorsToWindows(launcher.getVersionJar()));
            String Accounts = launcher.GetAccounts().getJvm();
            if (!Accounts.isEmpty()) {
                ClassPathBuilder.append(launcher.GetAccounts().getJvm());
            }
            if (AboutSetting.getSetting().isLlvmpipeLoader()) {
                ClassPathBuilder.append(LlbmpipeLoader());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ClassPathBuilder;
    }


    private String LlbmpipeLoader() {
        return " -javaagent:" + FilePath.getLlbmpipeLoader();
    }

    private void InsertclasspathSeparator(String str) {
        ClassPathBuilder.append(str).append(";");
    }

    private void InsertclasspathSeparator(File file) {
        InsertclasspathSeparator(file.getAbsolutePath());
    }

    private void InsertSpace(String str) {
        ClassPathBuilder.append(str).append(" ");
    }
}
