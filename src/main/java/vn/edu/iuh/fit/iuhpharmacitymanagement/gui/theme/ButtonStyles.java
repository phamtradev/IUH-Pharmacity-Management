package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Tiện ích chuẩn hóa màu sắc cho các nút hành động.
 * Giúp đồng bộ giao diện giữa các dialog/screen.
 */
public final class ButtonStyles {

    private static final int DEFAULT_ARC = 10;
    private static final Map<Type, Style> STYLE_MAP = new EnumMap<>(Type.class);

    static {
        STYLE_MAP.put(Type.PRIMARY, new Style("#0D6EFD", "#0B5ED7", "#0A58CA", Color.WHITE));
        STYLE_MAP.put(Type.SUCCESS, new Style("#28A745", "#218838", "#1E7E34", Color.WHITE));
        STYLE_MAP.put(Type.WARNING, new Style("#FFC107", "#FFCA2C", "#FFCD39", Color.BLACK));
        STYLE_MAP.put(Type.DANGER, new Style("#DC3545", "#C82333", "#BD2130", Color.WHITE));
        STYLE_MAP.put(Type.SECONDARY, new Style("#6C757D", "#5C636A", "#565E64", Color.WHITE));
        STYLE_MAP.put(Type.INFO, new Style("#17A2B8", "#138496", "#117A8B", Color.WHITE));
    }

    private ButtonStyles() {
    }

    /**
     * Tự động điều chỉnh kích thước button theo nội dung text
     */
    private static void adjustButtonSize(JButton button) {
        String text = button.getText();
        if (text == null || text.trim().isEmpty()) {
            // Button không có text - kích thước nhỏ
            button.setPreferredSize(new java.awt.Dimension(40, 40));
            return;
        }

        // Tính toán kích thước dựa trên độ dài text
        int textLength = text.length();
        int width;

        if (textLength <= 4) {
            // Text ngắn: THÊM, SỬA, XÓA
            width = 95;
        } else if (textLength <= 8) {
            // Text trung bình: Tìm kiếm, Đăng nhập
            width = 120;
        } else if (textLength <= 12) {
            // Text dài: Xem chi tiết
            width = 150;
        } else {
            // Text rất dài
            width = Math.max(150, textLength * 12);
        }

        button.setPreferredSize(new java.awt.Dimension(width, 40));
        button.setMinimumSize(new java.awt.Dimension(width, 40));
    }

    public static void apply(JButton button, Type type) {
        Style style = STYLE_MAP.get(type);
        if (style != null) {
            // Xóa các style cũ trước khi áp dụng mới
            button.setBackground(null);
            button.setForeground(null);

            // Áp dụng FlatLaf style
            button.putClientProperty(FlatClientProperties.STYLE, style.asFlatLafStyle(DEFAULT_ARC));

            // Override trực tiếp để đảm bảo hiệu quả
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            // Đặt lại kích thước chuẩn cho button - tự động điều chỉnh theo nội dung
            adjustButtonSize(button);

            // Đặt lại text position về CENTER
            button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            button.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        }
    }

    public enum Type {
        PRIMARY,
        SUCCESS,
        WARNING,
        DANGER,
        SECONDARY,
        INFO
    }

    private static final class Style {
        private final String backgroundHex;
        private final String hoverHex;
        private final String pressedHex;
        private final Color foreground;

        private Style(String backgroundHex, String hoverHex, String pressedHex, Color foreground) {
            this.backgroundHex = backgroundHex;
            this.hoverHex = hoverHex;
            this.pressedHex = pressedHex;
            this.foreground = foreground;
        }

        private String asFlatLafStyle(int arc) {
            return "background:" + backgroundHex + ";"
                    + "foreground:" + toHex(foreground) + ";"
                    + "hoverBackground:" + hoverHex + ";"
                    + "pressedBackground:" + pressedHex + ";"
                    + "arc:" + arc + ";"
                    + "borderWidth:0;"
                    + "focusWidth:0;";
        }

        private Color backgroundColor() {
            return Color.decode(backgroundHex);
        }

        private Color foreground() {
            return foreground;
        }

        private static String toHex(Color color) {
            return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        }
    }
}
