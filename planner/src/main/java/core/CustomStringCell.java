package core;

import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.input.KeyCode;

public class CustomStringCell extends JFXTreeTableCell<DataEntry, String> {
    protected TextField textField;
    protected ChangeListener<? super Boolean> changeListener = (obs, ov, nv) -> {
        if (!nv) {
            commitEdit(textField.getText());
        }
    };

    public CustomStringCell() {

    }

    @Override
    public void startEdit() {
        if (editableProperty().get()) {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.requestFocus();
            } else {
                super.startEdit();

            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                    textField.selectAll();
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    protected void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(changeListener);
        textField.setOnAction(evt -> commitEdit(textField.getText()));

        textField.setOnKeyPressed((ke) -> {
            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                textField.focusedProperty().removeListener(changeListener);
                cancelEdit();
            }
            if (ke.getCode().equals(KeyCode.TAB)) {
                commitEdit(textField.getText());
            }
        });
    }


    protected String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
    /*
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void commitEdit(String item) {
        textField.focusedProperty().removeListener(changeListener);
        if (isEditing()) {
            super.commitEdit(item);
        } else {
            if (table != null) {
                TreeTablePosition position = new TreeTablePosition(table,
                        currentCell.getIndex(), getTableColumn());
                TreeTableColumn.CellEditEvent editEvent = new TreeTableColumn.CellEditEvent(table, position,
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(getTableColumn(), editEvent);
            }
            updateItem(item, false);
            if (table != null) {
                table.edit(-1, null);
            }

        }
    }

     */
}
