package org.wdt.wdtc.ui.window;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.core.download.game.GameVersionList;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.GameFileManger;

import java.util.List;

public class GameVersionListWindow {


    public static void setWindowScene(Stage MainStage) {
        GameFileManger.downloadVersionManifestJsonFileTask();
        WindwosSizeManger size = new WindwosSizeManger(MainStage);
        AnchorPane pane = new AnchorPane();
        VBox list = new VBox();
        Consoler.setTopGrid(list);
        ScrollPane sp = new ScrollPane();
        JFXButton refreshButton = new JFXButton("刷新");
        refreshButton.setPrefSize(155, 30);
        refreshButton.getStyleClass().add("BackGroundWriteButton");
        AnchorPane.setBottomAnchor(refreshButton, 0.0);
        AnchorPane.setLeftAnchor(refreshButton, 0.0);
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
        refreshButton.setOnAction(event -> {
            DownloadVersionGameFile.startDownloadVersionManifestJsonFile();
            setWindowScene(MainStage);
        });
        Platform.runLater(() -> {
            List<VersionJsonObjectInterface> Versionlist = new GameVersionList().getVersionList();
            for (VersionJsonObjectInterface s : Versionlist) {
                JFXButton button = new JFXButton(s.getVersionNumber());
                button.setPrefWidth(458);
                size.ModifyWindwosSize(list, button);
                button.getStyleClass().add("BlackBorder");
                button.setOnAction(event -> {
                    Launcher launcher = new Launcher(button.getText());
                    ModChooseWindow Choose = new ModChooseWindow(launcher, MainStage);
                    Choose.setChooseWin();
                });
            }

        });
        sp.setContent(list);
        sp.setLayoutX(155.0);
        sp.setPrefHeight(WindwosSizeManger.WindowsHeight);
        sp.setPrefWidth(461);
        pane.getChildren().addAll(sp, back, tips, refreshButton);
        Consoler.setStylesheets(pane);
        pane.setBackground(Consoler.getBackground());
        MainStage.setTitle(Consoler.getWindowsTitle("下载游戏"));
        MainStage.setScene(new Scene(pane, 600, 450));
    }
}
