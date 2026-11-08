package components;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;

public class SmartZoneTree extends JTree {

    private int hoverRow = -1;
    private JTable boundTable;
    private JLabel boundTotalLabel;
    private SmartTerminal boundTerminal;

    public SmartZoneTree() {
        setBackground(new Color(30, 41, 59));
        setOpaque(false);
        setRowHeight(34);
        setRootVisible(true);
        setShowsRootHandles(true);
        setBorder(new EmptyBorder(6, 6, 6, 6));
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setUI(new FullWidthTreeUI());
        setCellRenderer(new CustomZoneTreeRenderer());

        setupInitialData();
        setupInteractions();
    }

    private void setupInteractions() {
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = getClosestRowForLocation(e.getX(), e.getY());
                if (row != -1) {
                    Rectangle bounds = getRowBounds(row);
                    if (bounds != null && e.getY() >= bounds.y && e.getY() <= bounds.y + bounds.height) {
                        if (e.getX() >= bounds.x) {
                            setSelectionRow(row);
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int row = getClosestRowForLocation(e.getX(), e.getY());
                Rectangle bounds = (row != -1) ? getRowBounds(row) : null;
                int newHover = (bounds != null && e.getY() >= bounds.y && e.getY() <= bounds.y + bounds.height) ? row : -1;
                if (newHover != hoverRow) {
                    hoverRow = newHover;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverRow != -1) {
                    hoverRow = -1;
                    repaint();
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);

        addTreeSelectionListener(e -> {
            repaint();
            updateBoundTable();
            ZoneData selected = getSelectedZoneData();
            if (selected != null && boundTerminal != null) {
                boundTerminal.appendLog("ZONES", "Selected Hierarchy Node: [" + selected.zoneType + "] "
                        + selected.name + " | Usage: " + selected.getCurrentUsageKW() + " kW");
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getUI() instanceof FullWidthTreeUI) {
                    ((FullWidthTreeUI) getUI()).invalidateNodeSizes();
                }
                repaint();
            }
        });
    }

    public void bindTable(JTable table) {
        this.boundTable = table;
        updateBoundTable();
    }

    public void bindTable(JTable table, JLabel totalUsageLabel) {
        this.boundTable = table;
        this.boundTotalLabel = totalUsageLabel;
        updateBoundTable();
    }

    public void bindTerminal(SmartTerminal terminal) {
        this.boundTerminal = terminal;
    }

    public ZoneData getSelectedZoneData() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (node != null && node.getUserObject() instanceof ZoneData) {
            return (ZoneData) node.getUserObject();
        }
        return null;
    }

    public DefaultMutableTreeNode getSelectedTreeNode() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (node == null) {
            node = (DefaultMutableTreeNode) getModel().getRoot();
        }
        return node;
    }

    /**
     * SmartZoneTable na 6 columns format ge sariyagi rows load maaduttade
     */
    public void updateBoundTable() {
        if (boundTable == null) {
            return;
        }

        DefaultMutableTreeNode selectedNode = getSelectedTreeNode();
        DefaultTableModel model = (DefaultTableModel) boundTable.getModel();

        if (model.getColumnCount() < 6) {
            model.setColumnIdentifiers(new String[]{
                "Zone Name", "Type", "Status", "Live Usage (kW)", "Daily Limit (kWh)", "Alerts"
            });
        }
        model.setRowCount(0);

        List<ZoneData> rowsToDisplay = new ArrayList<>();
        collectZoneData(selectedNode, rowsToDisplay);

        int totalUsage = 0;
        for (ZoneData data : rowsToDisplay) {
            String rowType = data.powerOn ? "Active" : "Warning";
            int liveKW = data.getCurrentUsageKW();
            model.addRow(new Object[]{
                data.name,
                rowType,
                data.powerOn,
                liveKW,
                data.dailyLimitKWh,
                data.hasAlert
            });
            totalUsage += liveKW;
        }

        if (boundTotalLabel != null) {
            boundTotalLabel.setText("Total Zone Usage: " + totalUsage + " kW");
        }
        boundTable.repaint();
    }

