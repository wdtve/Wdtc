package org.wdt.wdtc.game.config;

import com.google.gson.annotations.SerializedName;
import org.wdt.wdtc.utils.JavaHomePath;

public class DefaultGameConfig {
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
        return "DefaultGameConfig{" + "xmx=" + xmx + ", JavaHome='" + JavaHome + '\'' + ", width=" + width + ", hight=" + hight + '}';
    }
}
