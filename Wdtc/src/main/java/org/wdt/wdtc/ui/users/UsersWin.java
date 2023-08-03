package org.wdt.wdtc.ui.users;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.OfflineAccounts;
import org.wdt.wdtc.ui.Consoler;
import org.wdt.wdtc.ui.ErrorWin;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.regex.Pattern;

public class UsersWin {


    private final static Logger loggmaker = WdtcLogger.getLogger(UsersWin.class);

    public static void setUserWin(String wintitle, Stage MainStage) {
        Stage UserStage = new Stage();
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
        UserStage.setTitle(wintitle);
        UserStage.getIcons().add(new Image("/ico.jpg"));
        userspane.setBackground(Consoler.getBackground());
        UserStage.setScene(new Scene(userspane, 600, 400));
        UserStage.setResizable(false);
        UserStage.initOwner(MainStage);
        UserStage.initModality(Modality.WINDOW_MODAL);
        UserStage.show();
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
                    OfflineAccounts offline = new OfflineAccounts(username);
                    offline.WriteUserJson();
                    loggmaker.info("* 离线账户" + username + "注册成功");
                    UserStage.close();
                } else {
                    OKRegister.setText("不能带中文字符哦");
                }
            } catch (IOException exception) {
                ErrorWin.setErrorWin(exception);
            }
        });
        back.setOnAction(event -> UserStage.close());
        Littleskin.setOnAction(event -> LittleskinWin.setLittleskinWin(UserStage));

    }

    public static boolean isContainChinese(String str) {
        return Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,999}$").matcher(str).find();
    }

}
