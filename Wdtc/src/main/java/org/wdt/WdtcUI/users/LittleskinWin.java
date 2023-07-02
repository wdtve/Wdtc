package org.wdt.WdtcUI.users;

import com.alibaba.fastjson2.JSONObject;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.WdtcUI.Consoler;
import org.wdt.WdtcUI.ErrorWin;
import org.wdt.WdtcUI.LauncherWin;
import org.wdt.auth.Yggdrasil.YggdrasilAccounts;
import org.wdt.auth.Yggdrasil.YggdrasilTextures;
import org.wdt.download.FileUrl;
import org.wdt.game.FilePath;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LittleskinWin {
    private static final Logger logmaker = Logger.getLogger(LittleskinWin.class);

    public static void setLittleskinWin(Stage MainStage) {
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
        back.setOnAction(event -> UsersWin.setUserWin(MainStage.getTitle(), MainStage));
        pane.getChildren().addAll(littleskinTitle, username, Inputusername, powerwrod, inputpowerword, label, ok, back, LittleskinCom);
        ok.setOnAction(event -> {
            if (Objects.nonNull(inputpowerword.getText()) && Objects.nonNull(Inputusername.getText())
                    && inputpowerword.getText().equals("") && Inputusername.getText().equals("")) {
                label.setText("请输入用户名、密码");
                logmaker.warn("* 用户名、密码为空");
            } else {
                try {
                    YggdrasilAccounts yggdrasilAccounts = new YggdrasilAccounts(FileUrl.getLittleskinUrl(), Inputusername.getText(), inputpowerword.getText());
                    yggdrasilAccounts.WriteYggdrasilFile();
                    try {
                        Map<String, String> StringMap = new HashMap<>();
                        StringMap.put("userName", Inputusername.getText());
                        StringMap.put("type", "Yggdrasil");
                        FileUtils.writeStringToFile(FilePath.getUsersJson(), JSONObject.toJSONString(StringMap), "UTF-8");
                        YggdrasilTextures yggdrasilTextures = new YggdrasilTextures(yggdrasilAccounts);
                        yggdrasilTextures.DownloadUserSkin();
                        LauncherWin.setLauncherWin(MainStage);
                        logmaker.info("* Littleskin用户:" + Inputusername.getText() + "登陆成功!");
                    } catch (IOException e) {
                        ErrorWin.setErrorWin(e);
                    }
                } catch (IOException | RuntimeException e) {
                    label.setText("用户名或密码错误");
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
        MainStage.setScene(new Scene(pane, 600, 400));
    }
}
