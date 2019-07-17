package core;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

public class CustomComboBoxCell extends JFXTreeTableCell<DataEntry, String> {
    public ComboBox<String> comboBox;
    private ObservableList<String> obsList;
    private ChangeListener<? super Boolean> changeListener = (obs, ov, nv) -> {
        if (!nv) {
            commitEdit(comboBox.getValue());
        }
    };



    public CustomComboBoxCell(ObservableList<String> obsList) {
        this.obsList = obsList;
    }

    @Override
    public void startEdit() {
        if (editableProperty().get()) {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                comboBox.requestFocus();
                setText(null);
                setGraphic(comboBox);
                comboBox.setValue(getItem());
            } else {
                super.startEdit();

            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
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
                if (comboBox != null) {
                    comboBox.setValue(getItem());
                }
                setText(null);
                setGraphic(comboBox);
            } else {
                setText(getItem());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        comboBox = new JFXComboBox<>(obsList);
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.focusedProperty().addListener(changeListener);
        comboBox.setOnAction(evt -> commitEdit(comboBox.getValue()));

        comboBox.setOnKeyPressed((ke) -> {
            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                comboBox.focusedProperty().removeListener(changeListener);
                cancelEdit();
            }
            if (ke.getCode().equals(KeyCode.TAB)) {
                commitEdit(comboBox.getValue());
            }
        });
    }
}