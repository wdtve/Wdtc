package org.wdt.wdtc.launch;


import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.ExtractFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class GetStartLibraryPath {
    private static StringBuilder ClassPathBuilder;

    public static void getLibraryPath(Launcher launcher) throws IOException {
        ClassPathBuilder = new StringBuilder();
        GameLibraryPathAndUrl gameLibraryPathAndUrl = new GameLibraryPathAndUrl(launcher);
        Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
        JSONObject VersionJsonObject = JSONUtils.getJSONObject(launcher.getVersionJson());
        JSONArray LibrariesJsonObject = VersionJsonObject.getJSONArray("libraries");
        for (int i = 0; i < LibrariesJsonObject.size(); i++) {
            JSONObject LibraryObject = LibrariesJsonObject.getJSONObject(i);
            if (LibraryObject.has("natives")) {
                JSONObject NativesJson = LibraryObject.getJSONObject("natives");
                if (NativesJson.has("windows")) {
                    ExtractFile.unzipByFile(gameLibraryPathAndUrl.GetNativesLibPath(LibraryObject), launcher.getVersionNativesPath());
                }
            } else {
                JSONArray rules = LibraryObject.getJSONArray("rules");
                if (LibraryObject.has("rules")) {
                    JSONObject ActionObject = rules.getJSONObject(rules.size() - 1);
                    String action = ActionObject.getString("action");
                    String OsName = ActionObject.getJSONObject("os").getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(OsName, "osx")) {
                        Space(gameLibraryPathAndUrl.GetLibPath(LibraryObject));
                    } else if (Objects.equals(action, "allow") && Objects.equals(OsName, "windows")) {
                        Space(gameLibraryPathAndUrl.GetLibPath(LibraryObject));
                    }

                } else {
                    Space(gameLibraryPathAndUrl.GetLibPath(LibraryObject));
                }
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
