package org.ecsail.mvci_boat;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Builder;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.static_tools.StringTools;
import org.ecsail.widgetfx.DialogueFx;

public class CustomOwnerDialogue implements Builder<Alert> {

    private final BoatModel boatModel;
    private final BoatView boatView;
    private Alert alert;
    private ChangeListener<String> idTextFieldChangeListener;
    private ChangeListener<MembershipListDTO> selectedOwnerListener;
    private TextField idTextField;

    public CustomOwnerDialogue(BoatView boatView) {
        this.boatModel = boatView.getBoatModel();
        this.boatView = boatView;
    }

    @Override
    public Alert build() {
        this.alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Owner");
        alert.setHeaderText("Please enter membership ID of new owner!");
        DialogueFx.tieAlertToStage(alert);
        // Add style sheets
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        // Add the grid to the alert
        alert.getDialogPane().setContent(createGrid());
        return alert;
    }

    private Node createGrid() {
        // Create a new grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        // Add the text field to the grid
        grid.add(new Label("Membership Id:"), 0, 0);
        grid.add(getIdTextField(), 1, 0);
        grid.add(new Label("Member Name:"), 0, 1);
        grid.add(getNameTextField(), 1, 1);
        return grid;
    }

    private Node getNameTextField() {
        TextField nameTextField = new TextField();
        nameTextField.setEditable(false);
        nameTextField.setPromptText("Name");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        BooleanBinding isInvalid = nameTextField.textProperty().isEqualTo("").or(nameTextField.textProperty().isEqualTo("Member Not Found"));
        okButton.disableProperty().bind(isInvalid);
        // Listener for selected owner property
        this.selectedOwnerListener = (observable1, oldValue1, newValue1) -> {
            System.out.println(boatModel.getSelectedOwner().getFirstName() + " " + boatModel.getSelectedOwner().getLastName());
            nameTextField.setText(boatModel.getSelectedOwner().getFirstName() + " " + boatModel.getSelectedOwner().getLastName());
        };
        boatModel.selectedOwnerProperty().addListener(selectedOwnerListener);
        return nameTextField;
    }

    private Node getIdTextField() {
        this.idTextField = new TextField();
        idTextField.setPromptText("Id");
        // Listener for textField1
        this.idTextFieldChangeListener = (observable, oldValue, newValue) -> {
            if (StringTools.isInteger(newValue)) boatModel.setMembershipId(Integer.parseInt(newValue));
            else boatModel.setMembershipId(-1); // set to an invalid id
            boatView.action.accept(BoatMessage.GET_MEMBERSHIP); // directly call the action here
        };
        idTextField.textProperty().addListener(idTextFieldChangeListener);
        return idTextField;
    }

    public void removeListeners() {
        idTextField.textProperty().removeListener(idTextFieldChangeListener); // remove the listener when done
        boatModel.selectedOwnerProperty().removeListener(selectedOwnerListener); // remove the listener when done
    }
}
