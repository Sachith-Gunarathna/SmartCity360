package model;

import components.SmartTerminal;
import components.SmartZoneTable;
import components.SmartZoneTree;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.jdesktop.swingx.multislider.DefaultMultiThumbModel;

public class SmartZone {

    public interface ZoneComponent {

        String getZoneId();

        String getZoneName();

        String getZoneType();

        int getPowerUsageKW();

        int getActiveNodesCount();

        String getStatus();

        void setShutdown(boolean shutdown);

        void reportUsage(SmartTerminal terminal, String indent);
    }

    public static class streetNodeLeaf implements ZoneComponent {

        private final String zoneId;
        private final String name;
        private final String type;
        private final int normalPowerKW;
        private final int activeNodes;
        private boolean isShutdown = false;

        public streetNodeLeaf(String zoneId, String name, String type, int normalPowerKW, int activeNodes) {
            this.zoneId = zoneId;
            this.name = name;
            this.type = type;
            this.normalPowerKW = normalPowerKW;
            this.activeNodes = activeNodes;
        }

        public streetNodeLeaf(String name, int normalPowerKW, int activeNodes) {
            this("ZN-" + Math.abs(name.hashCode() % 900 + 100), name, "Sub-Zone Leaf", normalPowerKW, activeNodes);
        }

        @Override
        public String getZoneId() {
            return zoneId;
        }

        @Override
        public String getZoneName() {
            return name;
        }

        @Override
        public String getZoneType() {
            return type;
        }

        @Override
        public int getPowerUsageKW() {
            return isShutdown ? 0 : normalPowerKW;
        }

        @Override
        public int getActiveNodesCount() {
            return activeNodes;
        }

        @Override
        public String getStatus() {
            if (isShutdown) {
                return "Offline";
            }
            return normalPowerKW >= 120 ? "Peak Load" : "Optimal";
        }

        @Override
        public void setShutdown(boolean shutdown) {
            this.isShutdown = shutdown;
        }

        @Override
        public void reportUsage(SmartTerminal terminal, String indent) {
            if (terminal != null) {
                terminal.appendLog("Zone", indent + "└─ [Leaf] " + name + " (" + zoneId + ") -> "
                        + getPowerUsageKW() + " kW | Nodes: " + getActiveNodesCount() + " | Status: " + getStatus());
            }
        }

    }

    public static class CityZoneComposite implements ZoneComponent {

        private final String zoneId;
        private final String zoneName;
        private final String zoneType;
        private final int ownPowerkW;
        private boolean isShutdown = false;
        private final List<ZoneComponent> children = new ArrayList<>();

        public CityZoneComposite(String zoneId, String zoneName, String zoneType, int ownPowerkW) {
            this.zoneId = zoneId;
            this.zoneName = zoneName;
            this.zoneType = zoneType;
            this.ownPowerkW = ownPowerkW;
        }

        public CityZoneComposite(String zoneId, String zoneName, String zoneType) {
            this(zoneId, zoneName, zoneType, 0);
        }

        public CityZoneComposite(String zoneName) {
            this("COMP-01", zoneName, "City Root Composite");
        }

        public void addZone(ZoneComponent component) {
            children.add(component);
        }

        public List<ZoneComponent> getChildren() {
            return children;
        }

        public String getZoneId() {
            return zoneId;
        }

        @Override
        public String getZoneName() {
            return zoneName;
        }

        public String getZoneType() {
            return zoneType;
        }

        @Override
        public int getPowerUsageKW() {
            int total = isShutdown ? 0 : ownPowerkW;
            for (ZoneComponent c : children) {
                total += c.getPowerUsageKW();
            }
            return total;
        }

        @Override
        public int getActiveNodesCount() {
            int total = 0;
            for (ZoneComponent c : children) {
                total += c.getActiveNodesCount();
            }
            return total;
        }

        @Override
        public String getStatus() {
            int active = getActiveNodesCount();
            if (active == 0) {
                return "Offline";
            }
            return getPowerUsageKW() > 300 ? "High Load" : "Active";
        }

        @Override
        public void setShutdown(boolean shutdown) {
            this.isShutdown = shutdown;
            for (ZoneComponent c : children) {
                c.setShutdown(shutdown);
            }
        }

        @Override
        public void reportUsage(SmartTerminal terminal, String indent) {
            if (terminal != null) {
                terminal.appendLog("INFO", indent + " [Composite] " + zoneName
                        + " | Total Power: " + getPowerUsageKW() + " kW | Active Nodes: " + getActiveNodesCount());
            }
            for (ZoneComponent c : children) {
                c.reportUsage(terminal, indent + " ");
            }
        }

    }

