package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.piechart;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple pie-chart component with percentage labels on each slice.
 */
public class PieChart extends JPanel {
    private final List<PieChartItem> items = new ArrayList<>();
    private String emptyMessage = "Không có dữ liệu";
    private final Font valueFont = new Font("Segoe UI", Font.BOLD, 12);
    private final List<SliceInfo> sliceInfos = new ArrayList<>();
    private final DecimalFormat valueFormat = new DecimalFormat("#,##0.##");

    public PieChart() {
        setOpaque(false);
        setPreferredSize(new Dimension(320, 320));
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    public void setData(List<PieChartItem> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        repaint();
    }

    public void clear() {
        items.clear();
        repaint();
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        sliceInfos.clear();

        double total = items.stream()
                .mapToDouble(PieChartItem::getValue)
                .filter(value -> value > 0)
                .sum();

        int size = Math.min(getWidth(), getHeight()) - 20;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        if (total <= 0 || items.isEmpty() || size <= 0) {
            drawEmptyMessage(g2);
            g2.dispose();
            setToolTipText(null);
            return;
        }

        double startAngle = 90;
        double radius = size / 2d;
        double centerX = x + radius;
        double centerY = y + radius;
        for (PieChartItem item : items) {
            if (item.getValue() <= 0) {
                continue;
            }
            double percent = item.getValue() / total;
            double angle = percent * 360d;
            g2.setColor(item.getColor());
            g2.fill(new Arc2D.Double(x, y, size, size, startAngle, -angle, Arc2D.PIE));
            drawSliceLabel(g2, x, y, size, startAngle, angle, percent);
            sliceInfos.add(new SliceInfo(item, startAngle, angle, centerX, centerY, radius, percent));
            startAngle -= angle;
        }

        g2.dispose();
    }

    private void drawSliceLabel(Graphics2D g2, int x, int y, int size, double startAngle, double angle, double percent) {
        double middleAngle = Math.toRadians(startAngle - angle / 2d);
        double radius = size / 2d * 0.65d;
        double centerX = x + size / 2d;
        double centerY = y + size / 2d;

        double labelX = centerX + radius * Math.cos(middleAngle);
        double labelY = centerY - radius * Math.sin(middleAngle);

        String text = String.format("%.1f%%", percent * 100);

        g2.setFont(valueFont);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawString(text, (float) (labelX - textWidth / 2d), (float) (labelY + textHeight / 2d - 2));
    }

    private void drawEmptyMessage(Graphics2D g2) {
        g2.setFont(getFont().deriveFont(Font.PLAIN, 14f));
        g2.setColor(new Color(140, 140, 140));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(emptyMessage);
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + fm.getAscent()) / 2;
        g2.drawString(emptyMessage, x, y);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        SliceInfo slice = findSliceAt(event.getPoint());
        if (slice == null) {
            return null;
        }
        return String.format("%s: %s (%.1f%%)",
                slice.item.getLabel(),
                valueFormat.format(slice.item.getValue()),
                slice.percent * 100);
    }

    private SliceInfo findSliceAt(Point point) {
        for (SliceInfo info : sliceInfos) {
            if (info.contains(point.x, point.y)) {
                return info;
            }
        }
        return null;
    }

    private static class SliceInfo {
        private final PieChartItem item;
        private final double startAngle;
        private final double extent;
        private final double centerX;
        private final double centerY;
        private final double radius;
        private final double percent;

        private SliceInfo(PieChartItem item, double startAngle, double extent,
                          double centerX, double centerY, double radius, double percent) {
            this.item = item;
            this.startAngle = normalizeAngle(startAngle);
            this.extent = extent;
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.percent = percent;
        }

        private boolean contains(int x, int y) {
            double dx = x - centerX;
            double dy = centerY - y; // invert Y axis
            double distance = Math.hypot(dx, dy);
            if (distance <= 0 || distance > radius) {
                return false;
            }
            double angle = Math.toDegrees(Math.atan2(dy, dx));
            if (angle < 0) {
                angle += 360;
            }
            double endAngle = normalizeAngle(startAngle - extent);
            if (startAngle >= endAngle) {
                return angle <= startAngle && angle >= endAngle;
            }
            return angle <= startAngle || angle >= endAngle;
        }

        private static double normalizeAngle(double angle) {
            double normalized = angle % 360;
            return normalized < 0 ? normalized + 360 : normalized;
        }
    }
}