    public void collectZoneData(DefaultMutableTreeNode node, List<ZoneData> list) {
        if (node.getUserObject() instanceof ZoneData) {
            ZoneData data = (ZoneData) node.getUserObject();
            if (!"Category".equals(data.zoneType)) {
                list.add(data);
            }
        }
        Enumeration<TreeNode> children = node.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            collectZoneData(child, list);
        }
    }

    public ZoneData findZoneDataByName(String zoneName) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
        return findRecursive(root, zoneName);
    }

    private ZoneData findRecursive(DefaultMutableTreeNode node, String zoneName) {
        if (node.getUserObject() instanceof ZoneData) {
            ZoneData data = (ZoneData) node.getUserObject();
            if (data.name.equalsIgnoreCase(zoneName)) {
                return data;
            }
        }
        Enumeration<TreeNode> children = node.children();
        while (children.hasMoreElements()) {
            ZoneData found = findRecursive((DefaultMutableTreeNode) children.nextElement(), zoneName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private void setupInitialData() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                new ZoneData("Alawwa City", "City", IconType.CITY_MAP, "CyanDot", 570, 800, true, true, false));

        DefaultMutableTreeNode district = new DefaultMutableTreeNode(
                new ZoneData("District", "Category", IconType.MAP, "", 0, 0, false, true, false));
        DefaultMutableTreeNode centralDistrict = new DefaultMutableTreeNode(
                new ZoneData("Central District", "District", IconType.PIN, "Active", 345, 500, false, true, true));

        DefaultMutableTreeNode street = new DefaultMutableTreeNode(
                new ZoneData("Street", "Category", IconType.FOLDER, "", 0, 0, false, true, false));
        DefaultMutableTreeNode newtonStreet = new DefaultMutableTreeNode(
                new ZoneData("Newton Street", "Street", IconType.STREET, "Active", 120, 200, false, false, true));

        DefaultMutableTreeNode building = new DefaultMutableTreeNode(
                new ZoneData("Building", "Category", IconType.FOLDER, "", 0, 0, false, true, false));
        DefaultMutableTreeNode hospital = new DefaultMutableTreeNode(
                new ZoneData("Hospital Block A", "Building", IconType.BUILDING, "Active", 85, 120, false, true, false));
        DefaultMutableTreeNode techPark = new DefaultMutableTreeNode(
                new ZoneData("Tech Park Building", "Building", IconType.BUILDING, "Critical", 60, 80, false, true, false));
        DefaultMutableTreeNode aptC1 = new DefaultMutableTreeNode(
                new ZoneData("Apartment C (Block 1)", "Building", IconType.BUILDING, "Critical", 35, 50, false, true, true));
        DefaultMutableTreeNode aptC2 = new DefaultMutableTreeNode(
                new ZoneData("Apartment C (Block 2)", "Building", IconType.BUILDING, "Active", 25, 50, false, true, false));
        DefaultMutableTreeNode aptC3 = new DefaultMutableTreeNode(
                new ZoneData("Apartment C (Block 3)", "Building", IconType.BUILDING, "Active", 20, 50, false, true, false));

        DefaultMutableTreeNode aptD = new DefaultMutableTreeNode(
                new ZoneData("Apartment D", "Building", IconType.BUILDING, "Active", 45, 70, false, false, false));
        aptD.add(new DefaultMutableTreeNode(
                new ZoneData("Apt D - Floor 1", "Floor", IconType.BUILDING, "Active", 25, 40, false, true, false)));

        DefaultMutableTreeNode aptE = new DefaultMutableTreeNode(
                new ZoneData("Apartment E", "Building", IconType.BUILDING, "Active", 40, 60, false, true, false));
        aptE.add(new DefaultMutableTreeNode(
                new ZoneData("Apt E - Floor 1", "Floor", IconType.BUILDING, "Active", 20, 30, false, true, false)));

        building.add(hospital);
        building.add(techPark);
        building.add(aptC1);
        building.add(aptC2);
        building.add(aptC3);

        street.add(newtonStreet);
        street.add(building);

        district.add(centralDistrict);
        district.add(street);
        district.add(aptD);
        district.add(aptE);

        root.add(district);

        this.setModel(new DefaultTreeModel(root));

        expandPath(new TreePath(root.getPath()));
        expandPath(new TreePath(district.getPath()));
        expandPath(new TreePath(street.getPath()));
        expandPath(new TreePath(building.getPath()));
        setSelectionRow(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (getRowCount() > 0) {
            Rectangle r0 = getRowBounds(0);
            if (r0 != null) {
                GradientPaint gp = new GradientPaint(
                        0, r0.y, new Color(18, 43, 56),
                        getWidth(), r0.y, new Color(22, 58, 72));
                g2.setPaint(gp);
                g2.fillRoundRect(4, r0.y + 1, getWidth() - 8, r0.height - 2, 8, 8);
            }
        }

        if (hoverRow >= 0 && hoverRow < getRowCount() && !isRowSelected(hoverRow)) {
            Rectangle rh = getRowBounds(hoverRow);
            if (rh != null) {
                g2.setColor(new Color(24, 34, 52));
                g2.fillRoundRect(4, rh.y + 1, getWidth() - 8, rh.height - 2, 8, 8);
            }
        }

        int[] selectedRows = getSelectionRows();
        if (selectedRows != null) {
            for (int row : selectedRows) {
                Rectangle r = getRowBounds(row);
                if (r != null) {
                    g2.setColor(new Color(30, 52, 82));
                    g2.fillRoundRect(4, r.y + 1, getWidth() - 8, r.height - 2, 8, 8);

                    g2.setColor(new Color(56, 189, 248, 120));
                    g2.drawRoundRect(4, r.y + 1, getWidth() - 9, r.height - 3, 8, 8);

                    g2.setColor(new Color(56, 189, 248));
                    g2.fillRoundRect(4, r.y + 5, 4, r.height - 10, 4, 4);
                }
            }
        }
        g2.dispose();

        super.paintComponent(g);
    }

    public enum IconType {
        CITY_MAP, MAP, PIN, FOLDER, STREET, BUILDING
    }

    public static class ZoneData {
        public String name;
        public String zoneType;
        public IconType iconType;
        public String status;
        public int usageKW;
        public int dailyLimitKWh;
        public boolean isRootHeader;
        public boolean powerOn;
        public boolean hasAlert;

        public ZoneData(String name, String zoneType, IconType iconType, String status,
                int usageKW, int dailyLimitKWh, boolean isRootHeader) {
            this(name, zoneType, iconType, status, usageKW, dailyLimitKWh, isRootHeader, true, "Critical".equals(status));
        }

        public ZoneData(String name, String zoneType, IconType iconType, String status,
                int usageKW, int dailyLimitKWh, boolean isRootHeader, boolean powerOn, boolean hasAlert) {
            this.name = name;
            this.zoneType = zoneType;
            this.iconType = iconType;
            this.status = status;
            this.usageKW = usageKW;
            this.dailyLimitKWh = dailyLimitKWh;
            this.isRootHeader = isRootHeader;
            this.powerOn = powerOn;
            this.hasAlert = hasAlert;
        }

        public int getCurrentUsageKW() {
            return powerOn ? usageKW : 0;
        }

        public void setPowerState(boolean on) {
            this.powerOn = on;
            if (!isRootHeader && !"Category".equals(zoneType)) {
                this.status = on ? "Active" : "Critical";
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class FullWidthTreeUI extends BasicTreeUI {

        private final Color lineColor = new Color(45, 58, 77);

        public void invalidateNodeSizes() {
            if (treeState != null) {
                treeState.invalidateSizes();
            }
        }

        @Override
        protected void installDefaults() {
            super.installDefaults();
            setLeftChildIndent(12);
            setRightChildIndent(12);
            tree.setRowHeight(34);
        }

        @Override
        protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
            return new NodeDimensionsHandler() {
                @Override
                public Rectangle getNodeDimensions(Object value, int row, int depth, boolean expanded, Rectangle size) {
                    Rectangle r = super.getNodeDimensions(value, row, depth, expanded, size);
                    if (tree != null && tree.getWidth() > 0) {
                        r.width = Math.max(150, tree.getWidth() - getRowX(row, depth) - 12);
                    }
                    return r;
                }
            };
        }

        @Override
        protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, TreePath path) {
            int depth = path.getPathCount() - 1;
            if (depth == 0 && !getShowsRootHandles() && !isRootVisible()) {
                return;
            }
            int lineX = getRowX(-1, depth + 1) - getRightChildIndent() + insets.left;
            int clipLeft = clipBounds.x;
            int clipRight = clipBounds.x + (clipBounds.width - 1);

            if (lineX >= clipLeft && lineX <= clipRight) {
                int clipTop = clipBounds.y;
                int clipBottom = clipBounds.y + clipBounds.height;
                Rectangle parentBounds = getPathBounds(tree, path);
                Rectangle lastChildBounds = getPathBounds(tree, getLastChildPath(path));

                if (lastChildBounds == null) {
                    return;
                }
                int top = (parentBounds == null)
                        ? Math.max(insets.top + getVerticalLegBuffer(), clipTop)
                        : Math.max(parentBounds.y + parentBounds.height + getVerticalLegBuffer(), clipTop);

                int bottom = Math.min(lastChildBounds.y + (lastChildBounds.height / 2), clipBottom);

                if (top <= bottom) {
                    g.setColor(lineColor);
                    g.drawLine(lineX, top, lineX, bottom);
                }
            }
        }

        @Override
        protected void paintHorizontalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets,
                Rectangle bounds, TreePath path, int row, boolean isExpanded,
                boolean hasBeenExpanded, boolean isLeaf) {
            int depth = path.getPathCount() - 1;
            if ((depth == 0 || (depth == 1 && !isRootVisible())) && !getShowsRootHandles()) {
                return;
            }
            int clipLeft = clipBounds.x;
            int clipRight = clipBounds.x + (clipBounds.width - 1);
            int clipTop = clipBounds.y;
            int clipBottom = clipBounds.y + (clipBounds.height - 1);
            int lineY = bounds.y + bounds.height / 2;

            if (lineY >= clipTop && lineY <= clipBottom) {
                int leftX = getRowX(row, depth) - getRightChildIndent() + insets.left;
                int rightX = bounds.x - getHorizontalLegBuffer();
                if (leftX <= clipRight && rightX >= clipLeft && leftX < rightX) {
                    g.setColor(lineColor);
                    g.drawLine(Math.max(leftX, clipLeft), lineY, Math.min(rightX, clipRight), lineY);
                }
            }
        }

        @Override
        protected void paintExpandControl(Graphics g, Rectangle clipBounds, Insets insets,
                Rectangle bounds, TreePath path, int row, boolean isExpanded,
                boolean hasBeenExpanded, boolean isLeaf) {
            Object value = path.getLastPathComponent();
            if (!isLeaf && treeModel.getChildCount(value) > 0) {
                int middleXOfKnob = bounds.x - getRightChildIndent() + 1;
                int middleYOfKnob = bounds.y + (bounds.height / 2);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (row == 0) {
                    g2.setColor(new Color(140, 175, 195));
                    g2.setStroke(new BasicStroke(1.3f));
                    g2.drawRoundRect(middleXOfKnob - 6, middleYOfKnob - 6, 12, 12, 3, 3);
                    g2.drawLine(middleXOfKnob - 3, middleYOfKnob, middleXOfKnob + 3, middleYOfKnob);
                } else {
                    g2.setColor(new Color(135, 150, 170));
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    if (isExpanded) {
                        g2.drawLine(middleXOfKnob - 4, middleYOfKnob - 2, middleXOfKnob, middleYOfKnob + 2);
                        g2.drawLine(middleXOfKnob, middleYOfKnob + 2, middleXOfKnob + 4, middleYOfKnob - 2);
                    } else {
                        g2.drawLine(middleXOfKnob - 2, middleYOfKnob - 4, middleXOfKnob + 2, middleYOfKnob);
                        g2.drawLine(middleXOfKnob + 2, middleYOfKnob, middleXOfKnob - 2, middleYOfKnob + 4);
                    }
                }
                g2.dispose();
            }
        }
    }

    private class CustomZoneTreeRenderer extends JPanel implements TreeCellRenderer {

        private final JLabel lblIcon = new JLabel();
        private final JLabel lblText = new JLabel();
        private final BadgeComponent badge = new BadgeComponent();

        public CustomZoneTreeRenderer() {
            setLayout(new BorderLayout(8, 0));
            setOpaque(false);
            setBorder(new EmptyBorder(0, 4, 0, 10));

            lblText.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 13));
            lblText.setForeground(new Color(226, 232, 240));

            add(lblIcon, BorderLayout.WEST);
            add(lblText, BorderLayout.CENTER);
            add(badge, BorderLayout.EAST);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            if (node.getUserObject() instanceof ZoneData) {
                ZoneData data = (ZoneData) node.getUserObject();
                lblText.setText(data.name);
                lblText.setFont(new Font("Franklin Gothic Medium", (data.isRootHeader || selected) ? Font.BOLD : Font.PLAIN, 13));
                lblText.setForeground(selected ? Color.WHITE : new Color(226, 232, 240));
                lblIcon.setIcon(new VectorIcon(data.iconType));
                badge.setStatus(data.status);
            } else {
                lblText.setText(value.toString());
                lblIcon.setIcon(null);
                badge.setStatus("");
            }
            return this;
        }
    }

    private static class BadgeComponent extends JComponent {

        private String status = "";

        public void setStatus(String status) {
            this.status = status != null ? status : "";
            setVisible(!this.status.isEmpty());
        }

        @Override
        public Dimension getPreferredSize() {
            if (status.isEmpty()) {
                return new Dimension(0, 0);
            }
            if ("CyanDot".equals(status)) {
                return new Dimension(24, 24);
            }
            return new Dimension(56, 22);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (status.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if ("Active".equals(status) || "Critical".equals(status)) {
                boolean isActive = "Active".equals(status);
                Color bg = isActive ? new Color(19, 58, 46) : new Color(68, 26, 34);
                Color border = isActive ? new Color(34, 95, 73) : new Color(105, 40, 50);
                Color fg = isActive ? new Color(56, 210, 145) : new Color(235, 95, 105);

                int pillH = 20;
                int y = (h - pillH) / 2;

                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, y, w - 1, pillH, 18, 18));
                g2.setColor(border);
                g2.draw(new RoundRectangle2D.Float(0, y, w - 1, pillH, 18, 18));

                g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 11));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (w - fm.stringWidth(status)) / 2;
                int ty = y + ((pillH - fm.getHeight()) / 2) + fm.getAscent() - 1;
                g2.setColor(fg);
                g2.drawString(status, tx, ty);

            } else if ("CyanDot".equals(status)) {
                int cx = w / 2;
                int cy = h / 2;
                g2.setColor(new Color(0, 229, 255, 45));
                g2.fillOval(cx - 8, cy - 8, 16, 16);
                g2.setColor(new Color(0, 229, 255, 95));
                g2.fillOval(cx - 6, cy - 6, 12, 12);
                g2.setColor(new Color(56, 239, 255));
                g2.fillOval(cx - 4, cy - 4, 8, 8);
            }
            g2.dispose();
        }
    }

    private static class VectorIcon implements Icon {

        private final IconType type;

        public VectorIcon(IconType type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (type) {
                case CITY_MAP:
                    g2.setColor(new Color(34, 211, 238));
                    drawFoldedMap(g2);
                    break;
                case MAP:
                    g2.setColor(new Color(148, 163, 184));
                    drawFoldedMap(g2);
                    break;
                case PIN:
                    g2.setColor(new Color(148, 163, 184));
                    g2.drawOval(5, 2, 8, 8);
                    g2.fillOval(8, 5, 2, 2);
                    g2.drawLine(9, 10, 9, 14);
                    g2.drawOval(4, 13, 10, 3);
                    break;
                case FOLDER:
                    g2.setColor(new Color(148, 163, 184));
                    Path2D folder = new Path2D.Float();
                    folder.moveTo(2, 4);
                    folder.lineTo(7, 4);
                    folder.lineTo(9, 6);
                    folder.lineTo(16, 6);
                    folder.lineTo(16, 14);
                    folder.lineTo(2, 14);
                    folder.closePath();
                    g2.draw(folder);
                    break;
                case STREET:
                    g2.setColor(new Color(148, 163, 184));
                    g2.drawLine(4, 15, 11, 3);
                    g2.drawLine(8, 15, 15, 3);
                    g2.drawOval(3, 8, 3, 3);
                    g2.drawOval(12, 9, 3, 3);
                    break;
                case BUILDING:
                    g2.setColor(new Color(148, 163, 184));
                    g2.drawRect(3, 3, 6, 12);
                    g2.drawRect(9, 6, 6, 9);
                    g2.fillRect(5, 5, 2, 2);
                    g2.fillRect(5, 8, 2, 2);
                    g2.fillRect(5, 11, 2, 2);
                    g2.fillRect(11, 8, 2, 2);
                    g2.fillRect(11, 11, 2, 2);
                    break;
            }
            g2.dispose();
        }

        private void drawFoldedMap(Graphics2D g2) {
            g2.drawLine(2, 4, 6, 2);
            g2.drawLine(6, 2, 11, 4);
            g2.drawLine(11, 4, 15, 2);
            g2.drawLine(2, 14, 6, 12);
            g2.drawLine(6, 12, 11, 14);
            g2.drawLine(11, 4, 11, 14);
            g2.drawLine(15, 2, 15, 12);
            g2.drawLine(2, 4, 2, 14);
            g2.drawLine(6, 2, 6, 12);
        }
    }
}