    private final CityZoneComposite city;

    public SmartZone() {
        city = new CityZoneComposite("CITY-00", "Alawwa Smart City", "Root Composite");

        CityZoneComposite district = new CityZoneComposite("CAT-01", "District", "Category", 0);
        CityZoneComposite street = new CityZoneComposite("CAT-02", "Street", "Category", 0);
        CityZoneComposite building = new CityZoneComposite("CAT-03", "Building", "Category", 0);

        building.addZone(new streetNodeLeaf("BL-01", "Hospital Block A", "Building", 85, 10));
        building.addZone(new streetNodeLeaf("BL-02", "Tech Park Building", "Building", 60, 10));
        building.addZone(new streetNodeLeaf("BL-03", "Apartment C (Block 1)", "Building", 35, 10));
        building.addZone(new streetNodeLeaf("BL-04", "Apartment C (Block 2)", "Building", 25, 10));
        building.addZone(new streetNodeLeaf("BL-05", "Apartment C (Block 3)", "Building", 20, 10));

        street.addZone(new streetNodeLeaf("ST-01", "Narammala Street", "Street", 120, 10));
        street.addZone(building);

        CityZoneComposite aptD = new CityZoneComposite("BL-06", "Apartment D", "Building", 45);
        aptD.addZone(new streetNodeLeaf("FL-01", "Apt D - Floor 1", "Floor", 25, 5));

        CityZoneComposite aptE = new CityZoneComposite("BL-07", "Apartment E", "Building", 40);
        aptE.addZone(new streetNodeLeaf("FL-02", "Apt E - Floor 1", "Floor", 20, 5));

        district.addZone(new streetNodeLeaf("DIST-01", "Central District", "District", 345, 10));
        district.addZone(street);
        district.addZone(aptD);
        district.addZone(aptE);

        city.addZone(district);
    }

    public CityZoneComposite getRootCity() {
        return city;
    }

    public ZoneComponent findZoneByName(String searchName) {
        if (searchName == null || searchName.trim().isEmpty()) {
            return city;
        }
        ZoneComponent found = searchRecursive(city, searchName.trim());
        return found != null ? found : city;
    }

    private ZoneComponent searchRecursive(ZoneComponent current, String targetName) {
        if (current.getZoneName().equalsIgnoreCase(targetName)) {
            return current;
        }
        if (current instanceof CityZoneComposite) {
            for (ZoneComponent child : ((CityZoneComposite) current).getChildren()) {
                ZoneComponent match = searchRecursive(child, targetName);
                if (match != null) {
                    return match;
                }
            }
        }
        return null;
    }

    public void setSubtreePowerState(DefaultMutableTreeNode rootNode, boolean powerOn, SmartZoneTree tree, SmartZoneTable table) {
        applyPowerRecursive(rootNode, powerOn);
        ZoneComponent modelComp = findZoneByName(rootNode.getUserObject().toString());
        if (modelComp != null) {
            modelComp.setShutdown(!powerOn);
        }
        tree.updateBoundTable();
        table.applyCustomRenderers();
        tree.repaint();
    }

    private void applyPowerRecursive(DefaultMutableTreeNode node, boolean powerOn) {
        if (node.getUserObject() instanceof SmartZoneTree.ZoneData) {
            SmartZoneTree.ZoneData data = (SmartZoneTree.ZoneData) node.getUserObject();
            data.setPowerState(powerOn);
            if (!powerOn && !"Category".equals(data.zoneType)) {
                data.hasAlert = true;
            }
        }
        Enumeration<TreeNode> children = node.children();
        while (children.hasMoreElements()) {
            applyPowerRecursive((DefaultMutableTreeNode) children.nextElement(), powerOn);

        }
    }

    public int reportSubTreeUsage(DefaultMutableTreeNode node, SmartTerminal terminal, String indent) {

        ZoneComponent component = findZoneByName(node.getUserObject().toString());
        component.reportUsage(terminal, indent);
        return component.getPowerUsageKW();
    }

    public int countActiveZone(SmartZoneTree tree) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        List<SmartZoneTree.ZoneData> list = new ArrayList<>();
        tree.collectZoneData(root, list);
        int active = 0;
        for (SmartZoneTree.ZoneData d : list) {
            if (d.powerOn) {
                active++;
            }
        }
        return active;
    }
}
