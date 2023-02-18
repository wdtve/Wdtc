package org.WdtcUI.users;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ReadUserList {
    private static final File user_json = new File("WdtcCore/ResourceFile/Launcher/users/users.json");
    private static final Logger logmaker = Logger.getLogger(ReadUserList.class);

    public static boolean SetUserJson() throws IOException {
        if (!user_json.exists()) {
            UsersWin.setUserWin("您现在还没有账户呢!");
            logmaker.error("* 没有账户,前去创建");
            return false;
        } else {
            logmaker.info("* 有账户,开始启动");
            return true;
        }
    }
}
