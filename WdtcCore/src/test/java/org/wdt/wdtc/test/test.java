package org.wdt.wdtc.test;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.auth.Yggdrasil.YggdrasilAccounts;
import org.wdt.wdtc.download.FileUrl;

import java.io.IOException;

public class test {
    @Test
    public void getVersion() throws IOException {
        YggdrasilAccounts accounts = new YggdrasilAccounts(FileUrl.getLittleskinUrl(), "Wd_t", "zjh7454188");
        System.out.println(accounts.getUserInformation());

    }


}
