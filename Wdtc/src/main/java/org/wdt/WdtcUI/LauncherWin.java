package org.wdt.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.wdt.WdtcUI.users.ReadUserList;
import org.wdt.WdtcUI.users.UsersWin;
import org.wdt.platform.Starter;

import java.io.IOException;

public class LauncherWin {

    public static void setLauncherWin(Stage MainStage) {
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion() + " - 启动游戏");
        Pane pane = new Pane();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> HomeWin.setHome(MainStage));
        back.setStyle("-fx-border-color: #000000");
        Button deletVersion = new Button("删除版本");
        deletVersion.setLayoutX(451.0);
        deletVersion.setLayoutY(88.0);
        deletVersion.setOnAction(event -> {
            try {
                DeleteVersion.getStartList(MainStage);
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        Label warningDelet = new Label("版本删除就找不回来了哦");
        warningDelet.setLayoutX(417.0);
        warningDelet.setLayoutY(65.0);
        Button completioner = new Button("补全文件");
        completioner.setLayoutX(88.0);
        completioner.setLayoutY(88.0);
        completioner.setOnAction(event -> CompletionGame.CompletionGameVersion(MainStage));
        Label Tips = new Label("点击后没有反应是正常的,等一会就可以看到游戏窗口");
        Tips.setLayoutX(161.0);
        Tips.setLayoutY(166.0);
        TextField stater_path = new TextField();
        stater_path.setLayoutX(136.0);
        stater_path.setLayoutY(122.0);
        stater_path.setPrefHeight(23.0);
        stater_path.setPrefWidth(329.0);
        Button Modify_Account = new Button("修改账户");
        Modify_Account.setLayoutX(269.0);
        Modify_Account.setLayoutY(88.0);
        Modify_Account.setOnAction(event -> UsersWin.setUserWin("修改账户名", MainStage));
        JFXButton startGame = new JFXButton("启动游戏");
        startGame.setLayoutX(194.0);
        startGame.setLayoutY(191.0);
        startGame.setPrefHeight(189.0);
        startGame.setPrefWidth(213.0);
        startGame.setOnAction(event -> {
            try {
                if (ReadUserList.SetUserJson(MainStage)) {
                    new StartVersionList(Tips, stater_path).getStartList(MainStage);
                }
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        startGame.setStyle("-fx-border-color: #000000");
        pane.getChildren().addAll(back, deletVersion, warningDelet, completioner, Tips, stater_path, Modify_Account, startGame);
        pane.setBackground(Consoler.getBackground());
        MainStage.setScene(new Scene(pane));
    }
}
