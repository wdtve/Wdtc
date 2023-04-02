package org.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.WdtcLauncher.FilePath;
import org.WdtcUI.users.UsersWin;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Controller {
    public static final Stage MainStage = AppMain.MainStage;
    private static final Logger logmaker = Logger.getLogger(Controller.class);
    private static final File u_s = new File(FilePath.getUsersSettingJson());
    private static boolean log = false;
    private static boolean BMCLAPI = false;
    @FXML
    private Label start_label;
    @FXML
    private TextField stater_path;

    @FXML
    private void setDownload_game() throws IOException {
        MainStage.setTitle("Wdtc - Demo - 下载游戏");
//        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/down_win.fxml")));
        Pane pane = new Pane();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> {
            MainStage.setTitle("Wdtc - Demo");
            try {
                Pane homepane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/stage.fxml")));
                MainStage.setScene(new Scene(homepane));
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        back.setStyle("-fx-border-color: #000000");
        TextField textField = new TextField();
        textField.setPromptText("三个阶段");
        textField.setLayoutX(218.0);
        textField.setLayoutY(320.0);
        Button downGame = new Button("下载");
        downGame.setLayoutX(235.0);
        downGame.setLayoutY(227.0);
        downGame.setPrefHeight(23.0);
        downGame.setPrefWidth(108.0);
        downGame.setOnAction(event -> new VersionList(textField, BMCLAPI).getVersion_List());
        Label time = new Label("下载时间不会太长");
        Label status_bar = new Label("下面是状态栏");
        time.setLayoutX(241.0);
        time.setLayoutY(160.0);
        status_bar.setLayoutX(253.0);
        status_bar.setLayoutY(305.0);
        Button bmclHome = new Button("BMCLAPI");
        bmclHome.setLayoutX(531.0);
        bmclHome.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("cmd.exe /C start https://bmclapidoc.bangbang93.com/");
            } catch (IOException | RuntimeException exception) {
                ErrorWin.setErrorWin(exception);
            }
        });
        Label read_bmcl = new Label("国内快速下载源→");
        read_bmcl.setLayoutX(429.0);
        read_bmcl.setLayoutY(4.0);
        pane.getChildren().addAll(back, textField, downGame, time, status_bar, bmclHome, read_bmcl);
        Scene down_scene = new Scene(pane);
        MainStage.setScene(down_scene);
    }

    @FXML
    private void setStart() throws IOException {
        MainStage.setTitle("Wdtc - Demo - 启动游戏");
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Launcher_Win.fxml")));
        Scene scene = new Scene(pane);
        MainStage.setScene(scene);
    }

    @FXML
    private void setGithub_button() throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start https://github.com/zjh411025/Wdtc");
    }

    @FXML
    private void setSetting() {
        Pane pane = new Pane();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> {
            MainStage.setTitle("Wdtc - Demo");
            try {
                Pane homepane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/stage.fxml")));
                MainStage.setScene(new Scene(homepane));
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        back.setStyle("-fx-border-color: #000000");
        MainStage.setTitle("Wdtc - Demo - Setting");
        RadioButton true_log = new RadioButton("显示");
        RadioButton false_log = new RadioButton("不显示");
        true_log.setLayoutX(77.0);
        true_log.setLayoutY(111.0);
        false_log.setLayoutY(111.0);
        false_log.setLayoutX(139.0);
        false_log.setSelected(log);
        false_log.setOnAction(event -> {
            true_log.setSelected(false);
            logmaker.info("* 启动日志器关闭显示");
            Controller.log = false;
        });
        true_log.setOnAction(event -> {
            false_log.setSelected(false);
            logmaker.info("* 启动日志器开启显示");
            Controller.log = true;
        });
        Label cmd = new Label("启动时是否显示cmd窗口(如果按启动后长时间没反应可以设置显示,默认不显示):");
        cmd.setLayoutX(77.0);
        cmd.setLayoutY(89.0);
        Label BMCLAPI_Mess = new Label("是否启用BMCLAPI下载源(启用后下载速度也许会更快,默认不启用):");
        BMCLAPI_Mess.setLayoutX(77.0);
        BMCLAPI_Mess.setLayoutY(138.0);
        RadioButton true_bmcl = new RadioButton("启用");
        RadioButton false_bmcl = new RadioButton("不启用");
        true_bmcl.setLayoutX(76.0);
        true_bmcl.setLayoutY(159.0);
        false_bmcl.setLayoutX(139.0);
        false_bmcl.setLayoutY(159.0);
        true_bmcl.setOnAction(event -> {
            false_bmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已开启");
            Controller.BMCLAPI = true;
        });
        false_bmcl.setOnAction(event -> {
            true_bmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已关闭");
            Controller.BMCLAPI = false;
        });
        pane.getChildren().addAll(back, true_bmcl, false_bmcl, true_log, false_log, cmd, BMCLAPI_Mess);
        Scene scene = new Scene(pane, 600.0, 400.0);
        MainStage.setScene(scene);
        false_bmcl.setSelected(!BMCLAPI);
        false_log.setSelected(!log);
        true_bmcl.setSelected(BMCLAPI);
        true_log.setSelected(log);
    }

    @FXML
    private void setIsBMCLAPI() throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start https://bmclapidoc.bangbang93.com/");
    }

    @FXML
    private void setStarter() {
        new StartVersionList(start_label, log, stater_path, BMCLAPI).getStartList();
    }

    @FXML
    private void setDelete() {
        new DeleteVersion().getStartList();
    }

    @FXML
    private void Setcompletioner() {
        new CompletionGame().completion_game();
    }


    @FXML
    private void setSettingskin() {
        UsersWin.setUserWin("修改账户名");
    }

    @FXML
    private void setHome() throws IOException {
        MainStage.setTitle("Wdtc - Demo");
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/stage.fxml")));
        MainStage.setScene(new Scene(pane));

    }
    /*private void Setapplication() throws IOException {
        UsersSetting.user_game.clear();
        UsersSetting.users_jvm.clear();
        UsersSetting usersSetting = new UsersSetting();
        usersSetting.setUsers_jvm(userjvm.getText());
        usersSetting.setUser_game(WinWide.getText());
        usersSetting.setUser_game(WinHide.getText());
        usersSetting.setUser_game(GameMemory.getText());
        String usersetting = JSON.toJSONString(usersSetting);
        u_s.delete();
        FileUtils.writeStringToFile(u_s, usersetting, "UTF-8");
    }*/
}