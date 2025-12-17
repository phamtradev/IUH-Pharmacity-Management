/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.chatbot;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.ChatBotDatabaseService;

/**
 * Giao diện Chat Bot với AI
 *
 * @author PhamTra
 */
public class GD_ChatBot extends javax.swing.JPanel {

    private JPanel chatContainer;
    private JScrollPane scrollPane;
    private JTextArea txtInput;
    private JButton btnSend;
    private JButton btnClear;
    private DateTimeFormatter timeFormatter;
    private boolean isPlaceholder = true; // Trạng thái placeholder
    private ChatBotDatabaseService dbService; // Service truy vấn database
    private final List<ChatMessage> conversationHistory;
    private static final Set<String> PRODUCT_STOPWORDS = new HashSet<>(Arrays.asList(
            "thuoc", "thuốc", "san", "sản", "pham", "phẩm", "sp",
            "tim", "tìm", "kiem", "kiếm", "kiemtra", "kiểm", "tra",
            "thong", "thông", "tin", "ton", "tồn", "kho",
            "con", "còn", "co", "có", "bao", "nhieu", "nhiêu", "so", "số", "luong", "lượng",
            "lo", "lô", "hang", "hàng", "may", "mấy", "gi", "gì",
            "cho", "xin", "hoi", "hỏi", "toi", "tôi", "ban", "bán",
            "duoc", "được", "la", "là", "ve", "về"
    ));
    //loai bo ky tu dac biet
    private static final String NON_TEXT_PATTERN = "[^a-z0-9áàảãạăắằẳẵặâấầẩẫậđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ\\s]";

    // Cấu hình API - OpenAI
    private static final String OPENAI_API_KEY = loadApiKey();
    private static final String API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/openai/chat/completions";
    private static final String MODEL = "gemini-2.5-flash";
    private static final int CONNECT_TIMEOUT = 10000; // 10 giây
    private static final int READ_TIMEOUT = 30000; // 30 giây
    private static final String PLACEHOLDER_TEXT = "Nhập câu hỏi của bạn...";
    
    /**
     * Đọc API key từ file config để bảo mật (không hardcode trong code)
     */
    private static String loadApiKey() {
        try {
            Properties props = new Properties();
            InputStream is = GD_ChatBot.class.getResourceAsStream("/chatbot-config.properties");
            if (is != null) {
                props.load(is);
                String apiKey = props.getProperty("api.key", "").trim();
                if (!apiKey.isEmpty() && !apiKey.equals("YOUR_API_KEY_HERE")) {
                    return apiKey;
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể đọc API key từ config: " + e.getMessage());
        }
        // Fallback: trả về key mặc định (nếu file config không tồn tại)
        // Lưu ý: Key này sẽ bị leak nếu commit vào Git, nên luôn dùng file config
        return "AIzaSyCLx6iK70qDzzXTZ0oMvffTgpS8Slh67Qs";
    }

    public GD_ChatBot() {
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dbService = new ChatBotDatabaseService(); // Khởi tạo service
        conversationHistory = Collections.synchronizedList(new ArrayList<>());
        initComponents();
        customUI();
        addWelcomeMessage();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

        // ===== PANEL TIÊU ĐỀ =====
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // ===== VÙNG CHAT =====
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(new Color(245, 247, 250));
        chatContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // ===== PANEL NHẬP LIỆU =====
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 255));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Phần bên trái - Thông tin Bot
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        // Avatar Bot
        JLabel lblAvatar = new JLabel("🤖");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        leftPanel.add(lblAvatar);

        // Thông tin Bot
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblBotName = new JLabel("Pharmacity AI Assistant");
        lblBotName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBotName.setForeground(Color.WHITE);

        JLabel lblStatus = new JLabel("● Online - Sẵn sàng hỗ trợ bạn");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(200, 230, 255));

