package org.wdt.wdtc.core.auth.accounts;

import lombok.NonNull;
import org.apache.log4j.Logger;
import org.wdt.wdtc.core.auth.BaseUser;
import org.wdt.wdtc.core.auth.User;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.SkinUtils;
import org.wdt.wdtc.core.utils.StringUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.IOException;
import java.util.UUID;

public class OfflineAccounts extends BaseUser {
    private static final Logger logmaker = WdtcLogger.getLogger(OfflineAccounts.class);
    private final String username;
    private final String UserUuid = StringUtils.cleanStrInString(UUID.randomUUID().toString(), "-");

    public OfflineAccounts(@NonNull String username) {
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
        utils.setUserSkinInput(OfflineAccounts.class.getResourceAsStream("/assets/skin/steve.png"));
        user.setHeadFile(utils.writeSkinHead());
        JSONUtils.writeObjectToJsonFile(FileManger.getUsersJson(), user);
        logmaker.info(user);
        return user;
    }

    public SkinUtils getUtils() {
        return new SkinUtils(username);
    }
}
