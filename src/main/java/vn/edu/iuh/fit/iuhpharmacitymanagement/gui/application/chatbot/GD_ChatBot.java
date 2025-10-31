/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.chatbot;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import raven.toast.Notifications;

/**
 * Giao diện Chat Bot với AI
 * @author PhamTra
 */
public class GD_ChatBot extends javax.swing.JPanel {

    private JPanel chatContainer;
    private JScrollPane scrollPane;
    private JTextArea txtInput;
    private JButton btnSend;
    private JButton btnClear;
    private DateTimeFormatter timeFormatter;
    private boolean isPlaceholder = true; // Track placeholder state
    
    // API Configuration
    private static final String API_ENDPOINT = "http://localhost:8080/chat";
    private static final int CONNECT_TIMEOUT = 10000; // 10 seconds
    private static final int READ_TIMEOUT = 30000; // 30 seconds
    private static final String PLACEHOLDER_TEXT = "Nhập câu hỏi của bạn...";
    
    public GD_ChatBot() {
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        initComponents();
        customUI();
        addWelcomeMessage();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        
        // ===== HEADER PANEL =====
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ===== CHAT AREA =====
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
        
        // ===== INPUT PANEL =====
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 255));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Left side - Bot info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        // Bot Avatar
        JLabel lblAvatar = new JLabel("🤖");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        leftPanel.add(lblAvatar);
        
        // Bot Info
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
        
        // Right side - Clear button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        
        btnClear = new JButton("Xóa lịch sử");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        btnClear.setContentAreaFilled(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 8;" +
            "background: rgba(255,255,255,26);" +
            "hoverBackground: rgba(255,255,255,51);" +
            "pressedBackground: rgba(255,255,255,38)");
        
        btnClear.addActionListener(e -> clearChat());
        
        rightPanel.add(btnClear);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(12, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(15, 25, 20, 25));
        
        // Text input area
        txtInput = new JTextArea(1, 30);
        txtInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInput.setLineWrap(true);
        txtInput.setWrapStyleWord(true);
        txtInput.setRows(2);
        txtInput.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        txtInput.putClientProperty(FlatClientProperties.STYLE,
            "arc: 8;" +
            "background: #FAFAFA");
        
        // Add placeholder effect
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
        
        // Handle Enter key
        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.isShiftDown()) {
                        // Shift+Enter: new line (default behavior)
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
        
        // Send button
        btnSend = new JButton("Gửi");
        btnSend.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSend.setForeground(Color.WHITE);
        btnSend.setPreferredSize(new Dimension(100, 45));
        btnSend.setFocusPainted(false);
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSend.putClientProperty(FlatClientProperties.STYLE,
            "arc: 12;" +
            "background: #4682FF;" +
            "hoverBackground: #3A6FE6;" +
            "pressedBackground: #2E5BD1");
        
        btnSend.addActionListener(e -> sendMessage());
        
        inputPanel.add(scrollInput, BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);
        
        return inputPanel;
    }
    
    private void customUI() {
        putClientProperty(FlatClientProperties.STYLE,
            "background: #FFFFFF;" +
            "border: 0,0,0,0");
    }
    
    private void addWelcomeMessage() {
        String welcomeText = "👋 Xin chào! Tôi là IUH Pharmacity AI Assistant.\n\n" +
            "Tôi có thể giúp đỡ bạn cách sử dụng phần mềm.";
        
        addBotMessage(welcomeText);
    }
    
    private void sendMessage() {
        // Check if it's placeholder or empty
        if (isPlaceholder || txtInput.getText().trim().isEmpty()) {
            Notifications.getInstance().show(
                Notifications.Type.WARNING,
                "Vui lòng nhập câu hỏi!"
            );
            return;
        }
        
        String message = txtInput.getText().trim();
        
        // Add user message
        addUserMessage(message);
        
        // Clear input and keep it ready for next message
        txtInput.setText("");
        txtInput.setForeground(Color.BLACK);
        isPlaceholder = false; // Keep it false so user can type immediately
        
        // Simulate bot typing and response
        simulateBotResponse(message);
        
        // Keep focus in input field
        SwingUtilities.invokeLater(() -> txtInput.requestFocusInWindow());
    }
    
    private void simulateBotResponse(String userMessage) {
        // Show typing indicator
        JPanel typingPanel = createTypingIndicator();
        chatContainer.add(typingPanel);
        chatContainer.revalidate();
        chatContainer.repaint();
        scrollToBottom();
        
        // Call API in background thread
        new Thread(() -> {
            try {
                String response = callChatBotAPI(userMessage);
                
                // Update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    chatContainer.remove(typingPanel);
                    addBotMessage(response);
                    chatContainer.revalidate();
                    chatContainer.repaint();
                    scrollToBottom();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                
                // Show error on EDT
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
     * Gọi Chat Bot API
     */
    private String callChatBotAPI(String message) throws Exception {
        try {
            URL url = new URL(API_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            // Tạo JSON request body
            String jsonInputString = String.format(
                "{\"message\": \"%s\"}",
                escapeJson(message)
            );

            // Gửi request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Đọc response
            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // Parse response (hỗ trợ cả JSON và plain text)
                    return parseApiResponse(response.toString());
                }
            } else {
                // Đọc error message
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
            throw new Exception("Không thể kết nối đến Chat Bot API. Vui lòng kiểm tra server đã chạy chưa.");
        } catch (java.net.SocketTimeoutException e) {
            throw new Exception("Timeout khi gọi API. Vui lòng thử lại.");
        }
    }
    
    /**
     * Escape các ký tự đặc biệt trong JSON
     */
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
     * Parse API response - hỗ trợ cả JSON và plain text
     */
    private String parseApiResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return "Không nhận được phản hồi từ API.";
        }
        
        String trimmed = response.trim();
        
        // Nếu là plain text (không phải JSON), trả về trực tiếp
        if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) {
            return trimmed;
        }
        
        // Nếu là JSON, thử parse
        try {
            // Tìm "response": "..."
            int responseIndex = trimmed.indexOf("\"response\"");
            if (responseIndex != -1) {
                int colonIndex = trimmed.indexOf(":", responseIndex);
                int startQuote = trimmed.indexOf("\"", colonIndex);
                int endQuote = findClosingQuote(trimmed, startQuote + 1);
                if (startQuote != -1 && endQuote != -1) {
                    return unescapeJson(trimmed.substring(startQuote + 1, endQuote));
                }
            }
            
            // Tìm "message": "..."
            int messageIndex = trimmed.indexOf("\"message\"");
            if (messageIndex != -1) {
                int colonIndex = trimmed.indexOf(":", messageIndex);
                int startQuote = trimmed.indexOf("\"", colonIndex);
                int endQuote = findClosingQuote(trimmed, startQuote + 1);
                if (startQuote != -1 && endQuote != -1) {
                    return unescapeJson(trimmed.substring(startQuote + 1, endQuote));
                }
            }
            
            // Nếu không tìm thấy field, trả về toàn bộ
            return trimmed;
            
        } catch (Exception e) {
            return trimmed;
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
     * Unescape JSON string
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
        messagePanel.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
        messagePanel.setBorder(new EmptyBorder(12, 16, 12, 16));
        
        if (isUser) {
            messagePanel.setBackground(new Color(70, 130, 255));
        } else {
            messagePanel.setBackground(Color.WHITE);
        }
        
        // Message text
        JTextArea txtMessage = new JTextArea(message);
        txtMessage.setEditable(false);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setOpaque(false);
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        if (isUser) {
            txtMessage.setForeground(Color.WHITE);
        } else {
            txtMessage.setForeground(new Color(33, 33, 33));
        }
        
        // Time label
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
        
        // Rounded corners
        messagePanel.putClientProperty(FlatClientProperties.STYLE,
            "arc: 16");
        
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
        
        // Animated dots
        JLabel lblTyping = new JLabel("●●●");
        lblTyping.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTyping.setForeground(new Color(150, 150, 150));
        
        typingPanel.add(lblTyping);
        
        // Simple animation
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
     * @param response Response từ AI API
     */
    public void addBotResponse(String response) {
        addBotMessage(response);
    }
    
    /**
     * Hiển thị lỗi khi gọi API thất bại
     * @param errorMessage Thông báo lỗi
     */
    public void showError(String errorMessage) {
        addBotMessage("❌ Đã xảy ra lỗi: " + errorMessage + "\n\nVui lòng thử lại sau.");
    }
}

