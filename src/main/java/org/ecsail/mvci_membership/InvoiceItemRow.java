package org.ecsail.mvci_membership;

import javafx.beans.value.ObservableValue;
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
import org.ecsail.static_tools.StringTools;
import org.ecsail.widgetfx.VBoxFx;

import java.math.BigDecimal;

public class InvoiceItemRow extends HBox {
    private final InvoiceDTO invoiceDTO;
    private final DbInvoiceDTO dbInvoiceDTO; // LIST of these in InvoiceDTO, this one is relevant here
    private final InvoiceItemDTO invoiceItemDTO; // LIST of these in InvoiceDTO, this one is relevant here
    private InvoiceItemGroup invoiceItemGroup = null;
    private FeeDTO feeDTO = null; // LIST of these in InvoiceDTO, this one is relevant here


    public InvoiceItemRow(InvoiceItemDTO invoiceItemDTO, InvoiceItemGroup invoiceItemGroup) {  // this is used for groups
        this.invoiceDTO = invoiceItemGroup.getInvoiceDTO();
        this.dbInvoiceDTO = invoiceItemGroup.getDbInvoiceDTO();
        this.invoiceItemDTO = invoiceItemDTO;
        this.invoiceItemGroup = invoiceItemGroup;
        buildRow();
    }

    public InvoiceItemRow(InvoiceDTO invoiceDTO, DbInvoiceDTO dbInvoiceDTO, InvoiceView invoiceView) { // this is for individual items
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
        VBox vBox5 = VBoxFx.vBoxOf(120.0, Pos.CENTER_RIGHT); // width should match HBox in InvoiceItemGroup
        vBox5.getChildren().add(totalLabel());
        getChildren().addAll(vBox1, vBox2, vBox3, vBox4);
        if(invoiceItemDTO.getCategoryItem().equals("none")) getChildren().add(vBox5);
    }

    private Node totalLabel() {
        if(!invoiceItemDTO.getCategoryItem().equals("none")) return new Region();
        Label label = new Label();
        if(invoiceItemDTO.isCredit())
            label.getStyleClass().add("standard-red-label");
        else
            label.getStyleClass().add("standard-black-label");
        label.textProperty().bind(invoiceItemDTO.valueProperty());
        return label;
    }

    private FeeDTO attachCorrectFeeDTO() {
        if (dbInvoiceDTO.isAutoPopulate()) return null;
        for (FeeDTO feeDTO : invoiceDTO.getFeeDTOS()) {
            if (!invoiceItemDTO.getCategoryItem().equals("none")) {  // add fees for category invoice items
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
        TextField textField;
        switch (dbInvoiceDTO.getWidgetType()) {
            case "text-field" -> { return createTextField(); }
            case "spinner", "itemized" -> { return createSpinner(); } // may make "itemized-spinner", "itemized-textField" etc. in future
            case "none" -> { // I think this is never called
                textField = new TextField("none");
                textField.setVisible(false);
                return textField;
            }
        }
        return null;
    }

    private Control createTextField() {
        TextField textField = new TextField();
        textField.setPrefWidth(dbInvoiceDTO.getWidth());
        textField.setText(invoiceItemDTO.getValue());
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (oldValue) { // we have focused and unfocused
                invoiceItemDTO.setValue(StringTools.validateCurrency(textField.getText()));
                updateItem();
            }
        });
        return textField;
    }

    private Control createSpinner() {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setPrefWidth(dbInvoiceDTO.getWidth());
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, dbInvoiceDTO.getMaxQty(), invoiceItemDTO.getQty());
        spinner.setValueFactory(spinnerValueFactory);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            String calculatedTotal = String.valueOf(new BigDecimal(feeDTO.getFieldValue()).multiply(BigDecimal.valueOf(newValue)));
            invoiceItemDTO.setValue(calculatedTotal);
            invoiceItemDTO.setQty(newValue);
            updateItem();
        });
        return spinner;
    }

    private void updateItem() {
        if (invoiceItemGroup != null) invoiceItemGroup.updateGroupValues();  // important this updates before balance
        invoiceDTO.updateBalance();
    }

    private InvoiceItemDTO setItem() {
        // we will match this db_invoice to invoiceItem, if nothing found then create an invoice item
        InvoiceItemDTO currentInvoiceItem = invoiceDTO.getInvoiceItemDTOS().stream()
                .filter(i -> i.getFieldName().equals(dbInvoiceDTO.getFieldName())).findFirst().orElse(null);
        if (currentInvoiceItem == null) return addNewInvoiceItem();
        return currentInvoiceItem;
    }

    private InvoiceItemDTO addNewInvoiceItem() {
        InvoiceItemDTO newInvoiceItem = new InvoiceItemDTO(invoiceDTO.getId(), invoiceDTO.getMsId(), invoiceDTO.getYear(), dbInvoiceDTO.getFieldName());
        invoiceDTO.getInvoiceItemDTOS().add(newInvoiceItem);
        return newInvoiceItem;
    }
}
