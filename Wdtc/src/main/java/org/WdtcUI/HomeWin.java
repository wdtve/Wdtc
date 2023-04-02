package org.WdtcUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeWin {
    public static void setHome(Stage MainStage) {
        MainStage.setTitle("Wdtc - Demo");
        Pane pane = new Pane();
        Button home = new Button("首页");
        home.setPrefHeight(23.0);
        home.setPrefWidth(64.0);
        home.setDisable(false);
        Button downgame = new Button("下载游戏");
        downgame.setLayoutY(23.0);
        downgame.setOnAction(event -> DownloadGameWin.setDownGameWin(MainStage));
        Button startgame = new Button("启动游戏");
        startgame.setLayoutY(46.0);
        startgame.setOnAction(event -> LauncherWin.setLauncherWin(MainStage));
        Button github = new Button("GitHub");
        github.setLayoutY(377.0);
        github.setPrefHeight(23.0);
        github.setPrefWidth(64.0);
        github.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("cmd.exe /C start https://github.com/zjh411025/Wdtc");
            } catch (IOException | RuntimeException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        Button setting = new Button("设置");
        setting.setLayoutY(354.0);
        setting.setPrefHeight(23.0);
        setting.setPrefWidth(64.0);
        setting.setOnAction(event -> SettingWin.setSettingWin(MainStage));
        Label name = new Label("Wdtc");
        name.setLayoutX(17.0);
        name.setLayoutY(161.0);
        Label version_name = new Label("Demo");
        version_name.setLayoutX(17.0);
        version_name.setLayoutY(176.0);
        Label readme = new Label("一个简单到不能再简单的我的世界Java版启动器");
        readme.setLayoutX(172.0);
        readme.setLayoutY(166.0);
        pane.getChildren().addAll(home, downgame, startgame, github, setting, name, version_name, readme);
        MainStage.setScene(new Scene(pane));
    }
}
