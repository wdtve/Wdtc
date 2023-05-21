package org.wdt.WdtcLauncher;

import org.wdt.AboutSetting;
import org.wdt.WdtcLauncher.Yggdrasil.YggdrasilAccounts;

import java.io.IOException;

public class Accounts {
    private final boolean offline;

    public Accounts() throws IOException {
        this.offline = AboutSetting.GetUserType().equals("offline");
    }

    public String GetUserUUID() throws IOException {
        if (offline) {
            OfflineAccounts offlineAccounts = new OfflineAccounts(AboutSetting.UserName());
            return offlineAccounts.GetUserUuid();
        } else {
            YggdrasilAccounts yggdrasilAccounts = new YggdrasilAccounts();
            return yggdrasilAccounts.GetUserUuid();
        }
    }

    public String GetUserName() throws IOException {
        if (offline) {
            return AboutSetting.UserName();
        } else {
            YggdrasilAccounts yggdrasilAccounts = new YggdrasilAccounts();
            return yggdrasilAccounts.GetUserName();
        }
    }

    public String GetAccessToken() throws IOException {
        if (offline) {
            OfflineAccounts offlineAccounts = new OfflineAccounts(AboutSetting.UserName());
            return offlineAccounts.GetAccessToken();
        } else {
            YggdrasilAccounts yggdrasilAccounts = new YggdrasilAccounts();
            return yggdrasilAccounts.GetAccessToken();
        }
    }

}
