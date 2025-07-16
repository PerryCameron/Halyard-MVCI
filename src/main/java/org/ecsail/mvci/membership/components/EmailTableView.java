package org.ecsail.mvci.membership.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Builder;
import org.ecsail.custom.RadioButtonCell;
import org.ecsail.fx.EmailFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.mvci.membership.mvci.person.PersonMessage;
import org.ecsail.mvci.membership.mvci.person.PersonModel;
import org.ecsail.mvci.membership.mvci.person.PersonView;
import org.ecsail.static_tools.StringTools;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EmailTableView implements Builder<TableView<EmailFx>> {

    private final PersonFx person;
    private final MembershipView membershipView;
    private final PersonModel personModel;
    private final PersonView personView;

    public EmailTableView(PersonView personView) {
        this.person = personView.getPersonModel().getPersonDTO();
        this.personModel = personView.getPersonModel();
        this.membershipView = personView.getPersonModel().getMembershipView();
        this.personView = personView;
    }

    @Override
    public TableView<EmailFx> build() {
        TableView<EmailFx> tableView = TableViewFx.tableViewOf(146,true);
        tableView.setItems(person.getEmail());
        List<TableColumn<EmailFx, ?>> columns = Arrays.asList(createColumn1(), createColumn2(), createColumn3());
        tableView.getColumns().setAll(columns);
        TableView.TableViewSelectionModel<EmailFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) personModel.selectedEmailProperty().set(newSelection);
        });
        return tableView;
    }

    private TableColumn<EmailFx, String> createColumn1() {
        TableColumn<EmailFx, String> col1 = TableColumnFx.editableStringTableColumn(EmailFx::emailProperty, "Email");
        col1.setOnEditCommit(t -> {
            EmailFx emailDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            String oldValue = emailDTO.getEmail();
            String newValue = t.getNewValue();
            int emailId = emailDTO.getEmailId();
            // Only proceed if the value has changed and is a valid email
            if (!newValue.equals(oldValue) && StringTools.isValidEmail(newValue)) {
                emailDTO.setEmail(newValue);
                personModel.selectedEmailProperty().set(emailDTO);
                System.out.println("col1: Updated email for Email_ID=" + emailId);
                personView.sendMessage().accept(PersonMessage.UPDATE_EMAIL);
            } else if (!StringTools.isValidEmail(newValue)) {
                person.getEmail().stream()
                        .filter(q -> q.getEmailId() == emailId)
                        .forEach(s -> s.setEmail("Bad Email"));
                System.out.println("col1: Invalid email for Email_ID=" + emailId);
            }
        });
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        return col1;
    }

    private TableColumn<EmailFx, Boolean> createColumn2() {
        TableColumn<EmailFx, Boolean> col2 = new TableColumn<>("Primary");
        col2.setStyle("-fx-alignment: CENTER;");
        col2.setCellValueFactory(new PropertyValueFactory<>("isPrimaryUse"));
        // Create a single ToggleGroup for all radio buttons in the column
        ToggleGroup toggleGroup = new ToggleGroup();
        // Set the cell factory to use RadioButtonCell with a callback
        col2.setCellFactory(c -> new RadioButtonCell(
                personModel.selectedEmailProperty(),
                toggleGroup,
                personView
        ));
        // Set the initial selection based on the primary email
        Optional<EmailFx> primaryEmail = Optional.ofNullable(
                person.getEmail().stream().filter(EmailFx::getIsPrimaryUse).findFirst().orElse(null)
        );
        if (primaryEmail.isPresent()) {
            personModel.selectedEmailProperty().set(primaryEmail.get());
        }
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 25); // Set column width
        return col2;
    }

    private TableColumn<EmailFx,Boolean> createColumn3() {
        TableColumn<EmailFx, Boolean> col3 = new TableColumn<>("Listed");
        col3.setCellValueFactory(param -> {
            EmailFx emailDTO = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(emailDTO.getIsListed());
            booleanProp.addListener((observable, oldValue, newValue) -> {
                emailDTO.setListed(newValue);
                personModel.selectedEmailProperty().set(emailDTO);
                System.out.println("col1");
                personView.sendMessage().accept(PersonMessage.UPDATE_EMAIL);
            });
            return booleanProp;
        });
        col3.setCellFactory(p12 -> {
            CheckBoxTableCell<EmailFx, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 25);  // Listed
        return col3;
    }  // how come a column such as this does not trigger an update? Is it because we are in deeper with createColumn1?
}
