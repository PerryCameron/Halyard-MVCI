package org.ecsail.mvci_membership;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.ecsail.dto.*;
import org.ecsail.enums.Success;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.mvci_main.MainModel;

import java.util.ArrayList;

public class MembershipModel {

    // Person information for membership
    private ObservableMap<PersonDTO, TableView<EmailDTO>> emailTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TableView<PhoneDTO>> phoneTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TableView<AwardDTO>> awardTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TableView<OfficerDTO>> officerTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, StackPane> stackPaneMap = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, RadioButton> selectedRadioForPerson = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, ComboBox<String>> personComboBox = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, Tab> selectedPropertiesTab = FXCollections.observableHashMap();
    private ObservableMap<PersonDTO, TextField> personTextField = FXCollections.observableHashMap();
    private ObservableList<PersonDTO> people = FXCollections.observableArrayList();
    private final SimpleObjectProperty<MembershipListDTO> membership = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SlipDTO> slip = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<BoatDTO>> boatTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<NotesDTO>> notesTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<MembershipIdDTO>> idTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<InvoiceDTO>> invoiceListTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> peopleTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> infoTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> extraTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SlipUser.slip> slipRelationStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<MembershipMessage> returnMessage = new SimpleObjectProperty<>();
    private StringProperty sublease = new SimpleStringProperty("");
    private StringProperty membershipId = new SimpleStringProperty("");
    private final MainModel mainModel;
    private ObservableMap<String,Control> slipControls = FXCollections.observableHashMap();
    private SimpleObjectProperty<NotesDTO> selectedNote = new SimpleObjectProperty<>();
    private SimpleObjectProperty<AwardDTO> selectedAward = new SimpleObjectProperty<>();
    private SimpleObjectProperty<BoatDTO> selectedBoat = new SimpleObjectProperty<>();
    private SimpleObjectProperty<EmailDTO> selectedEmail = new SimpleObjectProperty<>();
    private SimpleObjectProperty<MembershipIdDTO> selectedMembershipId = new SimpleObjectProperty<>();
    private SimpleObjectProperty<OfficerDTO> selectedOfficer = new SimpleObjectProperty<>();
    private SimpleObjectProperty<PersonDTO> selectedPerson = new SimpleObjectProperty<>();
    private SimpleObjectProperty<PhoneDTO> selectedPhone = new SimpleObjectProperty<>();
    private SimpleObjectProperty<InvoiceDTO> selectedInvoice = new SimpleObjectProperty<>();
    private SimpleObjectProperty<PaymentDTO> selectedPayment = new SimpleObjectProperty<>();
    private StringProperty selectedString = new SimpleStringProperty("");
    private SimpleIntegerProperty selectedInvoiceCreateYear = new SimpleIntegerProperty(0);
    private SimpleObjectProperty<Success> invoiceSaved = new SimpleObjectProperty(Success.NULL);
    ObservableList<DbInvoiceDTO> categories = FXCollections.observableArrayList();




    public PaymentDTO getSelectedPayment() {
        return selectedPayment.get();
    }

    public SimpleObjectProperty<PaymentDTO> selectedPaymentProperty() {
        return selectedPayment;
    }

    public void setSelectedPayment(PaymentDTO selectedPayment) {
        this.selectedPayment.set(selectedPayment);
    }

    public Success getInvoiceSaved() {
        return invoiceSaved.get();
    }

    public SimpleObjectProperty<Success> invoiceSavedProperty() {
        return invoiceSaved;
    }

    public void setInvoiceSaved(Success invoiceSaved) {
        this.invoiceSaved.set(invoiceSaved);
    }

    public int getSelectedInvoiceCreateYear() {
        return selectedInvoiceCreateYear.get();
    }

    public SimpleIntegerProperty selectedInvoiceCreateYearProperty() {
        return selectedInvoiceCreateYear;
    }

    public void setSelectedInvoiceCreateYear(int selectedInvoiceCreateYear) {
        this.selectedInvoiceCreateYear.set(selectedInvoiceCreateYear);
    }

    public TableView<InvoiceDTO> getInvoiceListTableView() {
        return invoiceListTableView.get();
    }

    public SimpleObjectProperty<TableView<InvoiceDTO>> invoiceListTableViewProperty() {
        return invoiceListTableView;
    }

    public void setInvoiceListTableView(TableView<InvoiceDTO> invoiceListTableView) {
        this.invoiceListTableView.set(invoiceListTableView);
    }

    public InvoiceDTO getSelectedInvoice() {
        return selectedInvoice.get();
    }

    public SimpleObjectProperty<InvoiceDTO> selectedInvoiceProperty() {
        return selectedInvoice;
    }

    public void setSelectedInvoice(InvoiceDTO selectedInvoice) {
        this.selectedInvoice.set(selectedInvoice);
    }

    public MembershipMessage getReturnMessage() {
        return returnMessage.get();
    }

    public ObjectProperty<MembershipMessage> returnMessageProperty() {
        return returnMessage;
    }

    public void setReturnMessage(MembershipMessage returnMessage) {
        this.returnMessage.set(returnMessage);
    }

    public String getSelectedString() {
        return selectedString.get();
    }

    public StringProperty selectedStringProperty() {
        return selectedString;
    }

    public void setSelectedString(String selectedString) {
        this.selectedString.set(selectedString);
    }

    public PhoneDTO getSelectedPhone() {
        return selectedPhone.get();
    }

    public SimpleObjectProperty<PhoneDTO> selectedPhoneProperty() {
        return selectedPhone;
    }

