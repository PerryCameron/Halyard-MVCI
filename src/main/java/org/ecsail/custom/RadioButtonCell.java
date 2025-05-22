package org.ecsail.custom;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import org.ecsail.fx.EmailDTOFx;

public class RadioButtonCell extends TableCell<EmailDTOFx, Boolean> {

    private final ObjectProperty<EmailDTOFx> emailDTOObjectProperty;

    public RadioButtonCell(ObjectProperty<EmailDTOFx> emailDTOObjectProperty) {
        this.emailDTOObjectProperty = emailDTOObjectProperty;
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {

            RadioButton radioButton = new RadioButton();
            radioButton.setSelected(item);
            setGraphic(radioButton);

            radioButton.selectedProperty().addListener(
                    (o, old, selected) -> {
                        if (selected) {
                            EmailDTOFx emailDTO = getTableRow().getItem();
                            if (emailDTOObjectProperty.get() != null) {
                                emailDTOObjectProperty.get().setPrimaryUse(false);
//          TODO                      SqlUpdate.updateEmail("primary_use", emailDTOObjectProperty.get().getEmail_id(), false);
                            }

                            emailDTO.setPrimaryUse(true);
//          TODO                  SqlUpdate.updateEmail("primary_use", emailDTO.getEmail_id(), true);
                            emailDTOObjectProperty.set(emailDTO);
                        }
                    });
        } else {
            setGraphic(null);
        }
    }
}
