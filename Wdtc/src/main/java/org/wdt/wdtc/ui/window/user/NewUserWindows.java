package org.wdt.wdtc.ui.window.user;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.wdt.wdtc.core.auth.accounts.Accounts;
import org.wdt.wdtc.core.manger.URLManger;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.ui.window.Consoler;

public class NewUserWindows {
  private final Stage MainStage;
  private Accounts.AccountsType type = null;
  private String title;

  public NewUserWindows(Stage mainStage) {
    MainStage = mainStage;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setType(Accounts.AccountsType type) {
    this.type = type;
  }

  public void show() {
    Stage UserStage = new Stage();
    UserStage.setWidth(725);
    UserStage.setHeight(430);
    AnchorPane ParentPane = new AnchorPane();
    UserStage.getIcons().add(new Image("/assets/icon/ico.jpg"));
    Pane SonPane = new Pane();
    if (type == null) {
      UserListPane.setUserList(SonPane);
    } else {
      switch (type) {
        case Yggdrasil -> LittleskinWindow.setLittleskinWin(SonPane);
        case Offline -> OfflineUserWindow.setUserWin(SonPane);
      }
    }
    UserStage.initOwner(MainStage);
    UserStage.setTitle(title);
    SonPane.setPrefSize(600, 400);
    AnchorPane ButtonLine = new AnchorPane();
    ButtonLine.setPrefWidth(125);

    JFXButton UserListButton = new JFXButton("账户列表");
    UserListButton.setOnAction(event -> UserListPane.setUserList(SonPane));
    UserListButton.setPrefSize(125, 30);

    JFXButton OfflineButton = new JFXButton("离线账户");
    AnchorPane.setTopAnchor(OfflineButton, 30.0);
    OfflineButton.setPrefSize(125, 30);
    OfflineButton.setOnAction(event -> OfflineUserWindow.setUserWin(SonPane));
    JFXButton LittleskinCom = new JFXButton("Littleskin官网");
    LittleskinCom.setPrefSize(125, 30);
    AnchorPane.setBottomAnchor(LittleskinCom, 30.0);
    JFXButton buygame = new JFXButton("购买正版");
    buygame.setPrefSize(125, 30);
    AnchorPane.setBottomAnchor(buygame, 0.0);
    JFXButton YggdrasilButton = new JFXButton("Yggdrasil账户");
    YggdrasilButton.setPrefSize(125, 30);
    YggdrasilButton.setOnAction(event -> LittleskinWindow.setLittleskinWin(SonPane));
    AnchorPane.setTopAnchor(YggdrasilButton, 60.0);
    Consoler.setTopLowerRight(SonPane);
    AnchorPane.setBottomAnchor(SonPane, 0.0);
    AnchorPane.setLeftAnchor(SonPane, 125.0);
    ButtonLine.getChildren().addAll(UserListButton, OfflineButton, YggdrasilButton, LittleskinCom, buygame);
    ButtonLine.getStyleClass().add("BlackBorder");
    Consoler.setTopLowerLeft(ButtonLine);
    AnchorPane.setTopAnchor(ButtonLine, 0.0);
    ParentPane.setBackground(Consoler.getBackground());
    ParentPane.getChildren().addAll(ButtonLine, SonPane);
    UserStage.setScene(new Scene(ParentPane));
    Consoler.setStylesheets(ParentPane);
    UserStage.initModality(Modality.APPLICATION_MODAL);
    UserStage.setResizable(false);
    UserStage.show();
    LittleskinCom.setOnAction(event -> URLUtils.openSomething(URLManger.getLittleskinUrl()));
    buygame.setOnAction(event -> URLUtils.openSomething("https://www.minecraft.net/zh-hans"));
  }


}
