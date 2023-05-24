package org.wdt;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.WdtcLauncher.Accounts;

import java.io.IOException;

public class Launcher extends Version {
    private final Logger logmaker = Logger.getLogger(Launcher.class);
    private String Gameattribute;
    private String Jvmattribute;
    private String Librartattribute;

    public Launcher(String version) throws IOException {
        this(version, AboutSetting.GetDefaultGamePath());
    }

    public Launcher(String version, String here) {
        super(version, here);
    }

    public String getGameattribute() {
        return Gameattribute;
    }

    public void setGameattribute(StringBuilder gameattribute) {
        Gameattribute = gameattribute.toString();
    }

    public String getJvmattribute() {
        return Jvmattribute;
    }

    public void setJvmattribute(StringBuilder jvmattribute) {
        Jvmattribute = jvmattribute.toString();
    }

    public String getLibrartattribute() {
        return Librartattribute;
    }

    public void setLibrartattribute(StringBuilder librartattribute) {
        Librartattribute = librartattribute.toString();
    }

    public boolean bmclapi() {
        return AboutSetting.GetBmclSwitch();
    }

    public boolean log() {
        return AboutSetting.GetLogSwitch();
    }


    public void writeStartScript() {
        try {
            logmaker.info(GetStartScript());
            FileUtils.writeStringToFile(FilePath.getStarterBat(), GetStartScript(), "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetStartScript() {
        return getJvmattribute() + getLibrartattribute() + getGameattribute();
    }

    public Accounts GetAccounts() throws IOException {
        return new Accounts();
    }

}
