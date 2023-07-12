package org.wdt.wdtc.WdtcUI.users;

import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.PlatformUtils;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.File;
import java.io.IOException;

public class ReadUserList {
    private static final File UserJson = FilePath.getUsersJson();
    private static final Logger logmaker = getWdtcLogger.getLogger(ReadUserList.class);

    public static boolean SetUserJson(Stage MainStage) throws IOException {
        if (PlatformUtils.FileExistenceAndSize(UserJson)) {
            UsersWin.setUserWin("您现在还没有账户呢!", MainStage);
            logmaker.error("* 没有账户,前去创建");
            return false;
        } else {
            logmaker.info("* 有账户,开始启动");
            return true;
        }
    }
}
