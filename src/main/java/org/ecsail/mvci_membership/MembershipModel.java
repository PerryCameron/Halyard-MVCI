package org.ecsail.mvci_membership;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.ecsail.dto.*;
import org.ecsail.mvci_main.MainModel;

public class MembershipModel {

    // person model
    private ObservableMap<PersonDTO, TableView<EmailDTO>> emailTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TableView<PhoneDTO>> phoneTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TableView<AwardDTO>> awardTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TableView<OfficerDTO>> officerTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO,StackPane> stackPaneMap = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, RadioButton> selectedRadioForPerson = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, ComboBox<String>> personComboBox = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, Tab> personPropertiesTab = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TextField> personTextField = FXCollections.observableHashMap();
    private ObservableList<PersonDTO> people = FXCollections.observableArrayList();
    private final SimpleObjectProperty<PersonDTO> selectedPerson = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipListDTO> membership = new SimpleObjectProperty<>();
    private final BooleanProperty listsLoaded = new SimpleBooleanProperty(false);
    private final MainModel mainModel;




    public ObservableMap<PersonDTO, TableView<OfficerDTO>> getOfficerTableView() {
        return officerTableView;
    }

    public void setOfficerTableView(ObservableMap<PersonDTO, TableView<OfficerDTO>> officerTableView) {
        this.officerTableView = officerTableView;
    }

    public ObservableMap<PersonDTO, TableView<AwardDTO>> getAwardTableView() {
        return awardTableView;
    }

    public void setAwardTableView(ObservableMap<PersonDTO, TableView<AwardDTO>> awardTableView) {
        this.awardTableView = awardTableView;
    }

    public ObservableMap<PersonDTO, Tab> getPersonPropertiesTab() {
        return personPropertiesTab;
    }

    public void setPersonPropertiesTab(ObservableMap<PersonDTO, Tab> personPropertiesTab) {
        this.personPropertiesTab = personPropertiesTab;
    }

    public ObservableMap<PersonDTO, TableView<PhoneDTO>> getPhoneTableView() {
        return phoneTableView;
    }

    public void setPhoneTableView(ObservableMap<PersonDTO, TableView<PhoneDTO>> phoneTableView) {
        this.phoneTableView = phoneTableView;
    }

    public ObservableMap<PersonDTO, TableView<EmailDTO>> getEmailTableView() {
        return emailTableView;
    }

    public void setEmailTableView(ObservableMap<PersonDTO, TableView<EmailDTO>> emailTableView) {
        this.emailTableView = emailTableView;
    }

    public ObservableMap<PersonDTO, ComboBox<String>> getPersonComboBox() {
        return personComboBox;
    }

    public void setPersonComboBox(ObservableMap<PersonDTO, ComboBox<String>> personComboBox) {
        this.personComboBox = personComboBox;
    }

    public ObservableMap<PersonDTO, TextField> getPersonTextField() {
        return personTextField;
    }

    public void setPersonTextField(ObservableMap<PersonDTO, TextField> personTextField) {
        this.personTextField = personTextField;
    }

    public PersonDTO getSelectedPerson() {
        return selectedPerson.get();
    }
    public SimpleObjectProperty<PersonDTO> selectedPersonProperty() {
        return selectedPerson;
    }
    public void setSelectedPerson(PersonDTO selectedPerson) {
        this.selectedPerson.set(selectedPerson);
    }
    public void setSelectedRadioForPerson(ObservableMap<PersonDTO, RadioButton> selectedRadioForPerson) {
        this.selectedRadioForPerson = selectedRadioForPerson;
    }
    public ObservableMap<PersonDTO, RadioButton> getSelectedRadioForPerson() {
        return selectedRadioForPerson;
    }
    public ObservableMap<PersonDTO, StackPane> getStackPaneMap() {
        return stackPaneMap;
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
