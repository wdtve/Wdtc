package org.wdt.wdtc.test;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.utils.JavaFxUtils;

import java.io.IOException;

public class Tests {
    @Test
    public void testgetErrorWin() throws IOException {
        JavaFxUtils.setJavaFXListJson();
    }

    public void get(GetGamePath getGamePath) {
        System.out.println(getGamePath instanceof Launcher);
    }
}

