package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class SmartDispatchStatus extends JPanel {

    
    private final String[] rowTitles = {
        "Level 1: Dispatch Queue",
        "Level 2: Dispatch Queue",
        "Level 3: Dispatch Queue"
    };

    private final String[] badgeTexts = {
        "Status",
        "Concluded",
        "Damaged"
    };

    private final int[] badgeThemes = {0, 1, 2};
    private int hoveredRow = -1;
    private final Rectangle[] rowBounds = new Rectangle[3];
    private SmartTerminal boundTerminal;

    public SmartDispatchStatus() {
        setBackground(new Color(16, 23, 36));
        setPreferredSize(new Dimension(520, 175));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);

        for (int i = 0; i < 3; i++) {
            rowBounds[i] = new Rectangle();
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < 3; i++) {
                    if (rowBounds[i].contains(e.getPoint())) {
                        cycleRowStatus(i);
                        break;
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int newHover = -1;
                for (int i = 0; i < 3; i++) {
                    if (rowBounds[i].contains(e.getPoint())) {
                        newHover = i;
                        break;
                    }
                }
                if (newHover != hoveredRow) {
                    hoveredRow = newHover;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredRow != -1) {
                    hoveredRow = -1;
                    repaint();
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    private void cycleRowStatus(int rowIndex) {
        badgeThemes[rowIndex] = (badgeThemes[rowIndex] + 1) % 3;
        if (badgeThemes[rowIndex] == 0) {
            badgeTexts[rowIndex] = "Status";
        } else if (badgeThemes[rowIndex] == 1) {
            badgeTexts[rowIndex] = "Concluded";
        } else {
            badgeTexts[rowIndex] = "Damaged";
        }

        if (boundTerminal != null) {
            String logType = (badgeThemes[rowIndex] == 2) ? "WARNING" : "INFO";
            boundTerminal.appendLog(logType, rowTitles[rowIndex] + " state updated to -> [" + badgeTexts[rowIndex] + "]");
        }
        repaint();
    }

    public void bindTerminal(SmartTerminal terminal) {
        this.boundTerminal = terminal;
    }

  
    public void setQueueStatus(int level, String text, int theme) {
        if (level < 1 || level > 3) {
            return;
        }
        int idx = level - 1;
        badgeTexts[idx] = text;
        badgeThemes[idx] = Math.max(0, Math.min(2, theme));
        repaint();
    }

    
    public String getLevel1BadgeText() {
        return badgeTexts[0];
    }

    public void setLevel1BadgeText(String text) {
        this.badgeTexts[0] = text;
        repaint();
    }

    public String getLevel2BadgeText() {
        return badgeTexts[1];
    }

    public void setLevel2BadgeText(String text) {
        this.badgeTexts[1] = text;
        repaint();
    }

    public String getLevel3BadgeText() {
        return badgeTexts[2];
    }

    public void setLevel3BadgeText(String text) {
        this.badgeTexts[2] = text;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        
        g2.setColor(getBackground());
        g2.fillRect(0, 0, w, h);

        int padX = 14;
        int rowH = h / 3;

        for (int i = 0; i < 3; i++) {
            int rowY = i * rowH;
            rowBounds[i].setBounds(0, rowY, w, rowH);

            
            if (hoveredRow == i) {
                g2.setColor(new Color(24, 34, 50));
                g2.fillRoundRect(4, rowY + 3, w - 8, rowH - 6, 10, 10);
            }

            
            g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 15));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(new Color(235, 240, 248));
            int textY = rowY + ((rowH - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(rowTitles[i], padX, textY);

            
            drawStatusPill(g2, w - padX, rowY + (rowH / 2), badgeTexts[i], badgeThemes[i]);

            
            if (i < 2) {
                int lineY = rowY + rowH;
                g2.setColor(new Color(33, 45, 64));
                g2.setStroke(new BasicStroke(1.1f));
                g2.drawLine(padX, lineY, w - padX, lineY);
            }
        }

        g2.dispose();
    }

    private void drawStatusPill(Graphics2D g2, int rightEdgeX, int centerY, String text, int theme) {
        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 13));
        FontMetrics fm = g2.getFontMetrics();

        boolean hasDot = (theme == 0);
        int dotSpace = hasDot ? 15 : 0;
        int textW = fm.stringWidth(text);

        int pillW = Math.max(92, textW + dotSpace + 30);
        int pillH = 28;
        int pillX = rightEdgeX - pillW;
        int pillY = centerY - (pillH / 2);

        Color bgColor;
        Color borderColor;
        Color textColor;

        if (theme == 0 || theme == 1) {
            
            bgColor = new Color(18, 60, 50);
            borderColor = new Color(45, 198, 148);
            textColor = new Color(74, 222, 158);
        } else {
            
            bgColor = new Color(58, 46, 34);
            borderColor = new Color(185, 138, 78);
            textColor = new Color(238, 182, 108);
        }

       
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, pillH, pillH));

       
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, pillH, pillH));

        
        int contentTotalW = dotSpace + textW;
        int startX = pillX + (pillW - contentTotalW) / 2;

        if (hasDot) {
            int dotR = 8;
            int dotY = centerY - (dotR / 2);
            g2.setColor(new Color(45, 225, 160));
            g2.fill(new Ellipse2D.Float(startX, dotY, dotR, dotR));
        }

        g2.setColor(textColor);
        int textX = startX + dotSpace;
        int textY = pillY + ((pillH - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, textX, textY);
    }
}