package model;

import components.SmartTerminal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class SmartZone {
    
    public interface ZoneComponent{
        String getZoneId();
        String getZoneName();
        String getZoneType();
        int getPowerUsageKW();
        int getActiveNodesCount();
        String getStatus();
        void setShutdown(boolean shutdown);
        void reportUsage(SmartTerminal terminal, String indent);
    }
    
    public static class streetNodeLeaf implements ZoneComponent{
        
        private final String zoneId;
        private final String name;
        private final String type;
        private final int normalPowerKW;
        private final int activeNodes;
        private boolean isShutdown = false;

        
        
        public streetNodeLeaf(String zoneId, String name,String type, int normalPowerKW, int activeNodes) {
            this.zoneId = zoneId;
            this.name = name;
            this.type = type;
            this.normalPowerKW = normalPowerKW;
            this.activeNodes = activeNodes;
        }

        public streetNodeLeaf(String name, int normalPowerKW, int activeNodes){
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
            if(isShutdown){
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
            if(terminal != null){
                terminal.appendLog("Zone", indent + "└─ [Leaf] " + name + " (" + zoneId + ") -> "
                        + getPowerUsageKW() + " kW | Nodes: " + getActiveNodesCount() + " | Status: " + getStatus());
            }
        }
  
    }
    
    public static class CityZoneComposite implements ZoneComponent{
        
        private final String zoneId;
        private final String zoneName;
        private final String zoneType;
        private final List<ZoneComponent> children = new ArrayList<>();

        public CityZoneComposite(String zoneId ,String zoneName, String zoneType) {
            this.zoneId = zoneId;
            this.zoneName = zoneName;
            this.zoneType = zoneType;
        }
        
        public CityZoneComposite(String zoneName){
            this("COMP-01", zoneName, "City Root Composite");
        }
        
        public void addZone(ZoneComponent component){
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
            int total = 0;
            for(ZoneComponent c : children){
                total += c.getPowerUsageKW();
            }
            return total;
        }

        @Override
        public int getActiveNodesCount() {
           int total = 0;
           for(ZoneComponent c : children){
               total += c.getActiveNodesCount();
           }
           return total;
        }

        @Override
        public String getStatus() {
           int active = getActiveNodesCount();
           if(active == 0){
               return "Offline";
           }
           return getPowerUsageKW() > 300 ? "High Load" : "Active";
        }

        @Override
        public void setShutdown(boolean shutdown) {
            for(ZoneComponent c : children){
                c.setShutdown(shutdown);
            }
        }

        @Override
        public void reportUsage(SmartTerminal terminal, String indent) {
            if(terminal != null){
                terminal.appendLog("INFO", indent + " [Composite] " + zoneName
                        + " | Total Power: " + getPowerUsageKW() + " kW | Active Nodes: " + getActiveNodesCount());
            }
            for(ZoneComponent c : children){
                c.reportUsage(terminal, indent + " ");
            }
        }
  
    }
    
    private final CityZoneComposite city;
    
    public SmartZone(){
        city = new CityZoneComposite("CITY-00", "Alawwa Smart City", "Root Composite");
        
        CityZoneComposite centralDistrict = new CityZoneComposite("DIST-01", "Central District", "District Composite");
        centralDistrict.addZone(new streetNodeLeaf("ZN-101", "Narammala Road Grid", "Smart Street", 145, 42));
        centralDistrict.addZone(new streetNodeLeaf("ZN-102", "Central Hospital Sector", "Critical Grid", 95, 28));
        centralDistrict.addZone(new streetNodeLeaf("ZN-103", "Central Bus Station", "Public Zone", 60, 18));
        
        CityZoneComposite industrialZone = new CityZoneComposite("DIST-02", "Industrial Sector", "Sector Composite");
        industrialZone.addZone(new streetNodeLeaf("ZN-201", "EV Charging Hub A", "EV Station", 85, 22));
        industrialZone.addZone(new streetNodeLeaf("ZN-202", "Water Pumping Station", "Utility Node", 50, 18));
        
        city.addZone(centralDistrict);
        city.addZone(industrialZone);
    }
    
    public CityZoneComposite getRootCity(){
        return city;
    }
    
    public ZoneComponent findZoneByName(String searchName){
        if(searchName == null || searchName.trim().isEmpty()){
            return city;
        }
       ZoneComponent found = searchRecursive(city, searchName.trim());
       return found != null ? found : city;
    }
    
    private ZoneComponent searchRecursive(ZoneComponent current, String targetName){
        if (current.getZoneName().equalsIgnoreCase(targetName)
                || targetName.toLowerCase().contains(current.getZoneName().toLowerCase())){
            return current;
        }
        if(current instanceof CityZoneComposite){
            for(ZoneComponent child : ((CityZoneComposite) current).getChildren()){
                ZoneComponent match = searchRecursive(child, targetName);
                if(match != null){
                    return match;
                }
            }
        }
        return null;
    }
    
    public void PopulateJTreen(JTree tree){
        DefaultMutableTreeNode rootNode = buildTreeNode(city);
        tree.setModel(new DefaultTreeModel(rootNode));
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
    
    private DefaultMutableTreeNode buildTreeNode(ZoneComponent component){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(component.getZoneName());
        if(component instanceof CityZoneComposite){
            for(ZoneComponent child : ((CityZoneComposite) component).getChildren()){
                node.add(buildTreeNode(child));
            }
        }
        return node;
    }
    
    public void populateJTable(JTable table, ZoneComponent filterRoot){
        ZoneComponent target = (filterRoot != null) ? filterRoot : city;
        String[] columns = {"Zone ID", "Zone / Sector Name", "Component Type", "Active Nodes", "Power (kW)", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }  
        };
        
        List<ZoneComponent> flatList = new ArrayList<>();
        collectComponents(target, flatList);
        
        for(ZoneComponent comp : flatList){
            model.addRow(new Object[]{
                comp.getZoneId(),
                comp.getZoneName(),
                comp.getZoneType(),
                comp.getActiveNodesCount(),
                comp.getPowerUsageKW() + " kW",
                comp.getStatus()
            });
        }
        table.setModel(model);   
    }
    
    private void collectComponents(ZoneComponent comp, List<ZoneComponent> list){
        list.add(comp);
        if(comp instanceof CityZoneComposite){
            for(ZoneComponent child : ((CityZoneComposite) comp).getChildren()){
                collectComponents(child, list);
            }
        }
    }
    
    public int getTotalActiveZoneCount(){
        List<ZoneComponent> list = new ArrayList<>();
        collectComponents(city, list);
        int activeCount = 0;
        for(ZoneComponent c : list){
            if(!"Offline".equalsIgnoreCase(c.getStatus())){
                activeCount++;
            }
        }
        return activeCount;
    }
}

