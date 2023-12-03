package org.wdt.wdtc.core.utils;

import lombok.Data;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.manger.SettingManger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {
  private static final Logger logmaker = WdtcLogger.getLogger(JavaUtils.class);

  public static String getRunJavaHome() {
    return getJavaExePath(new File(System.getProperty("java.home")));
  }

  public static void main(String[] args) {
    try {
      for (String s : args) {
        if (DownloadUtils.isDownloadProcess()) {
          break;
        }
        getPotentialJava(s);
      }
      logmaker.info("Find Java Done");
    } catch (IOException e) {
      logmaker.error("Error, ", e);
    }
  }

  private static void getPotentialJava(String key) throws IOException {
    Process process = new ProcessBuilder("reg", "query", key).start();
    SettingManger.Setting setting = SettingManger.getSetting();
    Set<JavaInfo> newJavaList = setting.getJavaPath();
    for (String s : IOUtils.readLines(process.getInputStream())) {
      if (s.startsWith(key)) {
        for (Map<String, String> map : getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(s)))) {
          JavaInfo newInfo = new JavaUtils.JavaInfo(new File(map.get("JavaPath")));
          if (newJavaList.add(newInfo)) {
            logmaker.info("Find Java : " + newInfo.getJavaExeFile() + ", Version : " + newInfo.getVersionNumber());
          }

        }
      }
    }
    setting.setJavaPath(newJavaList);
    SettingManger.putSettingToFile(setting);
  }


  private static List<String> getPotentialJavaFolders(String key) throws IOException {
    List<String> List = new ArrayList<>();
    Process process = new ProcessBuilder("reg", "query", key).start();
    for (String s : IOUtils.readLines(process.getInputStream())) {
      if (s.startsWith(key)) {
        List.add(s);
      }
    }
    return List;
  }

  private static List<String> getPotentialJavaHome(List<String> list) throws IOException {
    List<String> JavaHomeList = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\s+JavaHome\\s+REG_SZ\\s+(.+)");
    for (String key : list) {
      Process process = new ProcessBuilder("reg", "query", key, "/v", "JavaHome").start();
      for (String s : IOUtils.readLines(process.getInputStream())) {
        String JavaHomeCleaned = StringUtils.cleanStrInString(s, " ");
        if (JavaHomeCleaned.startsWith("JavaHome")) {
          Matcher matcher = pattern.matcher(s);
          if (matcher.find()) {
            JavaHomeList.add(matcher.group(1));
          }
        }
      }
    }
    return JavaHomeList;
  }

  public static List<Map<String, String>> getJavaExeAndVersion(List<String> list) throws IOException {
    List<Map<String, String>> JavaList = new ArrayList<>();
    for (String path : list) {
      String JavaPath = getJavaExePath(new File(path));
      if (!Files.exists(Paths.get(path))) {
        logmaker.warn("warn : ", new IOException(path + " isn't exists"));
      } else {
        if (FileUtils.isFileExists(FileUtils.toFile(JavaPath))) {
          Map<String, String> JavaExeAndVersion = new HashMap<>();
          JavaExeAndVersion.put("JavaPath", path);
          JavaExeAndVersion.put("JavaVersion", getJavaVersion(JavaPath));
          JavaList.add(JavaExeAndVersion);
        }
      }
    }
    return JavaList;
  }

  public static String getJavaVersion(String JavaPath) {
    try {
      Process process = Runtime.getRuntime().exec(new String[]{JavaPath, "-XshowSettings:properties", "-version"});
      Pattern pattern = Pattern.compile("java\\.version = (.+)");
      for (String line : IOUtils.readLines(process.getErrorStream())) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          return matcher.group(1);
        }
      }
    } catch (IOException e) {
      logmaker.error(WdtcLogger.getExceptionMessage(e));
    }
    return null;
  }

  public static String getJavaExePath(File JavaHome) {
    String JavaHomePath = FileUtils.getCanonicalPath(JavaHome);
    if (Pattern.compile("\\s").matcher(JavaHomePath).find()) {
      if (JavaHomePath.endsWith("\\")) {
        return "\"" + JavaHome + "bin\\java.exe\"";
      } else {
        return "\"" + JavaHome + "\\bin\\java.exe\"";
      }
    } else {
      if (JavaHomePath.endsWith("\\")) {
        return JavaHome + "bin\\java.exe";
      } else {
        return JavaHome + "\\bin\\java.exe";
      }
    }
  }

  public static File getJavaExeFile(File JavaHome) {
    return new File(JavaHome, "bin/java.exe");
  }


  public enum JavaTips {
    JDK, JRE;

    public static boolean isJRE(File JavaHome) {
      try {
        return FileUtils.isFileNotExists(new File(JavaHome, "bin/javac.exe"));
      } catch (IOException e) {
        logmaker.error(WdtcLogger.getExceptionMessage(e));
      }
      return false;
    }

    public static JavaTips getJavaTips(File JavaHomeFile) {
      if (isJRE(JavaHomeFile)) return JRE;
      return JDK;
    }
  }

  @Data
  public static class JavaInfo {

    private File JavaHomeFile;
    private File JavaExeFile;
    private String VersionNumber;
    private JavaTips tips;

    public JavaInfo(File javaHomeFile, File javaExeFile, String versionNumber, JavaTips tips) {
      JavaHomeFile = javaHomeFile;
      JavaExeFile = javaExeFile;
      VersionNumber = versionNumber;
      this.tips = tips;
    }

    public JavaInfo(File javaHomeFile) {
      this(javaHomeFile, JavaUtils.getJavaExeFile(javaHomeFile), getJavaVersion(getJavaExePath(javaHomeFile)), JavaTips.getJavaTips(javaHomeFile));
    }

  }
}

