package org.wdt.launch;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.wdt.AboutSetting;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.download.forge.ForgeLaunchTask;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class GetStartLibraryPath {
    private static StringBuilder ClassPathBuilder;

    public static void getLibPath(Launcher launcher) throws IOException {
        ClassPathBuilder = new StringBuilder();
        ForgeLaunchTask forgeLaunchTask = new ForgeLaunchTask(launcher, "45.0.64");
        GetLibraryPathAndUrl getLibraryPathAndUrl = new GetLibraryPathAndUrl(launcher);
        Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
        JSONObject v_e_j = PlatformUtils.FileToJSONObject(launcher.getVersionJson());
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (Objects.isNull(natives_j)) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (Objects.isNull(rules)) {
                    Space(getLibraryPathAndUrl.GetLibPath(lib_j));
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    String os_n = action_j.getJSONObject("os").getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        Space(getLibraryPathAndUrl.GetLibPath(lib_j));
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        Space(getLibraryPathAndUrl.GetLibPath(lib_j));
                    }
                }
            } else {
                String natives_name = natives_j.getString("windows");
                if (Objects.nonNull(natives_name)) {
                    ExtractFile.unzipByFile(getLibraryPathAndUrl.GetNativesLibPath(lib_j), launcher.getVersionNativesPath());
                }
            }
        }
        if (launcher.getForgeDownloadTaskIsNull()) {
            for (String s : forgeLaunchTask.getForgeLaunchLibrary()) {
                Space(s);
            }
        }
        Add(launcher.getVersionJar());
        if (launcher.getForgeDownloadTaskIsNull()) {
            for (String s : forgeLaunchTask.getForgeLuanchJvm()) {
                Add(FilenameUtils.separatorsToUnix(s));
            }
        }
        Add(launcher.GetAccounts().GetJvm());
        if (AboutSetting.GetLlvmpipeSwitch()) {
            Add(LlbmpipeLoader());
        }
        if (launcher.getForgeDownloadTaskIsNull()) {
            Add("cpw.mods.bootstraplauncher.BootstrapLauncher");
        } else {
            Add(v_e_j.getString("mainClass"));
        }
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
