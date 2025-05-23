package org.ecsail.mvci.membership;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.ecsail.fx.*;
import org.ecsail.enums.Success;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.mvci.main.MainModel;
import org.ecsail.static_tools.HttpClientUtil;

import java.util.Objects;

public class MembershipModel {
    private final MainModel mainModel;
    private ObservableList<BoardPositionDTO> boardPositionDTOS;
    // Person information for membership
    private ObservableMap<PersonFx, TableView<EmailDTOFx>> emailTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonFx, TableView<PhoneFx>> phoneTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonFx, TableView<AwardDTOFx>> awardTableView = FXCollections.observableHashMap();
    private ObservableMap<PersonFx, TableView<OfficerFx>> officerTableView = FXCollections.observableHashMap();
    private final ObservableMap<PersonFx, StackPane> stackPaneMap = FXCollections.observableHashMap();
    private ObservableMap<PersonFx, RadioButton> selectedRadioForPerson = FXCollections.observableHashMap();
    private ObservableMap<PersonFx, ComboBox<String>> personComboBox = FXCollections.observableHashMap();
    private ObservableMap<PersonFx, Tab> selectedPropertiesTab = FXCollections.observableHashMap();
    private ObservableMap<PersonFx, TextField> personTextField = FXCollections.observableHashMap();
    private ObservableList<PersonFx> people = FXCollections.observableArrayList();

    private final SimpleObjectProperty<RosterFx> membershipFromRosterList = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipFx> membership = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<BoatDTOFx>> boatTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<NotesFx>> notesTableView = new SimpleObjectProperty<>();
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
    private final SimpleObjectProperty<NotesFx> selectedNote = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<AwardDTOFx> selectedAward = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<BoatDTOFx> selectedBoat = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<EmailDTOFx> selectedEmail = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipIdDTOFx> selectedMembershipId = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<OfficerFx> selectedOfficer = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PersonFx> selectedPerson = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PhoneFx> selectedPhone = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<InvoiceDTOFx> selectedInvoice = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PaymentDTO> selectedPayment = new SimpleObjectProperty<>();
    private final StringProperty selectedString = new SimpleStringProperty("");
    private final SimpleIntegerProperty selectedInvoiceCreateYear = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty selectedMembershipYear = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Success> invoiceSaved = new SimpleObjectProperty<>(Success.NULL);
    private final SimpleBooleanProperty envelopeIsCatalogue = new SimpleBooleanProperty(false);
    private int[] success = new int[16];


    public MembershipModel(RosterFx rosterDTOFx, int selectedYear, MainModel mainModel) {
        membershipFromRosterList.set(rosterDTOFx);
        this.httpClient = mainModel.getHttpClient();
        this.boardPositionDTOS = mainModel.getBoardPositionDTOS();
        this.selectedMembershipYear.set(selectedYear);
        this.mainModel = mainModel;
    }

    public BoatDTOFx getBoatById(int id) {
        return membership.get().getBoats().stream()
                .filter(boatDTOFx -> Objects.equals(boatDTOFx.getBoatId(), id))
                .findFirst()
                .orElse(null);
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

    public PhoneFx getSelectedPhone() {
        return selectedPhone.get();
    }

    public SimpleObjectProperty<PhoneFx> selectedPhoneProperty() {
        return selectedPhone;
    }

    public void setSelectedPhone(PhoneFx selectedPhone) {
        this.selectedPhone.set(selectedPhone);
    }

    public OfficerFx getSelectedOfficer() {
        return selectedOfficer.get();
    }

    public SimpleObjectProperty<OfficerFx> selectedOfficerProperty() {
        return selectedOfficer;
    }

    public void setSelectedOfficer(OfficerFx selectedOfficer) {
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

    public NotesFx getSelectedNote() {
        return selectedNote.get();
    }

    public SimpleObjectProperty<NotesFx> selectedNoteProperty() {
        return selectedNote;
    }

    public void setSelectedNote(NotesFx selectedNote) {
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

    public TableView<NotesFx> getNotesTableView() {
        return notesTableView.get();
    }

    public SimpleObjectProperty<TableView<NotesFx>> notesTableViewProperty() {
        return notesTableView;
    }

    public void setNotesTableView(TableView<NotesFx> notesTableView) {
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

    public ObservableMap<PersonFx, TableView<OfficerFx>> getOfficerTableView() {
        return officerTableView;
    }

    public void setOfficerTableView(ObservableMap<PersonFx, TableView<OfficerFx>> officerTableView) {
        this.officerTableView = officerTableView;
    }

    public ObservableMap<PersonFx, TableView<AwardDTOFx>> getAwardTableView() {
        return awardTableView;
    }

    public void setAwardTableView(ObservableMap<PersonFx, TableView<AwardDTOFx>> awardTableView) {
        this.awardTableView = awardTableView;
    }

    public ObservableMap<PersonFx, Tab> getSelectedPropertiesTab() {
        return selectedPropertiesTab;
    }

    public void setSelectedPropertiesTab(ObservableMap<PersonFx, Tab> selectedPropertiesTab) {
        this.selectedPropertiesTab = selectedPropertiesTab;
    }

    public ObservableMap<PersonFx, TableView<PhoneFx>> getPhoneTableView() {
        return phoneTableView;
    }

    public void setPhoneTableView(ObservableMap<PersonFx, TableView<PhoneFx>> phoneTableView) {
        this.phoneTableView = phoneTableView;
    }

    public ObservableMap<PersonFx, TableView<EmailDTOFx>> getEmailTableView() {
        return emailTableView;
    }

    public void setEmailTableView(ObservableMap<PersonFx, TableView<EmailDTOFx>> emailTableView) {
        this.emailTableView = emailTableView;
    }

    public ObservableMap<PersonFx, ComboBox<String>> getPersonComboBox() {
        return personComboBox;
    }

    public void setPersonComboBox(ObservableMap<PersonFx, ComboBox<String>> personComboBox) {
        this.personComboBox = personComboBox;
    }

    public ObservableMap<PersonFx, TextField> getPersonTextField() {
        return personTextField;
    }

    public void setPersonTextField(ObservableMap<PersonFx, TextField> personTextField) {
        this.personTextField = personTextField;
    }
    public PersonFx getSelectedPerson() {
        return selectedPerson.get();
    }
    public SimpleObjectProperty<PersonFx> selectedPersonProperty() {
        return selectedPerson;
    }
    public void setSelectedPerson(PersonFx selectedPerson) {
        this.selectedPerson.set(selectedPerson);
    }
    public void setSelectedRadioForPerson(ObservableMap<PersonFx, RadioButton> selectedRadioForPerson) {
        this.selectedRadioForPerson = selectedRadioForPerson;
    }
    public ObservableMap<PersonFx, RadioButton> getSelectedRadioForPerson() {
        return selectedRadioForPerson;
    }
    public ObservableMap<PersonFx, StackPane> getStackPaneMap() {
        return stackPaneMap;
    }
    public ObservableList<PersonFx> getPeople() {
        return people;
    }
    public void setPeople(ObservableList<PersonFx> people) {
        this.people = people;
    }


    public SimpleObjectProperty<MembershipFx> membershipProperty() {
        return membership;
    }

    public RosterFx getMembershipFromRosterList() {
        return membershipFromRosterList.get();
    }

    public SimpleObjectProperty<RosterFx> membershipFromRosterListProperty() {
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

    public MainModel getMainModel() {
        return mainModel;
    }


}
