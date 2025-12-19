package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.piechart;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
        
        // Enable tooltip
        setToolTipText(""); // Set empty string để enable tooltip
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.registerComponent(this);
        toolTipManager.setInitialDelay(0); // Hiển thị ngay lập tức
        toolTipManager.setReshowDelay(0); // Hiển thị lại ngay khi di chuyển
        toolTipManager.setDismissDelay(5000); // Giữ tooltip 5 giây
        
        // Đảm bảo component có thể nhận mouse events
        setEnabled(true);
    }

    public void setData(List<PieChartItem> data) {
        items.clear();
        sliceInfos.clear(); // Clear sliceInfos khi data thay đổi
        if (data != null) {
            items.addAll(data);
        }
        repaint();
    }

    public void clear() {
        items.clear();
        sliceInfos.clear(); // Clear sliceInfos khi clear
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
        
        // Clear sliceInfos trước khi tạo lại
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
            return;
        }

        double startAngle = 90;
        double radius = size / 2d;
        double centerX = x + radius;
        double centerY = y + radius;
        
        // Tạo lại sliceInfos với thông tin đầy đủ
        for (PieChartItem item : items) {
            if (item.getValue() <= 0) {
                continue;
            }
            double percent = item.getValue() / total;
            double angle = percent * 360d;
            g2.setColor(item.getColor());
            g2.fill(new Arc2D.Double(x, y, size, size, startAngle, -angle, Arc2D.PIE));
            drawSliceLabel(g2, x, y, size, startAngle, angle, percent);
            
            // Lưu thông tin slice với tọa độ và góc chính xác
            // Lưu ý: extent phải là giá trị âm để phù hợp với Arc2D (quay ngược chiều kim đồng hồ)
            sliceInfos.add(new SliceInfo(item, startAngle, -angle, centerX, centerY, radius, percent));
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
        if (event == null) {
            return null;
        }
        
        // Đảm bảo tooltip hiển thị ngay lập tức
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.setInitialDelay(0);
        toolTipManager.setReshowDelay(0);
        toolTipManager.setDismissDelay(5000);
        
        // Tìm slice tại vị trí chuột
        Point point = event.getPoint();
        SliceInfo slice = findSliceAt(point);
        
        if (slice == null) {
            // Debug: có thể log để kiểm tra
            return null;
        }
        
        // Hiển thị tooltip với tên đầy đủ và thông tin chi tiết
        String label = slice.item.getLabel();
        String value = valueFormat.format(slice.item.getValue());
        double percent = slice.percent * 100;
        
        // Format tooltip với HTML để hiển thị đẹp hơn
        return String.format("<html><b>%s</b><br>Tổng: %s<br>Tỷ lệ: %.1f%%</html>",
                htmlEscape(label),
                value,
                percent);
    }
    
    /**
     * Escape HTML special characters để tránh lỗi hiển thị
     */
    private String htmlEscape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private SliceInfo findSliceAt(Point point) {
        // sliceInfos được tạo lại mỗi lần paintComponent
        // Nếu rỗng nhưng có items, có thể component chưa được vẽ lần đầu
        if (sliceInfos.isEmpty()) {
            if (!items.isEmpty()) {
                // Component chưa được vẽ, trigger repaint
                // Nhưng không thể chờ repaint xong, nên return null lần này
                // Tooltip sẽ hoạt động sau khi component được vẽ
                return null;
            }
            return null;
        }
        
        // Tìm slice chứa điểm chuột
        // Duyệt ngược để ưu tiên slice được vẽ sau (có thể overlap)
        for (int i = sliceInfos.size() - 1; i >= 0; i--) {
            SliceInfo info = sliceInfos.get(i);
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
        private final Arc2D.Double arc; // Lưu Arc2D để dùng contains()

        private SliceInfo(PieChartItem item, double startAngle, double extent,
                          double centerX, double centerY, double radius, double percent) {
            this.item = item;
            this.startAngle = normalizeAngle(startAngle);
            this.extent = extent;
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.percent = percent;
            
            // Tạo Arc2D để dùng contains() method
            // Tính toán vị trí và kích thước của arc
            double size = radius * 2;
            double x = centerX - radius;
            double y = centerY - radius;
            this.arc = new Arc2D.Double(x, y, size, size, startAngle, extent, Arc2D.PIE);
        }

        private boolean contains(int x, int y) {
            // Sử dụng Arc2D.contains() để kiểm tra chính xác
            return arc.contains(x, y);
        }

        private static double normalizeAngle(double angle) {
            double normalized = angle % 360;
            return normalized < 0 ? normalized + 360 : normalized;
        }
    }
}

