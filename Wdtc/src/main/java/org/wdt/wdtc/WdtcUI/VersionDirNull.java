package org.wdt.wdtc.WdtcUI;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

public class VersionDirNull {
    private static final Logger logmaker = getWdtcLogger.getLogger(VersionDirNull.class);

    public static void setNullWin(Stage MainStage) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            Label label = new Label("您没有游戏版本,请去下载");
            Pane pane = new Pane();
            pane.prefHeight(400.0);
            pane.prefWidth(600.0);
            label.setLayoutX(217.0);
            label.setLayoutY(193.0);
            pane.getChildren().add(label);
            Scene scene = new Scene(pane, 600, 400);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/ico.jpg"));
            stage.setTitle("版本文件夹为空");
            stage.initOwner(MainStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        });
        logmaker.error("* 版本文件夹为空");
    }
}
