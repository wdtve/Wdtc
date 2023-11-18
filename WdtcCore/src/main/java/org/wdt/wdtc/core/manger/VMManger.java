package org.wdt.wdtc.core.manger;

import lombok.Getter;
import org.wdt.utils.io.FileUtils;

import java.io.File;
import java.util.List;

public class VMManger {
  public static final String LAUNCHER_VERSION = "wdtc.launcher.version";
  public static final String CONFIG_PATH = "wdtc.config.path";
  public static final String DEBUG = "wdtc.debug.switch";
  public static final String APPLICATION_TYPE = "wtdc.application.type";
  public static final String CLIENT_ID = "wtdc.oauth.clientId";
  private static final List<String> LAUNCHER_AUTHOR = List.of("Wdt~");
  @Getter
  private static final String OS = System.getProperty("os.name");

  public static String getClientId() {
    return System.getProperty(CLIENT_ID, "8c4a5ce9-55b9-442e-9bd0-17cf89689dd0");
  }

  public static String getLauncherVersion() {
    return System.getProperty(LAUNCHER_VERSION, "demo");
  }

  public static boolean isDebug() {
    return Boolean.getBoolean(DEBUG);
  }

  public static File getWdtcConfigFromVM() {
    String WdtcConfigPath = System.getProperty(CONFIG_PATH);
    return WdtcConfigPath != null
        ? new File(FileUtils.getCanonicalPath(FileUtils.toFile(WdtcConfigPath)))
        : new File(System.getProperty("user.home"));
  }

  public static String getApplicationType() {
    return System.getProperty(APPLICATION_TYPE, "ui");
  }

  public static boolean isConsole() {
    return getApplicationType().equals("console");
  }

  public static boolean isUI() {
    return getApplicationType().equals("ui");
  }
}
