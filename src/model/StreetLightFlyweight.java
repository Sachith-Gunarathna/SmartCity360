package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreetLightFlyweight {
   
    public static class LightModelFlyweight{
        private final String modelCode;
        private final String typeLabel;
        private final int wattage;

        public LightModelFlyweight(String modelCode, String typeLabel, int wattage) {
            this.modelCode = modelCode;
            this.typeLabel = typeLabel;
            this.wattage = wattage;
        }

        public String getModelCode() {
            return modelCode;
        }

        public String getTypeLabel() {
            return typeLabel;
        }

        public int getWattage() {
            return wattage;
        }
 
    }
    
    public static class LightFactory{
        private static final Map<String, LightModelFlyweight> SHARED_POOL = new HashMap<>();
        
        public static LightModelFlyweight getLightModel(String key){
            String normalized = key.toUpperCase();
            if(!SHARED_POOL.containsKey(normalized)){
                if("LED".equals(normalized)){
                    SHARED_POOL.put("LED", new LightModelFlyweight(
                            "LED_150W_Cyan", "Active Node (LED)", 150));
                }else{
                    SHARED_POOL.put("COMPACT", new LightModelFlyweight(
                            "COMP_60W_Amber", "Active Node (Compact)", 60));
                }
            }
            
            return SHARED_POOL.get(normalized);
        }
        
        public static int getIntrinsicTypeCount(){
            return SHARED_POOL.size();
        }
    }
    
    public static class DeployedLightContext{
        private final int id;
        private final String streetName;
        private final float x;
        private final float y;
        private final LightModelFlyweight intrinsicModel;
        
        public DeployedLightContext(int id, String streetName, float x, float y, LightModelFlyweight intrinsicModel){
            this.id = id;
            this.streetName = streetName;
            this.x = x;
            this.y = y;
            this.intrinsicModel = intrinsicModel;
        }

        public int getId() {
            return id;
        }

        public String getStreetName() {
            return streetName;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public LightModelFlyweight getIntrinsicModel() {
            return intrinsicModel;
        }

    }
    
    private final List<DeployedLightContext> deployedNodes = new ArrayList<>();
    private int totalSimulatedLights = 1540;
    
    public StreetLightFlyweight(){
        LightFactory.getLightModel("LED");
        LightFactory.getLightModel("COMPACT");
    }
    
    public DeployedLightContext deployNode(String typeKey,String streetName,float x,float y){
        LightModelFlyweight sharedModel = LightFactory.getLightModel(typeKey);
        totalSimulatedLights++;
        DeployedLightContext context = new DeployedLightContext(
                totalSimulatedLights, streetName, x, y, sharedModel);
        deployedNodes.add(context);
        return context;
    }

    public int getTotalSimulatedLights() {
        return totalSimulatedLights;
    }
    
    public int getBeforeFlyweightRamMB(){
        return (int) Math.round(totalSimulatedLights * 0.2026);
    }
    
    public int getWithFlyweightRamMB(){
        return (int) Math.round(totalSimulatedLights * 0.01558);
    }
    
    public double getMemorySavePrecentage(){
        int before = getBeforeFlyweightRamMB();
        int after = getWithFlyweightRamMB();
        return Math.round(((double)(before - after) / before) * 1000.0) / 10.0;
    }
}
