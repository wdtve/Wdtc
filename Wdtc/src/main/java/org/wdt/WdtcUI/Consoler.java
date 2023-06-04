package org.wdt.WdtcUI;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Consoler {
    public Consoler() {
    }

    public static Background getBackground() {
        Image image = new Image(String.valueOf(HomeWin.class.getResource("/BlackGround.jpg")));
        return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(800, 480, false, false, true, true)));
    }

    public static String BlackBorder() {
        return "-fx-border-color: #000000";
    }
}
