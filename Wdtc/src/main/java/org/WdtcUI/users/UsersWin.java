package org.WdtcUI.users;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UsersWin {
    private static final Stage userwin = new Stage();

    public static void setUserWin(String wintitle) throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(UsersWin.class.getResource("/fxml/users.fxml")));
        Scene scene = new Scene(pane);
        userwin.setScene(scene);
        userwin.setTitle(wintitle);
        userwin.getIcons().add(new Image("ico.jpg"));
        userwin.setResizable(false);
        userwin.show();
    }

}
