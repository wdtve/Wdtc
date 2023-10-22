package org.wdt.wdtc.auth.accounts;

import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.BaseUser;
import org.wdt.wdtc.auth.User;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.SkinUtils;
import org.wdt.wdtc.utils.StringUtils;
import org.wdt.wdtc.utils.WdtcLogger;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.IOException;
import java.util.UUID;

public class OfflineAccounts extends BaseUser {
    private static final Logger logmaker = WdtcLogger.getLogger(OfflineAccounts.class);
    private final String username;
    private final String UserUuid = StringUtils.cleanStrInString(UUID.randomUUID().toString(), "-");

    public OfflineAccounts(String username) {
        this.username = username;
    }


    @Override
    public User getUser() throws IOException {
        User user = new User();
        user.setUserName(username);
        user.setType(Accounts.AccountsType.Offline);
        user.setAccessToken("${auth_access_token}");
        user.setUuid(UserUuid);
        SkinUtils utils = new SkinUtils(getUtils().getSkinFile());
        utils.setUserSkinInput(OfflineAccounts.class.getResourceAsStream("/steve.png"));
        user.setHeadFile(utils.writeSkinHead());
        JSONUtils.writeObjectToJsonFile(FileManger.getUsersJson(), user);
        logmaker.info(user);
        return user;
    }

    public SkinUtils getUtils() {
        return new SkinUtils(username);
    }
}