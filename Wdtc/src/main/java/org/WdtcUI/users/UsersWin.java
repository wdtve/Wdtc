package org.WdtcUI.users;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.WdtcUI.ErrorWin;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsersWin {
    private static final Stage userwin = new Stage();
    private static final Pane userspane = new Pane();
    private static final Scene usersscene = new Scene(userspane, 600, 400);
    private final static Logger loggmaker = Logger.getLogger(UsersWin.class);

    public static void setUserWin(String wintitle) {
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
        userspane.getChildren().addAll(Registerusername, label, buygame, button, attention, OKRegister);
        userwin.setTitle(wintitle);
        userwin.getIcons().add(new Image("/ico.jpg"));
        userwin.setScene(usersscene);
        userwin.setResizable(false);
        userwin.show();
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
                    loggmaker.info("* 账户注册" + username + "成功");
                    OKRegister.setText("注册" + username + "成功");
                } else {
                    OKRegister.setText("不能带中文字符哦");
                }
            } catch (IOException | RuntimeException exception) {
                ErrorWin.setErrorWin(exception);
            }
        });
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return !m.find();
    }

}
