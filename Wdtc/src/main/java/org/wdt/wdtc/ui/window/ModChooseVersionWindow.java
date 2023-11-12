package org.wdt.wdtc.ui.window;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.wdtc.core.download.fabric.FabricAPIDownloadTask;
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList;
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.core.download.fabric.FabricVersionList;
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.core.download.forge.ForgeVersionList;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.download.infterface.VersionListInterface;
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.core.download.quilt.QuiltVersionList;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.ModUtils;

import java.io.IOException;

public class ModChooseVersionWindow {
  private final ModUtils.KindOfMod kind;

  private final Stage MainStage;
  private final WindwosSizeManger size;
  private final Launcher launcher;

  public ModChooseVersionWindow(ModUtils.KindOfMod kind, Stage MainStage, Launcher launcher) {
    this.kind = kind;
    this.MainStage = MainStage;
    this.launcher = launcher;
    this.size = new WindwosSizeManger(MainStage);
  }

  public VersionListInterface getModVersionList() throws IOException {
    if (kind == ModUtils.KindOfMod.FORGE) {
      return new ForgeVersionList(launcher);
    } else if (kind == ModUtils.KindOfMod.FABRICAPI) {
      return new FabricAPIVersionList(launcher);
    } else if (kind == ModUtils.KindOfMod.QUILT) {
      return new QuiltVersionList(launcher);
    } else {
      return new FabricVersionList();
    }
  }

  public void setModChooser() throws IOException {
    JFXButton back = new JFXButton("返回");
    back.getStyleClass().add("BlackBorder");
    Pane pane = new Pane();
    VBox ButtonList = new VBox();
    ScrollPane list = new ScrollPane();
    Label tips = new Label("选择一个Mod版本:");
    tips.setLayoutX(149.0);
    tips.setLayoutY(67.0);
    list.setLayoutY(134.0);
    list.setPrefSize(600, 316);
    Platform.runLater(() -> {
      try {
        for (VersionJsonObjectInterface versionJsonObject : getModVersionList().getVersionList()) {
          JFXButton VersionButton = getVersionButton(versionJsonObject, ButtonList);
          size.ModifyWindwosSize(ButtonList, VersionButton);
        }
      } catch (IOException e) {
        ExceptionWindow.setErrorWin(e);
      }
    });
    list.setContent(ButtonList);
    size.ModifyWindwosSize(pane, list, back, tips);
    pane.setBackground(Consoler.getBackground());
    Consoler.setStylesheets(pane);
    MainStage.setScene(new Scene(pane));
    back.setOnAction(event -> {
      ModChooseWindow Choose = new ModChooseWindow(launcher, MainStage);
      Choose.setChooseWin();
    });

  }

  private JFXButton getVersionButton(VersionJsonObjectInterface versionJsonObject, VBox ButtonList) {
    JFXButton VersionButton = new JFXButton(versionJsonObject.getVersionNumber());
    VersionButton.setStyle("-fx-border-color: #000000");
    VersionButton.setPrefWidth(600.0);
    VersionButton.setOnAction(event -> {
      switch (kind) {
        case FORGE -> launcher.setForgeModDownloadInfo(new ForgeDownloadInfo(launcher, versionJsonObject));
        case FABRIC -> launcher.setFabricModInstallInfo(new FabricDonwloadInfo(launcher, versionJsonObject));
        case QUILT -> launcher.setQuiltModDownloadInfo(new QuiltInstallTask(launcher, versionJsonObject));
        case FABRICAPI -> {
          if (launcher.fabricModInstallInfo != null) {
            launcher.fabricModInstallInfo.setApiDownloadTask(new FabricAPIDownloadTask(launcher, versionJsonObject));
          }
        }
      }
      ModChooseWindow Choose = new ModChooseWindow(launcher, MainStage);
      Choose.setChooseWin();
      ButtonList.getChildren().clear();
    });
    return VersionButton;
  }
}
