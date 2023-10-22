package org.wdt.wdtc.download.infterface;

import java.io.IOException;

public interface InstallTask {
    void execute() throws IOException;

    void setPatches() throws IOException;

    void afterDownloadTask() throws IOException;

    void beforInstallTask() throws IOException;
}
