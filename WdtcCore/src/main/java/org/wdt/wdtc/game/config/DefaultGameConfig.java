package org.wdt.wdtc.game.config;

import com.google.gson.annotations.SerializedName;
import org.wdt.wdtc.utils.JavaHomePath;

public class DefaultGameConfig {
    @SerializedName("RunningMemory")
    private int xmx = 1024;
    @SerializedName("JavaPath")
    private String JavaHome = JavaHomePath.GetRunJavaHome();
    @SerializedName("WindowWidth")
    private int width = 855;
    @SerializedName("WindowHeight")
    private int hight = 1000;

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

    @Override
    public String toString() {
        return "DefaultGameConfig{" +
                "xmx=" + xmx +
                ", JavaHome='" + JavaHome + '\'' +
                ", width=" + width +
                ", hight=" + hight +
                '}';
    }
}
