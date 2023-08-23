package org.wdt.wdtc.ui.users;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.wdt.wdtc.auth.Accounts;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.ui.Consoler;
import org.wdt.wdtc.utils.PlatformUtils;

public class NewUserWindows {
    private final Stage MainStage;
    private Accounts.AccountsType type = Accounts.AccountsType.Offline;
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
        UserStage.getIcons().add(new Image("/ico.jpg"));
        Pane SonPane = new Pane();
        UserListPane.setUserList(SonPane);
        UserStage.initOwner(MainStage);
        UserStage.setTitle(title);
        SonPane.setPrefSize(600, 400);
        AnchorPane ButtonLine = new AnchorPane();
        ButtonLine.setPrefWidth(125);

        JFXButton UserListButton = new JFXButton("账户列表");
        UserListButton.setOnAction(event -> {
            clean(SonPane);
            UserListPane.setUserList(SonPane);
        });
        UserListButton.setPrefSize(125, 30);

        JFXButton OfflineButton = new JFXButton("离线账户");
        AnchorPane.setTopAnchor(OfflineButton, 30.0);
        OfflineButton.setPrefSize(125, 30);
        OfflineButton.setOnAction(event -> {
            clean(SonPane);
            UsersWindow.setUserWin(SonPane);
        });
        JFXButton LittleskinCom = new JFXButton("Littleskin官网");
        LittleskinCom.setPrefSize(125, 30);
        AnchorPane.setBottomAnchor(LittleskinCom, 30.0);
        JFXButton buygame = new JFXButton("购买正版");
        buygame.setPrefSize(125, 30);
        AnchorPane.setBottomAnchor(buygame, 0.0);
        JFXButton YggdrasilButton = new JFXButton("Yggdrasil账户");
        YggdrasilButton.setPrefSize(125, 30);
        YggdrasilButton.setOnAction(event -> {
            clean(SonPane);
            LittleskinWindow.setLittleskinWin(SonPane);
        });
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
        LittleskinCom.setOnAction(event -> PlatformUtils.StartPath(FileUrl.getLittleskinUrl()));
        buygame.setOnAction(event -> PlatformUtils.StartPath("https://www.minecraft.net/zh-hans"));
    }


    private void clean(Pane pane) {
        pane.getChildren().clear();
    }
}
