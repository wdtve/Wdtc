package org.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import static org.WdtcUI.DownloadGameWin.BMCLAPI;
import static org.WdtcUI.DownloadGameWin.log;

public class SettingWin {
    private static final Logger logmaker = Logger.getLogger(SettingWin.class);

    public static void setSettingWin(Stage MainStage) {
        Pane pane = new Pane();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> HomeWin.setHome(MainStage));
        back.setStyle("-fx-border-color: #000000");
        MainStage.setTitle("Wdtc - Demo - Setting");
        RadioButton true_log = new RadioButton("显示");
        RadioButton false_log = new RadioButton("不显示");
        true_log.setLayoutX(77.0);
        true_log.setLayoutY(111.0);
        false_log.setLayoutY(111.0);
        false_log.setLayoutX(139.0);
        false_log.setOnAction(event -> {
            true_log.setSelected(false);
            logmaker.info("* 启动日志器关闭显示");
            log = false;
        });
        true_log.setOnAction(event -> {
            false_log.setSelected(false);
            logmaker.info("* 启动日志器开启显示");
            log = true;
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
            BMCLAPI = true;
        });
        false_bmcl.setOnAction(event -> {
            true_bmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已关闭");
            BMCLAPI = false;
        });
        pane.getChildren().addAll(back, true_bmcl, false_bmcl, true_log, false_log, cmd, BMCLAPI_Mess);
        Scene scene = new Scene(pane, 600.0, 400.0);
        MainStage.setScene(scene);
        false_bmcl.setSelected(!BMCLAPI);
        false_log.setSelected(!log);
        true_bmcl.setSelected(BMCLAPI);
        true_log.setSelected(log);
    }
}
