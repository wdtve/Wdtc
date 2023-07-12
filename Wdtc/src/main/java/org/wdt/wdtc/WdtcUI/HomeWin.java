package org.wdt.wdtc.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.IOException;

public class HomeWin {
    private static final Logger logmaker = getWdtcLogger.getLogger(HomeWin.class);

    public static void setHome(Stage MainStage) {
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion());
        Pane Menu = new Pane();
        Menu.setPrefHeight(450);
        Menu.setPrefWidth(64.0);
        JFXButton home = new JFXButton("首页");
        home.setPrefHeight(23.0);
        home.setPrefWidth(64.0);
        JFXButton downgame = new JFXButton("下载游戏");
        downgame.setLayoutY(23.0);
        downgame.setOnAction(event -> NewDownloadWin.SetWin(MainStage));
        JFXButton startgame = new JFXButton("启动游戏");
        startgame.setLayoutY(46.0);
        startgame.setOnAction(event -> LauncherWin.setLauncherWin(MainStage));
        JFXButton github = new JFXButton("GitHub");
        github.setLayoutY(427.0);
        github.setPrefHeight(23.0);
        github.setPrefWidth(64.0);
        github.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("cmd.exe /C start https://github.com/zjh411025/Wdtc");
            } catch (IOException | RuntimeException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        JFXButton setting = new JFXButton("设置");
        setting.setLayoutY(404.0);
        setting.setPrefHeight(23.0);
        setting.setPrefWidth(64.0);
        setting.setOnMousePressed(event -> {
            if (event.isControlDown()) {
                try {
                    Runtime.getRuntime().exec("cmd.exe /c start " + FilePath.getWdtcConfig());
                    logmaker.info("* 配置目录" + FilePath.getWdtcConfig() + "已打开");
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            } else {
                try {
                    SettingWin.setSettingWin(MainStage);
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            }
        });
        Label name = new Label("Wdtc\n" + Starter.getLauncherVersion());
        name.setLayoutX(17.0);
        name.setLayoutY(161.0);
        Label readme = new Label("一个简单到不能再简单的我的世界Java版启动器");
        readme.setLayoutX(172.0);
        readme.setLayoutY(166.0);
        Menu.getChildren().addAll(home, downgame, startgame, github, setting, name);
        Pane pane = new Pane(Menu);
        pane.getChildren().addAll(readme);
        pane.setBackground(Consoler.getBackground());
        Scene scene = new Scene(pane, 600, 450);
        MainStage.setScene(scene);
    }
}
