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
    private final MainModel mainMOdel;
    private ObservableList<BoardPositionDTO> boardPositionDTOS;
    // Person information for membership
    private ObservableMap<PersonDTOFx, TableView<EmailDTOFx>> emailTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TableView<PhoneDTOFx>> phoneTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TableView<AwardDTOFx>> awardTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TableView<OfficerDTOFx>> officerTableView = FXCollections.observableHashMap();
    private final ObservableMap<PersonDTOFx, StackPane> stackPaneMap = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, RadioButton> selectedRadioForPerson = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, ComboBox<String>> personComboBox = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, Tab> selectedPropertiesTab = FXCollections.observableHashMap();
    private ObservableMap<PersonDTOFx, TextField> personTextField = FXCollections.observableHashMap();
    private ObservableList<PersonDTOFx> people = FXCollections.observableArrayList();

    private final SimpleObjectProperty<RosterDTOFx> membershipFromRosterList = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipDTOFx> membership = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<BoatDTOFx>> boatTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<NotesDTOFx>> notesTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<MembershipIdDTOFx>> idTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<InvoiceDTOFx>> invoiceListTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> peopleTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> infoTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> extraTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SlipUser.slip> slipRelationStatus = new SimpleObjectProperty<>();
//    private final ObjectProperty<MembershipMessage> returnMessage = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty dataIsLoaded = new SimpleBooleanProperty(false);
    private final StringProperty sublease = new SimpleStringProperty("");
    private final StringProperty membershipId = new SimpleStringProperty("");
    private final HttpClientUtil httpClient;
    private ObservableMap<String,Control> slipControls = FXCollections.observableHashMap();
    private final SimpleObjectProperty<NotesDTOFx> selectedNote = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<AwardDTOFx> selectedAward = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<BoatDTOFx> selectedBoat = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<EmailDTOFx> selectedEmail = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipIdDTOFx> selectedMembershipId = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<OfficerDTOFx> selectedOfficer = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PersonDTOFx> selectedPerson = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PhoneDTOFx> selectedPhone = new SimpleObjectProperty<>();
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
        this.mainMOdel = mainModel;
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

    public SimpleObjectProperty<Success> invoiceSavedProperty() {
        return invoiceSaved;
    }

    public void setInvoiceSaved(Success invoiceSaved) {
        this.invoiceSaved.set(invoiceSaved);
    }

    public SimpleIntegerProperty selectedInvoiceCreateYearProperty() {
        return selectedInvoiceCreateYear;
    }

    public void setSelectedInvoiceCreateYear(int selectedInvoiceCreateYear) {
        this.selectedInvoiceCreateYear.set(selectedInvoiceCreateYear);
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

    public StringProperty selectedStringProperty() {
        return selectedString;
    }

    public void setSelectedString(String selectedString) {
        this.selectedString.set(selectedString);
    }

    public PhoneDTOFx getSelectedPhone() {
        return selectedPhone.get();
    }

    public SimpleObjectProperty<PhoneDTOFx> selectedPhoneProperty() {
        return selectedPhone;
    }

    public void setSelectedPhone(PhoneDTOFx selectedPhone) {
        this.selectedPhone.set(selectedPhone);
    }

    public OfficerDTOFx getSelectedOfficer() {
        return selectedOfficer.get();
    }

    public SimpleObjectProperty<OfficerDTOFx> selectedOfficerProperty() {
        return selectedOfficer;
    }

    public void setSelectedOfficer(OfficerDTOFx selectedOfficer) {
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

    public EmailDTOFx getSelectedEmail() {
        return selectedEmail.get();
    }

    public SimpleObjectProperty<EmailDTOFx> selectedEmailProperty() {
        return selectedEmail;
    }

    public void setSelectedEmail(EmailDTOFx selectedEmail) {
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

    public AwardDTOFx getSelectedAward() {
        return selectedAward.get();
    }

    public SimpleObjectProperty<AwardDTOFx> selectedAwardProperty() {
        return selectedAward;
    }

    public void setSelectedAward(AwardDTOFx selectedAward) {
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

    public ObservableMap<PersonDTOFx, TableView<OfficerDTOFx>> getOfficerTableView() {
        return officerTableView;
    }

    public void setOfficerTableView(ObservableMap<PersonDTOFx, TableView<OfficerDTOFx>> officerTableView) {
        this.officerTableView = officerTableView;
    }

    public ObservableMap<PersonDTOFx, TableView<AwardDTOFx>> getAwardTableView() {
        return awardTableView;
    }

    public void setAwardTableView(ObservableMap<PersonDTOFx, TableView<AwardDTOFx>> awardTableView) {
        this.awardTableView = awardTableView;
    }

    public ObservableMap<PersonDTOFx, Tab> getSelectedPropertiesTab() {
        return selectedPropertiesTab;
    }

    public void setSelectedPropertiesTab(ObservableMap<PersonDTOFx, Tab> selectedPropertiesTab) {
        this.selectedPropertiesTab = selectedPropertiesTab;
    }

    public ObservableMap<PersonDTOFx, TableView<PhoneDTOFx>> getPhoneTableView() {
        return phoneTableView;
    }

    public void setPhoneTableView(ObservableMap<PersonDTOFx, TableView<PhoneDTOFx>> phoneTableView) {
        this.phoneTableView = phoneTableView;
    }

    public ObservableMap<PersonDTOFx, TableView<EmailDTOFx>> getEmailTableView() {
        return emailTableView;
    }

    public void setEmailTableView(ObservableMap<PersonDTOFx, TableView<EmailDTOFx>> emailTableView) {
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


    public SimpleObjectProperty<MembershipDTOFx> membershipProperty() {
        return membership;
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

    public SimpleBooleanProperty dataIsLoadedProperty() {
        return dataIsLoaded;
    }

    public MainModel getMainMOdel() {
        return mainMOdel;
    }
}
