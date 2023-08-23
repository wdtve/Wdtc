package org.wdt.wdtc.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorWindow {
    private static final Logger logmaker = WdtcLogger.getLogger(ErrorWindow.class);


    public static void setErrorWin(Throwable e) {
        logmaker.error("* 发生错误:", e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        setWin(sw.getBuffer().toString(), "发生错误!");
    }

    public static void setWin(String e, String title) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setWidth(1000);
            stage.setHeight(600);
            TextArea label = new TextArea();
            label.setText(e);
            label.setFont(new Font(14));
            label.setPrefHeight(stage.getHeight());
            label.setPrefWidth(stage.getWidth());
            VBox vBox = new VBox();
            stage.setTitle(title);
            stage.getIcons().add(new Image("ico.jpg"));
            WindwosSize size = new WindwosSize(stage);
            size.ModifyWindwosSize(vBox, label);
            stage.setScene(new Scene(vBox));
            stage.show();
            stage.setOnCloseRequest(windowEvent -> vBox.getChildren().clear());
        });
    }
}
