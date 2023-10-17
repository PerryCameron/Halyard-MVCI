package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableViewFx;
import org.ecsail.widgetfx.VBoxFx;

import java.time.Year;

public class InvoiceView implements Builder<Tab> {
    private final MembershipView membershipView;
    private final InvoiceDTO invoiceDTO;
    public InvoiceView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.invoiceDTO = membershipView.getMembershipModel().getSelectedInvoice();
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText(String.valueOf(invoiceDTO.getYear()));
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2),"custom-tap-pane-frame",false); // makes outer border
        BorderPane borderPane = new BorderPane();
        borderPane.setId("box-background-light");
        VBox.setVgrow(borderPane, Priority.ALWAYS); // causes slip tab to grow to fit vertical space
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
    }

}
