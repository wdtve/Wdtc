package org.wdt.wdtc.WdtcUI;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class WindwosSize {
    private final Stage MainStage;


    public WindwosSize(Stage mainStage) {
        MainStage = mainStage;
    }

    public void windwosSize(Pane pane, Region... node) {
        pane.getChildren().addAll(node);
        for (Region region : node) {
            double widthratio = MainStage.getWidth() / Consoler.WindowsWidht;
            region.setLayoutX(region.getLayoutX() * widthratio);
            if (region.getPrefWidth() != -1) {
                region.setPrefWidth(region.getPrefWidth() * widthratio);
            }
            double heigthratio = MainStage.getHeight() / Consoler.WindowsHeight;
            region.setLayoutY(region.getLayoutY() * heigthratio);
            if (region.getPrefHeight() != -1) {
                region.setPrefHeight(region.getPrefHeight() * heigthratio);
            }
            MainStage.widthProperty().addListener((observable, oldValue, newValue) -> {
                double ratio = newValue.doubleValue() / oldValue.doubleValue();
                region.setLayoutX(region.getLayoutX() * ratio);
                if (region.getPrefWidth() != -1) {
                    region.setPrefWidth(region.getPrefWidth() * ratio);
                }
            });
            MainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
                double ratio = newValue.doubleValue() / oldValue.doubleValue();
                region.setLayoutY(region.getLayoutY() * ratio);
                if (region.getPrefHeight() != -1) {
                    region.setPrefHeight(region.getPrefHeight() * ratio);
                }
            });
        }
    }
}
