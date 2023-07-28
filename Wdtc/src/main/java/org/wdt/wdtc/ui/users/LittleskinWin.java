package org.wdt.wdtc.ui.users;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.Yggdrasil.YggdrasilAccounts;
import org.wdt.wdtc.auth.Yggdrasil.YggdrasilTextures;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.ui.Consoler;
import org.wdt.wdtc.ui.ErrorWin;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.IOException;
import java.util.Objects;


public class LittleskinWin {
    private static final Logger logmaker = getWdtcLogger.getLogger(LittleskinWin.class);

    public static void setLittleskinWin(Stage UserStage) {
        Pane pane = new Pane();
        Label littleskinTitle = new Label("Littleskin外置登录");
        littleskinTitle.setLayoutX(250.0);
        littleskinTitle.setLayoutY(69.0);
        Label username = new Label("用户名");
        username.setLayoutX(175.0);
        username.setLayoutY(107.0);
        TextField Inputusername = new TextField();
        Inputusername.setLayoutX(220.0);
        Inputusername.setLayoutY(103.0);
        Label powerwrod = new Label("密码:");
        powerwrod.setLayoutX(179.0);
        powerwrod.setLayoutY(135.0);
        TextField inputpowerword = new TextField();
        inputpowerword.setLayoutX(221.0);
        inputpowerword.setLayoutY(131.0);
        Label label = new Label();
        label.setLayoutX(220.0);
        label.setLayoutY(185.0);
        label.setPrefHeight(15.0);
        label.setPrefWidth(110.0);
        Button ok = new Button("登录");
        ok.setLayoutX(267.0);
        ok.setLayoutY(219.0);
        Button back = new Button("返回");
        Button LittleskinCom = new Button("Littleskin官网");
        LittleskinCom.setLayoutY(23.0);
        back.setOnAction(event -> UserStage.close());
        pane.getChildren().addAll(littleskinTitle, username, Inputusername, powerwrod, inputpowerword, label, ok, back, LittleskinCom);
        ok.setOnAction(event -> {
            if (Objects.nonNull(inputpowerword.getText()) && Objects.nonNull(Inputusername.getText())
                    && inputpowerword.getText().isEmpty() && Inputusername.getText().isEmpty()) {
                label.setText("请输入用户名、密码");
                logmaker.warn("* 用户名、密码为空");
            } else {
                try {
                    YggdrasilAccounts yggdrasilAccounts = new YggdrasilAccounts(FileUrl.getLittleskinUrl(), Inputusername.getText(), inputpowerword.getText());
                    yggdrasilAccounts.WriteUserJson();
                    try {
                        YggdrasilTextures yggdrasilTextures = new YggdrasilTextures(yggdrasilAccounts);
                        yggdrasilTextures.DownloadUserSkin();
                        logmaker.info("* Littleskin用户:" + Inputusername.getText() + "登陆成功!");
                        UserStage.close();
                    } catch (IOException e) {
                        ErrorWin.setErrorWin(e);
                    }
                } catch (RuntimeException | IOException e) {
                    label.setText("用户名或密码错误");
                    logmaker.warn("* 用户名或密码错误", e);
                }
            }
        });
        LittleskinCom.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("cmd.exe /C start " + FileUrl.getLittleskinUrl());
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        pane.setBackground(Consoler.getBackground());
        UserStage.setScene(new Scene(pane, 600, 400));
    }
}
