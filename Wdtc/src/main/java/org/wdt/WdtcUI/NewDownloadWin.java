package org.wdt.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.download.VersionList;
import org.wdt.game.Launcher;
import org.wdt.platform.Starter;

import java.util.List;

public class NewDownloadWin {


    public static void SetWin(Stage MainStage) {
        Pane pane = new Pane();
        VBox list = new VBox();
        ScrollPane sp = new ScrollPane();
        JFXButton back = new JFXButton("返回");
        Label tips = new Label("选择右侧的一个版本");
        tips.setLayoutX(27.0);
        tips.setLayoutY(71.0);
        back.setOnAction(event -> HomeWin.setHome(MainStage));
        Platform.runLater(() -> {
            List<String> Versionlist = VersionList.getVersionList();
            for (String s : Versionlist) {
                JFXButton button = new JFXButton(s);
                list.getChildren().add(button);
                button.setPrefWidth(440.0);
                button.setOnAction(event -> {
                    Launcher launcher = new Launcher(button.getText());
                    ModChooseWin Choose = new ModChooseWin(launcher, MainStage);
                    Choose.setChooseWin();
                });

            }
        });
        sp.setContent(list);
        sp.setLayoutX(155.0);
        sp.setPrefHeight(450.0);
        sp.setPrefWidth(445.0);
        pane.getChildren().addAll(sp, back);
        pane.getStylesheets().addAll(Consoler.getCssFile());
        pane.setBackground(Consoler.getBackground());
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion() + " - 下载游戏");
        MainStage.setScene(new Scene(pane, 600, 450));
    }
}
