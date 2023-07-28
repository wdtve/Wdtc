package org.wdt.wdtc.download.infterface;

import java.io.IOException;
import java.util.List;

public interface VersionList {
    List<String> getVersionList() throws IOException;
}
