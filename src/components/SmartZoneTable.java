package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class SmartZoneTable extends JTable {

    private int hoverRow = -1;
    private int hoverCol = -1;
    private JTextArea boundLogArea;
    private JLabel boundTotalLabel;
    private SmartTerminal boundTerminal;
    private SmartZoneTree boundTree;
    private Runnable onStateChangeCallback;

    private final int[] defaultUsages = {345, 120, 85, 60, 35, 45};

    public SmartZoneTable() {
        setRowHeight(45);
        setBackground(new Color(15, 23, 42));
        setForeground(Color.WHITE);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTableHeader header = getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setBackground(new Color(15, 23, 42));
        header.setForeground(new Color(203, 213, 225));
        header.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(30, 41, 59)));

        setupTableModel();
        applyCustomRenderers();
        setupMouseInteractions();
    }

    public final void applyCustomRenderers() {
        CustomTableRenderer renderer = new CustomTableRenderer();
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        if (getColumnCount() >= 6) {
            getColumnModel().getColumn(0).setPreferredWidth(180);
            getColumnModel().getColumn(1).setPreferredWidth(100);
            getColumnModel().getColumn(2).setPreferredWidth(80);
            getColumnModel().getColumn(3).setPreferredWidth(160);
            getColumnModel().getColumn(4).setPreferredWidth(160);
            getColumnModel().getColumn(5).setPreferredWidth(70);
        }
    }

    public void bindOutputs(JTextArea logArea, JLabel totalUsageLabel) {
        this.boundLogArea = logArea;
        this.boundTotalLabel = totalUsageLabel;
        updateTotalUsage();
    }

    public void bindSmartComponents(SmartTerminal terminal, SmartZoneTree tree, Runnable onStateChange) {
        this.boundTerminal = terminal;
        this.boundTree = tree;
        this.onStateChangeCallback = onStateChange;
    }

    private void logAction(String level, String message) {
        if (boundTerminal != null) {
            boundTerminal.appendLog(level, message);
        }
        if (boundLogArea != null) {
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            boundLogArea.append("[" + time + "] [" + level + "] " + message + "\n");
            boundLogArea.setCaretPosition(boundLogArea.getDocument().getLength());
        }
    }

    private void updateTotalUsage() {
        int total = 0;
        for (int i = 0; i < getRowCount(); i++) {
            Object val = getValueAt(i, 3);
            if (val != null) {
                try {
                    total += Integer.parseInt(val.toString().replaceAll("[^0-9]", ""));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        if (boundTotalLabel != null) {
            boundTotalLabel.setText("Total Zone Usage: " + total + " kW");
        }
        if (onStateChangeCallback != null) {
            onStateChangeCallback.run();
        }
    }

    private void setupTableModel() {
        String[] columns = {"Zone Name", "Type", "Status", "Live Usage (kW)", "Daily Limit (kWh)", "Alerts"};

        Object[][] data = {
            {"Central District", "Active", true, 345, 345, true},
            {"Newton Street", "Warning", false, 120, 120, true},
            {"Hospital Block A", "Active", true, 85, 120, false},
            {"Tech Park Building", "Active", true, 60, 80, false},
            {"Apartment C", "Active", true, 35, 7, true},
            {"Apartment D", "Active", false, 45, 6, false}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    private void setupMouseInteractions() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());

                if (row < 0 || col < 0) {
                    return;
                }

                setRowSelectionInterval(row, row);
                Rectangle cellRect = getCellRect(row, col, false);
                int relX = e.getX() - cellRect.x;

                if (col == 2) {
                    togglePowerSwitch(row);
                } else if (col == 5) {
                    if (relX >= cellRect.width - 35) {
                        showRowActionMenu(e.getComponent(), e.getX(), e.getY(), row);
                    } else if (relX <= 32) {
                        boolean currentAlert = Boolean.TRUE.equals(getValueAt(row, 5));
                        if (currentAlert) {
                            setValueAt(false, row, 5);
                            String zoneName = getValueAt(row, 0).toString();
                            if (boundTree != null) {
                                SmartZoneTree.ZoneData zd = boundTree.findZoneDataByName(zoneName);
                                if (zd != null) {
                                    zd.hasAlert = false;
                                }
                            }
                            logAction("INFO", "Alert acknowledged and cleared for " + zoneName);
                            if (onStateChangeCallback != null) {
                                onStateChangeCallback.run();
                            }
                            repaint();
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int r = rowAtPoint(e.getPoint());
                int c = columnAtPoint(e.getPoint());
                if (r != hoverRow || c != hoverCol) {
                    hoverRow = r;
                    hoverCol = c;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverRow = -1;
                hoverCol = -1;
                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void togglePowerSwitch(int row) {
        if (row < 0 || row >= getRowCount()) {
            return;
        }
        String zoneName = getValueAt(row, 0).toString();
        boolean currentState = Boolean.TRUE.equals(getValueAt(row, 2));
        boolean newState = !currentState;

        setValueAt(newState, row, 2);

        int restoredUsage = (row < defaultUsages.length) ? defaultUsages[row] : 50;
        if (boundTree != null) {
            SmartZoneTree.ZoneData zd = boundTree.findZoneDataByName(zoneName);
            if (zd != null) {
                restoredUsage = zd.usageKW > 0 ? zd.usageKW : restoredUsage;
                zd.setPowerState(newState);
                boundTree.repaint();
            }
        }

        if (newState) {
            setValueAt("Active", row, 1);
            setValueAt(restoredUsage, row, 3);
            logAction("SUCCESS", zoneName + " power restored (" + restoredUsage + " kW)");
        } else {
            setValueAt("Warning", row, 1);
            setValueAt(0, row, 3);
            logAction("WARNING", zoneName + " power switched OFF (0 kW)");
        }

        updateTotalUsage();
        repaint();
    }

    private void showRowActionMenu(Component invoker, int x, int y, int row) {
        String zoneName = getValueAt(row, 0).toString();
        boolean isPowerOn = Boolean.TRUE.equals(getValueAt(row, 2));
        boolean hasAlert = Boolean.TRUE.equals(getValueAt(row, 5));

        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(15, 23, 42));
        menu.setBorder(new LineBorder(new Color(56, 189, 248, 120), 1, true));

        JMenuItem itemToggle = createStyledMenuItem(
                isPowerOn ? "Turn Power OFF" : "Turn Power ON",
                new Color(56, 189, 248)
        );
        itemToggle.addActionListener(ev -> togglePowerSwitch(row));

        JMenuItem itemReport = createStyledMenuItem("Report Zone Usage", new Color(226, 232, 240));
        itemReport.addActionListener(ev -> {
            String usage = getValueAt(row, 3).toString();
            String limit = getValueAt(row, 4).toString();
            logAction("ZONES", zoneName + " -> Live Usage: " + usage + " kW | Limit: " + limit + " kWh");
            JOptionPane.showMessageDialog(this,
                    "Zone: " + zoneName + "\nStatus: " + getValueAt(row, 1)
                    + "\nLive Usage: " + usage + " kW\nDaily Limit: " + limit + " kWh",
                    "Zone Usage Report", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem itemAlert = createStyledMenuItem(
                hasAlert ? "Clear / Resolve Alert" : "Flag Warning Alert",
                new Color(250, 204, 21)
        );
        itemAlert.addActionListener(ev -> {
            boolean newAlertState = !hasAlert;
            setValueAt(newAlertState, row, 5);
            if (boundTree != null) {
                SmartZoneTree.ZoneData zd = boundTree.findZoneDataByName(zoneName);
                if (zd != null) {
                    zd.hasAlert = newAlertState;
                }
            }
            logAction(newAlertState ? "WARNING" : "INFO",
                    newAlertState ? "Manual warning alert flagged for " + zoneName
                            : "Warning alert resolved for " + zoneName);
            if (onStateChangeCallback != null) {
                onStateChangeCallback.run();
            }
            repaint();
        });

        JMenuItem itemShutdown = createStyledMenuItem("Emergency Shutdown", new Color(248, 113, 113));
        itemShutdown.addActionListener(ev -> {
            setValueAt(false, row, 2);
            setValueAt("Warning", row, 1);
            setValueAt(0, row, 3);
            setValueAt(true, row, 5);
            if (boundTree != null) {
                SmartZoneTree.ZoneData zd = boundTree.findZoneDataByName(zoneName);
                if (zd != null) {
                    zd.setPowerState(false);
                    zd.hasAlert = true;
                    boundTree.repaint();
                }
            }
            updateTotalUsage();
            logAction("CRITICAL", "Immediate emergency power cutoff executed for " + zoneName);
            repaint();
        });

        menu.add(itemToggle);
        menu.add(itemReport);
        menu.add(itemAlert);
        menu.addSeparator();
        menu.add(itemShutdown);

        menu.show(invoker, x - 140, y + 10);
    }

    private JMenuItem createStyledMenuItem(String text, Color textColor) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 13));
        item.setForeground(textColor);
        item.setBackground(new Color(15, 23, 42));
        item.setBorder(new EmptyBorder(8, 14, 8, 14));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return item;
    }

    private class CustomTableRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = getWidth();
                    int h = getHeight();

                    if (isSelected) {
                        g2.setColor(new Color(24, 43, 60));
                        g2.fillRect(0, 0, w, h);

                        g2.setColor(new Color(6, 182, 212));
                        g2.setStroke(new BasicStroke(1.5f));
                        if (column == 0) {
                            g2.drawLine(2, 2, 2, h - 3);
                        }
                        if (column == getColumnCount() - 1) {
                            g2.drawLine(w - 3, 2, w - 3, h - 3);
                        }
                        g2.drawLine(0, 2, w, 2);
                        g2.drawLine(0, h - 3, w, h - 3);
                    } else if (row == hoverRow) {
                        g2.setColor(new Color(20, 31, 53));
                        g2.fillRect(0, 0, w, h);
                        g2.setColor(new Color(30, 41, 59));
                        g2.drawLine(0, h - 1, w, h - 1);
                    } else {
                        g2.setColor(new Color(15, 23, 42));
                        g2.fillRect(0, 0, w, h);
                        g2.setColor(new Color(30, 41, 59));
                        g2.drawLine(0, h - 1, w, h - 1);
                    }

                    g2.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 13));
                    FontMetrics fm = g2.getFontMetrics();
                    int textY = (h - fm.getHeight()) / 2 + fm.getAscent();

                    if (value == null) {
                        g2.dispose();
                        return;
                    }

                    switch (column) {
                        case 0:
                            g2.setColor(Color.WHITE);
                            g2.drawString(value.toString(), 15, textY);
                            break;

                        case 1:
                            String type = value.toString();
                            if ("Active".equalsIgnoreCase(type)) {
                                g2.setColor(new Color(52, 211, 153));
                            } else {
                                g2.setColor(new Color(250, 204, 21));
                            }
                            g2.drawString(type, 15, textY);
                            break;

                        case 2:
                            boolean isToggleOn = Boolean.TRUE.equals(value);
                            boolean isWarningRow = "Warning".equalsIgnoreCase(table.getValueAt(row, 1).toString());

                            int tW = 38, tH = 18;
                            int tX = 15, tY = (h - tH) / 2;

                            if (row == hoverRow && column == 2) {
                                g2.setColor(new Color(56, 189, 248, 40));
                                g2.fillRoundRect(tX - 3, tY - 3, tW + 6, tH + 6, tH + 6, tH + 6);
                            }

                            if (isToggleOn) {
                                g2.setColor(new Color(6, 182, 212, 50));
                                g2.fillRoundRect(tX, tY, tW, tH, tH, tH);
                                g2.setColor(new Color(6, 182, 212));
                                g2.drawRoundRect(tX, tY, tW, tH, tH, tH);
                                g2.fillOval(tX + tW - 16, tY + 2, 14, 14);
                            } else if (isWarningRow) {
                                g2.setColor(new Color(239, 68, 68, 50));
                                g2.fillRoundRect(tX, tY, tW, tH, tH, tH);
                                g2.setColor(new Color(239, 68, 68));
                                g2.drawRoundRect(tX, tY, tW, tH, tH, tH);
                                g2.fillOval(tX + 2, tY + 2, 14, 14);
                            } else {
                                g2.setColor(new Color(30, 58, 82));
                                g2.fillRoundRect(tX, tY, tW, tH, tH, tH);
                                g2.setColor(new Color(6, 182, 212));
                                g2.fillOval(tX + 2, tY + 2, 14, 14);
                            }
                            break;

                        case 3:
                        case 4:
                            String valStr = value.toString().replaceAll("[^0-9.]", "");
                            String unit = (column == 3) ? " kW" : " kWh";
                            String fullText = valStr + unit;

                            boolean isWarning = "Warning".equalsIgnoreCase(table.getValueAt(row, 1).toString());
                            if (column == 3 && isWarning) {
                                g2.setColor(new Color(250, 204, 21));
                            } else {
                                g2.setColor(Color.WHITE);
                            }
                            g2.drawString(fullText, 15, textY);

                            int chartX = 15 + fm.stringWidth(fullText) + 15;
                            int chartY = (h - 15) / 2;
                            Color chartColor = isWarning ? new Color(239, 68, 68) : new Color(6, 182, 212);
                            drawSparkline(g2, chartX, chartY, 45, 15, chartColor, row * column);
                            break;

                        case 5:
                            boolean hasAlert = Boolean.TRUE.equals(value);
                            int iconX = 15;
                            int iconY = h / 2;

                            if (hasAlert) {
                                g2.setColor(new Color(239, 68, 68));
                                Path2D alertIcon = new Path2D.Float();
                                alertIcon.moveTo(iconX + 6, iconY - 6);
                                alertIcon.lineTo(iconX + 12, iconY + 6);
                                alertIcon.lineTo(iconX, iconY + 6);
                                alertIcon.closePath();
                                g2.fill(alertIcon);

                                g2.setColor(Color.BLACK);
                                g2.fillRect(iconX + 5, iconY - 1, 2, 4);
                                g2.fillRect(iconX + 5, iconY + 4, 2, 2);
                            }

                            int dotX = w - 28;
                            if (row == hoverRow && column == 5) {
                                g2.setColor(new Color(51, 65, 85));
                                g2.fillRoundRect(dotX - 5, iconY - 10, 24, 20, 6, 6);
                            }

                            g2.setColor(row == hoverRow && column == 5 ? Color.WHITE : new Color(148, 163, 184));
                            g2.fillOval(dotX, iconY - 1, 3, 3);
                            g2.fillOval(dotX + 5, iconY - 1, 3, 3);
                            g2.fillOval(dotX + 10, iconY - 1, 3, 3);
                            break;
                    }
                    g2.dispose();
                }
            };
            panel.setOpaque(false);
            return panel;
        }

        private void drawSparkline(Graphics2D g2, int x, int y, int width, int height, Color color, int seed) {
            Path2D line = new Path2D.Float();
            Path2D fill = new Path2D.Float();

            int[] points = {height, height - 3, height - 8, height - 5, height - 12, 2};
            if (seed % 3 == 1) {
                points = new int[]{height - 2, height - 2, height - 2, height - 4, height - 10, 0};
            }
            if (seed % 3 == 2) {
                points = new int[]{height, height - 5, height - 2, height - 10, height - 15, 4};
            }

            int step = width / (points.length - 1);

            line.moveTo(x, y + points[0]);
            fill.moveTo(x, y + height);
            fill.lineTo(x, y + points[0]);

            for (int i = 1; i < points.length; i++) {
                line.lineTo(x + (i * step), y + points[i]);
                fill.lineTo(x + (i * step), y + points[i]);
            }

            fill.lineTo(x + width, y + height);
            fill.closePath();

            GradientPaint gp = new GradientPaint(0, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), 80),
                    0, y + height, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
            g2.setPaint(gp);
            g2.fill(fill);

            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(line);
        }
    }
}