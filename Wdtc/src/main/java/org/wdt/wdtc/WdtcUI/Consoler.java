package org.wdt.wdtc.WdtcUI;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Objects;

public class Consoler {
    public static final double WindowsWidht = 616.0;
    public static final double WindowsHeight = 489.0;

    public Consoler() {
    }

    public static Background getBackground(double width, double height) {
        Image image = new Image(Objects.requireNonNull(Consoler.class.getResourceAsStream("/BlackGround.jpg")));
        return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(width, height, false, false, true, true)));
    }

    public static Background getBackground() {
        return getBackground(800, 480);
    }

    public static String BlackBorder() {
        return "-fx-border-color: #000000";
    }

    public static String getCssFile() {
        return String.valueOf(Consoler.class.getResource("/css/color.css"));
    }
}
