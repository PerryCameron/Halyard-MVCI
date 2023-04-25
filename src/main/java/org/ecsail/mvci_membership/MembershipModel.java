package org.ecsail.mvci_membership;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.Messages;
import org.ecsail.mvci_main.MainModel;

import java.util.HashMap;

public class MembershipModel {

    private final ObservableMap<PersonDTO,StackPane> stackPaneMap = FXCollections.observableHashMap();
    private final ObservableMap<RadioButton, Messages> message = FXCollections.observableHashMap();
    private ObservableList<PersonDTO> people = FXCollections.observableArrayList();
    private final SimpleObjectProperty<MembershipListDTO> membership = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<RadioButton> selectedRadioButton = new SimpleObjectProperty<>(new RadioButton());

    private final BooleanProperty listsLoaded = new SimpleBooleanProperty(false);
    private final MainModel mainModel;




    public ObservableMap<PersonDTO, StackPane> getStackPaneMap() {
        return stackPaneMap;
    }

    public RadioButton getSelectedRadioButton() {
        return selectedRadioButton.get();
    }

    public SimpleObjectProperty<RadioButton> selectedRadioButtonProperty() {
        return selectedRadioButton;
    }

    public void setSelectedRadioButton(RadioButton selectedRadioButton) {
        this.selectedRadioButton.set(selectedRadioButton);
    }

    public MainModel getMainModel() {
        return mainModel;
    }
    public boolean isListsLoaded() {
        return listsLoaded.get();
    }
    public BooleanProperty listsLoadedProperty() {
        return listsLoaded;
    }
    public void setListsLoaded(boolean listsLoaded) {
        this.listsLoaded.set(listsLoaded);
    }
    public ObservableList<PersonDTO> getPeople() {
        return people;
    }
    public void setPeople(ObservableList<PersonDTO> people) {
        this.people = people;
    }
    public MembershipListDTO getMembership() {
        return membership.get();
    }
    public SimpleObjectProperty<MembershipListDTO> membershipProperty() {
        return membership;
    }
    public void setMembership(MembershipListDTO membership) {
        this.membership.set(membership);
    }
    public MembershipModel(MembershipListDTO membershipListDTO, MainModel mainModel) {
        membership.set(membershipListDTO);
        this.mainModel = mainModel;
    }


}
