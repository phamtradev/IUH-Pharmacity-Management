package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart;

public class ModelChart {

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public ModelChart(String label, double[] values) {
        this.label = label;
        this.values = values;
        this.fullLabel = label; // Mặc định fullLabel = label
    }
    
    public ModelChart(String label, double[] values, String fullLabel) {
        this.label = label;
        this.values = values;
        this.fullLabel = fullLabel != null ? fullLabel : label;
    }

    public ModelChart() {
    }

    private String label;
    private double values[];
    private String fullLabel; // Tên đầy đủ để hiển thị trong tooltip

    public String getFullLabel() {
        return fullLabel != null ? fullLabel : label;
    }

    public void setFullLabel(String fullLabel) {
        this.fullLabel = fullLabel;
    }

    public double getMaxValues() {
        double max = 0;
        for (double v : values) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }
}
