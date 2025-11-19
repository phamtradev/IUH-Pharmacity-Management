package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.piechart;

import java.awt.Color;

/**
 * Lightweight model describing a single slice in {@link PieChart}.
 */
public class PieChartItem {
    private final String label;
    private final double value;
    private final Color color;

    public PieChartItem(String label, double value, Color color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }
}

