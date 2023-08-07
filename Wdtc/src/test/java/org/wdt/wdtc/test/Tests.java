package org.wdt.wdtc.test;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GetGamePath;

public class Tests {
    @Test
    public void testgetErrorWin() {
        Launcher launcher = new Launcher("1.20.1");
        get(launcher.getGetGamePath());
    }

    public void get(GetGamePath getGamePath) {
        System.out.println(getGamePath instanceof Launcher);
    }
}

