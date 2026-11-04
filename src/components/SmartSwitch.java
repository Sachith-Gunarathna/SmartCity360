package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class SmartSwitch extends JToggleButton {

    private float thumbPosition = 1.0f; // 0.0f = OFF (Left), 1.0f = ON (Right)
    private Timer animationTimer;
    private boolean hovered = false;

    // Chitradalli iruva bannaagalu (Colors)
    private Color activeTrackColor = new Color(45, 168, 242);      // Bright Cyan/Blue Fill
    private Color activeBorderColor = new Color(86, 194, 255);     // Lighter Cyan Outline
    private Color inactiveTrackColor = new Color(22, 32, 48);      // Dark Slate OFF Track
    private Color inactiveBorderColor = new Color(51, 65, 85);     // Muted Slate Border
    private Color knobFillColor = new Color(27, 40, 58);           // Dark Navy Knob Center
    private Color inactiveKnobColor = new Color(100, 116, 139);    // Muted Knob Ring when OFF

    public SmartSwitch() {
        super();
        setSelected(true); // Chitradalli iruva haage default ON sthithi
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(56, 28));

        // Smooth sliding animation timer
        animationTimer = new Timer(12, e -> {
            float target = isSelected() ? 1.0f : 0.0f;
            float step = 0.14f;

            if (Math.abs(thumbPosition - target) <= step) {
                thumbPosition = target;
                animationTimer.stop();
            } else if (thumbPosition < target) {
                thumbPosition += step;
            } else {
                thumbPosition -= step;
            }
            repaint();
        });

        addActionListener(e -> {
            if (!animationTimer.isRunning()) {
                animationTimer.start();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        if (animationTimer != null && !isShowing()) {
            thumbPosition = b ? 1.0f : 0.0f;
        } else if (animationTimer == null) {
            thumbPosition = b ? 1.0f : 0.0f;
        } else if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Padding mattu pill track alate (Dimensions)
        int pad = 2;
        int trackW = w - (pad * 2);
        int trackH = h - (pad * 2);
        int arc = trackH;

        // ON mattu OFF bannaagala mishrana (Color interpolation during animation)
        Color currentTrack = blendColors(inactiveTrackColor, activeTrackColor, thumbPosition);
        Color currentBorder = blendColors(inactiveBorderColor, activeBorderColor, thumbPosition);

        // 1. Hover Glow Effect
        if (hovered) {
            g2.setColor(new Color(45, 168, 242, 40));
            g2.fill(new RoundRectangle2D.Float(pad - 2, pad - 2, trackW + 4, trackH + 4, arc + 4, arc + 4));
        }

        // 2. Pill Track Background & Border
        g2.setColor(currentTrack);
        g2.fill(new RoundRectangle2D.Float(pad, pad, trackW, trackH, arc, arc));

        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(currentBorder);
        g2.draw(new RoundRectangle2D.Float(pad, pad, trackW, trackH, arc, arc));

        // 3. Circular Knob (Thumb) Position
        int knobMargin = 3;
        int knobDiameter = trackH - (knobMargin * 2);
        float minX = pad + knobMargin;
        float maxX = pad + trackW - knobMargin - knobDiameter;
        float knobX = minX + (maxX - minX) * thumbPosition;
        float knobY = pad + knobMargin;

        // Knob Inner Dark Fill (Chitradalli iruva dark navy vrutta)
        g2.setColor(knobFillColor);
        g2.fill(new Ellipse2D.Float(knobX, knobY, knobDiameter, knobDiameter));

        // Knob Ring / Border
        Color currentKnobRing = blendColors(inactiveKnobColor, activeBorderColor, thumbPosition);
        g2.setStroke(new BasicStroke(2.0f));
        g2.setColor(currentKnobRing);
        g2.draw(new Ellipse2D.Float(knobX, knobY, knobDiameter, knobDiameter));

        g2.dispose();
    }

    private Color blendColors(Color c1, Color c2, float ratio) {
        ratio = Math.max(0f, Math.min(1f, ratio));
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * ratio);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio);
        return new Color(r, g, b);
    }

    // NetBeans Properties Window nalli banna badalisalu Getters & Setters
    public Color getActiveTrackColor() {
        return activeTrackColor;
    }

    public void setActiveTrackColor(Color activeTrackColor) {
        this.activeTrackColor = activeTrackColor;
        repaint();
    }

    public Color getInactiveTrackColor() {
        return inactiveTrackColor;
    }

    public void setInactiveTrackColor(Color inactiveTrackColor) {
        this.inactiveTrackColor = inactiveTrackColor;
        repaint();
    }

    public Color getKnobFillColor() {
        return knobFillColor;
    }

    public void setKnobFillColor(Color knobFillColor) {
        this.knobFillColor = knobFillColor;
        repaint();
    }
}