package org.wdt.wdtc.game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetermineVersionSize {

    public static boolean DetermineSize(String OriginalVersionNumber, Launcher launcher) {
        Matcher OriginalVersion = getVersionNumber(OriginalVersionNumber);
        Matcher Version = getVersionNumber(launcher.getVersion());
        if (OriginalVersion.find() && Version.find()) {
            int Original = Integer.parseInt(OriginalVersion.group(1));
            int target = Integer.parseInt(Version.group(1));
            if (Original == target) {
                if (OriginalVersion.group(2) != null && Version.group(2) != null) {
                    int OriginalMinorVersionNumber = Integer.parseInt(OriginalVersion.group(2));
                    int MinorVersionNumber = Integer.parseInt(Version.group(2));
                    if (OriginalMinorVersionNumber == MinorVersionNumber) return true;
                    return OriginalMinorVersionNumber < MinorVersionNumber;
                } else if (OriginalVersion.group(2) != null) {
                    return false;
                } else if (Version.group(2) != null) {
                    return true;
                } else {
                    return true;
                }
            }
            return Original < target;

        }
        return false;
    }

    private static Matcher getVersionNumber(String str) {
        return Pattern.compile("1\\.([0-9][0-9])\\.?([0-9])?").matcher(str);
    }
}
