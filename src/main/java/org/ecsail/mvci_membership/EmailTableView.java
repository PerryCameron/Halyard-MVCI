package org.ecsail.mvci_membership;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Builder;
import org.ecsail.custom.RadioButtonCell;
import org.ecsail.dto.EmailDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.Messages;
import org.ecsail.static_calls.StringTools;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;
import java.util.List;

public class EmailTableView implements Builder<TableView<EmailDTO>> {

    private final PersonDTO person;
    private final MembershipView membershipView;

    public EmailTableView(PersonDTO personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipView = membershipView;
    }

    @Override
    public TableView<EmailDTO> build() {
        TableView<EmailDTO> tableView = TableViewFx.tableViewOf(EmailDTO.class);
        tableView.setItems(person.getEmail());
        List<TableColumn<EmailDTO, ?>> columns = Arrays.asList(createColumn1(), createColumn2(), createColumn3());
        tableView.getColumns().setAll(columns);
        return tableView;
    }

    private TableColumn<EmailDTO,String> createColumn1() { //
        TableColumn<EmailDTO, String> col1 = TableColumnFx.tableColumnOf(EmailDTO::emailProperty,"Email");
        col1.setPrefWidth(137);
        col1.setOnEditCommit(t -> {
            int email_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getEmail_id();
            if(StringTools.isValidEmail(t.getNewValue())) {
                EmailDTO emailDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                emailDTO.setEmail(t.getNewValue());
                int returnedRows = membershipView.sendMessage().apply(Messages.MessageType.UPDATE,emailDTO);
//                membershipView.checkTheCorrectNumberOfReturnedRows(returnedRows);
            } else {
                person.getEmail().stream()
                        .filter(q -> q.getEmail_id() == email_id)
                        .forEach(s -> s.setEmail("Bad Email"));
            }
        });
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 50);   // Phone
        return col1;
    }

    private TableColumn<EmailDTO,Boolean> createColumn2() {
        TableColumn<EmailDTO, Boolean> col2 = new TableColumn<>("Primary");
        col2.setStyle( "-fx-alignment: CENTER;");
        col2.setCellValueFactory(new PropertyValueFactory<>("isPrimaryUse"));
        ObjectProperty<EmailDTO> previousEmailDTO = new SimpleObjectProperty<>();
        previousEmailDTO.set(person.getEmail().stream().filter(EmailDTO::getIsPrimaryUse).findFirst().orElse(null));
        // TODO make this fucking thing work
        col2.setCellFactory(c -> new RadioButtonCell(previousEmailDTO));
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 25);  // Type
        return col2;
    }

    private TableColumn<EmailDTO,Boolean> createColumn3() {
        TableColumn<EmailDTO, Boolean> col3 = new TableColumn<>("Listed");
        col3.setCellValueFactory(param -> {
            EmailDTO emailDTO = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(emailDTO.getIsListed());
            // Note: singleCol.setOnEditCommit(): Not work for
            // CheckBoxTableCell.
            // When "Listed?" column change.
            booleanProp.addListener((observable, oldValue, newValue) -> {
                emailDTO.setListed(newValue);
                int returnedRows = membershipView.sendMessage().apply(Messages.MessageType.UPDATE,emailDTO);
//                membershipView.checkTheCorrectNumberOfReturnedRows(returnedRows);
            });
            return booleanProp;
        });

        col3.setCellFactory(p12 -> {
            CheckBoxTableCell<EmailDTO, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 25);  // Listed
        return col3;
    }
}
