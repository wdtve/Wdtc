package org.wdt.wdtc.auth.accounts;

import org.wdt.wdtc.auth.User;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.manger.UrlManger;

import java.io.IOException;

public class Accounts {
    private final AccountsType type;

    public Accounts() {
        this.type = User.getUsers().getType() == AccountsType.Offline ? AccountsType.Offline : AccountsType.Yggdrasil;
    }

    public boolean AccountsIsOffline() {
        return type != AccountsType.Yggdrasil;
    }

    public String getJvm() throws IOException {
        return AccountsIsOffline() ? "" : " -javaagent:" + FileManger.getAuthlibInjector() + "=" + UrlManger.getLittleskinApi() + " -Dauthlibinjector.yggdrasil.prefetched=" +
                User.getUsers().getAPIBase64();
    }


    public enum AccountsType {
        Offline,
        Yggdrasil
    }

}
