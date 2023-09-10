package org.wdt.wdtc.ui;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.wdtc.download.game.GameVersionList;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.Version;
import org.wdt.wdtc.manger.VMManger;

import java.util.List;

public class NewDownloadWindow {


    public static void SetWin(Stage MainStage) {
        Version.DownloadVersionManifestJsonFileTask();
        WindwosSize size = new WindwosSize(MainStage);
        AnchorPane pane = new AnchorPane();
        VBox list = new VBox();
        Consoler.setTopGrid(list);
        ScrollPane sp = new ScrollPane();
        AnchorPane.setLeftAnchor(sp, 155.0);
        AnchorPane.setTopAnchor(sp, 0.0);
        AnchorPane.setBottomAnchor(sp, 0.0);
        AnchorPane.setRightAnchor(sp, 0.0);
        JFXButton back = new JFXButton("返回");
        back.getStyleClass().add("BlackBorder");
        Label tips = new Label("选择右侧的一个版本");
        tips.setLayoutX(27.0);
        tips.setLayoutY(71.0);
        back.setOnAction(event -> {
            HomeWindow win = new HomeWindow();
            win.setHome(MainStage);
        });
        Platform.runLater(() -> {
            List<String> Versionlist = new GameVersionList().getVersionList();
            for (String s : Versionlist) {
                JFXButton button = new JFXButton(s);
                button.setPrefWidth(458);
                size.ModifyWindwosSize(list, button);
                button.getStyleClass().add("BlackBorder");
                button.setOnAction(event -> {
                    Launcher launcher = new Launcher(button.getText());
                    ModChooseWindows Choose = new ModChooseWindows(launcher, MainStage);
                    Choose.setChooseWin();
                });
            }

        });
        sp.setContent(list);
        sp.setLayoutX(155.0);
        sp.setPrefHeight(WindwosSize.WindowsHeight);
        sp.setPrefWidth(461);
//        size.ModifyWindwosSize(pane, sp, back, tips);
        pane.getChildren().addAll(sp, back, tips);
        Consoler.setStylesheets(pane);
        pane.setBackground(Consoler.getBackground());
        MainStage.setTitle("Wdtc - " + VMManger.getLauncherVersion() + " - 下载游戏");
        MainStage.setScene(new Scene(pane, 600, 450));
    }
}
