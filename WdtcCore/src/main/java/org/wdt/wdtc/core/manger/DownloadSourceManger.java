package org.wdt.wdtc.core.manger;

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.download.source.BmclDownloadSource;
import org.wdt.wdtc.core.download.source.McbbsDownloadSource;
import org.wdt.wdtc.core.download.source.OfficialDownloadSource;

public class DownloadSourceManger {
    public static DownloadSourceInterface getOfficialDownloadSource() {
        return new OfficialDownloadSource();
    }

    public static DownloadSourceManger.DownloadSourceList getDownloadSourceKind() {
        return SettingManger.getSetting().getDownloadSource();
    }

    public static DownloadSourceInterface getDownloadSource() {
        DownloadSourceList source = getDownloadSourceKind();
        if (source == DownloadSourceList.OFFICIAL) {
            return new OfficialDownloadSource();
        } else if (source == DownloadSourceList.BMCLAPI) {
            return new BmclDownloadSource();
        } else {
            return new McbbsDownloadSource();
        }
    }

    public static boolean isNotOfficialDownloadSource() {
        return !isOfficialDownloadSource();
    }

    public static boolean isOfficialDownloadSource() {
        return getDownloadSourceKind() == DownloadSourceList.OFFICIAL;
    }

    public enum DownloadSourceList {
        OFFICIAL,
        BMCLAPI,
        MCBBS;

    }
}
