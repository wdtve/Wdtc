package org.wdt.wdtc.core.auth.accounts;

import org.wdt.wdtc.core.auth.User;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.URLManger;
import org.wdt.wdtc.core.utils.StringUtils;

public class Accounts {
    private final AccountsType type;

    public Accounts() {
        this.type = User.getUser().getType() == AccountsType.Offline ? AccountsType.Offline : AccountsType.Yggdrasil;
    }

    public boolean ifAccountsIsOffline() {
        return type != AccountsType.Yggdrasil;
    }

    public String getJvmCommand() {
        return ifAccountsIsOffline()
                ? StringUtils.STRING_EMPTY
                : StringUtils.appendForString(StringUtils.STRING_SPACE, "-javaagent:", FileManger.getAuthlibInjector(),
                "=", URLManger.getLittleskinApi(), StringUtils.STRING_SPACE, "-Dauthlibinjector.yggdrasil.prefetched=", User.getUser().getAPIBase64());
    }


    public enum AccountsType {
        Offline,
        Yggdrasil
    }

}
