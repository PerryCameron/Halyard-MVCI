package org.ecsail.mvci_membership;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.ecsail.dto.DbInvoiceDTO;
import org.ecsail.dto.FeeDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.widgetfx.VBoxFx;

import java.math.BigDecimal;


public class InvoiceItemRow extends HBox {
    private InvoiceDTO invoiceDTO;
    private DbInvoiceDTO dbInvoiceDTO;
    private InvoiceItemDTO invoiceItemDTO;
    private InvoiceItemGroup invoiceItemGroup = null;
    private FeeDTO feeDTO = null;
    private TextField textField;
    private Spinner<Integer> spinner;
    private ComboBox<Integer> comboBox;

    public InvoiceItemRow(InvoiceItemDTO invoiceItemDTO, InvoiceItemGroup invoiceItemGroup) {  // this is used for sub items to groups
        this.invoiceDTO = invoiceItemGroup.getInvoiceDTO();
        this.dbInvoiceDTO = invoiceItemGroup.getDbInvoiceDTO();
        this.invoiceItemDTO = invoiceItemDTO;
        this.invoiceItemGroup = invoiceItemGroup;
        buildRow();
    }

    public InvoiceItemRow(InvoiceDTO invoiceDTO, DbInvoiceDTO dbInvoiceDTO) { // this is for items
        this.invoiceDTO = invoiceDTO;
        this.dbInvoiceDTO = dbInvoiceDTO;
        this.invoiceItemDTO = setItem();
        buildRow();
    }

    private void buildRow() {
        this.feeDTO = attachCorrectFeeDTO();
        VBox vBox1 = VBoxFx.vBoxOf(140.0, Pos.CENTER_LEFT);
        vBox1.getChildren().add(new Label(invoiceItemDTO.getFieldName() + ":"));
        VBox vBox2 = VBoxFx.vBoxOf(65.0, Pos.CENTER_LEFT);
        vBox2.getChildren().add(widgetControl());
        VBox vBox3 = VBoxFx.vBoxOf(40.0, Pos.CENTER_RIGHT);
        vBox3.getChildren().add(addMultiple());
        VBox vBox4 = VBoxFx.vBoxOf(70.0, Pos.CENTER_RIGHT);
        vBox4.getChildren().add(addFee());
        VBox vBox5 = VBoxFx.vBoxOf(110.0, Pos.CENTER_RIGHT);
        vBox5.getChildren().add(totalLabel());
        getChildren().addAll(vBox1, vBox2, vBox3, vBox4);
        if(invoiceItemDTO.getCategory().equals("none")) getChildren().add(vBox5);
    }

    private Node totalLabel() {
        if(!invoiceItemDTO.getCategory().equals("none")) return new Region();
        Label label = new Label();
        label.getStyleClass().add("standard-black-label");
        label.textProperty().bind(invoiceItemDTO.valueProperty());
        return label;
    }

    private FeeDTO attachCorrectFeeDTO() {
        if (dbInvoiceDTO.isAutoPopulate()) return null;
        for (FeeDTO feeDTO : invoiceDTO.getFeeDTOS()) {
            if (!invoiceItemDTO.getCategory().equals("none")) {  // add fees for category invoice items
                if (invoiceItemDTO.getFieldName().equals(feeDTO.getDescription()))
                    return feeDTO;
            } else {  // add fees for invoice items
                if (invoiceItemDTO.getFieldName().equals(feeDTO.getFieldName()))
                    return feeDTO;
            }
        }
        return null;
    }

    private Node addFee() {
        Label label = new Label("");
        label.getStyleClass().add("standard-black-label");
        if (feeDTO != null) label.setText(feeDTO.getFieldValue());
        return label;
    }

    private Node addMultiple() {
        Label label = new Label();
        if (dbInvoiceDTO.isMultiplied()) label.setText("X");
        return label;
    }

    private Control widgetControl() {
        switch (dbInvoiceDTO.getWidgetType()) {
            case "text-field" -> {
                textField = new TextField();
                textField.setPrefWidth(dbInvoiceDTO.getWidth());
                textField.setText(invoiceItemDTO.getValue());
//                setTextFieldListener();
                // below if statement added because it needed to update dues.
//                if(!invoiceItemDTO.getValue().equals("0.00")) {
//                    invoiceItemDTO.setQty(1);
//                    updateBalance();
//                    checkIfNotCommittedAndUpdateSql();
//                }
                return textField;
            }
            case "spinner", "itemized" -> {
                spinner = new Spinner<>();
                spinner.setPrefWidth(dbInvoiceDTO.getWidth());
                setSpinnerListener();
//                if (dbInvoiceDTO.isPrice_editable())
//                    setPriceChangeListener(new TextField(price.getText()));
                return spinner;
            }
            case "combo-box" -> {
                comboBox = new ComboBox<>();
                comboBox.setPrefWidth(dbInvoiceDTO.getWidth());
//                price.setText(String.valueOf(fee.getFieldValue()));
//                // fill comboBox
//                for (int j = 0; j < dbInvoiceDTO.getMaxQty(); j++) comboBox.getItems().add(j);
//                comboBox.getSelectionModel().select(invoiceItemDTO.getQty());
////                setComboBoxListener();
//                if (dbInvoiceDTO.isPrice_editable())
//                    setPriceChangeListener(new TextField(price.getText()));
                return comboBox;
            }
            case "none" -> {
                textField = new TextField("none");
                textField.setVisible(false);
                return textField;
            }
        }
        return null;
    }

    private InvoiceItemDTO setItem() {
        // we will match this db_invoice to invoiceItem, if nothing found then create an invoice item
        InvoiceItemDTO currentInvoiceItem = invoiceDTO.getInvoiceItemDTOS().stream()
                .filter(i -> i.getFieldName().equals(dbInvoiceDTO.getFieldName())).findFirst().orElse(null);
        if (currentInvoiceItem == null) return addNewInvoiceItem();
        return currentInvoiceItem;
    }

    private InvoiceItemDTO addNewInvoiceItem() { //
        InvoiceItemDTO newInvoiceItem = new InvoiceItemDTO(invoiceDTO.getId(), invoiceDTO.getMsId(), invoiceDTO.getYear(), dbInvoiceDTO.getFieldName());
        invoiceDTO.getInvoiceItemDTOS().add(newInvoiceItem);
//        SqlInsert.addInvoiceItemRecord(newInvoiceItem);
        return newInvoiceItem;
    }

    private void setSpinnerListener() {
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, dbInvoiceDTO.getMaxQty(), invoiceItemDTO.getQty());
        spinner.setValueFactory(spinnerValueFactory);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            String calculatedTotal = String.valueOf(new BigDecimal(feeDTO.getFieldValue()).multiply(BigDecimal.valueOf(newValue)));
            invoiceItemDTO.setValue(calculatedTotal);
            invoiceItemDTO.setQty(newValue);
            if(invoiceItemGroup != null) invoiceItemGroup.updateGroupTotal();
//            updateBalance();
        });
    }

    private void updateBalance() {

    }

}
