package org.ecsail.mvci_membership;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ecsail.dto.DbInvoiceDTO;
import org.ecsail.dto.FeeDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.widgetfx.VBoxFx;


public class InvoiceItemRow extends HBox {

    private final InvoiceDTO invoiceDTO;
    private final DbInvoiceDTO dbInvoiceDTO;
    protected InvoiceItemDTO invoiceItemDTO;
    private FeeDTO feeDTO = null;
    private TextField textField;
    private Spinner<Integer> spinner;
    private ComboBox<Integer> comboBox;



    public InvoiceItemRow(InvoiceDTO invoiceDTO, DbInvoiceDTO dbInvoiceDTO) {
        this.invoiceDTO = invoiceDTO;
        this.dbInvoiceDTO = dbInvoiceDTO;
        this.invoiceItemDTO = setItem();
        for (FeeDTO f : invoiceDTO.getFeeDTOS()) {
            if (dbInvoiceDTO.getFieldName().equals(f.getFieldName()))
                this.feeDTO = f;
        }
        VBox vBox1 = VBoxFx.vBoxOf(140.0 ,Pos.CENTER_LEFT);
        vBox1.getChildren().add(new Label(dbInvoiceDTO.getFieldName() + ":"));
        VBox vBox2 = VBoxFx.vBoxOf(65.0 ,Pos.CENTER_LEFT);
        VBox vBox3 = VBoxFx.vBoxOf(30.0 ,Pos.CENTER_RIGHT);
        VBox vBox4 = VBoxFx.vBoxOf(50.0 ,Pos.CENTER_RIGHT);
        VBox vBox5 = VBoxFx.vBoxOf(70.0 ,Pos.CENTER_RIGHT);
        getChildren().addAll(vBox1,vBox2,vBox3,vBox4,vBox5);
    }

//    private Control setControlWidget() {
//        switch (dbInvoiceDTO.getWidgetType()) {
//            case "text-field" -> {
//                textField = new TextField();
//                textField.setPrefWidth(dbInvoiceDTO.getWidth());
//                textField.setText(invoiceItemDTO.getValue());
////                setTextFieldListener();
//                // below if statement added because it needed to update dues.
//                if(!invoiceItemDTO.getValue().equals("0.00")) {
//                    invoiceItemDTO.setQty(1);
//                    updateBalance();
//                    checkIfNotCommittedAndUpdateSql();
//                }
//                return textField;
//            }
//            case "spinner" -> {
//                spinner = new Spinner<>();
//                spinner.setPrefWidth(dbInvoiceDTO.getWidth());
//                price.setText(String.valueOf(feeDTO.getFieldValue()));
////                setSpinnerListener();
//                if (dbInvoiceDTO.isPrice_editable())
//                    setPriceChangeListener(new TextField(price.getText()));
//                return spinner;
//            }
//            case "combo-box" -> {
//                comboBox = new ComboBox<>();
//                comboBox.setPrefWidth(dbInvoiceDTO.getWidth());
//                price.setText(String.valueOf(fee.getFieldValue()));
//                // fill comboBox
//                for (int j = 0; j < dbInvoiceDTO.getMaxQty(); j++) comboBox.getItems().add(j);
//                comboBox.getSelectionModel().select(invoiceItemDTO.getQty());
////                setComboBoxListener();
//                if (dbInvoiceDTO.isPrice_editable())
//                    setPriceChangeListener(new TextField(price.getText()));
//                return comboBox;
//            }
//            case "itemized" -> { // more complex so layout and logic in different class
//                // setForTitledPane(); <- gets called in setEdit() in normal but here in mock
//                TitledPane titledPane = new TitledPane(fee.getFieldName(),new ItemizedCategory(this));
//                titledPane.setExpanded(false);
//                titledPane.getStyleClass().add("custom-titlepane");
//                return titledPane;
//            }
//            case "none" -> {
//                textField = new TextField("none");
//                textField.setVisible(false);
//                return textField;
//            }
//        }
//        return null;
//    }

    private InvoiceItemDTO setItem() {
        // we will match this db_invoice to invoiceItem, if nothing found then create an invoice item
        InvoiceItemDTO currentInvoiceItem = invoiceDTO.getInvoiceItemDTOS().stream()
                .filter(i -> i.getFieldName().equals(dbInvoiceDTO.getFieldName())).findFirst().orElse(null);
        if(currentInvoiceItem == null) return addNewInvoiceItem();
        return currentInvoiceItem;
    }

    private InvoiceItemDTO addNewInvoiceItem() { //
        InvoiceItemDTO newInvoiceItem = new InvoiceItemDTO(invoiceDTO.getId(),invoiceDTO.getMsId(),invoiceDTO.getYear(),dbInvoiceDTO.getFieldName());
        invoiceDTO.getInvoiceItemDTOS().add(newInvoiceItem);
//        SqlInsert.addInvoiceItemRecord(newInvoiceItem);
        return newInvoiceItem;
    }
}
