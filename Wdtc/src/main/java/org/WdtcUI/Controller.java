package org.WdtcUI;

import com.alibaba.fastjson2.JSON;
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
import org.WdtcUI.users.Registeruser;
import org.WdtcUI.users.UsersSetting;
import org.WdtcUI.users.UsersWin;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    public static final Stage MainStage = AppMain.MainStage;
    private static final Logger logmaker = Logger.getLogger(Controller.class);
    private static boolean log = false;
    private static boolean BMCLAPI = false;
    private static final File u_s = new File(FilePath.getUsersSettingJson());
    @FXML
    private Button Down_WinDownload;
    @FXML
    private Button start;
    @FXML
    private Button download_game;
    @FXML
    private TextField textField;
    @FXML
    private Label start_label;
    @FXML
    private Button github_button;
    @FXML
    private Button Setting;
    @FXML
    private RadioButton true_log = new RadioButton();
    @FXML
    private RadioButton false_log = new RadioButton();
    @FXML
    private RadioButton true_BMCLAPI = new RadioButton();
    @FXML
    private RadioButton false_BMCLAPI = new RadioButton();
    @FXML
    private Button isBMCLAPI;
    @FXML
    private TextField stater_path;
    @FXML
    private TextField Registerusername;
    @FXML
    private Label OKRegister;
    @FXML
    private TextField userjvm;
    @FXML
    private TextField WinWide = new TextField();
    @FXML
    private TextField WinHide = new TextField();
    @FXML
    private TextField GameMemory = new TextField();

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return !m.find();
    }

    @FXML
    private void setDownWinDownload() {
        new VersionList(textField, BMCLAPI).getVersion_List();
    }

    @FXML
    private void setDownload_game() throws IOException {
        MainStage.setTitle("Wdtc - Demo - 下载游戏");
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/down_win.fxml")));
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
    private void setTrue_log() {
        false_log.setSelected(false);
        logmaker.info("* 启动日志器开启显示");
        Controller.log = true;
    }

    @FXML
    private void setFalse_log() {
        true_log.setSelected(false);
        logmaker.info("* 启动日志器关闭显示");
        Controller.log = false;
    }

    @FXML
    private void setSetting() throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Settings_Win.fxml")));
        MainStage.setTitle("Wdtc - Demo - Setting");
        Scene scene = new Scene(pane);
        MainStage.setScene(scene);
    }

    @FXML
    private void setTrue_BMCLAPI() {
        false_BMCLAPI.setSelected(false);
        logmaker.info("* BMCLAPI下载加速已开启");
        Controller.BMCLAPI = true;
    }

    @FXML
    private void setFalse_BMCLAPI() {
        true_BMCLAPI.setSelected(false);
        logmaker.info("* BMCLAPI下载加速已关闭");
        Controller.BMCLAPI = false;
    }

    @FXML
    private void setIsBMCLAPI() throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start https://bmclapidoc.bangbang93.com/");
    }

    @FXML
    private void setStarter() throws IOException {
        new StartVersionList(start_label, log, stater_path, BMCLAPI).getStartList();
    }

    @FXML
    private void setDelete() throws IOException {
        new DeleteVersion().getStartList();
    }

    @FXML
    private void Setcompletioner() throws IOException {
        new CompletionGame().completion_game();
    }

    @FXML
    private void Setregister() throws IOException {
        String username = Registerusername.getText();
        if (isContainChinese(username)) {
            Registeruser.RegisterUser(username);
            logmaker.info("* 账户注册" + username + "成功");
            OKRegister.setText("注册" + username + "成功");
        } else {
            OKRegister.setText("不能带中文字符哦");
        }
    }

    @FXML
    private void setSettingskin() throws IOException {
        UsersWin.setUserWin("修改账户名");
    }

    @FXML
    void setbuy() throws IOException {
        Runtime.getRuntime().exec("cmd.exe /C start https://www.minecraft.net/");
    }

    @FXML
    private void setHome() throws IOException {
        MainStage.setTitle("Wdtc - Demo");
        Pane main_pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/stage.fxml")));
        Scene scene = new Scene(main_pane);
        MainStage.setScene(scene);
    }
    @FXML
    private void Setapplication() throws IOException {
        UsersSetting.user_game.clear();
        UsersSetting.users_jvm.clear();
        UsersSetting usersSetting = new UsersSetting();
        usersSetting.setUsers_jvm(userjvm.getText());
        usersSetting.setUser_game(WinWide.getText());
        usersSetting.setUser_game(WinHide.getText());
        usersSetting.setUser_game(GameMemory.getText());
        String usersetting = JSON.toJSONString(usersSetting);
        u_s.delete();
        FileUtils.writeStringToFile(u_s,usersetting,"UTF-8");
    }


}
