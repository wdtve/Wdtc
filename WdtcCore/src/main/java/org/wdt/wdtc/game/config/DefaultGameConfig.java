package org.wdt.wdtc.game.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.wdt.wdtc.utils.JavaUtils;

@Setter
@Getter
@ToString
public class DefaultGameConfig {
    @SerializedName("info")
    private VersionInfo info;
    @SerializedName("config")
    private Config config;

    @Setter
    @Getter
    @ToString
    public static class Config {
        @SerializedName("RunningMemory")
        private int Memory = 1024;
        @SerializedName("JavaPath")
        private String JavaPath = JavaUtils.GetRunJavaHome();
        @SerializedName("WindowWidth")
        private int width = 618;
        @SerializedName("WindowHeight")
        private int hight = 1000;
    }


}
