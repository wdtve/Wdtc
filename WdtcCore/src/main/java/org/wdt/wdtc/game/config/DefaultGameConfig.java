package org.wdt.wdtc.game.config;

import com.google.gson.annotations.SerializedName;
import org.wdt.wdtc.utils.JavaHomePath;

public class DefaultGameConfig {
    @SerializedName("info")
    private VersionInfo info;
    @SerializedName("config")
    private Config config;

    public VersionInfo getInfo() {
        return info;
    }

    public void setInfo(VersionInfo info) {
        this.info = info;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "DefaultGameConfig{" +
                "info=" + info +
                ", config=" + config +
                '}';
    }

    public static class Config {
        @SerializedName("RunningMemory")
        public int xmx = 1024;
        @SerializedName("JavaPath")
        public String JavaHome = JavaHomePath.GetRunJavaHome();
        @SerializedName("WindowWidth")
        public int width = 618;
        @SerializedName("WindowHeight")
        public int hight = 1000;


        public void setXmx(int xmx) {
            this.xmx = xmx;
        }

        public void setJavaHome(String javaHome) {
            JavaHome = javaHome;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHight(int hight) {
            this.hight = hight;
        }

        public int getRunningMemory() {
            return xmx;
        }

        public String getJavaPath() {
            return JavaHome;
        }

        public int getWindowWidth() {
            return width;
        }

        public int getWindowHeight() {
            return hight;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "xmx=" + xmx +
                    ", JavaHome='" + JavaHome + '\'' +
                    ", width=" + width +
                    ", hight=" + hight +
                    '}';
        }
    }


}
