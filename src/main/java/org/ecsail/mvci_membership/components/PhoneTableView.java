package org.ecsail.mvci_membership.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Builder;
import org.ecsail.fx.PersonFx;
import org.ecsail.fx.PhoneFx;
import org.ecsail.enums.PhoneType;
import org.ecsail.mvci_membership.MembershipMessage;
import org.ecsail.mvci_membership.MembershipModel;
import org.ecsail.mvci_membership.MembershipView;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PhoneTableView implements Builder<TableView<PhoneFx>> {

    private final MembershipView membershipView;
    private final PersonFx person;
    private final MembershipModel membershipModel;

    public PhoneTableView(PersonFx personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public TableView<PhoneFx> build() {
        TableView<PhoneFx> tableView = TableViewFx.tableViewOf(PhoneFx.class, 146);
        tableView.setItems(person.getPhones());
        List<TableColumn<PhoneFx, ?>> columns = new ArrayList<>();
        columns.add(createColumn1());
        columns.add(createColumn2());
        columns.add(createColumn3());
        tableView.getColumns().addAll(columns);
        TableView.TableViewSelectionModel<PhoneFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipModel.setSelectedPhone(newSelection);
        });
        return tableView;
    }

    private TableColumn<PhoneFx,String> createColumn1() {
        TableColumn<PhoneFx, String> col1 = TableColumnFx.editableStringTableColumn(PhoneFx::phoneProperty,"Phone");
        col1.setOnEditCommit(
                new EventHandler<>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<PhoneFx, String> t) {
                        String processedNumber = processNumber(t.getNewValue());
                        PhoneFx phoneDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        phoneDTO.setPhone(t.getNewValue());
                        membershipModel.setSelectedPhone(phoneDTO);
                        membershipView.sendMessage().accept(MembershipMessage.UPDATE_PHONE);
                        person.getPhones().stream()
                                .filter(p -> p.getPhoneId() == phoneDTO.getPhoneId())
                                .forEach(s -> s.setPhone(processedNumber));
                    }

                    private String processNumber(String newValue) {
                        // adds dashes
                        if (Pattern.matches("\\d{10}", newValue)) {
                            return addDashes(newValue);
                        }
                        // need to add area code
                        else if (Pattern.matches("\\d{7}", newValue)) {
                            return addDashes("317" + newValue);
                        }
                        // perfect no need to change anything
                        else if (Pattern.matches("(?:\\d{3}-){2}\\d{4}", newValue)) {
                            return newValue;
                        }
                        // removes all junk as long as there are 10 numbers
                        else if (keepOnlyNumbers(newValue).length() == 10) {
                            return addDashes(keepOnlyNumbers(newValue));
                        }
                        // removes all junk and adds default area code if there are 7 numbers
                        else if (keepOnlyNumbers(newValue).length() == 7) {
                            return addDashes("317" + keepOnlyNumbers(newValue));
                        } else {
                            return "ill-formatted number";
                        }
                    }

                    private String addDashes(String newValue) {
                        StringBuilder resString = new StringBuilder(newValue);
                        return resString.insert(3, "-").insert(7, "-").toString();
                    }

                    private String keepOnlyNumbers(String newValue) {
                        return newValue.replaceAll("[^0-9]", "");
                    }
                }
        );
        col1.setMaxWidth( 1f * Integer.MAX_VALUE * 50);   // Phone
        return col1;
    }

    private TableColumn<PhoneFx,PhoneType> createColumn2() {
        // example for this column found at https://o7planning.org/en/11079/javafx-tableview-tutorial
        ObservableList<PhoneType> phoneTypeList = FXCollections.observableArrayList(PhoneType.values());
        TableColumn<PhoneFx, PhoneType> Col2 = new TableColumn<>("Type");
        Col2.setCellValueFactory(param -> {
            PhoneFx thisPhone = param.getValue();
            String phoneCode = thisPhone.getPhoneType();
            PhoneType phoneType = PhoneType.getByCode(phoneCode);
            return new SimpleObjectProperty<>(phoneType);
        });

        Col2.setCellFactory(ComboBoxTableCell.forTableColumn(phoneTypeList));

        Col2.setOnEditCommit((TableColumn.CellEditEvent<PhoneFx, PhoneType> event) -> {
            TablePosition<PhoneFx, PhoneType> pos = event.getTablePosition();
            PhoneType newPhoneType = event.getNewValue();
            int row = pos.getRow();
            PhoneFx phoneDTO = event.getTableView().getItems().get(row);
            phoneDTO.setPhoneType(newPhoneType.getCode()); // makes UI feel snappy
            membershipModel.setSelectedPhone(phoneDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_PHONE);
        });
        Col2.setMaxWidth( 1f * Integer.MAX_VALUE * 30 );  // Type
        return Col2;
    }

    private TableColumn<PhoneFx,?> createColumn3() {
        TableColumn<PhoneFx, Boolean> Col3 = new TableColumn<>("Listed");
        Col3.setCellValueFactory(param -> {
            PhoneFx phoneDTO = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(phoneDTO.getPhoneListed());
            booleanProp.addListener((observable, oldValue, newValue) -> {
                phoneDTO.setPhoneListed(newValue); // makes UI feel snappy
                membershipModel.setSelectedPhone(phoneDTO);
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_PHONE);
            });
            return booleanProp;
        });

        //
        Col3.setCellFactory(p1 -> {
            CheckBoxTableCell<PhoneFx, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        Col3.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );  // Listed
        return Col3;
    }
}
