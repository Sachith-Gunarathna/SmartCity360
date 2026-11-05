package components;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoundTextAreaPanel extends JPanel {

    private final JTextArea textArea;
    private int cornerRadius = 15; 

    public RoundTextAreaPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(new Color(15, 23, 42)); 
        setBorder(new EmptyBorder(8, 12, 8, 12));

        textArea = new JTextArea();
        textArea.setOpaque(false);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(new Color(6, 182, 212));
        textArea.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        
        
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
        
        
        setMinimumSize(new Dimension(100, 40));
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, cornerRadius, cornerRadius));

        
        g2.setColor(new Color(51, 65, 85)); 
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, cornerRadius, cornerRadius));

        g2.dispose();
        super.paintComponent(g);
    }
}