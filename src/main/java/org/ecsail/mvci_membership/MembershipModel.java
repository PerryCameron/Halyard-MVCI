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
import org.ecsail.static_tools.HttpClientUtil;

public class MembershipModel {
    private ObservableList<BoardPositionDTO> boardPositionDTOS;
    // Person information for membership
    private ObservableMap<PersonDTOFx, TableView<EmailDTO>> emailTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TableView<PhoneDTO>> phoneTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TableView<AwardDTO>> awardTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TableView<OfficerDTO>> officerTableView = FXCollections.observableHashMap();
    private final ObservableMap<PersonDTOFx, StackPane> stackPaneMap = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, RadioButton> selectedRadioForPerson = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, ComboBox<String>> personComboBox = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, Tab> selectedPropertiesTab = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TextField> personTextField = FXCollections.observableHashMap();
    private ObservableList<PersonDTOFx> people = FXCollections.observableArrayList();
    private final SimpleObjectProperty<RosterDTOFx> membershipFromRosterList = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipListDTO> membership = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SlipDTO> slip = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<BoatDTOFx>> boatTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<NotesDTOFx>> notesTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<MembershipIdDTOFx>> idTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<InvoiceDTOFx>> invoiceListTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> peopleTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> infoTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> extraTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SlipUser.slip> slipRelationStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<MembershipMessage> returnMessage = new SimpleObjectProperty<>();
    private final StringProperty sublease = new SimpleStringProperty("");
    private final StringProperty membershipId = new SimpleStringProperty("");
    private final HttpClientUtil httpClient;
    private ObservableMap<String,Control> slipControls = FXCollections.observableHashMap();
    private final SimpleObjectProperty<NotesDTOFx> selectedNote = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<AwardDTO> selectedAward = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<BoatDTOFx> selectedBoat = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<EmailDTO> selectedEmail = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipIdDTOFx> selectedMembershipId = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<OfficerDTO> selectedOfficer = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PersonDTOFx> selectedPerson = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PhoneDTO> selectedPhone = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<InvoiceDTOFx> selectedInvoice = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PaymentDTO> selectedPayment = new SimpleObjectProperty<>();
    private final StringProperty selectedString = new SimpleStringProperty("");
    private final SimpleIntegerProperty selectedInvoiceCreateYear = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty selectedMembershipYear = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Success> invoiceSaved = new SimpleObjectProperty<>(Success.NULL);
    private final SimpleBooleanProperty envelopeIsCatalogue = new SimpleBooleanProperty(false);
    private int[] success = new int[16];


    public MembershipModel(RosterDTOFx rosterDTOFx, int selectedYear, MainModel mainModel) {
        membershipFromRosterList.set(rosterDTOFx);
        this.httpClient = mainModel.getHttpClient();
        this.boardPositionDTOS = mainModel.getBoardPositionDTOS();
        this.selectedMembershipYear.set(selectedYear);
    }


    public int[] getSuccess() {
        return success;
    }

    public void setSuccess(int[] success) {
        this.success = success;
    }

    public boolean isEnvelopeIsCatalogue() {
        return envelopeIsCatalogue.get();
    }

    public SimpleBooleanProperty envelopeIsCatalogueProperty() {
        return envelopeIsCatalogue;
    }

    public void setEnvelopeIsCatalogue(boolean envelopeIsCatalogue) {
        this.envelopeIsCatalogue.set(envelopeIsCatalogue);
    }

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

    public TableView<InvoiceDTOFx> getInvoiceListTableView() {
        return invoiceListTableView.get();
    }

    public SimpleObjectProperty<TableView<InvoiceDTOFx>> invoiceListTableViewProperty() {
        return invoiceListTableView;
    }

    public void setInvoiceListTableView(TableView<InvoiceDTOFx> invoiceListTableView) {
        this.invoiceListTableView.set(invoiceListTableView);
    }

    public InvoiceDTOFx getSelectedInvoice() {
        return selectedInvoice.get();
    }

    public SimpleObjectProperty<InvoiceDTOFx> selectedInvoiceProperty() {
        return selectedInvoice;
    }

