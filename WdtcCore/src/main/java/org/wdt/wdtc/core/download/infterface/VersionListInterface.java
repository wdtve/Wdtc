package org.wdt.wdtc.core.download.infterface;

import java.io.IOException;
import java.util.List;

public interface VersionListInterface {
  List<VersionJsonObjectInterface> getVersionList() throws IOException;
}
