package org.wdt.wdtc;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.auth.skin.SkinUtils;

import java.io.File;
import java.io.IOException;

public class Pack {
    @Test
    public void exec() throws IOException {
        SkinUtils utils = new SkinUtils(new File("D:\\Wdtc\\WdtcCore\\src\\test\\resources\\alex.png"));
        utils.writeSkinHead();
    }


}
