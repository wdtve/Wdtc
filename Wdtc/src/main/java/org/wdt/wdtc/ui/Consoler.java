package org.wdt.wdtc.ui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Objects;

public class Consoler {

    public Consoler() {
    }

    public static Background getBackground() {
        //Form:https://www.bilibili.com/video/BV1EY411m7uZ
        Image image = new Image(Objects.requireNonNull(Consoler.class.getResourceAsStream("/BlackGround.jpg")));
        return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(WindwosSize.WindowsWidht, WindwosSize.WindowsHeight, false, false, true, true)));
    }


    public static String getCssFile() {
        return String.valueOf(Consoler.class.getResource("/css/color.css"));
    }

    public static void setCss(String id, Region... pane) {
        for (Region region : pane) {
            region.getStyleClass().add(id);
        }
    }

    public static void setStylesheets(Pane pane) {
        pane.getStylesheets().add(getCssFile());
    }

    public static void setTopGrid(Node node) {
        setTopLowerLeft(node);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
    }

    public static void setTopLowerLeft(Node node) {
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
    }

    public static void setTopLowerRight(Node node) {
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }
}
