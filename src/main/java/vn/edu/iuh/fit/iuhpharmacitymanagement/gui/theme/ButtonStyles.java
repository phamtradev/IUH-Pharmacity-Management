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

    public static void apply(AbstractButton button, Type type) {
        Style style = STYLE_MAP.get(type);
        if (style == null) {
            throw new IllegalArgumentException("Chưa định nghĩa style cho type: " + type);
        }

        button.putClientProperty(FlatClientProperties.STYLE, style.asFlatLafStyle(DEFAULT_ARC));
        button.setBackground(style.backgroundColor());
        button.setForeground(style.foreground());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        if (button instanceof JButton) {
            ((JButton) button).setContentAreaFilled(true);
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

