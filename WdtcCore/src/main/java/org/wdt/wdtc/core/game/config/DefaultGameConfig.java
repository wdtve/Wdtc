package org.wdt.wdtc.core.game.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.JavaUtils;

@Setter
@Getter
@ToString
public class DefaultGameConfig {
  @SerializedName("info")
  private VersionInfo info;
  @SerializedName("config")
  private Config config;

  public DefaultGameConfig(Launcher launcher) {
    this.info = launcher.getVersionInfo();
    this.config = new Config();
  }

  public DefaultGameConfig() {
  }

  @Setter
  @Getter
  @ToString
  public static class Config {
    @SerializedName("RunningMemory")
    private int Memory;
    @SerializedName("JavaPath")
    private String JavaPath;
    @SerializedName("WindowWidth")
    private int width;
    @SerializedName("WindowHeight")
    private int hight;

    public Config() {
      this.Memory = 1024;
      this.JavaPath = JavaUtils.getRunJavaHome();
      this.width = 1000;
      this.hight = 618;
    }

    public Config(int memory, String javaPath, int width, int hight) {
      Memory = memory;
      JavaPath = javaPath;
      this.width = width;
      this.hight = hight;
    }
  }


}