    public void setSelectedInvoice(InvoiceDTOFx selectedInvoice) {
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

    public MembershipIdDTOFx getSelectedMembershipId() {
        return selectedMembershipId.get();
    }

    public SimpleObjectProperty<MembershipIdDTOFx> selectedMembershipIdProperty() {
        return selectedMembershipId;
    }

    public void setSelectedMembershipId(MembershipIdDTOFx selectedMembershipId) {
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

    public BoatDTOFx getSelectedBoat() {
        return selectedBoat.get();
    }

    public SimpleObjectProperty<BoatDTOFx> selectedBoatProperty() {
        return selectedBoat;
    }

    public void setSelectedBoat(BoatDTOFx selectedBoat) {
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

    public NotesDTOFx getSelectedNote() {
        return selectedNote.get();
    }

    public SimpleObjectProperty<NotesDTOFx> selectedNoteProperty() {
        return selectedNote;
    }

    public void setSelectedNote(NotesDTOFx selectedNote) {
        this.selectedNote.set(selectedNote);
    }

    public TableView<MembershipIdDTOFx> getIdTableView() {
        return idTableView.get();
    }

    public SimpleObjectProperty<TableView<MembershipIdDTOFx>> idTableViewProperty() {
        return idTableView;
    }

    public void setIdTableView(TableView<MembershipIdDTOFx> idTableView) {
        this.idTableView.set(idTableView);
    }

    public TableView<NotesDTOFx> getNotesTableView() {
        return notesTableView.get();
    }

    public SimpleObjectProperty<TableView<NotesDTOFx>> notesTableViewProperty() {
        return notesTableView;
    }

    public void setNotesTableView(TableView<NotesDTOFx> notesTableView) {
        this.notesTableView.set(notesTableView);
    }

    public TableView<BoatDTOFx> getBoatTableView() {
        return boatTableView.get();
    }

    public SimpleObjectProperty<TableView<BoatDTOFx>> boatTableViewProperty() {
        return boatTableView;
    }

    public void setBoatTableView(TableView<BoatDTOFx> boatTableView) {
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

    public ObservableMap<PersonDTOFx, TableView<OfficerDTO>> getOfficerTableView() {
        return officerTableView;
    }

    public void setOfficerTableView(ObservableMap<PersonDTOFx, TableView<OfficerDTO>> officerTableView) {
        this.officerTableView = officerTableView;
    }

    public ObservableMap<PersonDTOFx, TableView<AwardDTO>> getAwardTableView() {
        return awardTableView;
    }

    public void setAwardTableView(ObservableMap<PersonDTOFx, TableView<AwardDTO>> awardTableView) {
        this.awardTableView = awardTableView;
    }

    public ObservableMap<PersonDTOFx, Tab> getSelectedPropertiesTab() {
        return selectedPropertiesTab;
    }

    public void setSelectedPropertiesTab(ObservableMap<PersonDTOFx, Tab> selectedPropertiesTab) {
        this.selectedPropertiesTab = selectedPropertiesTab;
    }

    public ObservableMap<PersonDTOFx, TableView<PhoneDTO>> getPhoneTableView() {
        return phoneTableView;
    }

    public void setPhoneTableView(ObservableMap<PersonDTOFx, TableView<PhoneDTO>> phoneTableView) {
        this.phoneTableView = phoneTableView;
    }

    public ObservableMap<PersonDTOFx, TableView<EmailDTO>> getEmailTableView() {
        return emailTableView;
    }

    public void setEmailTableView(ObservableMap<PersonDTOFx, TableView<EmailDTO>> emailTableView) {
        this.emailTableView = emailTableView;
    }

    public ObservableMap<PersonDTOFx, ComboBox<String>> getPersonComboBox() {
        return personComboBox;
    }

    public void setPersonComboBox(ObservableMap<PersonDTOFx, ComboBox<String>> personComboBox) {
        this.personComboBox = personComboBox;
    }

    public ObservableMap<PersonDTOFx, TextField> getPersonTextField() {
        return personTextField;
    }

    public void setPersonTextField(ObservableMap<PersonDTOFx, TextField> personTextField) {
        this.personTextField = personTextField;
    }
    public PersonDTOFx getSelectedPerson() {
        return selectedPerson.get();
    }
    public SimpleObjectProperty<PersonDTOFx> selectedPersonProperty() {
        return selectedPerson;
    }
    public void setSelectedPerson(PersonDTOFx selectedPerson) {
        this.selectedPerson.set(selectedPerson);
    }
    public void setSelectedRadioForPerson(ObservableMap<PersonDTOFx, RadioButton> selectedRadioForPerson) {
        this.selectedRadioForPerson = selectedRadioForPerson;
    }
    public ObservableMap<PersonDTOFx, RadioButton> getSelectedRadioForPerson() {
        return selectedRadioForPerson;
    }
    public ObservableMap<PersonDTOFx, StackPane> getStackPaneMap() {
        return stackPaneMap;
    }
    public ObservableList<PersonDTOFx> getPeople() {
        return people;
    }
    public void setPeople(ObservableList<PersonDTOFx> people) {
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
    public RosterDTOFx getMembershipFromRosterList() {
        return membershipFromRosterList.get();
    }

    public SimpleObjectProperty<RosterDTOFx> membershipFromRosterListProperty() {
        return membershipFromRosterList;
    }

    public SimpleIntegerProperty selectedMembershipYearProperty() {
        return selectedMembershipYear;
    }

    public HttpClientUtil getHttpClient() {
        return httpClient;
    }

    public ObservableList<BoardPositionDTO> getBoardPositionDTOS() {
        return boardPositionDTOS;
    }
}
