package org.wdt.wdtc.ui.window.user;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.wdt.wdtc.core.auth.UsersList;
import org.wdt.wdtc.core.auth.accounts.OfflineAccounts;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.ui.window.ExceptionWindow;

import java.io.IOException;
import java.util.regex.Pattern;

public class OfflineUserWindow {


  private final static Logger loggmaker = WdtcLogger.getLogger(OfflineUserWindow.class);

  public static void setUserWin(Pane pane) {
    pane.getChildren().clear();
    pane.setPrefSize(600, 400);
    TextField Registerusername = new TextField();
    Registerusername.setLayoutX(224.0);
    Registerusername.setLayoutY(106.0);
    Label label = new Label("用户名:");
    label.setLayoutY(110.0);
    label.setLayoutX(177.0);
    Button button = new Button("注册一个!");
    button.setLayoutX(266.0);
    button.setLayoutY(221.0);

    Label attention = new Label("用户名用于游戏内成就等地方,用户名不能包含空格、中文、波浪线等特殊字符，下划线、英文、数字允许");
    attention.setLayoutX(22.0);
    attention.setLayoutY(78.0);
    Label OKRegister = new Label();
    OKRegister.setLayoutX(284.0);
    OKRegister.setLayoutY(282.0);
    pane.getChildren().addAll(Registerusername, label, button, attention, OKRegister);
    button.setOnAction(event -> {
      try {
        String username = Registerusername.getText();
        if (isQualified(username)) {
          OfflineAccounts offline = new OfflineAccounts(username);
          UsersList.addUser(offline.getUser());
          loggmaker.info("离线账户" + username + "注册成功");
          UserListPane.setUserList(pane);
        } else {
          OKRegister.setText("不能带中文字符哦");
        }
      } catch (IOException exception) {
        ExceptionWindow.setErrorWin(exception);
      }
    });
  }

  public static boolean isQualified(String str) {
    return Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,999}$").matcher(str).find();
  }

}
