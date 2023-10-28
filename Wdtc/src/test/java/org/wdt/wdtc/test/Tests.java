package org.wdt.wdtc.test;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.GameDirectoryManger;
import org.wdt.wdtc.ui.JavaFxUtils;

import java.io.IOException;

public class Tests {
    @Test
    public void testgetErrorWin() throws IOException {
        JavaFxUtils.setJavaFXListJson();
    }

    public void get(GameDirectoryManger gameDirectoryManger) {
        System.out.println(gameDirectoryManger instanceof Launcher);
    }
}

