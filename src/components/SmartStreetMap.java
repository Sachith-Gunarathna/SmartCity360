package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class SmartStreetMap extends JPanel {

    
    public static class LightTypeFlyweight {
        private final String typeName;
        private final char symbol;
        private final Color glowColor;
        private final Color coreColor;
        private final int wattage;

        public LightTypeFlyweight(String typeName, char symbol, Color glowColor, Color coreColor, int wattage) {
            this.typeName = typeName;
            this.symbol = symbol;
            this.glowColor = glowColor;
            this.coreColor = coreColor;
            this.wattage = wattage;
        }

        public void renderNode(Graphics2D g2, int cx, int cy, boolean isPowered, boolean isHovered) {
            int radius = isHovered ? 11 : 9;

            if (isPowered) {
               
                for (int r = radius + 8; r >= radius + 2; r -= 2) {
                    int alpha = (r == radius + 2) ? 65 : 25;
                    g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), alpha));
                    g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
                }

                
                g2.setColor(coreColor);
                g2.fill(new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2));

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2));
            } else {
                
                g2.setColor(new Color(30, 41, 59));
                g2.fill(new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2));
                g2.setColor(new Color(100, 116, 139));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2));
            }

            
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            FontMetrics fm = g2.getFontMetrics();
            String s = String.valueOf(symbol);
            int tx = cx - fm.stringWidth(s) / 2;
            int ty = cy + (fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(isPowered ? new Color(10, 20, 35) : new Color(148, 163, 184));
            g2.drawString(s, tx, ty);
        }

        public String getTypeName() {
            return typeName;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    
    public static class LightFlyweightFactory {
        private static final Map<String, LightTypeFlyweight> CACHE = new HashMap<>();

        public static LightTypeFlyweight getLightType(String key) {
            if (!CACHE.containsKey(key)) {
                if ("LED".equalsIgnoreCase(key)) {
                    CACHE.put("LED", new LightTypeFlyweight(
                            "LED_150W_Cyan", 'L',
                            new Color(6, 232, 255), new Color(34, 211, 238), 150
                    ));
                } else {
                    CACHE.put("COMPACT", new LightTypeFlyweight(
                            "COMP_60W_Amber", 'C',
                            new Color(250, 204, 21), new Color(251, 191, 36), 60
                    ));
                }
            }
            return CACHE.get(key.toUpperCase());
        }

        public static int getTotalSharedObjects() {
            return CACHE.size();
        }
    }

    
    public static class StreetLightNode {
        public final float normX;
        public final float normY;
        public boolean active = true;
        public final LightTypeFlyweight flyweight;

        public StreetLightNode(float normX, float normY, LightTypeFlyweight flyweight) {
            this.normX = normX;
            this.normY = normY;
            this.flyweight = flyweight;
        }
    }

    private final List<StreetLightNode> nodes = new ArrayList<>();
    private boolean masterOptionOn = true;
    private boolean showCompact = true;
    private boolean showLed = true;
    private int hoveredNodeIndex = -1;
    private SmartTerminal boundTerminal;

    private final Rectangle switchBounds = new Rectangle();
    private final Rectangle compactRowBounds = new Rectangle();
    private final Rectangle ledRowBounds = new Rectangle();
    private final Random random = new Random(42);

    public SmartStreetMap() {
        setBackground(new Color(12, 18, 28));
        setPreferredSize(new Dimension(500, 480));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setupInitialNodes();
        setupInteractions();
    }

    private void setupInitialNodes() {
        nodes.clear();
        LightTypeFlyweight led = LightFlyweightFactory.getLightType("LED");
        LightTypeFlyweight compact = LightFlyweightFactory.getLightType("COMPACT");

        
        float[][] ledCoords = {
            {0.45f, 0.11f}, {0.36f, 0.12f}, {0.29f, 0.21f}, {0.49f, 0.15f}, {0.44f, 0.21f},
            {0.54f, 0.21f}, {0.39f, 0.25f}, {0.37f, 0.28f}, {0.33f, 0.30f}, {0.61f, 0.29f},
            {0.66f, 0.36f}, {0.77f, 0.31f}, {0.86f, 0.29f}, {0.94f, 0.27f}, {0.43f, 0.39f},
            {0.60f, 0.41f}, {0.68f, 0.39f}, {0.72f, 0.43f}, {0.78f, 0.48f}, {0.83f, 0.52f},
            {0.18f, 0.48f}, {0.23f, 0.44f}, {0.21f, 0.42f}, {0.24f, 0.57f}, {0.45f, 0.55f},
            {0.40f, 0.60f}, {0.32f, 0.69f}, {0.49f, 0.65f}, {0.62f, 0.63f}, {0.67f, 0.59f},
            {0.71f, 0.56f}, {0.77f, 0.59f}, {0.81f, 0.62f}, {0.84f, 0.72f}, {0.44f, 0.77f},
            {0.51f, 0.76f}, {0.38f, 0.85f}, {0.50f, 0.85f}
        };

        float[][] compactCoords = {
            {0.40f, 0.12f}, {0.34f, 0.15f}, {0.23f, 0.27f}, {0.16f, 0.33f}, {0.09f, 0.42f},
            {0.06f, 0.50f}, {0.26f, 0.39f}, {0.29f, 0.51f}, {0.52f, 0.28f}, {0.48f, 0.33f},
            {0.57f, 0.57f}, {0.26f, 0.63f}, {0.28f, 0.74f}, {0.39f, 0.73f}, {0.55f, 0.71f},
            {0.23f, 0.81f}, {0.18f, 0.88f}, {0.13f, 0.95f}
        };

        for (float[] pt : ledCoords) {
            nodes.add(new StreetLightNode(pt[0], pt[1], led));
        }
        for (float[] pt : compactCoords) {
            nodes.add(new StreetLightNode(pt[0], pt[1], compact));
        }
    }

    private void setupInteractions() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();

                
                if (switchBounds.contains(p)) {
                    masterOptionOn = !masterOptionOn;
                    if (boundTerminal != null) {
                        boundTerminal.appendLog(masterOptionOn ? "INFO" : "WARNING",
                                "Street Light Grid Master Option switched " + (masterOptionOn ? "ON" : "OFF"));
                    }
                    repaint();
                    return;
                }

                
                if (compactRowBounds.contains(p)) {
                    showCompact = !showCompact;
                    if (boundTerminal != null) {
                        boundTerminal.appendLog("FLYWEIGHT", "Compact-bulb ('C') visibility: " + (showCompact ? "VISIBLE" : "HIDDEN"));
                    }
                    repaint();
                    return;
                }

                
                if (ledRowBounds.contains(p)) {
                    showLed = !showLed;
                    if (boundTerminal != null) {
                        boundTerminal.appendLog("FLYWEIGHT", "LED Flyweights ('L') visibility: " + (showLed ? "VISIBLE" : "HIDDEN"));
                    }
                    repaint();
                    return;
                }

                
                int clickedIdx = findNodeAt(p);
                if (clickedIdx != -1) {
                    StreetLightNode node = nodes.get(clickedIdx);
                    node.active = !node.active;
                    if (boundTerminal != null) {
                        String hash = "0x" + Integer.toHexString(System.identityHashCode(node.flyweight)).toUpperCase();
                        boundTerminal.appendLog("FLYWEIGHT",
                                String.format("Node (%d, %d) toggled %s | Shared Flyweight [%s @ %s]",
                                        (int) (node.normX * 1000), (int) (node.normY * 1000),
                                        node.active ? "ON" : "OFF",
                                        node.flyweight.getTypeName(), hash));
                    }
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int idx = findNodeAt(e.getPoint());
                if (idx != hoveredNodeIndex) {
                    hoveredNodeIndex = idx;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredNodeIndex != -1) {
                    hoveredNodeIndex = -1;
                    repaint();
                }
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    private int findNodeAt(Point p) {
        int w = getWidth();
        int h = getHeight();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            StreetLightNode n = nodes.get(i);
            if ((n.flyweight.getSymbol() == 'C' && !showCompact)
                    || (n.flyweight.getSymbol() == 'L' && !showLed)) {
                continue;
            }
            int nx = (int) (n.normX * w);
            int ny = (int) (n.normY * h);
            if (p.distance(nx, ny) <= 12) {
                return i;
            }
        }
        return -1;
    }

    
    public void deployNewNode(String typeKey) {
        LightTypeFlyweight sharedFlyweight = LightFlyweightFactory.getLightType(typeKey);
        float rx = 0.18f + random.nextFloat() * 0.65f;
        float ry = 0.18f + random.nextFloat() * 0.65f;
        nodes.add(new StreetLightNode(rx, ry, sharedFlyweight));

        if (boundTerminal != null) {
            String hash = "0x" + Integer.toHexString(System.identityHashCode(sharedFlyweight)).toUpperCase();
            boundTerminal.appendLog("PATTERN",
                    "Reusing Shared Intrinsic Flyweight (" + sharedFlyweight.getTypeName() + " @ " + hash + ")");
            boundTerminal.appendLog("SUCCESS",
                    "Deployed Extrinsic Node #" + nodes.size() + " at Grid(" + (int) (rx * 1000) + ", " + (int) (ry * 1000) + ")");
        }
        repaint();
    }

    public void bindTerminal(SmartTerminal terminal) {
        this.boundTerminal = terminal;
    }

    public int getTotalDeployedNodes() {
        return nodes.size();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        
        g2.setColor(new Color(14, 21, 32));
        g2.fillRoundRect(0, 0, w, h, 14, 14);

        
        drawCityMapGeometry(g2, w, h);

        
        drawNewtonStreetLabel(g2, w, h);

        
        for (int i = 0; i < nodes.size(); i++) {
            StreetLightNode node = nodes.get(i);
            if (node.flyweight.getSymbol() == 'C' && !showCompact) {
                continue;
            }
            if (node.flyweight.getSymbol() == 'L' && !showLed) {
                continue;
            }
            int cx = (int) (node.normX * w);
            int cy = (int) (node.normY * h);
            boolean isPowered = masterOptionOn && node.active;
            node.flyweight.renderNode(g2, cx, cy, isPowered, i == hoveredNodeIndex);
        }

        
        drawLocationPill(g2, 10, 14, "Alawwa City");
        drawLocationPill(g2, 8, (int) (h * 0.59f), "Central District");

        
        drawTopRightLegend(g2, w);

        
        drawBottomRightOptionCard(g2, w, h);

        
        g2.setColor(new Color(38, 52, 71));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

        g2.dispose();
    }

    private void drawCityMapGeometry(Graphics2D g2, int w, int h) {
        
        g2.setColor(new Color(22, 31, 46));
        g2.setStroke(new BasicStroke(16f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

        
        float[][] crossStreets = {
            {-0.05f, 0.45f, 0.52f, -0.05f},
            {-0.05f, 0.62f, 0.68f, -0.05f},
            {0.02f, 0.78f, 0.85f, 0.02f},
            {0.08f, 0.96f, 0.98f, 0.14f},
            {0.22f, 1.05f, 1.05f, 0.28f},
            {0.42f, 1.05f, 1.05f, 0.48f},
            {0.62f, 1.05f, 1.05f, 0.68f}
        };

        
        float[][] diagStreets = {
            {0.25f, -0.05f, 1.05f, 0.68f},
            {0.05f, -0.02f, 0.95f, 0.82f},
            {-0.05f, 0.10f, 0.85f, 0.94f},
            {-0.05f, 0.30f, 0.68f, 1.02f},
            {-0.05f, 0.52f, 0.48f, 1.05f},
            {-0.05f, 0.72f, 0.28f, 1.05f}
        };

        
        g2.setColor(new Color(33, 45, 64));
        g2.setStroke(new BasicStroke(11f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (float[] s : crossStreets) {
            g2.draw(new Line2D.Float(s[0] * w, s[1] * h, s[2] * w, s[3] * h));
        }
        for (float[] s : diagStreets) {
            g2.draw(new Line2D.Float(s[0] * w, s[1] * h, s[2] * w, s[3] * h));
        }

        
        g2.setColor(new Color(18, 26, 39));
        g2.setStroke(new BasicStroke(3.5f));
        for (float[] s : crossStreets) {
            g2.draw(new Line2D.Float(s[0] * w, s[1] * h, s[2] * w, s[3] * h));
        }
        for (float[] s : diagStreets) {
            g2.draw(new Line2D.Float(s[0] * w, s[1] * h, s[2] * w, s[3] * h));
        }

       
        g2.setColor(new Color(38, 53, 74));
        g2.setStroke(new BasicStroke(20f));
        g2.draw(new Line2D.Float(0.02f * w, 0.08f * h, 0.85f * w, 0.85f * h));

        g2.setColor(new Color(14, 165, 233, 130));
        g2.setStroke(new BasicStroke(1.6f));
        g2.draw(new Line2D.Float(0.02f * w, 0.08f * h, 0.85f * w, 0.85f * h));
    }

    private void drawNewtonStreetLabel(Graphics2D g2, int w, int h) {
        AffineTransform old = g2.getTransform();
        double angle = Math.atan2(0.77 * h, 0.83 * w);
        g2.translate(w * 0.41, h * 0.44);
        g2.rotate(angle);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.setColor(new Color(226, 232, 240));
        g2.drawString("Narammala Road", -42, -5);
        g2.setTransform(old);
    }

    private void drawLocationPill(Graphics2D g2, int x, int y, String text) {
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        FontMetrics fm = g2.getFontMetrics();
        int pw = fm.stringWidth(text) + 22;
        int ph = 28;

        g2.setColor(new Color(24, 34, 48, 220));
        g2.fill(new RoundRectangle2D.Float(x, y, pw, ph, 10, 10));
        g2.setColor(new Color(51, 65, 85, 180));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(x, y, pw, ph, 10, 10));

        g2.setColor(new Color(241, 245, 249));
        g2.drawString(text, x + 11, y + ((ph - fm.getHeight()) / 2) + fm.getAscent());
    }

    private void drawTopRightLegend(Graphics2D g2, int w) {
        int boxW = 205;
        int boxH = 66;
        int boxX = w - boxW - 10;
        int boxY = 10;

        g2.setColor(new Color(20, 29, 43, 235));
        g2.fill(new RoundRectangle2D.Float(boxX, boxY, boxW, boxH, 10, 10));
        g2.setColor(new Color(51, 65, 85));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(boxX, boxY, boxW, boxH, 10, 10));

        
        g2.setColor(new Color(6, 232, 255));
        g2.fillOval(boxX + 12, boxY + 11, 14, 14);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.setColor(new Color(226, 232, 240));
        g2.drawString("Active Node (LED)", boxX + 34, boxY + 23);

        
        g2.setColor(new Color(250, 204, 21));
        g2.fillOval(boxX + 12, boxY + 38, 14, 14);
        g2.setColor(new Color(226, 232, 240));
        g2.drawString("Active Node (Compact)", boxX + 34, boxY + 50);
    }

    private void drawBottomRightOptionCard(Graphics2D g2, int w, int h) {
        int cardW = 190;
        int cardH = 108;
        int cardX = w - cardW - 14;
        int cardY = h - cardH - 14;

        g2.setColor(new Color(20, 29, 43, 240));
        g2.fill(new RoundRectangle2D.Float(cardX, cardY, cardW, cardH, 10, 10));
        g2.setColor(new Color(51, 65, 85));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(cardX, cardY, cardW, cardH, 10, 10));

        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.setColor(new Color(241, 245, 249));
        g2.drawString("Option", cardX + 12, cardY + 25);

        int swW = 36, swH = 18;
        int swX = cardX + cardW - swW - 12;
        int swY = cardY + 10;
        switchBounds.setBounds(swX, swY, swW, swH);

        g2.setColor(masterOptionOn ? new Color(30, 85, 65) : new Color(45, 55, 72));
        g2.fillRoundRect(swX, swY, swW, swH, swH, swH);
        g2.setColor(masterOptionOn ? new Color(52, 211, 153) : new Color(148, 163, 184));
        int knobX = masterOptionOn ? (swX + swW - 16) : (swX + 2);
        g2.fillOval(knobX, swY + 2, 14, 14);

        
        g2.setColor(new Color(38, 52, 71));
        g2.drawLine(cardX + 8, cardY + 38, cardX + cardW - 8, cardY + 38);

        
        int r1Y = cardY + 46;
        compactRowBounds.setBounds(cardX + 8, r1Y, cardW - 16, 24);
        g2.setColor(showCompact ? new Color(250, 204, 21) : new Color(100, 116, 139));
        g2.fillOval(cardX + 14, r1Y + 7, 9, 9);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.setColor(showCompact ? new Color(226, 232, 240) : new Color(100, 116, 139));
        g2.drawString("Compact-bulb", cardX + 30, r1Y + 16);

        LightFlyweightFactory.getLightType("COMPACT")
                .renderNode(g2, cardX + cardW - 22, r1Y + 12, showCompact, false);

        
        int r2Y = cardY + 76;
        ledRowBounds.setBounds(cardX + 8, r2Y, cardW - 16, 24);
        g2.setColor(showLed ? new Color(6, 232, 255) : new Color(100, 116, 139));
        g2.fillOval(cardX + 14, r2Y + 7, 9, 9);
        g2.setColor(showLed ? new Color(226, 232, 240) : new Color(100, 116, 139));
        g2.drawString("LED Flyweights", cardX + 30, r2Y + 16);

        LightFlyweightFactory.getLightType("LED")
                .renderNode(g2, cardX + cardW - 22, r2Y + 12, showLed, false);
    }
}