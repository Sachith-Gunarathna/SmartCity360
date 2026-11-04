package components;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.*;

public class SmartTerminal extends JPanel {

    private final JTextPane textPane;
    private final StyledDocument doc;
    private final JScrollPane scrollPane;

    // Chitradalli iruva bannaagalu (Colors)
    private static final Color BG_COLOR = new Color(8, 15, 22);
    private static final Color BORDER_COLOR = new Color(38, 52, 71);
    private static final Color TEXT_COLOR = new Color(226, 232, 240);
    private static final Color GREEN_TAG = new Color(74, 222, 128);   // [INFO], [SUCCESS]
    private static final Color CYAN_TAG = new Color(45, 212, 191);    // [PATTERN], [DECORATOR]
    private static final Color YELLOW_TAG = new Color(250, 204, 21);  // [WARNING], [ALERT]
    private static final Color RED_TAG = new Color(248, 113, 113);    // [ERROR], [CRITICAL]
    private static final Color TIME_COLOR = new Color(148, 163, 184); // Timestamps

    private static final Pattern TAG_PATTERN = Pattern.compile("\\[([^\\]]+)\\]");

    public SmartTerminal() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new LineBorder(BORDER_COLOR, 1));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBackground(BG_COLOR);
        textPane.setForeground(TEXT_COLOR);
        textPane.setCaretColor(CYAN_TAG);
        textPane.setBorder(new EmptyBorder(10, 12, 10, 12));
        textPane.setFont(new Font("Consolas", Font.PLAIN, 14));

        doc = textPane.getStyledDocument();

        scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_COLOR);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Dark Scrollbar ವಿನ್ಯಾಸ
        styleScrollBar(scrollPane.getVerticalScrollBar());
        styleScrollBar(scrollPane.getHorizontalScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        // NetBeans Design View nalli kanisalu initial sample data
        loadSampleLogs();
    }

    private void loadSampleLogs() {
        clear();
        appendLog("INFO", "Initialized raw data stream from EM_82931");
        appendLog("PATTERN", "Applying Decorator Design Pattern dynamically");
        appendLog("DECORATOR", "Wrapped with EncryptionDecorator (Status: AES-256 Active)");
        appendLog("DECORATOR", "Wrapped with AuthenticationDecorator (HMAC-SHA256 Sign Active)");
        appendLog("DECORATOR", "Wrapped with CompressionDecorator (GZIP Compression Active)");
        appendLog("SUCCESS", "Secure packet EnDeCo(RawStreamData) transmitted successfully to urban API.");
    }

    /**
     * Terminal clear maadalu
     */
    public void clear() {
        textPane.setText("");
    }

    /**
     * Tag mattu message prathyekavaagi kaluhisi print maadalu
     * Udaharane: terminal.appendLog("DECORATOR", "Wrapped with EncryptionDecorator");
     */
    public void appendLog(String tag, String message) {
        String cleanTag = tag.replace("[", "").replace("]", "").trim();
        Color tagColor = resolveTagColor(cleanTag);

        appendStyledText("[" + cleanTag + "] ", tagColor, false);
        appendStyledText(message + "\n", TEXT_COLOR, false);
        scrollToBottom();
    }

    /**
     * Neraavaagi string salugalanu print maadalu (Tag bannavannu swayamchalitavaagi guruthisuttade)
     * Udaharane: terminal.append("[SUCCESS] Data transmitted!\n");
     */
    public void append(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        String[] lines = text.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.isEmpty()) {
                parseAndAppendLine(line);
            }
            if (i < lines.length - 1) {
                appendStyledText("\n", TEXT_COLOR, false);
            }
        }
        scrollToBottom();
    }

    public void setText(String text) {
        clear();
        append(text);
    }

    private void parseAndAppendLine(String line) {
        Matcher matcher = TAG_PATTERN.matcher(line);
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                String before = line.substring(lastIndex, matcher.start());
                appendStyledText(before, TEXT_COLOR, false);
            }

            String tagContent = matcher.group(1);
            Color tagColor = resolveTagColor(tagContent);
            appendStyledText("[" + tagContent + "]", tagColor, false);

            lastIndex = matcher.end();
        }

        if (lastIndex < line.length()) {
            appendStyledText(line.substring(lastIndex), TEXT_COLOR, false);
        }
    }

    private Color resolveTagColor(String tag) {
        String upper = tag.toUpperCase();

        // Timestamp aagiddare (udaharane: 18:24:31)
        if (upper.matches("\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?")) {
            return TIME_COLOR;
        }
        if (upper.contains("INFO") || upper.contains("SUCCESS") || upper.contains("POWER ON") || upper.contains("RESOLVED")) {
            return GREEN_TAG;
        }
        if (upper.contains("PATTERN") || upper.contains("DECORATOR") || upper.contains("COMPOSITE")
                || upper.contains("FLYWEIGHT") || upper.contains("INTERPRETER") || upper.contains("CHAIN")
                || upper.contains("REPORT") || upper.contains("OUTPUT")) {
            return CYAN_TAG;
        }
        if (upper.contains("WARN") || upper.contains("ALERT") || upper.contains("PASS")) {
            return YELLOW_TAG;
        }
        if (upper.contains("ERR") || upper.contains("CRIT") || upper.contains("SHUTDOWN") || upper.contains("OFF")) {
            return RED_TAG;
        }
        return CYAN_TAG;
    }

    private void appendStyledText(String text, Color color, boolean bold) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);
        StyleConstants.setFontFamily(attrs, "Consolas");
        StyleConstants.setFontSize(attrs, 14);
        StyleConstants.setBold(attrs, bold);

        try {
            doc.insertString(doc.getLength(), text, attrs);
        } catch (BadLocationException ignored) {
        }
    }

    private void scrollToBottom() {
        textPane.setCaretPosition(doc.getLength());
    }

    private void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setPreferredSize(new Dimension(10, 10));
        scrollBar.setBackground(new Color(15, 23, 34));
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(71, 85, 105);
                this.trackColor = new Color(15, 23, 34);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createArrowButton(true);
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createArrowButton(false);
            }

            private JButton createArrowButton(boolean isUp) {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(15, 23, 34));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setColor(new Color(148, 163, 184));
                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        if (isUp) {
                            g2.drawLine(cx - 3, cy + 1, cx, cy - 2);
                            g2.drawLine(cx, cy - 2, cx + 3, cy + 1);
                        } else {
                            g2.drawLine(cx - 3, cy - 1, cx, cy + 2);
                            g2.drawLine(cx, cy + 2, cx + 3, cy - 1);
                        }
                        g2.dispose();
                    }
                };
                btn.setPreferredSize(new Dimension(10, 14));
                btn.setBorder(null);
                btn.setContentAreaFilled(false);
                return btn;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                        thumbBounds.width - 4, thumbBounds.height - 4, 6, 6);
                g2.dispose();
            }
        });
    }
}