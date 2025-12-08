package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Quản lý tập trung tất cả font size trong ứng dụng
 * 
 * @author PhamTra
 */
public final class FontStyles {

  // Font family mặc định
  private static final String DEFAULT_FONT_FAMILY = "Segoe UI";

  // Map lưu trữ các font size theo type
  private static final Map<Type, FontConfig> FONT_MAP = new EnumMap<>(Type.class);

  static {
    // Định nghĩa các font size chuẩn
    FONT_MAP.put(Type.HEADING_LARGE, new FontConfig(24, Font.BOLD)); // Tiêu đề lớn
    FONT_MAP.put(Type.HEADING_MEDIUM, new FontConfig(20, Font.BOLD)); // Tiêu đề trung bình
    FONT_MAP.put(Type.HEADING_SMALL, new FontConfig(18, Font.BOLD)); // Tiêu đề nhỏ

    FONT_MAP.put(Type.BUTTON_LARGE, new FontConfig(16, Font.PLAIN)); // Button lớn
    FONT_MAP.put(Type.BUTTON_MEDIUM, new FontConfig(14, Font.PLAIN)); // Button trung bình
    FONT_MAP.put(Type.BUTTON_SMALL, new FontConfig(12, Font.PLAIN)); // Button nhỏ

    FONT_MAP.put(Type.TEXT_LARGE, new FontConfig(16, Font.PLAIN)); // Text lớn
    FONT_MAP.put(Type.TEXT_MEDIUM, new FontConfig(14, Font.BOLD)); // Text trung bình
    FONT_MAP.put(Type.TEXT_SMALL, new FontConfig(12, Font.PLAIN)); // Text nhỏ

    FONT_MAP.put(Type.LABEL_LARGE, new FontConfig(16, Font.BOLD)); // Label lớn
    FONT_MAP.put(Type.LABEL_MEDIUM, new FontConfig(14, Font.BOLD)); // Label trung bình
    FONT_MAP.put(Type.LABEL_SMALL, new FontConfig(12, Font.BOLD)); // Label nhỏ

    FONT_MAP.put(Type.TABLE_HEADER, new FontConfig(14, Font.BOLD)); // Header bảng
    FONT_MAP.put(Type.TABLE_CONTENT, new FontConfig(13, Font.PLAIN)); // Nội dung bảng

    FONT_MAP.put(Type.INPUT_FIELD, new FontConfig(14, Font.PLAIN)); // TextField, TextArea
    FONT_MAP.put(Type.PLACEHOLDER, new FontConfig(13, Font.ITALIC)); // Placeholder text

    FONT_MAP.put(Type.NOTIFICATION, new FontConfig(13, Font.PLAIN)); // Thông báo
    FONT_MAP.put(Type.TOOLTIP, new FontConfig(12, Font.PLAIN)); // Tooltip
  }

  private FontStyles() {

  }
  
    /**
     * Chuyển đổi văn bản của các nút (JButton, JCheckBox...) thành IN HOA.
     * Hàm này an toàn, không lỗi nếu nút chưa có text hoặc null.
     */
    public static void toUpperCase(AbstractButton... buttons) {
        for (AbstractButton btn : buttons) {
            if (btn != null) {
                String text = btn.getText();
                if (text != null && !text.isEmpty()) {
                    btn.setText(text.toUpperCase());
                }
            }
        }
    }

    /**
     * Chuyển đổi văn bản của các nhãn (JLabel) thành IN HOA.
     */
    public static void toUpperCase(JLabel... labels) {
        for (JLabel lbl : labels) {
            if (lbl != null) {
                String text = lbl.getText();
                if (text != null && !text.isEmpty()) {
                    lbl.setText(text.toUpperCase());
                }
            }
        }
    }

    // ==========================================================================

  public static void apply(JComponent component, Type type) {
    FontConfig config = FONT_MAP.get(type);
    if (config == null) {
      throw new IllegalArgumentException("Chưa định nghĩa font cho type: " + type);
    }

    Font font = new Font(DEFAULT_FONT_FAMILY, config.style(), config.size());
    component.setFont(font);
  }

  public static void apply(JComponent component, Type type, String fontFamily) {
    FontConfig config = FONT_MAP.get(type);
    if (config == null) {
      throw new IllegalArgumentException("Chưa định nghĩa font cho type: " + type);
    }

    Font font = new Font(fontFamily, config.style(), config.size());
    component.setFont(font);
  }

  /**
   * Chuyển toàn bộ text của component sang chữ hoa.
   * Dùng cho label, button, text field, text area...
   */
  public static void applyUpperCase(JLabel label) {
    if (label == null) return;
    String text = label.getText();
    if (text != null) {
      label.setText(text.toUpperCase());
    }
  }

  public static void applyUpperCase(AbstractButton button) {
    if (button == null) return;
    String text = button.getText();
    if (text != null) {
      button.setText(text.toUpperCase());
    }
  }

  public static void applyUpperCase(JTextComponent textComponent) {
    if (textComponent == null) return;
    String text = textComponent.getText();
    if (text != null) {
      textComponent.setText(text.toUpperCase());
    }
  }

  /**
   * Lấy Font object theo type
   */
  public static Font getFont(Type type) {
    FontConfig config = FONT_MAP.get(type);
    if (config == null) {
      throw new IllegalArgumentException("Chưa định nghĩa font cho type: " + type);
    }

    return new Font(DEFAULT_FONT_FAMILY, config.style(), config.size());
  }

  /**
   * Lấy Font object với family tùy chỉnh
   */
  public static Font getFont(Type type, String fontFamily) {
    FontConfig config = FONT_MAP.get(type);
    if (config == null) {
      throw new IllegalArgumentException("Chưa định nghĩa font cho type: " + type);
    }

    return new Font(fontFamily, config.style(), config.size());
  }

  /**
   * Enum định nghĩa các loại font
   */
  public enum Type {
    // Tiêu đề
    HEADING_LARGE,
    HEADING_MEDIUM,
    HEADING_SMALL,

    // Button
    BUTTON_LARGE,
    BUTTON_MEDIUM,
    BUTTON_SMALL,

    // Text thông thường
    TEXT_LARGE,
    TEXT_MEDIUM,
    TEXT_SMALL,

    // Label
    LABEL_LARGE,
    LABEL_MEDIUM,
    LABEL_SMALL,

    // Bảng
    TABLE_HEADER,
    TABLE_CONTENT,

    // Input
    INPUT_FIELD,
    PLACEHOLDER,

    // Khác
    NOTIFICATION,
    TOOLTIP
  }

  /**
   * Record lưu trữ cấu hình font
   */
  private static record FontConfig(int size, int style) {
  }
}
