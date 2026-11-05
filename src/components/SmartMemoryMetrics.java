package components;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;

public class SmartMemoryMetrics extends JPanel {

    private int totalLights = 1540;
    private int beforeRamMB = 312;
    private int withFlyweightRamMB = 24;
    private double savedPercentage = 92.3;
    private String intrinsicTypes = "2 (LED, Compact)";
    private String memoryPerInstance = "0.15 KB";

    public SmartMemoryMetrics() {
        setBackground(new Color(18, 25, 38));
        setPreferredSize(new Dimension(460, 345));
        setOpaque(true);
    }

    
    public void addDeployedLights(int count) {
        this.totalLights += count;
        this.beforeRamMB = Math.max(1, (int) Math.round(totalLights * 0.2026));
        this.withFlyweightRamMB = Math.max(1, (int) Math.round(totalLights * 0.0155));
        this.savedPercentage = Math.round(((double) (beforeRamMB - withFlyweightRamMB) / beforeRamMB) * 1000.0) / 10.0;
        repaint();
    }

    public void setTotalLights(int totalLights) {
        this.totalLights = totalLights;
        addDeployedLights(0);
    }

    public int getTotalLights() {
        return totalLights;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padX = 16;
        int contentW = w - (padX * 2);

        
        g2.setColor(getBackground());
        g2.fillRect(0, 0, w, h);

        
        drawTopComparisonHeader(g2, w, padX, 22);

        
        int bar1Y = 64;
        int barH = 42;
        drawBeforeFlyweightBar(g2, padX, bar1Y, contentW, barH);

        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 14));
        g2.setColor(new Color(203, 185, 190));
        g2.drawString(beforeRamMB + " MB RAM Used", padX + 2, bar1Y + barH + 22);

        
        int bar2Y = 144;
        drawWithFlyweightBar(g2, padX, bar2Y, contentW, barH);

        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 14));
        g2.setColor(new Color(165, 205, 215));
        g2.drawString(withFlyweightRamMB + " MB RAM Used", padX + 2, bar2Y + barH + 22);

        
        int cardY = 224;
        int cardH = Math.max(106, h - cardY - 12);
        drawBottomMetricsCard(g2, padX, cardY, contentW, cardH);

        g2.dispose();
    }

    private void drawTopComparisonHeader(Graphics2D g2, int w, int padX, int topY) {
        int leftCenterX = padX + (w / 2 - padX) / 2 - 10;
        int rightCenterX = w / 2 + (w / 2 - padX) / 2 + 10;

        Font titleFont = new Font("Franklin Gothic Medium", Font.PLAIN, 16);
        Font subFont = new Font("Franklin Gothic Medium", Font.PLAIN, 13);

        
        g2.setFont(titleFont);
        g2.setColor(new Color(241, 245, 249));
        drawCenteredString(g2, "Before Flyweight", leftCenterX, topY);

        g2.setFont(subFont);
        g2.setColor(new Color(148, 163, 184));
        drawCenteredString(g2, "(Total Extrinsic Instances)", leftCenterX, topY + 20);

        
        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 16));
        g2.setColor(new Color(226, 232, 240));
        drawCenteredString(g2, "VS.", w / 2, topY + 11);

        
        g2.setFont(titleFont);
        g2.setColor(new Color(241, 245, 249));
        drawCenteredString(g2, "With Flyweight", rightCenterX, topY);

        g2.setFont(subFont);
        g2.setColor(new Color(148, 163, 184));
        drawCenteredString(g2, "(Intrinsic Shared Objects)", rightCenterX, topY + 20);
    }

    private void drawBeforeFlyweightBar(Graphics2D g2, int x, int y, int w, int h) {
        int arc = 14;

        
        for (int i = 5; i >= 1; i--) {
            g2.setColor(new Color(217, 119, 60, 10));
            g2.fill(new RoundRectangle2D.Float(x - i, y - i, w + (i * 2), h + (i * 2), arc + i, arc + i));
        }

        
        g2.setColor(new Color(48, 33, 39));
        g2.fill(new RoundRectangle2D.Float(x, y, w, h, arc, arc));

        
        int pad = 5;
        int fillW = (int) ((w - pad * 2) * 0.89f);
        int fillH = h - (pad * 2);

        GradientPaint redToGold = new GradientPaint(
                x + pad, y, new Color(190, 52, 68),
                x + pad + fillW, y, new Color(212, 165, 38)
        );
        g2.setPaint(redToGold);
        g2.fill(new RoundRectangle2D.Float(x + pad, y + pad, fillW, fillH, 10, 10));

        
        g2.setColor(new Color(168, 92, 78));
        g2.setStroke(new BasicStroke(1.3f));
        g2.draw(new RoundRectangle2D.Float(x, y, w, h, arc, arc));
    }

    private void drawWithFlyweightBar(Graphics2D g2, int x, int y, int w, int h) {
        int arc = 14;

        
        for (int i = 6; i >= 1; i--) {
            g2.setColor(new Color(6, 232, 255, 14));
            g2.fill(new RoundRectangle2D.Float(x - i, y - i, w + (i * 2), h + (i * 2), arc + i, arc + i));
        }

        
        GradientPaint trackBg = new GradientPaint(
                x, y, new Color(16, 48, 58),
                x + w, y, new Color(18, 38, 48)
        );
        g2.setPaint(trackBg);
        g2.fill(new RoundRectangle2D.Float(x, y, w, h, arc, arc));

        
        int pad = 5;
        int fillW = Math.max(36, (int) ((w - pad * 2) * 0.088f));
        int fillH = h - (pad * 2);

        
        for (int i = 6; i >= 1; i -= 2) {
            g2.setColor(new Color(6, 245, 255, 28));
            g2.fill(new RoundRectangle2D.Float(x + pad - 1, y + pad - 1, fillW + i * 2, fillH + 2, 10, 10));
        }

        GradientPaint cyanFill = new GradientPaint(
                x + pad, y + pad, new Color(56, 248, 255),
                x + pad + fillW, y + pad + fillH, new Color(0, 195, 225)
        );
        g2.setPaint(cyanFill);
        g2.fill(new RoundRectangle2D.Float(x + pad, y + pad, fillW, fillH, 9, 9));

        
        String savedText = savedPercentage + "% Memory Saved";
        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        int pillW = fm.stringWidth(savedText) + 24;
        int pillH = 26;
        int pillX = x + w - pillW - 10;
        int pillY = y + (h - pillH) / 2;

        g2.setColor(new Color(17, 62, 56));
        g2.fill(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, pillH, pillH));

        g2.setColor(new Color(52, 185, 150));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, pillH, pillH));

        g2.setColor(new Color(110, 231, 195));
        int tx = pillX + 12;
        int ty = pillY + ((pillH - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(savedText, tx, ty);

        
        g2.setColor(new Color(45, 212, 225));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(x, y, w, h, arc, arc));
    }

    private void drawBottomMetricsCard(Graphics2D g2, int x, int y, int w, int h) {
        int arc = 12;

        
        g2.setColor(new Color(16, 23, 36));
        g2.fill(new RoundRectangle2D.Float(x, y, w, h, arc, arc));

        g2.setColor(new Color(38, 52, 72));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(x, y, w, h, arc, arc));

        int rowStep = h / 3;
        int r1Y = y + rowStep / 2 + 5;
        int r2Y = y + rowStep + rowStep / 2 + 3;
        int r3Y = y + (rowStep * 2) + rowStep / 2 + 1;

        int iconX = x + 16;
        int labelX = x + 38;
        int rightX = x + w - 16;

        Font labelFont = new Font("Franklin Gothic Medium", Font.PLAIN, 14);
        Font valFont = new Font("Franklin Gothic Medium", Font.PLAIN, 15);
        Color labelColor = new Color(203, 213, 225);
        Color valColor = new Color(248, 250, 252);
        Color iconColor = new Color(120, 140, 165);

       
        drawBulbIcon(g2, iconX, r1Y - 11, iconColor);
        g2.setFont(labelFont);
        g2.setColor(labelColor);
        g2.drawString("Total Lights:", labelX, r1Y);

        String formattedLights = NumberFormat.getNumberInstance(Locale.US).format(totalLights);
        g2.setFont(valFont);
        g2.setColor(valColor);
        drawRightAlignedString(g2, formattedLights, rightX, r1Y);

        
        drawLayersIcon(g2, iconX, r2Y - 11, iconColor);
        g2.setFont(labelFont);
        g2.setColor(labelColor);
        g2.drawString("Intrinsic State Types:", labelX, r2Y);

        g2.setFont(valFont);
        g2.setColor(valColor);
        drawRightAlignedString(g2, intrinsicTypes, rightX, r2Y);

        
        drawChipIcon(g2, iconX, r3Y - 11, iconColor);
        g2.setFont(labelFont);
        g2.setColor(labelColor);
        g2.drawString("Memory / Instance:", labelX, r3Y);

        g2.setFont(valFont);
        g2.setColor(valColor);
        drawRightAlignedString(g2, memoryPerInstance, rightX, r3Y);
    }

    private void drawBulbIcon(Graphics2D g2, int x, int y, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(x + 3, y + 2, 8, 8);
        g2.drawLine(x + 5, y + 11, x + 9, y + 11);
        g2.drawLine(x + 6, y + 13, x + 8, y + 13);
        
        g2.drawLine(x + 7, y - 1, x + 7, y);
        g2.drawLine(x + 1, y + 6, x, y + 6);
        g2.drawLine(x + 13, y + 6, x + 14, y + 6);
    }

    private void drawLayersIcon(Graphics2D g2, int x, int y, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.3f));
        g2.draw(new Ellipse2D.Float(x + 1, y + 1, 12, 4));
        g2.drawArc(x + 1, y + 4, 12, 5, 180, 180);
        g2.drawArc(x + 1, y + 8, 12, 5, 180, 180);
    }

    private void drawChipIcon(Graphics2D g2, int x, int y, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.3f));
        g2.drawRoundRect(x + 2, y + 1, 10, 12, 2, 2);
        g2.drawRect(x + 5, y + 4, 4, 6);
    }

    private void drawCenteredString(Graphics2D g2, String text, int centerX, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, centerX - fm.stringWidth(text) / 2, y);
    }

    private void drawRightAlignedString(Graphics2D g2, String text, int rightX, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, rightX - fm.stringWidth(text), y);
    }
}