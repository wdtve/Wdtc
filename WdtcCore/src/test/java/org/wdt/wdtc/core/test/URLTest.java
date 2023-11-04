package org.wdt.wdtc.core.test;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.core.utils.URLUtils;

import java.io.IOException;

public class URLTest {
    @Test
    public void getRedirectUrlTest() throws IOException {
        System.out.println(URLUtils.isNetworkHasThisFile(URLUtils.toURL("https://bmclapi2.bangbang93.com/version/1.20.1/json")));
    }
}
