package model;

import java.util.ArrayList;
import java.util.List;

public class SmartZone {
    
    public interface ZoneComponent{
        String getZoneName();
        int getPowerUsageKW();
        int getActiveNodesCount();
    }
    
    public static class streetNodeLeaf implements ZoneComponent{
    
        private final String name;
        private int powerUsageKW;
        private final int activeNodes;

        public streetNodeLeaf(String name, int powerUsageKW, int activeNodes) {
            this.name = name;
            this.powerUsageKW = powerUsageKW;
            this.activeNodes = activeNodes;
        }

        public void setPowerUsageKW(int powerUsageKW) {
            this.powerUsageKW = powerUsageKW;
        }

        @Override
        public String getZoneName() {
           return name;
        }

        @Override
        public int getPowerUsageKW() {
            return powerUsageKW;
        }

        @Override
        public int getActiveNodesCount() {
            return activeNodes;
        }   
    
    }
    
    public static class CityZoneComposite implements ZoneComponent{
        private final String zoneName;
        private final List<ZoneComponent> children = new ArrayList<>();

        public CityZoneComposite(String zoneName) {
            this.zoneName = zoneName;
        }
        
        public void addZone(ZoneComponent component){
            children.add(component);
        }

        @Override
        public String getZoneName() {
            return zoneName;
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
        
    }
    
}
