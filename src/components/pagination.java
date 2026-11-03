package components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class pagination extends JComponent {

    private int index;
    private int currentIndex;
    private eventPagination event;

    public void setEventPagination(eventPagination event) {
        this.event = event;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setTotalPage(int totalPage) {
        removeAll();
        
        for(int i=0;i<totalPage;i++){
            add(new Item(i,event));
        }
        
        repaint();
        revalidate();
    }

    public pagination() {
        
        setLayout(new MigLayout("inset 15","[center]10[center]"));

    }

    private class Item extends JButton {

        public Item(int index, eventPagination event) {
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(5, 5, 5, 5));
            setBackground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    event.onClick(index);
                }

            });
        }

        @Override
        public void paint(Graphics grphics) {

            super.paint(grphics);
            int width = getWidth();
            int height = getHeight();

            Graphics2D g2d = (Graphics2D) grphics.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

            g2d.setColor(getBackground());
            g2d.fillOval(0, 0, width, height);

            g2d.dispose();
        }

    }

}
