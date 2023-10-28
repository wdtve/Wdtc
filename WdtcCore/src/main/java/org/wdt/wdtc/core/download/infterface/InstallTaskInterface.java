package org.wdt.wdtc.core.download.infterface;

import java.io.IOException;

public interface InstallTaskInterface {
    void execute() throws IOException;

    void setPatches() throws IOException;

    void afterDownloadTask() throws IOException;

    void beforInstallTask() throws IOException;
}
