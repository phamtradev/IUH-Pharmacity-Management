package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart;

import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.blankchart.BlankPlotChart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.blankchart.BlankPlotChatRender;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.blankchart.SeriesSize;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.DinhDangSo;

public class Chart extends javax.swing.JPanel {

    private List<ModelLegend> legends = new ArrayList<>();
    private List<ModelChart> model = new ArrayList<>();
    private final int seriesSize = 14;
    private final int seriesSpace = 12;
    private final Animator animator;
    private float animate;
    private static final DoubleFunction<String> DEFAULT_VALUE_FORMATTER = value ->
            DinhDangSo.dinhDangTien(value);
    private DoubleFunction<String> valueFormatter = DEFAULT_VALUE_FORMATTER;
    private final List<BarInfo> barInfos = new ArrayList<>();
    private int cachedTextWidth = 70; // Cache textWidth để dùng khi check tooltip

    public Chart() {
        initComponents();
        // Enable tooltip cho cả Chart panel và blankPlotChart
        javax.swing.ToolTipManager.sharedInstance().registerComponent(blankPlotChart);
        javax.swing.ToolTipManager.sharedInstance().setInitialDelay(0);
        // Setup tooltip provider cho blankPlotChart
        blankPlotChart.setTooltipProvider(point -> getToolTipTextForPoint(point));
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = fraction;
                repaint();
            }
        };
        animator = new Animator(800, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        blankPlotChart.setBlankPlotChatRender(new BlankPlotChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override
            public void renderSeries(BlankPlotChart chart, Graphics2D g2, SeriesSize size, int index) {
                // Chỉ clear barInfos ở lần render đầu tiên (index = 0)
                if (index == 0) {
                    barInfos.clear();
                }
                
                // SeriesSize.getX() và getY() đã là tọa độ absolute trong BlankPlotChart
                // (được tính từ insets.left + textWidth + spaceText và insets.top)
                double totalSeriesWidth = (seriesSize * legends.size()) + (seriesSpace * (legends.size() - 1));
                double x = (size.getWidth() - totalSeriesWidth) / 2;

                for (int i = 0; i < legends.size(); i++) {
                    ModelLegend legend = legends.get(i);
                    g2.setColor(legend.getColor());
                    double seriesValues = chart.getSeriesValuesOf(model.get(index).getValues()[i], size.getHeight()) * animate;

                    // Chỉ vẽ cột nếu giá trị khác 0
                    if (seriesValues > 0) {
                        // Tọa độ trong Graphics2D context (đã là absolute trong BlankPlotChart)
                        int barX = (int) (size.getX() + x - 6);
                        int barY = (int) (size.getY() + size.getHeight() - seriesValues);
                        int barWidth = seriesSize + 18;
                        int barHeight = (int) seriesValues;

                        // Vẽ cột
                        g2.fillRect(barX, barY, barWidth, barHeight);

                        // Lưu thông tin cột với tọa độ absolute trong BlankPlotChart
                        double value = model.get(index).getValues()[i];
                        String label = model.get(index).getLabel();
                        barInfos.add(new BarInfo(barX, barY, barWidth, barHeight, value, legend.getName(), label, size));
                    }

                    x += seriesSpace + seriesSize; // Cập nhật vị trí x cho cột tiếp theo
                }
            }
        });

    }

    public void addLegend(String name, Color color) {
        ModelLegend data = new ModelLegend(name, color);
        legends.add(data);
        panelLegend.add(new LegendItem(data));
        panelLegend.repaint();
        panelLegend.revalidate();
    }
    
    public void clearLegends() {
        legends.clear();
        panelLegend.removeAll();
        panelLegend.repaint();
        panelLegend.revalidate();
    }

    public void addData(ModelChart data) {
        model.add(data);
        blankPlotChart.setLabelCount(model.size());
        double max = data.getMaxValues();
        if (max > blankPlotChart.getMaxValues()) {
            blankPlotChart.setMaxValues(max);
        }
    }

    public void clear() {
        animate = 0;
        blankPlotChart.setLabelCount(0);
        model.clear();
        barInfos.clear();
        repaint();
    }
    
    /**
     * Lấy tooltip text khi hover vào cột
     */
    public String getToolTipTextForPoint(Point point) {
        if (barInfos.isEmpty()) {
            return null;
        }
        
        // BarInfo đã lưu tọa độ absolute trong BlankPlotChart
        // Point từ MouseEvent cũng là absolute trong BlankPlotChart
        // Dùng Rectangle.contains() để check chính xác hơn
        for (BarInfo barInfo : barInfos) {
            if (barInfo.contains(point.x, point.y)) {
                return String.format("%s - %s: %s", 
                    barInfo.getLabel(),
                    barInfo.getLegendName(),
                    valueFormatter.apply(barInfo.getValue()));
            }
        }
        return null;
    }
    
    /**
     * Class lưu thông tin về một cột trong chart
     */
    private static class BarInfo {
        private final java.awt.Rectangle rect; // Dùng Rectangle để check contains dễ hơn
        private final double value;
        private final String legendName;
        private final String label;
        
        public BarInfo(int x, int y, int width, int height, double value, String legendName, String label, SeriesSize seriesSize) {
            this.rect = new java.awt.Rectangle(x, y, width, height);
            this.value = value;
            this.legendName = legendName;
            this.label = label;
        }
        
        public boolean contains(int px, int py) {
            // Mở rộng vùng hit một chút để dễ hover
            java.awt.Rectangle expanded = new java.awt.Rectangle(
                rect.x - 3, rect.y - 3, 
                rect.width + 6, rect.height + 6
            );
            return expanded.contains(px, py);
        }
        
        public double getValue() {
            return value;
        }
        
        public String getLegendName() {
            return legendName;
        }
        
        public String getLabel() {
            return label;
        }
    }

    public void start() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public void setValueFormatter(DoubleFunction<String> valueFormatter) {
        this.valueFormatter = valueFormatter != null ? valueFormatter : DEFAULT_VALUE_FORMATTER;
    }

    public void setValuesFormat(String valuesFormat) {
        if (valuesFormat != null && !valuesFormat.isBlank()) {
            blankPlotChart.setValuesFormat(valuesFormat);
        } else {
            blankPlotChart.setValuesFormat("#,##0.##");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blankPlotChart = new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.blankchart.BlankPlotChart();
        panelLegend = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.blankchart.BlankPlotChart blankPlotChart;
    private javax.swing.JPanel panelLegend;
    // End of variables declaration//GEN-END:variables
}
