package core;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class CustomDateCell extends JFXTreeTableCell<DataEntry, Date> {
    protected DatePicker datePicker;
    public CustomDateCell() {
    }
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            datePicker.requestFocus();
            setText(null);
            setGraphic(datePicker);
        }
    }
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        //setText((getDate().toString()));
        setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getDate());
                }
                setText(null);
                setGraphic(datePicker);
            } else if (item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                setGraphic(null);
            }
        }
    }


    private void createDatePicker() {
        datePicker = new JFXDatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((e) -> {
            if (datePicker.getValue() != null) {

                System.out.println("Committed: " + datePicker.getValue().toString());
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            } else {
                commitEdit(null);
            }
        });
        datePicker.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue && datePicker.getValue() != null) {
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
    }
    protected LocalDate getDate() {
        return getItem() == null ?  LocalDate.now() : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    }
}
