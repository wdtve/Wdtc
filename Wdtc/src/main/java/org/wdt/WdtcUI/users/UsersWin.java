package org.wdt.WdtcUI.users;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.WdtcUI.ErrorWin;
import org.wdt.WdtcUI.LauncherWin;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsersWin {


    private final static Logger loggmaker = Logger.getLogger(UsersWin.class);

    public static void setUserWin(String wintitle, Stage MainStage) {
        Pane userspane = new Pane();
        TextField Registerusername = new TextField();
        Registerusername.setLayoutX(224.0);
        Registerusername.setLayoutY(106.0);
        Label label = new Label("用户名:");
        label.setLayoutY(110.0);
        label.setLayoutX(177.0);
        Button button = new Button("注册一个!");
        button.setLayoutX(266.0);
        button.setLayoutY(221.0);
        Button buygame = new Button("购买正版");
        buygame.setLayoutX(536.0);
        Label attention = new Label("用户名用于游戏内成就等地方,用户名不能包含空格、中文、波浪线等特殊字符，下划线、英文、数字允许");
        attention.setLayoutX(22.0);
        attention.setLayoutY(78.0);
        Label OKRegister = new Label();
        OKRegister.setLayoutX(284.0);
        OKRegister.setLayoutY(282.0);
        Button back = new Button("返回");
        Button Littleskin = new Button("Littleskin外置登录");
        Littleskin.setLayoutY(23.0);
        userspane.getChildren().addAll(Registerusername, label, buygame, button, attention, OKRegister, back, Littleskin);
        MainStage.setTitle(wintitle);
        MainStage.getIcons().add(new Image("/ico.jpg"));
        MainStage.setScene(new Scene(userspane, 600, 400));
        MainStage.setResizable(false);
        MainStage.show();
        buygame.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("cmd.exe /C start https://www.minecraft.net/");
            } catch (IOException | RuntimeException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        button.setOnAction(event -> {
            try {
                String username = Registerusername.getText();
                if (isContainChinese(username)) {
                    Registeruser.RegisterUser(username);
                    loggmaker.info("* 离线账户" + username + "注册成功");
                    LauncherWin.setLauncherWin(MainStage);
                } else {
                    OKRegister.setText("不能带中文字符哦");
                }
            } catch (IOException exception) {
                ErrorWin.setErrorWin(exception);
            }
        });
        back.setOnAction(event -> LauncherWin.setLauncherWin(MainStage));
        Littleskin.setOnAction(event -> LittleskinWin.setLittleskinWin(MainStage));
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return !m.find();
    }

}
