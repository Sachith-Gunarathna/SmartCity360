package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import javax.swing.*;

public class SmartActiveZones extends JPanel {

    
    private static class MapNode {
        String name;
        float x, y;
        Color color;
        boolean isPrimary;
        boolean hasPillLabel;

        MapNode(String name, float x, float y, Color color, boolean isPrimary, boolean hasPillLabel) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.color = color;
            this.isPrimary = isPrimary;
            this.hasPillLabel = hasPillLabel;
        }
    }

    private final MapNode[] nodes = {
        
        new MapNode("Alawwa City", 0.28f, 0.40f, new Color(6, 232, 255), true, true),
        new MapNode("Newton Street", 0.54f, 0.46f, new Color(6, 232, 255), true, false),
        new MapNode("Central District", 0.47f, 0.81f, new Color(6, 232, 255), true, false),
        
        
        new MapNode("", 0.485f, 0.33f, new Color(34, 197, 94), false, false), 
        new MapNode("", 0.59f, 0.30f, new Color(34, 197, 94), false, false),  
        new MapNode("", 0.765f, 0.32f, new Color(45, 212, 191), false, false), 
        new MapNode("", 0.845f, 0.47f, new Color(34, 197, 94), false, false),  
        new MapNode("", 0.77f, 0.45f, new Color(245, 158, 11), false, false), 
        new MapNode("", 0.693f, 0.555f, new Color(34, 197, 94), false, false), 
        new MapNode("", 0.468f, 0.54f, new Color(34, 197, 94), false, false),  
        new MapNode("", 0.395f, 0.63f, new Color(34, 197, 94), false, false),  
        new MapNode("", 0.306f, 0.63f, new Color(34, 197, 94), false, false),  
        new MapNode("", 0.431f, 0.772f, new Color(245, 158, 11), false, false), 
        new MapNode("", 0.832f, 0.73f, new Color(245, 158, 11), false, false),  
        new MapNode("", 0.517f, 0.632f, new Color(34, 197, 94), false, false)  
    };

    private String activityFilter = "General Activity";
    private int hoveredNodeIndex = -1;
    private int hoveredDropdown = -1; 
    private final Rectangle dropdownBounds = new Rectangle();
    private SmartTerminal boundTerminal;

    public SmartActiveZones() {
        setBackground(new Color(15, 22, 35));
        setPreferredSize(new Dimension(540, 260));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                
                if (dropdownBounds.contains(p)) {
                    toggleActivityFilter();
                }

                
                int clicked = findNodeAt(p);
                if (clicked != -1 && nodes[clicked].isPrimary) {
                    if (boundTerminal != null) {
                        boundTerminal.appendLog("ZONES", "Active Smart Zone Selected: " + nodes[clicked].name);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int newHover = findNodeAt(p);
                if (newHover != hoveredNodeIndex) {
                    hoveredNodeIndex = newHover;
                    repaint();
                }

                int newDropHover = dropdownBounds.contains(p) ? 1 : -1;
                if (newDropHover != hoveredDropdown) {
                    hoveredDropdown = newDropHover;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredNodeIndex != -1 || hoveredDropdown != -1) {
                    hoveredNodeIndex = -1;
                    hoveredDropdown = -1;
                    repaint();
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    private int findNodeAt(Point p) {
        int w = getWidth();
        int h = getHeight();
        for (int i = 0; i < nodes.length; i++) {
            int nx = (int) (nodes[i].x * w);
            int ny = (int) (nodes[i].y * h);
            if (p.distance(nx, ny) <= 14) {
                return i;
            }
        }
        return -1;
    }

    private void toggleActivityFilter() {
        if ("General Activity".equals(activityFilter)) {
            activityFilter = "Traffic Density";
        } else if ("Traffic Density".equals(activityFilter)) {
            activityFilter = "Energy Outages";
        } else {
            activityFilter = "General Activity";
        }
        if (boundTerminal != null) {
            boundTerminal.appendLog("INFO", "Zone display filter switched to: " + activityFilter);
        }
        repaint();
    }

    public void bindTerminal(SmartTerminal terminal) {
        this.boundTerminal = terminal;
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
        g2.fillRoundRect(0, 0, w, h, 14, 14);


        drawBackgroundStreets(g2, w, h);

       
        drawDropdownBadge(g2, w);

        
        paintTriangularLinkNetwork(g2, w, h);

       
        drawAllOverlayNodes(g2, w, h);

       
        drawLegendAndRoutingOverlay(g2, w, h);

        
        g2.setColor(new Color(28, 39, 56));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

        g2.dispose();
    }

    private void drawBackgroundStreets(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(23, 29, 42));
        g2.setStroke(new BasicStroke(4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        
        float[][] diagonalLanes = {
            {0.05f, 0.15f, 0.40f, 0.55f},
            {-0.02f, 0.40f, 0.28f, 0.85f},
            {0.25f, 0.05f, 0.75f, 0.70f},
            {0.48f, 0.12f, 0.95f, 0.65f},
            {0.15f, 0.80f, 0.55f, 0.96f},
            {0.68f, 0.45f, 0.98f, 0.75f},
            {0.08f, 0.32f, 0.96f, 0.15f},
            {0.12f, 0.58f, 0.92f, 0.38f},
            {0.32f, 0.90f, 0.98f, 0.50f}
        };

        for (float[] lane : diagonalLanes) {
            g2.draw(new Line2D.Float(lane[0] * w, lane[1] * h, lane[2] * w, lane[3] * h));
        }

        
        g2.setColor(new Color(18, 23, 34));
        g2.setStroke(new BasicStroke(1.8f));
        for (float[] lane : diagonalLanes) {
            g2.draw(new Line2D.Float(lane[0] * w, lane[1] * h, lane[2] * w, lane[3] * h));
        }
    }

    private void drawDropdownBadge(Graphics2D g2, int w) {
        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        int bW = fm.stringWidth(activityFilter) + 34;
        int bH = 24;
        int bX = w - bW - 14;
        int bY = 10;

        dropdownBounds.setBounds(bX, bY, bW, bH);

        g2.setColor(hoveredDropdown == 1 ? new Color(28, 38, 56) : new Color(21, 29, 43));
        g2.fill(new RoundRectangle2D.Float(bX, bY, bW, bH, 8, 8));

        g2.setColor(new Color(45, 58, 77));
        g2.setStroke(new BasicStroke(1.1f));
        g2.draw(new RoundRectangle2D.Float(bX, bY, bW, bH, 8, 8));

        g2.setColor(new Color(165, 180, 200));
        int textY = bY + ((bH - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(activityFilter, bX + 11, textY);

        
        int cx = bX + bW - 15;
        int cy = bY + bH / 2 - 1;
        g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(cx - 3, cy - 1, cx, cy + 2);
        g2.drawLine(cx, cy + 2, cx + 3, cy - 1);
    }

    private void paintTriangularLinkNetwork(Graphics2D g2, int w, int h) {
        int x1 = (int) (nodes[0].x * w);
        int y1 = (int) (nodes[0].y * h);
        int x2 = (int) (nodes[1].x * w);
        int y2 = (int) (nodes[1].y * h);
        int x3 = (int) (nodes[2].x * w);
        int y3 = (int) (nodes[2].y * h);

        Path2D.Float triangle = new Path2D.Float();
        triangle.moveTo(x1, y1);
        triangle.lineTo(x2, y2);
        triangle.lineTo(x3, y3);
        triangle.closePath();

        
        g2.setColor(new Color(6, 232, 255, 30));
        g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(triangle);

        g2.setColor(new Color(6, 232, 255, 60));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(triangle);

        
        g2.setColor(new Color(14, 165, 233));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(triangle);
    }

    private void drawAllOverlayNodes(Graphics2D g2, int w, int h) {
        g2.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < nodes.length; i++) {
            MapNode n = nodes[i];
            int cx = (int) (n.x * w);
            int cy = (int) (n.y * h);
            boolean isHovered = (i == hoveredNodeIndex);

            int r = isHovered ? 6 : 5;

            
            if (n.isPrimary) {
                g2.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), 45));
                g2.fill(new Ellipse2D.Float(cx - r - 6, cy - r - 6, (r + 6) * 2, (r + 6) * 2));
                
                g2.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), 90));
                g2.fill(new Ellipse2D.Float(cx - r - 3, cy - r - 3, (r + 3) * 2, (r + 3) * 2));

                g2.setColor(n.color);
                g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
            } else {
                g2.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), 70));
                g2.fill(new Ellipse2D.Float(cx - r - 2, cy - r - 2, (r + 2) * 2, (r + 2) * 2));
                
                g2.setColor(n.color);
                g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
            }

            
            if (n.isPrimary) {
                if (n.hasPillLabel) {
                   
                    int bW = fm.stringWidth(n.name) + 20;
                    int bH = 22;
                    int bX = cx - bW / 2;
                    int bY = cy - bH - 6;

                    g2.setColor(new Color(24, 34, 48, 220));
                    g2.fill(new RoundRectangle2D.Float(bX, bY, bW, bH, 6, 6));
                    g2.setColor(new Color(51, 65, 85, 180));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.draw(new RoundRectangle2D.Float(bX, bY, bW, bH, 6, 6));

                    g2.setColor(new Color(226, 232, 240));
                    g2.drawString(n.name, bX + 10, bY + 15);
                } else {
                    
                    g2.setColor(new Color(15, 23, 42, 200));
                    g2.drawString(n.name, cx + 11, cy + 5);
                    g2.drawString(n.name, cx + 9, cy + 5);
                    g2.drawString(n.name, cx + 10, cy + 4);
                    g2.drawString(n.name, cx + 10, cy + 6);

                    g2.setColor(new Color(226, 232, 240));
                    g2.drawString(n.name, cx + 10, cy + 5);
                }
            }
        }
    }

    private void drawLegendAndRoutingOverlay(Graphics2D g2, int w, int h) {
        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();

        
        int ly = h - 22;
        g2.setColor(new Color(148, 163, 184));
        g2.drawString("Stati:", 14, ly + 4);

        int dotsStartX = 14 + fm.stringWidth("Stati: ");
        Color[] statiColors = {
            new Color(6, 182, 212),  
            new Color(34, 197, 94),  
            new Color(245, 158, 11), 
            new Color(71, 85, 105)   
        };

        for (int i = 0; i < statiColors.length; i++) {
            g2.setColor(statiColors[i]);
            g2.fillOval(dotsStartX + i * 16, ly - 4, 8, 8);
        }

        
        int progressW = 54;
        int progressH = 10;
        int rX = w - progressW - 14;
        int rY = ly - 4;

        g2.setColor(new Color(148, 163, 184));
        g2.drawString("Routing", rX - fm.stringWidth("Routing") - 11, ly + 4);

        g2.setColor(new Color(28, 37, 52));
        g2.fillRoundRect(rX, rY, progressW, progressH, progressH, progressH);

        
        g2.setColor(new Color(6, 182, 212));
        g2.fillRoundRect(rX, rY, (int) (progressW * 0.70f), progressH, progressH, progressH);
    }
}