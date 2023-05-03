package org.wdt.WdtcUI;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorWin {
    private static final Logger LOGGER = Logger.getLogger(ErrorWin.class);
    private static final VBox V_BOX = new VBox();
    private static final Stage stage = new Stage();
    private static final ScrollPane sp = new ScrollPane();
    private static final Scene scene = new Scene(sp);

    public static void setErrorWin(Throwable e) {
        LOGGER.error("* 发生错误:", e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        setWin(sw.getBuffer().toString());
    }

    public static void setWin(String e) {
        Label label = new Label();
        label.setText(e);
        V_BOX.getChildren().add(label);
        sp.setContent(V_BOX);
        stage.setTitle("发生错误!");
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.getIcons().add(new Image("ico.jpg"));
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> V_BOX.getChildren().clear());
    }
}
