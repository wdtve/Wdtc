package org.wdt.Yggdrasil;

import org.junit.jupiter.api.Test;
import org.wdt.WdtcLauncher.Yggdrasil.YggdrasilAccounts;

import java.io.IOException;

public class PostTest {


    @Test
    public void test() throws IOException {
        YggdrasilAccounts accounts = new YggdrasilAccounts("https://littleskin.cn", "Wd_t", "zjh7454188");
//        YggdrasilTextures yggdrasilTextures = new YggdrasilTextures(accounts);
//        yggdrasilTextures.DownloadUserSkin();
        accounts.WriteYggdrasilFile();
    }

    @Test
    public void GetTest() throws IOException {


    }
}