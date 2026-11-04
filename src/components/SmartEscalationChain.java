package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class SmartEscalationChain extends JPanel {

    private int activeLevel = 1; // 1 = Local Staff, 2 = Supervisor, 3 = City Manager
    private int hoverLevel = -1;
    private SmartTerminal boundTerminal;

    private String level1Eta = "15 mins";
    private String level2Eta = "30 mins";
    private String level3Eta = "45 mins";

    private final Rectangle[] cardBounds = new Rectangle[3];

    public SmartEscalationChain() {
        setBackground(new Color(30,41,59));
        setPreferredSize(new Dimension(540, 360));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        for (int i = 0; i < 3; i++) {
            cardBounds[i] = new Rectangle();
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < 3; i++) {
                    if (cardBounds[i].contains(e.getPoint())) {
                        escalateToLevel(i + 1);
                        break;
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int newHover = -1;
                for (int i = 0; i < 3; i++) {
                    if (cardBounds[i].contains(e.getPoint())) {
                        newHover = i + 1;
                        break;
                    }
                }
                if (newHover != hoverLevel) {
                    hoverLevel = newHover;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverLevel != -1) {
                    hoverLevel = -1;
                    repaint();
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    /**
     * Ikonektar ti SmartTerminal tapno automatiko a ma-log ti panagtaray ti Chain of Responsibility
     */
    public void bindTerminal(SmartTerminal terminal) {
        this.boundTerminal = terminal;
    }

    /**
     * Baliwan ti aktibo nga agpang (1, 2, wenno 3)
     */
    public void escalateToLevel(int level) {
        if (level < 1 || level > 3) {
            return;
        }
        this.activeLevel = level;
        repaint();

        if (boundTerminal != null) {
            if (level == 1) {
                boundTerminal.appendLog("CHAIN", "Level 1: Local Field Staff assigned (ETA: " + level1Eta + ", Status: Dispatched)");
            } else if (level == 2) {
                boundTerminal.appendLog("WARNING", "Level 1 exceeded threshold -> Escalated to Level 2: Maintenance Supervisor");
            } else if (level == 3) {
                boundTerminal.appendLog("CRITICAL", "Critical emergency -> Escalated to Level 3: City Manager");
            }
        }
    }

    public int getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(int activeLevel) {
        escalateToLevel(activeLevel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // 1. Akinruar a Card Background ken Border
        g2.setColor(new Color(30,41,59));
        g2.fillRoundRect(0, 0, w, h, 14, 14);
        g2.setColor(new Color(30,41,59));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(1, 1, w - 3, h - 3, 14, 14);

        g2.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("Escalation Chain Routing Path", 22, 34);
        
        // 3.rukod ken posision dagiti 3 nga agpang
        int leftNumX = 20;
        int iconCenterX = 64;
        int cardX = 104;
        int cardW = Math.max(260, w - cardX - 22);

        int y1 = 54;
        int h1 = (activeLevel == 1) ? 92 : 60;

        int gap = 38;
        int y2 = y1 + h1 + gap;
        int h2 = (activeLevel == 2) ? 92 : 60;

        int y3 = y2 + h2 + gap;
        int h3 = (activeLevel == 3) ? 92 : 60;

        int[] ys = {y1, y2, y3};
        int[] hs = {h1, h2, h3};

        String[] titles = {
            "Level 1: Local Field Staff",
            "Level 2: Maintenance Supervisor",
            "Level 3: City Manager"
        };

        String[] etas = {level1Eta, level2Eta, level3Eta};

        // 4. Ipinta dagiti puted-puted a pana (Dotted Arrows) iti baet dagiti agpang
        for (int i = 0; i < 2; i++) {
            int topCircleBottom = (ys[i] + hs[i] / 2) + 26;
            int nextCircleTop = (ys[i + 1] + hs[i + 1] / 2) - 26;
            drawDottedArrow(g2, iconCenterX, topCircleBottom, nextCircleTop, new Color(100, 116, 139));

            int cardCenterX = cardX + (cardW / 2);
            int topCardBottom = ys[i] + hs[i] + 5;
            int nextCardTop = ys[i + 1] - 5;
            drawDottedArrow(g2, cardCenterX, topCardBottom, nextCardTop, new Color(100, 116, 139));
        }

        // 5. Ipinta ti tunggal agpang (Level 1, 2, 3)
        for (int i = 0; i < 3; i++) {
            int levelNum = i + 1;
            int cy = ys[i] + hs[i] / 2;
            boolean isActive = (activeLevel == levelNum);
            boolean isHovered = (hoverLevel == levelNum);

            cardBounds[i].setBounds(20, ys[i], w - 40, hs[i]);

            // Numero iti kannigid (1., 2., 3.)
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            g2.setColor(new Color(148, 163, 184));
            g2.drawString(levelNum + ".", leftNumX, cy + 5);

            // Nagbukel nga Icon agraman Glow
            drawLevelCircleIcon(g2, iconCenterX, cy, levelNum, isActive);

            // Kahon ti Card iti kannawan
            drawLevelCard(g2, cardX, ys[i], cardW, hs[i], levelNum, titles[i], etas[i], isActive, isHovered);
        }

        g2.dispose();
    }

    private void drawLevelCircleIcon(Graphics2D g2, int cx, int cy, int level, boolean isActive) {
        int r = 21;
        Color mainColor;
        Color fillBg;

        if (level == 1) {
            mainColor = new Color(45, 212, 160); // Glowing Green
            fillBg = new Color(18, 62, 54);
        } else if (level == 2) {
            mainColor = new Color(217, 107, 99); // Coral Red
            fillBg = new Color(56, 31, 36);
        } else {
            mainColor = isActive ? new Color(56, 189, 248) : new Color(115, 130, 150); // Slate Gray
            fillBg = new Color(36, 45, 60);
        }

        // Outer Glow
        if (level == 1 || isActive) {
            for (int glow = 10; glow >= 2; glow -= 2) {
                g2.setColor(new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 18));
                g2.fill(new Ellipse2D.Float(cx - r - glow, cy - r - glow, (r + glow) * 2, (r + glow) * 2));
            }
        } else if (level == 2) {
            for (int glow = 6; glow >= 2; glow -= 2) {
                g2.setColor(new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 14));
                g2.fill(new Ellipse2D.Float(cx - r - glow, cy - r - glow, (r + glow) * 2, (r + glow) * 2));
            }
        }

        // Circle Fill & Border
        g2.setColor(fillBg);
        g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));

        g2.setColor(mainColor);
        g2.setStroke(new BasicStroke(1.8f));
        g2.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));

        // Vector Icons iti uneg ti nagbukel
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (level == 1) {
            // Field Staff Icon (Hardhat + Worker Shoulders)
            g2.drawArc(cx - 6, cy - 10, 12, 10, 0, 180);
            g2.drawLine(cx - 8, cy - 5, cx + 8, cy - 5);
            g2.drawArc(cx - 5, cy - 8, 10, 10, 180, 180);
            g2.drawRoundRect(cx - 9, cy + 3, 18, 8, 4, 4);
            g2.drawLine(cx - 4, cy + 3, cx - 4, cy + 11);
            g2.drawLine(cx + 4, cy + 3, cx + 4, cy + 11);
        } else if (level == 2) {
            // Supervisor + Walkie-Talkie Radio Icon
            g2.drawOval(cx - 8, cy - 9, 9, 9);
            g2.drawArc(cx - 11, cy + 1, 15, 12, 0, 180);
            g2.drawRoundRect(cx + 4, cy - 3, 6, 11, 2, 2);
            g2.drawLine(cx + 8, cy - 7, cx + 8, cy - 3);
            g2.fillOval(cx + 6, cy - 1, 2, 2);
        } else {
            // City Manager (City Hall / Dome Building Icon)
            g2.drawArc(cx - 6, cy - 7, 12, 10, 0, 180);
            g2.drawLine(cx, cy - 10, cx, cy - 7);
            g2.drawRect(cx - 9, cy - 2, 18, 10);
            g2.drawLine(cx - 5, cy - 2, cx - 5, cy + 8);
            g2.drawLine(cx, cy - 2, cx, cy + 8);
            g2.drawLine(cx + 5, cy - 2, cx + 5, cy + 8);
            g2.drawLine(cx - 11, cy + 8, cx + 11, cy + 8);
        }
    }

    private void drawLevelCard(Graphics2D g2, int x, int y, int w, int h, int level,
            String title, String eta, boolean isActive, boolean isHovered) {

        // Card Glow & Fill
        if (isActive) {
            for (int glow = 6; glow >= 2; glow -= 2) {
                g2.setColor(new Color(34, 211, 238, 18));
                g2.draw(new RoundRectangle2D.Float(x - glow / 2f, y - glow / 2f, w + glow, h + glow, 14, 14));
            }
            g2.setColor(new Color(24, 37, 54));
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, 12, 12));

            g2.setColor(new Color(45, 212, 225));
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(new RoundRectangle2D.Float(x, y, w, h, 12, 12));
        } else {
            g2.setColor(isHovered ? new Color(25, 35, 52) : new Color(21, 29, 43));
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, 12, 12));

            g2.setColor(new Color(45, 58, 77));
            g2.setStroke(new BasicStroke(1.1f));
            g2.draw(new RoundRectangle2D.Float(x, y, w, h, 12, 12));
        }

        // Card Text
        if (isActive) {
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.setColor(Color.WHITE);
            g2.drawString(title, x + 18, y + 28);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.setColor(new Color(148, 163, 184));
            g2.drawString("ETA: ", x + 18, y + 52);
            int etaLabelW = g2.getFontMetrics().stringWidth("ETA: ");
            g2.setColor(new Color(241, 245, 249));
            g2.drawString(eta, x + 18 + etaLabelW, y + 52);

            g2.setColor(new Color(148, 163, 184));
            g2.drawString("Status: ", x + 18, y + 74);
            int statusLabelW = g2.getFontMetrics().stringWidth("Status: ");
            g2.setColor(new Color(241, 245, 249));
            g2.drawString("Dispatched", x + 18 + statusLabelW, y + 74);
        } else {
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.setColor(new Color(241, 245, 249));
            int ty = y + (h / 2) + 5;
            g2.drawString(title, x + 18, ty);
        }

        // Pill Badge iti kannawan
        String badgeText;
        Color badgeBg;
        Color badgeBorder;
        Color badgeFg;

        if (isActive) {
            badgeText = "Assigned / Processing";
            badgeBg = new Color(18, 58, 48);
            badgeBorder = new Color(45, 212, 160);
            badgeFg = new Color(74, 222, 158);
        } else if (level == 2) {
            badgeText = (activeLevel > 2) ? "Escalated to Level 3" : "Pending / Escalation point";
            badgeBg = new Color(58, 30, 38);
            badgeBorder = new Color(95, 45, 55);
            badgeFg = new Color(235, 115, 115);
        } else if (level == 3) {
            badgeText = "Awaiting Level 2 Escalation";
            badgeBg = new Color(42, 50, 66);
            badgeBorder = new Color(60, 72, 92);
            badgeFg = new Color(203, 213, 225);
        } else {
            badgeText = "Passed / Escalated";
            badgeBg = new Color(42, 50, 66);
            badgeBorder = new Color(60, 72, 92);
            badgeFg = new Color(148, 163, 184);
        }

        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int pillW = fm.stringWidth(badgeText) + 24;
        int pillH = 24;
        int pillX = x + w - pillW - 14;
        int pillY = isActive ? (y + 13) : (y + (h - pillH) / 2);

        g2.setColor(badgeBg);
        g2.fill(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, pillH, pillH));

        g2.setColor(badgeBorder);
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, pillH, pillH));

        g2.setColor(badgeFg);
        int textX = pillX + 12;
        int textY = pillY + ((pillH - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(badgeText, textX, textY);
    }

    private void drawDottedArrow(Graphics2D g2, int x, int yStart, int yEnd, Color color) {
        if (yEnd <= yStart) {
            return;
        }
        Stroke oldStroke = g2.getStroke();
        g2.setColor(color);
        float[] dash = {2.0f, 4.0f};
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f));
        g2.drawLine(x, yStart, x, yEnd - 4);

        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D arrowHead = new Path2D.Float();
        arrowHead.moveTo(x - 4, yEnd - 7);
        arrowHead.lineTo(x, yEnd - 2);
        arrowHead.lineTo(x + 4, yEnd - 7);
        g2.draw(arrowHead);

        g2.setStroke(oldStroke);
    }
}