package org.wdt.wdtc.ui.window;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class TipsWindow {
    private final String tips;
    private final Stage MainStage;
    private String title;

    public TipsWindow(String tips, Stage mainStage) {
        this.tips = tips;
        MainStage = mainStage;
    }

    public void show() {
        Stage TipStage = new Stage();
        TipStage.initOwner(MainStage);
        TipStage.initModality(Modality.WINDOW_MODAL);
        AnchorPane pane = new AnchorPane();
        Label label = new Label(tips);
        AnchorPane.setLeftAnchor(label, 10.0);
        AnchorPane.setRightAnchor(label, 10.0);
        TipStage.show();
    }
}
