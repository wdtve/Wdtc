package org.wdt;

import org.wdt.WdtcDownload.FileUrl;

import java.io.IOException;

public class Launcher extends Version {

    public Launcher(String version) {
        super(version);
    }

    public Launcher(String version, String here) {
        super(version, here);
    }

    public boolean bmclapi() throws IOException {
        return AboutSetting.GetBmclSwitch();
    }

    public boolean log() throws IOException {
        return AboutSetting.GetLogSwitch();
    }

    public FileUrl GetFileUrl() throws IOException {
        return new FileUrl(bmclapi());
    }
}
