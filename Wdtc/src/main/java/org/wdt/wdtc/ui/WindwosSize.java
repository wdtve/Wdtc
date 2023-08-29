package org.wdt.wdtc.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.wdt.wdtc.platform.SettingManger;

public class WindwosSize {
    public static final double WindowsWidht = 616.0;
    public static final double WindowsHeight = 489.0;
    private final Stage MainStage;


    public WindwosSize(Stage mainStage) {
        MainStage = mainStage;
    }

    public void ModifyWindwosSize(Pane pane, Region... node) {
        pane.getChildren().addAll(node);
        for (Region region : node) {
            double widthratio = MainStage.getWidth() / WindowsWidht;
            region.setLayoutX(region.getLayoutX() * widthratio);
            if (region.getPrefWidth() != -1) {
                region.setPrefWidth(region.getPrefWidth() * widthratio);
            }
            MainStage.widthProperty().addListener((observable, oldValue, newValue) -> {
                double ratio = newValue.doubleValue() / oldValue.doubleValue();
                region.setLayoutX(region.getLayoutX() * ratio);
                if (region.getPrefWidth() != -1) {
                    region.setPrefWidth(region.getPrefWidth() * ratio);
                }
            });
//            double heigthratio = MainStage.getHeight() / WindowsHeight;
//            region.setLayoutY(region.getLayoutY() * heigthratio);
//            if (region.getPrefHeight() != -1) {
//                region.setPrefHeight(region.getPrefHeight() * heigthratio);
//            }

//            MainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
//                double ratio = newValue.doubleValue() / oldValue.doubleValue();
//                region.setLayoutY(region.getLayoutY() * ratio);
//                if (region.getPrefHeight() != -1) {
//                    region.setPrefHeight(region.getPrefHeight() * ratio);
//                }
//            });
        }
    }

    public void SettingSize() {
        SettingManger.Setting setting = SettingManger.getSetting();
        MainStage.setWidth(setting.getWindowsWidth());
        MainStage.setHeight(setting.getWindowsHeight());
    }


    @Override
    public String toString() {
        return "WindwosSize{" + "MainStageSize=" + MainStage.getWidth() + "," + MainStage.getHeight() + "}";
    }
}
