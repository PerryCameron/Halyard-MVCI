package org.ecsail.mvci.membership;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import org.ecsail.fx.*;
import org.ecsail.enums.Success;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.mvci.main.MainModel;
import org.ecsail.static_tools.HttpClientUtil;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class MembershipModel {
    private final MainModel mainModel;
    private final ObservableList<BoardPositionDTO> boardPositionDTOS;

    // TODO i don't think I need this, it is located in the membership object
    private ObservableList<PersonFx> people = FXCollections.observableArrayList();
    private final SimpleObjectProperty<RosterFx> membershipFromRosterList = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipFx> membership = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<BoatFx>> boatTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<NoteFx>> notesTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<MembershipIdFx>> idTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableView<InvoiceFx>> invoiceListTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> peopleTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> infoTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TabPane> extraTabPane = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SlipUser.slip> slipRelationStatus = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty dataIsLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty tableInEditMode = new SimpleBooleanProperty(false);
    private final StringProperty sublease = new SimpleStringProperty("");
    private final StringProperty membershipId = new SimpleStringProperty("");
    private final HttpClientUtil httpClient;
    private ObservableMap<String,Control> slipControls = FXCollections.observableHashMap();
    private final SimpleObjectProperty<NoteFx> selectedNote = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<BoatFx> selectedBoat = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipIdFx> selectedMembershipId = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PersonFx> selectedPerson = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<InvoiceFx> selectedInvoice = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PaymentDTO> selectedPayment = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TextArea> textArea = new SimpleObjectProperty<>(new TextArea());
    private final StringProperty selectedString = new SimpleStringProperty("");
    private final StringProperty errorMessage = new SimpleStringProperty();
    private final SimpleIntegerProperty selectedInvoiceCreateYear = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty selectedMembershipYear = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Success> invoiceSaved = new SimpleObjectProperty<>(Success.NULL);
    private final BooleanProperty envelopeIsCatalogue = new SimpleBooleanProperty(false);
    private final BooleanProperty personAdded = new SimpleBooleanProperty(false);
    private int[] success = new int[16];
    private ExecutorService executorService = null;


    public MembershipModel(RosterFx rosterDTOFx, int selectedYear, MainModel mainModel) {
        membershipFromRosterList.set(rosterDTOFx);
        this.httpClient = mainModel.getHttpClient();
        this.boardPositionDTOS = mainModel.getBoardPositionDTOS();
        this.selectedMembershipYear.set(selectedYear);
        this.mainModel = mainModel;
    }
    public BoatFx getBoatById(int id) {
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
    public BooleanProperty envelopeIsCatalogueProperty() {
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
    public SimpleObjectProperty<TableView<InvoiceFx>> invoiceListTableViewProperty() {
        return invoiceListTableView;
    }
    public void setInvoiceListTableView(TableView<InvoiceFx> invoiceListTableView) {
        this.invoiceListTableView.set(invoiceListTableView);
    }
    public InvoiceFx getSelectedInvoice() {
        return selectedInvoice.get();
    }
    public SimpleObjectProperty<InvoiceFx> selectedInvoiceProperty() {
        return selectedInvoice;
    }
    public void setSelectedInvoice(InvoiceFx selectedInvoice) {
        this.selectedInvoice.set(selectedInvoice);
    }
    public StringProperty selectedStringProperty() {
        return selectedString;
    }
    public void setSelectedString(String selectedString) {
        this.selectedString.set(selectedString);
    }
    public MembershipIdFx getSelectedMembershipId() {
        return selectedMembershipId.get();
    }
    public SimpleObjectProperty<MembershipIdFx> selectedMembershipIdProperty() {
        return selectedMembershipId;
    }
    public void setSelectedMembershipId(MembershipIdFx selectedMembershipId) {
        this.selectedMembershipId.set(selectedMembershipId);
    }
    public BoatFx getSelectedBoat() {
        return selectedBoat.get();
    }
    public SimpleObjectProperty<BoatFx> selectedBoatProperty() {
        return selectedBoat;
    }
    public void setSelectedBoat(BoatFx selectedBoat) {
        this.selectedBoat.set(selectedBoat);
    }
    public NoteFx getSelectedNote() {
        return selectedNote.get();
    }
    public SimpleObjectProperty<NoteFx> selectedNoteProperty() {
        return selectedNote;
    }
    public void setSelectedNote(NoteFx selectedNote) {
        this.selectedNote.set(selectedNote);
    }
    public TableView<MembershipIdFx> getIdTableView() {
        return idTableView.get();
    }
    public SimpleObjectProperty<TableView<MembershipIdFx>> idTableViewProperty() {
        return idTableView;
    }
    public void setIdTableView(TableView<MembershipIdFx> idTableView) {
        this.idTableView.set(idTableView);
    }
    public TableView<NoteFx> getNotesTableView() {
        return notesTableView.get();
    }
    public SimpleObjectProperty<TableView<NoteFx>> notesTableViewProperty() {
        return notesTableView;
    }
    public void setNotesTableView(TableView<NoteFx> notesTableView) {
        this.notesTableView.set(notesTableView);
    }
    public TableView<BoatFx> getBoatTableView() {
        return boatTableView.get();
    }
    public SimpleObjectProperty<TableView<BoatFx>> boatTableViewProperty() {
        return boatTableView;
    }
    public void setBoatTableView(TableView<BoatFx> boatTableView) {
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
    public PersonFx getSelectedPerson() {
        return selectedPerson.get();
    }
    public SimpleObjectProperty<PersonFx> selectedPersonProperty() {
        return selectedPerson;
    }
    public void setSelectedPerson(PersonFx selectedPerson) {
        this.selectedPerson.set(selectedPerson);
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
    public TextArea getTextArea() {
        return textArea.get();
    }
    public SimpleObjectProperty<TextArea> textAreaProperty() {
        return textArea;
    }
    public ExecutorService getExecutorService() {
        return executorService;
    }
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
    public StringProperty errorMessageProperty() {
        return errorMessage;
    }
    public boolean isTableInEditMode() {
        return tableInEditMode.get();
    }
    public BooleanProperty tableInEditModeProperty() {
        return tableInEditMode;
    }
    public void setTableInEditMode(boolean inEditMode) {
        this.tableInEditMode.set(inEditMode);
    }
    public BooleanProperty personAddedProperty() {
        return personAdded;
    }
    public void togglePersonAdded() {
        personAddedProperty().set(true);
        personAddedProperty().set(false);
    }
}
