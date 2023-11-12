package org.wdt.wdtc.core.download.infterface;

import java.io.IOException;

public interface InstallTaskInterface {
  void overwriteVersionJson() throws IOException;

  void writeVersionJsonPatches() throws IOException;

  void afterDownloadTask() throws IOException;

  void beforInstallTask() throws IOException;
}
