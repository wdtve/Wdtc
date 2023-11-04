package org.wdt.wdtc.core.download.infterface;

import org.wdt.wdtc.core.utils.ModUtils;

public interface ModDownloadInfoInterface {

    String getModVersion();

    InstallTaskInterface getModInstallTask();

    ModUtils.KindOfMod getModKind();

}
