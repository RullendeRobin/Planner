package core;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import ui.OverviewController;

public class CustomComboBoxCell extends JFXTreeTableCell<DataEntry, String> {
    private ComboBox<String> comboBox;
    private OverviewController controller;
    private int index;
    private ChangeListener<? super Boolean> changeListener = (obs, ov, nv) -> {
        if (!nv) {
            commitEdit(comboBox.getValue());
        }
    };


    public CustomComboBoxCell(OverviewController controller, int index) {
        this.controller = controller;
        this.index = index;
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

    // Creates a combobox with the appropriate properties, the switch is used to determine which observable list to use
    private void createComboBox() {
        switch (index){
            case 0:
                comboBox = new JFXComboBox<>(controller.getGroupComboList());
                break;
            case 1:
                comboBox = new JFXComboBox<>(controller.getMandatoryComboList());
                break;
            case 2:
                comboBox = new JFXComboBox<>(controller.getResponsibleComboList());
                break;
            default:
                comboBox = new JFXComboBox<>();
        }
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