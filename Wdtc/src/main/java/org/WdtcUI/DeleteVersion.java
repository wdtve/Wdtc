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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.WdtcDownload.SetFilePath.SetPath;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DeleteVersion {
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static final Logger logmaker = Logger.getLogger(DeleteVersion.class);
    private static final Stage Delete = new Stage();

    private static final VBox V_BOX = new VBox();
    private static final ScrollPane SCROLL_PANE = new ScrollPane();
    private static final Scene SCENE = new Scene(SCROLL_PANE);


    public void getStartList() throws IOException {
        logmaker.info("* 开始加载版本列表");
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
                    Delete.close();
                    delFolder(s_e_j.getString("v_lib_path") + button.getText());
                    logmaker.info("* 版本删除成功");

                });
            }
            SCROLL_PANE.setContent(V_BOX);
            Delete.setHeight(500);
            Delete.setWidth(500);
            Delete.getIcons().add(new Image("ico.jpg"));
            Delete.setTitle("Start Version List");
            Delete.setScene(SCENE);
            Delete.setResizable(false);
            logmaker.info("* 版本列表加载成功");
            Delete.show();
            Delete.setOnCloseRequest(windowEvent -> V_BOX.getChildren().clear());
        } catch (NullPointerException e) {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/NullVersion.fxml")));
            Scene scene = new Scene(pane);
            Delete.setScene(scene);
            Delete.getIcons().add(new Image("ico.jpg"));
            Delete.setTitle("Error");
            Delete.setResizable(false);
            logmaker.error("* 版本文件夹为空");
            Delete.show();


        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径 ,"Z:/xuyun/save"
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            ErrorWin.setErrorWin(e);
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径 ,"Z:/xuyun/save"
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
