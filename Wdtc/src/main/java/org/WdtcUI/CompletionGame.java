package org.WdtcUI;

import com.alibaba.fastjson2.JSONObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.WdtcDownload.CompleteDocument.CompleteDocument;
import org.WdtcDownload.SetFilePath.SetPath;
import org.WdtcLauncher.Version;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CompletionGame {
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static final Logger logmaker = Logger.getLogger(CompletionGame.class);
    private static final Stage stage = new Stage();

    private static final VBox V_BOX = new VBox();
    private static final ScrollPane SCROLL_PANE = new ScrollPane();
    private static final Scene SCENE = new Scene(SCROLL_PANE);


    public void completion_game() throws IOException {
        SetPath.main();
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_j = JSONObject.parseObject(s_e);
        File version_path = new File(s_e_j.getString("v_lib_path"));
        File[] files = version_path.listFiles();
        //foreach遍历数组
        try {
            for (File file2 : files) {
                Button button = new Button(file2.getName());
                V_BOX.getChildren().add(button);
                button.setMaxSize(100, 50);
                button.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    V_BOX.getChildren().clear();
                    stage.close();
                    Version version = new Version(button.getText());
                    try {
                        CompleteDocument completeDocument = new CompleteDocument(new File(version.getVersion_json()), version.getVersion_path(), version.getVersion(), false);
                        completeDocument.readdown();
                        completeDocument.gethash();
                        logmaker.info("* 版本补全完成");
                    } catch (IOException | InterruptedException | RuntimeException e) {
                        ErrorWin.setErrorWin(e);
                    }
                });
            }
            SCROLL_PANE.setContent(V_BOX);
            stage.setHeight(500);
            stage.setWidth(500);
            stage.getIcons().add(new Image("ico.jpg"));
            stage.setTitle("Start Version List");
            stage.setScene(SCENE);
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(windowEvent -> V_BOX.getChildren().clear());
        } catch (NullPointerException e) {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/NullVersion.fxml")));
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.getIcons().add(new Image("ico.jpg"));
            stage.setTitle("Error");
            stage.setResizable(false);
            logmaker.error("* 版本文件夹为空");
            stage.show();


        }
    }
}