        infoPanel.add(lblBotName);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblStatus);

        leftPanel.add(infoPanel);

        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Bỏ nút xóa lịch sử (để trống bên phải)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(12, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(15, 25, 20, 25));

        // Vùng nhập văn bản
        txtInput = new JTextArea(1, 30);
        txtInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInput.setLineWrap(true);
        txtInput.setWrapStyleWord(true);
        txtInput.setRows(2);
        txtInput.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtInput.setBackground(new Color(250, 250, 250));

        // Thêm hiệu ứng placeholder
        txtInput.setForeground(Color.GRAY);
        txtInput.setText(PLACEHOLDER_TEXT);
        isPlaceholder = true;

        txtInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholder) {
                    txtInput.setText("");
                    txtInput.setForeground(Color.BLACK);
                    isPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Only restore placeholder if text is empty
                if (txtInput.getText().trim().isEmpty() && !isPlaceholder) {
                    txtInput.setForeground(Color.GRAY);
                    txtInput.setText(PLACEHOLDER_TEXT);
                    isPlaceholder = true;
                }
            }
        });

        // Xử lý phím Enter
        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.isShiftDown()) {
                        // Shift+Enter: xuống dòng mới
                    } else {
                        e.consume();
                        sendMessage();
                    }
                }
            }
        });

        JScrollPane scrollInput = new JScrollPane(txtInput);
        scrollInput.setBorder(null);
        scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Nút gửi
        btnSend = new JButton("Gửi");
        btnSend.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSend.setForeground(Color.WHITE);
        btnSend.setPreferredSize(new Dimension(100, 45));
        btnSend.setFocusPainted(false);
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSend.putClientProperty(FlatClientProperties.STYLE,
                "arc: 12;"
                + "background: #4682FF;"
                + "hoverBackground: #3A6FE6;"
                + "pressedBackground: #2E5BD1");

        btnSend.addActionListener(e -> sendMessage());

        inputPanel.add(scrollInput, BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);

        return inputPanel;
    }

    private void customUI() {
        putClientProperty(FlatClientProperties.STYLE,
                "background: #FFFFFF;"
                + "border: 0,0,0,0");
    }

    private void addWelcomeMessage() {
        String welcomeText = "👋 Xin chào! Tôi là IUH Pharmacity AI Assistant.\n\n"
                + "Tôi có thể giúp bạn:\n"
                + "• Hướng dẫn sử dụng phần mềm\n"
                + "• Kiểm tra tồn kho sản phẩm\n"
                + "• Tìm kiếm thông tin thuốc\n"
                + "• Xem sản phẩm sắp hết hạn\n"
                + "• Thống kê tổng quan\n\n"
                + "Hãy hỏi tôi bất cứ điều gì! 😊";

        addBotMessage(welcomeText);
    }

    private void sendMessage() {
        // Kiểm tra nếu là placeholder hoặc rỗng
        if (isPlaceholder || txtInput.getText().trim().isEmpty()) {
            Notifications.getInstance().show(
                    Notifications.Type.WARNING,
                    "Vui lòng nhập câu hỏi!"
            );
            return;
        }

        String message = txtInput.getText().trim();

        // Thêm tin nhắn người dùng
        addUserMessage(message);
        addMessageToHistory("user", message);

        // Xóa ô nhập và giữ sẵn sàng cho tin nhắn tiếp theo
        txtInput.setText("");
        txtInput.setForeground(Color.BLACK);
        isPlaceholder = false; // Giữ false để người dùng có thể gõ ngay

        // Mô phỏng bot đang gõ và phản hồi
        simulateBotResponse(message);

        // Giữ focus ở ô nhập
        SwingUtilities.invokeLater(() -> txtInput.requestFocusInWindow());
    }

    private void simulateBotResponse(String userMessage) {
        // Hiển thị chỉ báo đang gõ
        JPanel typingPanel = createTypingIndicator();
        chatContainer.add(typingPanel);
        chatContainer.revalidate();
        chatContainer.repaint();
        scrollToBottom();

        // Gọi API trong luồng nền
        new Thread(() -> {
            try {
                String response = callChatBotAPI(userMessage);

                // Cập nhật giao diện trên EDT
                SwingUtilities.invokeLater(() -> {
                    chatContainer.remove(typingPanel);
                    addBotMessage(response);
                    chatContainer.revalidate();
                    chatContainer.repaint();
                    scrollToBottom();
                });

            } catch (Exception e) {
                e.printStackTrace();

                // Hiển thị lỗi trên EDT
                SwingUtilities.invokeLater(() -> {
                    chatContainer.remove(typingPanel);
                    showError(e.getMessage());
                    chatContainer.revalidate();
                    chatContainer.repaint();
                    scrollToBottom();
                });
            }
        }).start();
    }

    /**
     * Gọi OpenAI-compatible API (Gemini via OpenAI endpoint)
     */
    private String callChatBotAPI(String message) throws Exception {
        try {
            // Kiểm tra xem câu hỏi có liên quan đến database không
            String databaseContext = checkAndQueryDatabase(message);

            URL url = new URL(API_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            // Tạo nội dung JSON request theo định dạng OpenAI Chat Completions
            String systemMessage = "Bạn là trợ lý AI của hệ thống quản lý nhà thuốc IUH Pharmacity. "
                    + "Hệ thống này được phát triển cho việc quản lý nhà thuốc bao gồm: "
                    + "quản lý thuốc, quản lý nhân viên, quản lý khách hàng, quản lý đơn hàng, "
                    + "quản lý hóa đơn, quản lý kho, thống kê báo cáo, và nhiều chức năng khác. "
                    + "Hãy giúp người dùng giải đáp các thắc mắc về cách sử dụng phần mềm, "
                    + "các chức năng của hệ thống, hoặc các vấn đề liên quan đến quản lý nhà thuốc. "
                    + "Trả lời ngắn gọn, rõ ràng và hữu ích bằng tiếng Việt.";

            List<ChatMessage> historySnapshot = getHistorySnapshot();
            String jsonInputString = buildRequestPayload(systemMessage, historySnapshot, message, databaseContext);

            // Gửi yêu cầu
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Đọc phản hồi
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Phân tích phản hồi theo định dạng OpenAI
                    String parsedResponse = parseOpenAIResponse(response.toString());
                    addMessageToHistory("assistant", parsedResponse);
                    return parsedResponse;
                }
            } else {
                // Đọc thông báo lỗi
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder error = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        error.append(errorLine.trim());
                    }
                    throw new Exception("API trả về lỗi (HTTP " + responseCode + "): " + error.toString());
                }
            }
        } catch (java.net.ConnectException e) {
            throw new Exception("Không thể kết nối đến API. Vui lòng kiểm tra kết nối internet.");
        } catch (java.net.SocketTimeoutException e) {
            throw new Exception("Timeout khi gọi API. Vui lòng thử lại.");
        }
    }

    /**
     * Kiểm tra và truy vấn database nếu câu hỏi liên quan
     */
    private String checkAndQueryDatabase(String message) {
        String lowerMessage = message.toLowerCase();
        String productName = extractProductName(message);

        if (isSalesTodayQuestion(lowerMessage)) {
            return dbService.layThongTinBanHangHomNay();
        }

        // Câu hỏi về xuất hủy / hàng hỏng
        if (isDisposalQuestion(lowerMessage)) {
            return dbService.layThongTinDonCanXuatHuy();
        }

        // Phát hiện câu hỏi về số lô hàng (ưu tiên cao - kiểm tra trước)
        if (lowerMessage.contains("bao nhiêu lô") || lowerMessage.contains("bao nhieu lo")
                || lowerMessage.contains("mấy lô") || lowerMessage.contains("may lo")
                || lowerMessage.contains("số lô") || lowerMessage.contains("so lo")
                || lowerMessage.contains("có mấy lô") || lowerMessage.contains("co may lo")
                || lowerMessage.contains("có bao nhiêu lô") || lowerMessage.contains("co bao nhieu lo")
                || (lowerMessage.contains("lô") && (lowerMessage.contains("bao nhiêu") || lowerMessage.contains("bao nhieu") || lowerMessage.contains("mấy") || lowerMessage.contains("may")))
                || (lowerMessage.contains("lo") && (lowerMessage.contains("bao nhiêu") || lowerMessage.contains("bao nhieu") || lowerMessage.contains("mấy") || lowerMessage.contains("may")))) {

            if (productName != null && !productName.isEmpty()) {
                return dbService.demSoLoHang(productName);
            }
        }

        // Phát hiện câu hỏi về tồn kho (không chứa từ "lô")
        if (!lowerMessage.contains("lô") && !lowerMessage.contains("lo")
                && (lowerMessage.contains("tồn kho") || lowerMessage.contains("ton kho")
                || lowerMessage.contains("còn bao nhiêu") || lowerMessage.contains("con bao nhieu")
                || lowerMessage.contains("còn lại") || lowerMessage.contains("con lai")
                || lowerMessage.contains("số lượng") || lowerMessage.contains("so luong"))) {

            if (productName != null && !productName.isEmpty()) {
                return dbService.kiemTraTonKho(productName);
            }
        }

        // Phát hiện yêu cầu xem thông tin thuốc/sản phẩm
        if (isProductInfoQuestion(lowerMessage) && productName != null && !productName.isEmpty()) {
            return dbService.layThongTinSanPhamTheoTen(productName);
        }

        // Phát hiện câu hỏi tìm kiếm sản phẩm
        if (lowerMessage.contains("tìm") || lowerMessage.contains("tim")
                || lowerMessage.contains("tìm kiếm") || lowerMessage.contains("tim kiem")
                || lowerMessage.contains("có thuốc") || lowerMessage.contains("co thuoc")
                || lowerMessage.contains("có sản phẩm") || lowerMessage.contains("co san pham")) {

            if (productName != null && !productName.isEmpty()) {
                return dbService.timKiemSanPham(productName);
            }
        }

        // Phát hiện câu hỏi về sản phẩm / lô sắp hết hạn
        if (lowerMessage.contains("hết hạn") || lowerMessage.contains("het han")
                || lowerMessage.contains("sắp hết hạn") || lowerMessage.contains("sap het han")
                || lowerMessage.contains("hạn sử dụng") || lowerMessage.contains("han su dung")) {

            // Nếu người dùng có nêu tên sản phẩm → ưu tiên kiểm tra theo từng sản phẩm
            if (productName != null && !productName.isEmpty()) {
                return dbService.layLoSapHetHanTheoTenSanPham(productName);
            }

            // Không nêu cụ thể sản phẩm → trả về danh sách toàn bộ sản phẩm sắp hết hạn
            return dbService.laySanPhamSapHetHan();
        }

        // Phát hiện câu hỏi thống kê
        if (lowerMessage.contains("thống kê") || lowerMessage.contains("thong ke")
                || lowerMessage.contains("tổng quan") || lowerMessage.contains("tong quan")
                || lowerMessage.contains("báo cáo") || lowerMessage.contains("bao cao")) {
            return dbService.layThongKeTongQuan();
        }

        // Nếu người dùng chỉ nhập tên sản phẩm
        if (isOnlyProductName(message, productName)) {
            return dbService.layThongTinSanPhamTheoTen(productName);
        }

        return null; // Không phải câu hỏi về database
    }

    /**
     * Trích xuất tên sản phẩm từ câu hỏi
     */
    //nó xóa những câu hỏi của mình và chừa cái tên để thực hiện query
    private String extractProductName(String message) {
        if (message == null || message.trim().isEmpty()) {
            return null;
        }
        String sanitized = message.toLowerCase().replaceAll(NON_TEXT_PATTERN, " ");
        String[] tokens = sanitized.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String token : tokens) {
            if (token.isEmpty() || PRODUCT_STOPWORDS.contains(token)) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(token);
        }
        String cleaned = builder.toString().trim();
        return cleaned.length() >= 2 ? cleaned : null;
    }

    private boolean isSalesTodayQuestion(String lowerMessage) {
        boolean mentionToday = lowerMessage.contains("hôm nay") || lowerMessage.contains("hom nay");
        boolean mentionOrder = lowerMessage.contains("đơn") || lowerMessage.contains("don")
                || lowerMessage.contains("hóa đơn") || lowerMessage.contains("hoa don");
        boolean mentionSales = lowerMessage.contains("bán") || lowerMessage.contains("ban")
                || lowerMessage.contains("doanh thu") || lowerMessage.contains("ban duoc")
                || lowerMessage.contains("bán được");
        return mentionToday && mentionOrder && mentionSales;
    }

    private boolean isProductInfoQuestion(String lowerMessage) {
        boolean mentionInfo = lowerMessage.contains("thông tin") || lowerMessage.contains("thong tin")
                || lowerMessage.contains("chi tiết") || lowerMessage.contains("chi tiet");
        boolean mentionProduct = lowerMessage.contains("thuốc") || lowerMessage.contains("thuoc")
                || lowerMessage.contains("sản phẩm") || lowerMessage.contains("san pham");
        return mentionInfo && mentionProduct;
    }

    private boolean isDisposalQuestion(String lowerMessage) {
        boolean mentionDisposal = lowerMessage.contains("xuất hủy") || lowerMessage.contains("xuat huy")
                || lowerMessage.contains("hủy hàng") || lowerMessage.contains("huy hang")
                || lowerMessage.contains("hàng hỏng") || lowerMessage.contains("hang hong")
                || lowerMessage.contains("hủy thuốc") || lowerMessage.contains("huy thuoc");
        boolean mentionNeed = lowerMessage.contains("cần") || lowerMessage.contains("can")
                || lowerMessage.contains("phải") || lowerMessage.contains("phai")
                || lowerMessage.contains("có đơn") || lowerMessage.contains("co don")
                || lowerMessage.contains("đơn nào") || lowerMessage.contains("don nao");
        return mentionDisposal && (mentionNeed || lowerMessage.contains("hôm nay") || lowerMessage.contains("hom nay"));
    }

    private boolean isOnlyProductName(String originalMessage, String extractedName) {
        if (originalMessage == null || extractedName == null) {
            return false;
        }
        String normalizedOriginal = normalizeForComparison(originalMessage);
        return !normalizedOriginal.isEmpty() && normalizedOriginal.equals(extractedName);
    }

    private String normalizeForComparison(String input) {
        if (input == null) {
            return "";
        }
        return input.toLowerCase()
                .replaceAll(NON_TEXT_PATTERN, " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /**
     * Escape các ký tự đặc biệt trong JSON
     */
    //trong json ko hỗ trợ /n /t ""
    //nên phải thêm \\ trước 
    //ví dụ tôi nói: \\"xin chào\\"
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Phân tích phản hồi API theo định dạng OpenAI Định dạng:
     * {"choices":[{"message":{"content":"..."}}]}
     */
    private String parseOpenAIResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return "Không nhận được phản hồi từ API.";
        }

        String trimmed = response.trim();

        try {
            // Tìm content trong choices[0].message.content
            int choicesIndex = trimmed.indexOf("\"choices\"");
            if (choicesIndex == -1) {
                return "Lỗi: Không tìm thấy choices trong phản hồi.";
            }

            int messageIndex = trimmed.indexOf("\"message\"", choicesIndex);
            if (messageIndex == -1) {
                return "Lỗi: Không tìm thấy message trong phản hồi.";
            }

            int contentIndex = trimmed.indexOf("\"content\"", messageIndex);
            if (contentIndex == -1) {
                return "Lỗi: Không tìm thấy content trong phản hồi.";
            }

            int colonIndex = trimmed.indexOf(":", contentIndex);
            int startQuote = trimmed.indexOf("\"", colonIndex);
            if (startQuote == -1) {
                return "Lỗi: Không thể phân tích content từ phản hồi.";
            }

            int endQuote = findClosingQuote(trimmed, startQuote + 1);
            if (endQuote == -1) {
                return "Lỗi: Không thể tìm thấy dấu ngoặc kép đóng.";
            }

            String text = trimmed.substring(startQuote + 1, endQuote);
            return unescapeJson(text);

        } catch (Exception e) {
            return "Lỗi khi phân tích phản hồi: " + e.getMessage();
        }
    }

    /**
     * Tìm dấu " đóng
     */
    private int findClosingQuote(String str, int startIndex) {
        for (int i = startIndex; i < str.length(); i++) {
            if (str.charAt(i) == '"' && (i == 0 || str.charAt(i - 1) != '\\')) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Giải mã chuỗi JSON
     */
    private String unescapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }

    private void addUserMessage(String message) {
        JPanel messagePanel = createMessagePanel(message, true);
        chatContainer.add(messagePanel);
        chatContainer.add(Box.createVerticalStrut(12));
        chatContainer.revalidate();
        chatContainer.repaint();
        scrollToBottom();
    }

    private void addBotMessage(String message) {
        JPanel messagePanel = createMessagePanel(message, false);
        chatContainer.add(messagePanel);
        chatContainer.add(Box.createVerticalStrut(12));
        chatContainer.revalidate();
        chatContainer.repaint();
        scrollToBottom();
    }

    private JPanel createMessagePanel(String message, boolean isUser) {
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));
        outerPanel.setOpaque(false);

        if (isUser) {
            outerPanel.add(Box.createHorizontalGlue());
        }

        JPanel messagePanel = new JPanel(new BorderLayout(8, 4));
        messagePanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        if (isUser) {
            messagePanel.setBackground(new Color(70, 130, 255));
        } else {
            messagePanel.setBackground(Color.WHITE);
        }

        // Nội dung tin nhắn
        JTextArea txtMessage = new JTextArea(message);
        txtMessage.setEditable(false);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setOpaque(false);
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMessage.setColumns(40); // Đặt chiều rộng khoảng 40 ký tự
        txtMessage.setRows(0); // Tự động tính số dòng

        if (isUser) {
            txtMessage.setForeground(Color.WHITE);
        } else {
            txtMessage.setForeground(new Color(33, 33, 33));
        }

        // Nhãn thời gian
        String timeStr = LocalDateTime.now().format(timeFormatter);
        JLabel lblTime = new JLabel(timeStr);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        if (isUser) {
            lblTime.setForeground(new Color(200, 220, 255));
        } else {
            lblTime.setForeground(new Color(150, 150, 150));
        }

        messagePanel.add(txtMessage, BorderLayout.CENTER);
        messagePanel.add(lblTime, BorderLayout.SOUTH);

        // Bo góc tròn
        messagePanel.putClientProperty(FlatClientProperties.STYLE,
                "arc: 16");

        // Tính toán kích thước dựa trên nội dung
        // Đặt chiều rộng tối đa là 450px, nhưng nội dung ngắn sẽ có width nhỏ hơn
        int maxWidth = 450;
        int padding = 32; // 16px left + 16px right

        // Nếu text quá dài, sẽ wrap lại, cần tính lại chiều cao
        txtMessage.setSize(maxWidth - padding, Short.MAX_VALUE);
        Dimension preferredSize = txtMessage.getPreferredSize();

        // Đặt preferred size cho messagePanel
        messagePanel.setPreferredSize(new Dimension(
                Math.min(preferredSize.width + padding, maxWidth),
                preferredSize.height + 40 // Thêm khoảng trống cho time label
        ));
        messagePanel.setMaximumSize(new Dimension(maxWidth, preferredSize.height + 40));

        outerPanel.add(messagePanel);

        if (!isUser) {
            outerPanel.add(Box.createHorizontalGlue());
        }

        return outerPanel;
    }

    private JPanel createTypingIndicator() {
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));
        outerPanel.setOpaque(false);

        JPanel typingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        typingPanel.setMaximumSize(new Dimension(100, 50));
        typingPanel.setBackground(Color.WHITE);
        typingPanel.setBorder(new EmptyBorder(8, 12, 8, 12));

        typingPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 16");

        // Hiệu ứng chấm động
        JLabel lblTyping = new JLabel("●●●");
        lblTyping.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTyping.setForeground(new Color(150, 150, 150));

        typingPanel.add(lblTyping);

        // Hiệu ứng đơn giản
        Timer timer = new Timer(500, new ActionListener() {
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                count = (count + 1) % 4;
                lblTyping.setText("●".repeat(Math.max(1, count)));
            }
        });
        timer.start();

        outerPanel.add(typingPanel);
        outerPanel.add(Box.createHorizontalGlue());

        return outerPanel;
    }

    private void clearChat() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa toàn bộ lịch sử chat?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            chatContainer.removeAll();
            addWelcomeMessage();
            chatContainer.revalidate();
            chatContainer.repaint();
            resetConversationHistory();

            // Reset input field to placeholder
            txtInput.setText(PLACEHOLDER_TEXT);
            txtInput.setForeground(Color.GRAY);
            isPlaceholder = true;

            Notifications.getInstance().show(
                    Notifications.Type.SUCCESS,
                    "Đã xóa lịch sử chat"
            );
        }
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    // ===== PUBLIC METHODS FOR API INTEGRATION =====
    /**
     * Gọi phương thức này từ ChatBotService để thêm response từ API
     *
     * @param response Response từ AI API
     */
    public void addBotResponse(String response) {
        addBotMessage(response);
    }

    /**
     * Hiển thị lỗi khi gọi API thất bại
     *
     * @param errorMessage Thông báo lỗi
     */
    public void showError(String errorMessage) {
        addBotMessage("❌ Đã xảy ra lỗi: " + errorMessage + "\n\nVui lòng thử lại sau.");
    }

    // ====== Conversation history helpers ======
    private static final int MAX_HISTORY_MESSAGES = 12;

    private void addMessageToHistory(String role, String content) {
        synchronized (conversationHistory) {
            conversationHistory.add(new ChatMessage(role, content));
            while (conversationHistory.size() > MAX_HISTORY_MESSAGES) {
                conversationHistory.remove(0);
            }
        }
    }

    private List<ChatMessage> getHistorySnapshot() {
        synchronized (conversationHistory) {
            return new ArrayList<>(conversationHistory);
        }
    }

    private void resetConversationHistory() {
        synchronized (conversationHistory) {
            conversationHistory.clear();
        }
    }

    private String buildRequestPayload(String systemMessage,
            List<ChatMessage> historySnapshot,
            String latestUserMessage,
            String databaseContext) {

        StringBuilder messagesBuilder = new StringBuilder();
        messagesBuilder.append(String.format("{\"role\":\"system\",\"content\":\"%s\"}", escapeJson(systemMessage)));

        boolean hasLatestUser = false;
        for (int i = 0; i < historySnapshot.size(); i++) {
            ChatMessage msg = historySnapshot.get(i);
            boolean isLatestEntry = (i == historySnapshot.size() - 1);
            boolean isLatestUser = isLatestEntry && "user".equals(msg.getRole());

            String content = msg.getContent();
            if (isLatestUser && databaseContext != null && !databaseContext.isEmpty()) {
                content = content + "\n\n[Dữ liệu từ hệ thống]:\n" + databaseContext;
            }

            messagesBuilder.append(",");
            messagesBuilder.append(String.format("{\"role\":\"%s\",\"content\":\"%s\"}",
                    msg.getRole(),
                    escapeJson(content)));

            if (isLatestUser) {
                hasLatestUser = true;
            }
        }

        if (!hasLatestUser) {
            String content = latestUserMessage;
            if (databaseContext != null && !databaseContext.isEmpty()) {
                content = content + "\n\n[Dữ liệu từ hệ thống]:\n" + databaseContext;
            }
            messagesBuilder.append(",");
            messagesBuilder.append(String.format("{\"role\":\"user\",\"content\":\"%s\"}", escapeJson(content)));
        }

        return String.format("{\"model\":\"%s\",\"messages\":[%s]}", MODEL, messagesBuilder);
    }

    private static class ChatMessage {

        private final String role;
        private final String content;

        ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }
}
