package org.ecsail.mvci.membership.components;

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
import org.ecsail.fx.EmailDTOFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.MembershipModel;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.static_tools.StringTools;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;
import java.util.List;

public class EmailTableView implements Builder<TableView<EmailDTOFx>> {

    private final PersonFx person;
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;

    public EmailTableView(PersonFx personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public TableView<EmailDTOFx> build() {
        TableView<EmailDTOFx> tableView = TableViewFx.tableViewOf(EmailDTOFx.class, 146);
        tableView.setItems(person.getEmail());
        List<TableColumn<EmailDTOFx, ?>> columns = Arrays.asList(createColumn1(), createColumn2(), createColumn3());
        tableView.getColumns().setAll(columns);
        TableView.TableViewSelectionModel<EmailDTOFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipModel.setSelectedEmail(newSelection);
        });
        return tableView;
    }

    private TableColumn<EmailDTOFx,String> createColumn1() { //
        TableColumn<EmailDTOFx, String> col1 = TableColumnFx.editableStringTableColumn(EmailDTOFx::emailProperty,"Email");
        col1.setOnEditCommit(t -> {
            int email_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getEmailId();
            if(StringTools.isValidEmail(t.getNewValue())) {
                EmailDTOFx emailDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                emailDTO.setEmail(t.getNewValue());
                membershipModel.setSelectedEmail(emailDTO);
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_EMAIL);
            } else {
                person.getEmail().stream()
                        .filter(q -> q.getEmailId() == email_id)
                        .forEach(s -> s.setEmail("Bad Email"));
            }
        });
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 50);   // Phone
        return col1;
    }

    private TableColumn<EmailDTOFx,Boolean> createColumn2() {
        TableColumn<EmailDTOFx, Boolean> col2 = new TableColumn<>("Primary");
        col2.setStyle( "-fx-alignment: CENTER;");
        col2.setCellValueFactory(new PropertyValueFactory<>("isPrimaryUse"));
        ObjectProperty<EmailDTOFx> previousEmailDTO = new SimpleObjectProperty<>();
        previousEmailDTO.set(person.getEmail().stream().filter(EmailDTOFx::getIsPrimaryUse).findFirst().orElse(null));
        // TODO make this fucking thing work
        membershipModel.setSelectedEmail(previousEmailDTO.get()); // this may be wrong
        membershipView.sendMessage().accept(MembershipMessage.EMAIL_IS_PRIMARY_USE);
        col2.setCellFactory(c -> new RadioButtonCell(previousEmailDTO));
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 25);  // Type
        return col2;
    }

    private TableColumn<EmailDTOFx,Boolean> createColumn3() {
        TableColumn<EmailDTOFx, Boolean> col3 = new TableColumn<>("Listed");
        col3.setCellValueFactory(param -> {
            EmailDTOFx emailDTO = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(emailDTO.getIsListed());
            booleanProp.addListener((observable, oldValue, newValue) -> {
                emailDTO.setListed(newValue);
                membershipModel.setSelectedEmail(emailDTO);
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_EMAIL);
            });
            return booleanProp;
        });
        col3.setCellFactory(p12 -> {
            CheckBoxTableCell<EmailDTOFx, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 25);  // Listed
        return col3;
    }
}