    public void setSelectedPhone(PhoneDTO selectedPhone) {
        this.selectedPhone.set(selectedPhone);
    }

    public OfficerDTO getSelectedOfficer() {
        return selectedOfficer.get();
    }

    public SimpleObjectProperty<OfficerDTO> selectedOfficerProperty() {
        return selectedOfficer;
    }

    public void setSelectedOfficer(OfficerDTO selectedOfficer) {
        this.selectedOfficer.set(selectedOfficer);
    }

    public MembershipIdDTO getSelectedMembershipId() {
        return selectedMembershipId.get();
    }

    public SimpleObjectProperty<MembershipIdDTO> selectedMembershipIdProperty() {
        return selectedMembershipId;
    }

    public void setSelectedMembershipId(MembershipIdDTO selectedMembershipId) {
        this.selectedMembershipId.set(selectedMembershipId);
    }

    public EmailDTO getSelectedEmail() {
        return selectedEmail.get();
    }

    public SimpleObjectProperty<EmailDTO> selectedEmailProperty() {
        return selectedEmail;
    }

    public void setSelectedEmail(EmailDTO selectedEmail) {
        this.selectedEmail.set(selectedEmail);
    }

    public BoatDTO getSelectedBoat() {
        return selectedBoat.get();
    }

    public SimpleObjectProperty<BoatDTO> selectedBoatProperty() {
        return selectedBoat;
    }

    public void setSelectedBoat(BoatDTO selectedBoat) {
        this.selectedBoat.set(selectedBoat);
    }

    public AwardDTO getSelectedAward() {
        return selectedAward.get();
    }

    public SimpleObjectProperty<AwardDTO> selectedAwardProperty() {
        return selectedAward;
    }

    public void setSelectedAward(AwardDTO selectedAward) {
        this.selectedAward.set(selectedAward);
    }

    public NotesDTO getSelectedNote() {
        return selectedNote.get();
    }

    public SimpleObjectProperty<NotesDTO> selectedNoteProperty() {
        return selectedNote;
    }

    public void setSelectedNote(NotesDTO selectedNote) {
        this.selectedNote.set(selectedNote);
    }

    public TableView<MembershipIdDTO> getIdTableView() {
        return idTableView.get();
    }

    public SimpleObjectProperty<TableView<MembershipIdDTO>> idTableViewProperty() {
        return idTableView;
    }

    public void setIdTableView(TableView<MembershipIdDTO> idTableView) {
        this.idTableView.set(idTableView);
    }

    public TableView<NotesDTO> getNotesTableView() {
        return notesTableView.get();
    }

    public SimpleObjectProperty<TableView<NotesDTO>> notesTableViewProperty() {
        return notesTableView;
    }

    public void setNotesTableView(TableView<NotesDTO> notesTableView) {
        this.notesTableView.set(notesTableView);
    }

    public TableView<BoatDTO> getBoatTableView() {
        return boatTableView.get();
    }

    public SimpleObjectProperty<TableView<BoatDTO>> boatTableViewProperty() {
        return boatTableView;
    }

    public void setBoatTableView(TableView<BoatDTO> boatTableView) {
        this.boatTableView.set(boatTableView);
    }

    public TabPane getExtraTabPane() {
        return extraTabPane.get();
    }

    public SimpleObjectProperty<TabPane> extraTabPaneProperty() {
        return extraTabPane;
    }

    public void setExtraTabPane(TabPane extraTabPane) {
        this.extraTabPane.set(extraTabPane);
    }

    public ObservableMap<String, Control> getSlipControls() {
        return slipControls;
    }

    public void setSlipControls(ObservableMap<String, Control> slipControls) {
        this.slipControls = slipControls;
    }

    public String getMembershipId() {
        return membershipId.get();
    }

    public StringProperty membershipIdProperty() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId.set(membershipId);
    }

    public String getSublease() {
        return sublease.get();
    }

    public StringProperty subleaseProperty() {
        return sublease;
    }

    public void setSublease(String sublease) {
        this.sublease.set(sublease);
    }

    public SlipUser.slip getSlipRelationStatus() {
        return slipRelationStatus.get();
    }

    public SimpleObjectProperty<SlipUser.slip> slipRelationStatusProperty() {
        return slipRelationStatus;
    }

    public void setSlipRelationStatus(SlipUser.slip slipRelationStatus) {
        this.slipRelationStatus.set(slipRelationStatus);
    }

    public TabPane getInfoTabPane() {
        return infoTabPane.get();
    }

    public SimpleObjectProperty<TabPane> infoTabPaneProperty() {
        return infoTabPane;
    }

    public void setInfoTabPane(TabPane infoTabPane) {
        this.infoTabPane.set(infoTabPane);
    }

    public TabPane getPeopleTabPane() {
        return peopleTabPane.get();
    }

    public SimpleObjectProperty<TabPane> peopleTabPaneProperty() {
        return peopleTabPane;
    }

    public void setPeopleTabPane(TabPane peopleTabPane) {
        this.peopleTabPane.set(peopleTabPane);
    }

    public SlipDTO getSlip() {
        return slip.get();
    }

    public SimpleObjectProperty<SlipDTO> slipProperty() {
        return slip;
    }

    public void setSlip(SlipDTO slip) {
        this.slip.set(slip);
    }

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

    public ObservableMap<PersonDTO, Tab> getSelectedPropertiesTab() {
        return selectedPropertiesTab;
    }

    public void setSelectedPropertiesTab(ObservableMap<PersonDTO, Tab> selectedPropertiesTab) {
        this.selectedPropertiesTab = selectedPropertiesTab;
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
