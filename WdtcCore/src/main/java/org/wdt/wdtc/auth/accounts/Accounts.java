package org.wdt.wdtc.auth.accounts;

import org.wdt.wdtc.auth.User;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.manger.UrlManger;

import java.io.IOException;

public class Accounts {
    private final AccountsType type;

    public Accounts() {
        if (User.getUsers().getType() == AccountsType.Offline) {
            type = AccountsType.Offline;
        } else {
            type = AccountsType.Yggdrasil;
        }
    }

    public boolean AccountsIsOffline() {
        return type != AccountsType.Yggdrasil;
    }

    public String getJvm() throws IOException {
        if (AccountsIsOffline()) {
            return "";
        } else {
            return " -javaagent:" + FileManger.getAuthlibInjector() + "=" + UrlManger.getLittleskinApi() + " -Dauthlibinjector.yggdrasil.prefetched=" +
                    User.getUsers().getAPIBase64();
        }
    }


    public enum AccountsType {
        Offline,
        Yggdrasil
    }

}
