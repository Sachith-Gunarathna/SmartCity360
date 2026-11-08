package model;

import components.SmartTerminal;
import java.util.ArrayList;
import java.util.List;

public class StreetLightFlyweight {

    private static final int INITIAL_SIMULATED_LIGHTS = 1540;
    private static final double RAM_PER_NON_FLYWEIGHT_MB = 0.2026;
    private static final double RAM_PER_FLYWEIGHT_MB = 0.01558;

    public static abstract class AbstractStreetLight {

        public abstract void display(SmartTerminal terminal);
    }

    public static class SharedStreetLight {

        private final String type;
        private final int wattage;
        private final String colorSpectrum;

        public SharedStreetLight(String type, int wattage, String colorSpectrum) {
            this.type = type;
            this.wattage = wattage;
            this.colorSpectrum = colorSpectrum;
        }

        public String getType() {
            return type;
        }

        public int getWattage() {
            return wattage;
        }

        public String getColorSpectrum() {
            return colorSpectrum;
        }

    }

    public static class SharedStreetLightPool {

        private static final ArrayList<SharedStreetLight> sharedLightList = new ArrayList<>();

        public static SharedStreetLight getSharedLight(String type, int wattage, String colorSpectrum) {
            SharedStreetLight sl = null;
            for (SharedStreetLight item : sharedLightList) {
                if (type.equalsIgnoreCase(item.getType())
                        && wattage == item.getWattage()
                        && colorSpectrum.equalsIgnoreCase(item.getColorSpectrum())) {
                    sl = item;
                    break;
                }
            }

            if (sl == null) {
                sl = new SharedStreetLight(type, wattage, colorSpectrum);
                sharedLightList.add(sl);
            }
            return sl;
        }

        public static int getPoolSize() {
            return sharedLightList.size();
        }
    }

    public static class StreetLight extends AbstractStreetLight {

        private final int x;
        private final int y;
        private final String streetName;
        private final SharedStreetLight sharedLight;

        public StreetLight(String type, int wattage, String colorSpectrum, int x, int y, String streetName) {
            this.x = x;
            this.y = y;
            this.streetName = streetName;
            this.sharedLight = SharedStreetLightPool.getSharedLight(type, wattage, colorSpectrum);
        }

        @Override
        public void display(SmartTerminal terminal) {
            if (terminal != null) {
                terminal.appendLog("FLYWEIGHT", "Deployed [" + sharedLight.getType() + " " + sharedLight.getWattage()
                        + "W " + sharedLight.getColorSpectrum() + "] at (" + x + ", " + y + ") on " + streetName
                        + " | Shared Pool Objects: " + SharedStreetLightPool.getPoolSize());
            }
        }
    }

    private final List<StreetLight> deployedLights = new ArrayList<>();
    private int totalSimulatedLights = INITIAL_SIMULATED_LIGHTS;

    public StreetLightFlyweight() {
        SharedStreetLightPool.getSharedLight("LED", 150, "Cyan");
        SharedStreetLightPool.getSharedLight("COMPACT", 60, "Amber");
    }

    public StreetLight deployNode(String typeKey, String streetName, float normX, float normY, SmartTerminal terminal) {
        boolean isLed = "LED".equalsIgnoreCase(typeKey);
        int wattage = isLed ? 150 : 60;
        String spectrum = isLed ? "Cyan" : "Amber";

        totalSimulatedLights++;
        int coordX = Math.round(normX * 1000);
        int coordY = Math.round(normY * 1000);

        StreetLight light = new StreetLight(isLed ? "LED" : "COMPACT", wattage, spectrum, coordX, coordY, streetName);
        deployedLights.add(light);
        light.display(terminal);
        return light;
    }

    public StreetLight deployedLights(String typeKey, String streetName, float normX, float normY) {
        return deployNode(typeKey, streetName, normX, normY, null);
    }

    public int getTotalSimulatedLights() {
        return totalSimulatedLights;
    }

    public int getBeforeFlyweightRamMB() {
        return (int) Math.round(totalSimulatedLights * RAM_PER_NON_FLYWEIGHT_MB);
    }

    public int getWithFlyweightRamMB() {
        return (int) Math.round(totalSimulatedLights * RAM_PER_FLYWEIGHT_MB);
    }

}
