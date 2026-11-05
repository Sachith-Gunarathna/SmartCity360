package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import javax.swing.*;

public class SmartPowerChart extends JPanel {

   
    private float[] anchorX = {0.00f, 0.14f, 0.35f, 0.46f, 0.605f, 0.75f, 0.845f, 1.00f};
    private float[] anchorKW = {100f, 50f, 236f, 190f, 345f, 222f, 258f, 86f};

    private String primaryPeakLabel = "345 kW";
    private String secondaryPeakLabel = "345 kW";
    private String timelineMode = "Peak timeline";
    private boolean showTimelineDropdown = true;

    private int hoveredPeak = -1; 
    private final Rectangle primaryBadgeBounds = new Rectangle();
    private final Rectangle secondaryBadgeBounds = new Rectangle();
    private final Rectangle timelinePillBounds = new Rectangle();

    private SmartTerminal boundTerminal;

    public SmartPowerChart() {
        setBackground(new Color(16, 23, 36));
        setPreferredSize(new Dimension(540, 250));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (showTimelineDropdown && timelinePillBounds.contains(p)) {
                    toggleTimelineMode();
                } else if (primaryBadgeBounds.contains(p)) {
                    if (boundTerminal != null) {
                        boundTerminal.appendLog("METRIC", "Primary Peak inspected at 14:30h -> Load: " + primaryPeakLabel);
                    }
                } else if (secondaryBadgeBounds.contains(p)) {
                    if (boundTerminal != null) {
                        boundTerminal.appendLog("METRIC", "Evening Peak inspected at 20:15h -> Load: " + secondaryPeakLabel);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int newHover = -1;
                if (primaryBadgeBounds.contains(p)) {
                    newHover = 0;
                } else if (secondaryBadgeBounds.contains(p)) {
                    newHover = 1;
                } else if (showTimelineDropdown && timelinePillBounds.contains(p)) {
                    newHover = 2;
                }
                if (newHover != hoveredPeak) {
                    hoveredPeak = newHover;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredPeak != -1) {
                    hoveredPeak = -1;
                    repaint();
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    private void toggleTimelineMode() {
        if ("Peak timeline".equals(timelineMode)) {
            timelineMode = "Average load";
            anchorKW = new float[]{90f, 60f, 210f, 175f, 310f, 200f, 240f, 80f};
            primaryPeakLabel = "310 kW";
            secondaryPeakLabel = "240 kW";
        } else {
            timelineMode = "Peak timeline";
            anchorKW = new float[]{100f, 50f, 236f, 190f, 345f, 222f, 258f, 86f};
            primaryPeakLabel = "345 kW";
            secondaryPeakLabel = "345 kW";
        }
        if (boundTerminal != null) {
            boundTerminal.appendLog("INFO", "Power chart timeline view switched to: " + timelineMode);
        }
        repaint();
    }

    public void bindTerminal(SmartTerminal terminal) {
        this.boundTerminal = terminal;
    }

    public void setPeakLoadKW(int kw) {
        this.anchorKW[4] = Math.max(50, Math.min(400, kw));
        this.primaryPeakLabel = kw + " kW";
        repaint();
    }

    public boolean isShowTimelineDropdown() {
        return showTimelineDropdown;
    }

    public void setShowTimelineDropdown(boolean showTimelineDropdown) {
        this.showTimelineDropdown = showTimelineDropdown;
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
        g2.fillRoundRect(0, 0, w, h, 12, 12);

       
        int topOffset = showTimelineDropdown ? 42 : 24;
        if (showTimelineDropdown) {
            drawTimelineDropdownPill(g2, w);
        }

        
        int leftPad = 46;
        int rightPad = 24;
        int bottomPad = 34;
        int chartX = leftPad;
        int chartY = topOffset;
        int chartW = Math.max(100, w - leftPad - rightPad);
        int chartH = Math.max(80, h - chartY - bottomPad);

        
        drawGridAndAxes(g2, chartX, chartY, chartW, chartH);

        
        int n = anchorX.length;
        float[] px = new float[n];
        float[] py = new float[n];
        for (int i = 0; i < n; i++) {
            px[i] = chartX + anchorX[i] * chartW;
            py[i] = chartY + chartH - (anchorKW[i] / 400f) * chartH;
        }

        
        Path2D.Float wavePath = buildSmoothWavePath(px, py);

        
        Path2D.Float areaPath = (Path2D.Float) wavePath.clone();
        areaPath.lineTo(px[n - 1], chartY + chartH);
        areaPath.lineTo(px[0], chartY + chartH);
        areaPath.closePath();

        
        paintAreaUnderCurve(g2, areaPath, chartX, chartY, chartW, chartH, px[4], py[4]);

        
        drawVerticalDashedLine(g2, (int) px[4], (int) py[4] + 6, chartY + chartH);

        
        paintGlowingWaveLine(g2, wavePath, chartX, chartW);

        
        drawPeakNodeAndCallout(g2, (int) px[4], (int) py[4], primaryPeakLabel, true, hoveredPeak == 0, primaryBadgeBounds);
        drawPeakNodeAndCallout(g2, (int) px[6], (int) py[6], secondaryPeakLabel, false, hoveredPeak == 1, secondaryBadgeBounds);

        g2.dispose();
    }

    private void drawTimelineDropdownPill(Graphics2D g2, int w) {
        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        int pillW = fm.stringWidth(timelineMode) + 34;
        int pillH = 26;
        int pillX = w - pillW - 14;
        int pillY = 8;

        timelinePillBounds.setBounds(pillX, pillY, pillW, pillH);

        g2.setColor(hoveredPeak == 2 ? new Color(28, 39, 56) : new Color(21, 30, 44));
        g2.fill(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, 8, 8));

        g2.setColor(new Color(45, 58, 78));
        g2.setStroke(new BasicStroke(1.1f));
        g2.draw(new RoundRectangle2D.Float(pillX, pillY, pillW, pillH, 8, 8));

        g2.setColor(new Color(165, 180, 200));
        int textY = pillY + ((pillH - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(timelineMode, pillX + 11, textY);

        
        int cx = pillX + pillW - 15;
        int cy = pillY + pillH / 2 - 1;
        g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(cx - 3, cy - 1, cx, cy + 2);
        g2.drawLine(cx, cy + 2, cx + 3, cy - 1);
    }

    private void drawGridAndAxes(Graphics2D g2, int chartX, int chartY, int chartW, int chartH) {
        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();

        int[] yValues = {400, 300, 200, 100, 0};
        float[] dashPattern = {4.0f, 4.0f};
        BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);

        for (int val : yValues) {
            int y = chartY + chartH - (int) ((val / 400.0) * chartH);

            
            String lbl = String.valueOf(val);
            g2.setColor(new Color(120, 136, 158));
            g2.drawString(lbl, chartX - fm.stringWidth(lbl) - 12, y + 4);

            
            if (val == 0) {
                g2.setColor(new Color(45, 58, 78));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawLine(chartX, y, chartX + chartW, y);
            } else {
                g2.setColor(new Color(34, 46, 64));
                g2.setStroke(dashedStroke);
                g2.drawLine(chartX, y, chartX + chartW, y);
            }
        }

        
        String[] xLabels = {"00h", "04h", "08h", "12h", "16h", "20h", "24h"};
        g2.setColor(new Color(130, 146, 168));
        for (int i = 0; i < xLabels.length; i++) {
            float ratio = i / (float) (xLabels.length - 1);
            int x = chartX + (int) (ratio * chartW);
            int lw = fm.stringWidth(xLabels[i]);
            g2.drawString(xLabels[i], x - lw / 2, chartY + chartH + 22);
        }
    }

    private Path2D.Float buildSmoothWavePath(float[] px, float[] py) {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(px[0], py[0]);

        for (int i = 0; i < px.length - 1; i++) {
            float x0 = px[i];
            float y0 = py[i];
            float x1 = px[i + 1];
            float y1 = py[i + 1];
            float dx = x1 - x0;

            if (i == 0) {
                
                path.curveTo(x0 + dx * 0.35f, y0 + (y1 - y0) * 0.45f, x1 - dx * 0.45f, y1, x1, y1);
            } else if (i == px.length - 2) {
                
                path.curveTo(x0 + dx * 0.45f, y0, x1 - dx * 0.25f, y0 + (y1 - y0) * 0.55f, x1, y1);
            } else {
                
                path.curveTo(x0 + dx * 0.46f, y0, x1 - dx * 0.46f, y1, x1, y1);
            }
        }
        return path;
    }

    private void paintAreaUnderCurve(Graphics2D g2, Path2D.Float areaPath, int chartX, int chartY, int chartW, int chartH, float peakX, float peakY) {
        Shape oldClip = g2.getClip();
        g2.clip(areaPath);

        
        LinearGradientPaint baseVertical = new LinearGradientPaint(
                0, chartY, 0, chartY + chartH,
                new float[]{0.0f, 0.55f, 1.0f},
                new Color[]{
                    new Color(6, 215, 245, 135),
                    new Color(14, 116, 205, 80),
                    new Color(15, 23, 42, 10)
                }
        );
        g2.setPaint(baseVertical);
        g2.fillRect(chartX, chartY, chartW, chartH);

        
        float radius = Math.max(90f, chartH * 0.95f);
        RadialGradientPaint peakGlow = new RadialGradientPaint(
                new Point2D.Float(peakX, peakY + 25),
                radius,
                new float[]{0.0f, 0.6f, 1.0f},
                new Color[]{
                    new Color(34, 245, 255, 150),
                    new Color(14, 165, 233, 60),
                    new Color(15, 23, 42, 0)
                }
        );
        g2.setPaint(peakGlow);
        g2.fillRect(chartX, chartY, chartW, chartH);

        g2.setClip(oldClip);
    }

    private void drawVerticalDashedLine(Graphics2D g2, int x, int yStart, int yEnd) {
        float[] dash = {3.5f, 4.5f};
        g2.setColor(new Color(125, 211, 252, 120));
        g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        g2.drawLine(x, yStart, x, yEnd);
    }

    private void paintGlowingWaveLine(Graphics2D g2, Path2D.Float wavePath, int chartX, int chartW) {
       
        g2.setColor(new Color(6, 232, 255, 35));
        g2.setStroke(new BasicStroke(7.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(wavePath);

        g2.setColor(new Color(6, 232, 255, 65));
        g2.setStroke(new BasicStroke(4.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(wavePath);

        
        LinearGradientPaint lineGradient = new LinearGradientPaint(
                chartX, 0, chartX + chartW, 0,
                new float[]{0.0f, 0.58f, 1.0f},
                new Color[]{
                    new Color(45, 212, 245),
                    new Color(80, 250, 255),
                    new Color(29, 155, 240)
                }
        );
        g2.setPaint(lineGradient);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(wavePath);
    }

    private void drawPeakNodeAndCallout(Graphics2D g2, int cx, int cy, String label, boolean isPrimary, boolean isHovered, Rectangle boundsOut) {
        
        int r = isHovered ? 6 : 5;
        if (isPrimary) {
            g2.setColor(new Color(6, 240, 255, 80));
            g2.fill(new Ellipse2D.Float(cx - r - 4, cy - r - 4, (r + 4) * 2, (r + 4) * 2));

            g2.setColor(new Color(20, 184, 212));
            g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));

            g2.setColor(new Color(224, 252, 255));
            g2.setStroke(new BasicStroke(2.0f));
            g2.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        } else {
            g2.setColor(new Color(29, 155, 240, 65));
            g2.fill(new Ellipse2D.Float(cx - r - 3, cy - r - 3, (r + 3) * 2, (r + 3) * 2));

            g2.setColor(new Color(24, 90, 165));
            g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));

            g2.setColor(new Color(96, 185, 250));
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        }

        
        g2.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int boxW = fm.stringWidth(label) + 22;
        int boxH = 26;
        int boxX = cx - boxW / 2;
        int boxY = cy - boxH - 13;

        boundsOut.setBounds(boxX, boxY, boxW, boxH + 15);

        
        g2.setColor(isHovered ? new Color(30, 42, 60, 245) : new Color(22, 31, 45, 235));
        g2.fill(new RoundRectangle2D.Float(boxX, boxY, boxW, boxH, 8, 8));

        g2.setColor(isPrimary ? new Color(65, 90, 120) : new Color(52, 70, 95));
        g2.setStroke(new BasicStroke(1.1f));
        g2.draw(new RoundRectangle2D.Float(boxX, boxY, boxW, boxH, 8, 8));

        
        Path2D.Float pointer = new Path2D.Float();
        pointer.moveTo(cx - 4, boxY + boxH);
        pointer.lineTo(cx, boxY + boxH + 4);
        pointer.lineTo(cx + 4, boxY + boxH);
        pointer.closePath();
        g2.setColor(isHovered ? new Color(30, 42, 60, 245) : new Color(22, 31, 45, 235));
        g2.fill(pointer);
        g2.setColor(isPrimary ? new Color(65, 90, 120) : new Color(52, 70, 95));
        g2.drawLine(cx - 4, boxY + boxH, cx, boxY + boxH + 4);
        g2.drawLine(cx, boxY + boxH + 4, cx + 4, boxY + boxH);

        
        g2.setColor(new Color(241, 245, 249));
        int textX = boxX + (boxW - fm.stringWidth(label)) / 2;
        int textY = boxY + ((boxH - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(label, textX, textY);
    }
}