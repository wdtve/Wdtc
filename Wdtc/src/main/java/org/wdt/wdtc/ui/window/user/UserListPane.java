package org.wdt.wdtc.ui.window.user;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.wdt.utils.gson.Json;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.auth.User;
import org.wdt.wdtc.core.auth.UsersList;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.ui.window.Consoler;
import org.wdt.wdtc.ui.window.ExceptionWindow;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserListPane {
  private static final File UserFile = FileManger.getUsersJson();
  private static final File UserListFile = FileManger.getUserListFile();

  public static void setUserList(Pane pane) {
    pane.getChildren().clear();
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setPrefSize(600, 400);
    VBox vBox = new VBox();
    vBox.setPrefSize(595, 395);
    List<User> users = UsersList.getUserList();
    if (!users.isEmpty()) {
      for (User user : users) {
        String UserName = user.getUserName();
        AnchorPane UserPane = new AnchorPane();
        UserPane.setPrefSize(595, 40);
        RadioButton enter = new RadioButton();
        AnchorPane.setTopAnchor(enter, 15.0);
        AnchorPane.setBottomAnchor(enter, 15.0);
        AnchorPane.setLeftAnchor(enter, 15.0);
        enter.setOnAction(event -> {
          try {
            User.setUserToJson(UsersList.getUser(UserName));
            setUserList(pane);
          } catch (IOException e) {
            ExceptionWindow.setErrorWin(e);
          }
        });
        if (user.equals(User.getUser())) {
          enter.setSelected(true);
        }
        Image image = null;
        try {
          image = new Image(FileUtils.newInputStream(user.getHeadFile()));
        } catch (IOException e) {
          ExceptionWindow.setErrorWin(e);
        }
        ImageView head = new ImageView(image);
        head.setFitHeight(32);
        head.setFitWidth(32);
        AnchorPane.setTopAnchor(head, 15.0);
        AnchorPane.setBottomAnchor(head, 15.0);
        AnchorPane.setLeftAnchor(head, 50.0);
        Label UserNameLabel = new Label(user.getUserName());
        AnchorPane.setTopAnchor(UserNameLabel, 10.0);
        AnchorPane.setLeftAnchor(UserNameLabel, 96.0);
        Label UserTypeLabel = new Label();
        switch (user.getType()) {
          case Offline -> UserTypeLabel.setText("离线账户");
          case Yggdrasil -> UserTypeLabel.setText("Yggdrasil外置登录");
        }
        AnchorPane.setTopAnchor(UserTypeLabel, 30.0);
        AnchorPane.setLeftAnchor(UserTypeLabel, 96.0);
        JFXButton detele = new JFXButton("删除");
        AnchorPane.setTopAnchor(detele, 17.0);
        AnchorPane.setLeftAnchor(detele, 530.0);
        detele.setOnAction(event -> {
          try {
            JsonObject UserListObject = UsersList.getUserListObject();
            UserListObject.remove(UserName);
            JsonUtils.writeObjectToFile(UserListFile, UserListObject, Json.getBuilder().setPrettyPrinting());
            setUserList(pane);
          } catch (IOException e) {
            ExceptionWindow.setErrorWin(e);
          }
        });
        Consoler.setCss("BlackBorder", UserPane);
        UserPane.getChildren().addAll(enter, head, UserNameLabel, UserTypeLabel, detele);
        vBox.getChildren().add(UserPane);
      }
    }
    Consoler.setStylesheets(vBox);
    scrollPane.setContent(vBox);
    pane.getChildren().add(scrollPane);
  }
}
