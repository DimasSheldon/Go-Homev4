package sheldon.com.android.gohomev4.content;

import sheldon.com.android.gohomev4.helper.ColorPicker;

public class WidgetControl {
    private String label, value;
    private int cvColor, iconColor;
    private String updateIndicator;
    private boolean toggleState;


    public WidgetControl(String label, String color, String value, String updateIndicator) {
        setColor(color);
        this.label = label;
        this.value = value;
        this.updateIndicator = updateIndicator;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setColor(String responseColor) {
        this.cvColor = ColorPicker.pickColorLight(responseColor);
        this.iconColor = ColorPicker.pickColorDark(responseColor);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUpdateIndicator(String updateIndicator) {
        this.updateIndicator = updateIndicator;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int cvColor() {
        return cvColor;
    }

    public int iconColor() {
        return iconColor;
    }

    public String getUpdateIndicator() {
        return updateIndicator;
    }

    public boolean getToggleState() {
        toggleState = value.equals("ON");
        return toggleState;
    }

    public void setToggleState(boolean toggleState) {
        this.toggleState = toggleState;
    }
}
