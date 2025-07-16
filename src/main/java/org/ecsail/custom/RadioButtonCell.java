package org.ecsail.custom;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ToggleGroup;

import org.ecsail.fx.EmailDTOFx;
import org.ecsail.mvci.membership.mvci.person.PersonMessage;
import org.ecsail.mvci.membership.mvci.person.PersonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// your solution did not work, it caused it not to trigger when the selection was changed
public class RadioButtonCell extends TableCell<EmailDTOFx, Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(RadioButtonCell.class);
    private final RadioButton radioButton;
    private final ObjectProperty<EmailDTOFx> selectedEmail;
    private final PersonView personView;
    private boolean isUserAction = false; // Flag to track user-initiated actions

    public RadioButtonCell(ObjectProperty<EmailDTOFx> selectedEmail, ToggleGroup toggleGroup, PersonView personView) {
        this.selectedEmail = selectedEmail;
        this.personView = personView;
        radioButton = new RadioButton();
        radioButton.setToggleGroup(toggleGroup);
        // Detect user clicks on the radio button
        radioButton.setOnMouseClicked(event -> {
            isUserAction = true; // Mark as user action
            // Ensure the radio button is selected before processing
            if (radioButton.isSelected() && getTableRow() != null) {
                processSelection();
            }
        });
        // Listen for selection changes
        radioButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && getTableRow() != null && isUserAction) { // Only process user-initiated changes
                processSelection();
            }
        });
        // Reset isUserAction after processing to prevent re-triggering
        radioButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            isUserAction = false; // Reset after each selection change
        });
    }

    private void processSelection() {
        EmailDTOFx emailDTOFx = getTableRow().getItem();
        if (emailDTOFx != null && selectedEmail.get().getEmailId() != emailDTOFx.getEmailId()) { // I added the second condition here and it works perfect
            emailDTOFx.setPrimaryUse(true);
            // set lastEmail to current selection
            logger.info("Switched: " + emailDTOFx.getEmail());
            selectedEmail.set(emailDTOFx);
            personView.sendMessage().accept(PersonMessage.UPDATE_EMAIL);
        }
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        logger.debug("updateItem called: empty={}, item={}", empty, item);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            radioButton.setSelected(item);
            setGraphic(radioButton);
        }
    }
}

