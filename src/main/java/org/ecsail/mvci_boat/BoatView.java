package org.ecsail.mvci_boat;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.interfaces.ListCallBack;
import org.ecsail.mvci_boatlist.BoatListModel;
import org.ecsail.mvci_boatlist.BoatListRadioHBox;
import org.ecsail.mvci_boatlist.BoatListSettingsCheckBox;
import org.ecsail.mvci_boatlist.BoatListTableView;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.function.Consumer;


public class BoatView implements Builder<Region>, ListCallBack {

    BoatModel boatModel;
    Consumer<Mode> action;
    public BoatView(BoatModel rm, Consumer<Mode> a) {
        boatModel = rm;
        action = a;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
//        borderPane.setRight(setUpRightPane());
//        borderPane.setCenter(setUpTableView());
        return borderPane;
    }


}
