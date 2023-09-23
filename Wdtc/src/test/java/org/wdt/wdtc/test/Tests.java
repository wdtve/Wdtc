package org.wdt.wdtc.test;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.JavaFxUtils;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.manger.GameFolderManger;

import java.io.IOException;

public class Tests {
    @Test
    public void testgetErrorWin() throws IOException {
        JavaFxUtils.setJavaFXListJson();
    }

    public void get(GameFolderManger gameFolderManger) {
        System.out.println(gameFolderManger instanceof Launcher);
    }
}

