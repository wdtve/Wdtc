package org.wdt.wdtc.test;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.JavaFxUtils;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GamePath;

import java.io.IOException;

public class Tests {
    @Test
    public void testgetErrorWin() throws IOException {
        JavaFxUtils.setJavaFXListJson();
    }

    public void get(GamePath gamePath) {
        System.out.println(gamePath instanceof Launcher);
    }
}

