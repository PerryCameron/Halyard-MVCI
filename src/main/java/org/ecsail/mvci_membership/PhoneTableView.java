package org.ecsail.mvci_membership;

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
import org.ecsail.dto.PersonDTO;
import org.ecsail.dto.PhoneDTO;
import org.ecsail.enums.PhoneType;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.regex.Pattern;

public class PhoneTableView implements Builder<TableView> {

    private final MembershipModel membershipModel;
    private final MembershipView membershipView;
    private final PersonDTO person;

    public PhoneTableView(PersonDTO personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
    }

    @Override
    public TableView build() {
        TableView<PhoneDTO> tableView = TableViewFx.tableViewOf(PhoneDTO.class);
        tableView.setItems(person.getPhones());
        tableView.getColumns().addAll(createColumn1(), createColumn2(), createColumn3());
        return tableView;
    }

    private TableColumn<PhoneDTO,String> createColumn1() {
        TableColumn<PhoneDTO, String> Col1 = TableColumnFx.tableColumnOf(PhoneDTO::phoneNumberProperty,"Phone");
        Col1.setOnEditCommit(
                new EventHandler<>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<PhoneDTO, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setPhoneNumber(t.getNewValue());
                        String processedNumber = processNumber(t.getNewValue());
                        int phone_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getPhone_ID();
                        // TODO send Consumer<Object>
//                        SqlUpdate.updatePhone("phone", phone_id, processedNumber);
                        person.getPhones().stream()
                                .filter(p -> p.getPhone_ID() == phone_id)
                                .forEach(s -> s.setPhoneNumber(processedNumber));
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
        Col1.setMaxWidth( 1f * Integer.MAX_VALUE * 50);   // Phone
        return Col1;
    }

    private TableColumn<PhoneDTO,PhoneType> createColumn2() {
        // example for this column found at https://o7planning.org/en/11079/javafx-tableview-tutorial
        ObservableList<PhoneType> phoneTypeList = FXCollections.observableArrayList(PhoneType.values());
        TableColumn<PhoneDTO, PhoneType> Col2 = new TableColumn<>("Type");
        Col2.setCellValueFactory(param -> {
            PhoneDTO thisPhone = param.getValue();
            String phoneCode = thisPhone.getPhoneType();
            PhoneType phoneType = PhoneType.getByCode(phoneCode);
            return new SimpleObjectProperty<>(phoneType);
        });

        Col2.setCellFactory(ComboBoxTableCell.forTableColumn(phoneTypeList));

        Col2.setOnEditCommit((TableColumn.CellEditEvent<PhoneDTO, PhoneType> event) -> {
            TablePosition<PhoneDTO, PhoneType> pos = event.getTablePosition();
            PhoneType newPhoneType = event.getNewValue();
            int row = pos.getRow();
            PhoneDTO thisPhone = event.getTableView().getItems().get(row);
            // TODO add Consumer<Object> here
//            SqlUpdate.updatePhone("phone_type", thisPhone.getPhone_ID(), newPhoneType.getCode());
            thisPhone.setPhoneType(newPhoneType.getCode());
        });
        Col2.setMaxWidth( 1f * Integer.MAX_VALUE * 30 );  // Type
        return Col2;
    }

    private TableColumn<PhoneDTO,?> createColumn3() {
        TableColumn<PhoneDTO, Boolean> Col3 = new TableColumn<>("Listed");
        Col3.setCellValueFactory(param -> {
            PhoneDTO phone = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(phone.isIsListed());
            // Note: singleCol.setOnEditCommit(): Not work for
            // CheckBoxTableCell.
            // When "isListed?" column change.
            booleanProp.addListener((observable, oldValue, newValue) -> {
                phone.setIsListed(newValue);
                // TODO Consumer<Object> here
//                SqlUpdate.updateListed("phone_listed",phone.getPhone_ID(), newValue);
            });
            return booleanProp;
        });

        //
        Col3.setCellFactory(p1 -> {
            CheckBoxTableCell<PhoneDTO, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        Col3.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );  // Listed
        return Col3;
    }
}