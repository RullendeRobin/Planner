package core;

import javafx.scene.control.ToggleButton;

public class RadioToggleButton extends ToggleButton {

    public RadioToggleButton(String text) {
        super(text);
    }

    // Prevents toggle from being deselected
    @Override
    public void fire() {
        // we don't toggle from selected to not selected if part of a group
        if (getToggleGroup() == null || !isSelected()) {
            super.fire();
        }
    }
}
