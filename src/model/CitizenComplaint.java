package model;

import components.SmartTerminal;

public class CitizenComplaint {

    public static final int LEVEL_LOCAL_STAF = 1;
    public static final int LEVEL_SUPERVISOR = 2;
    public static final int LEVEL_CITY_MANAGER = 3;
    
    public static class ComplaintRequest {

        private final String citizenName;
        private final String contactInfo;
        private final String category;
        private final String zone;
        private final String description;

        public ComplaintRequest(String citizenName, String contactInfo, String category, String zone, String description) {
            this.citizenName = citizenName;
            this.contactInfo = contactInfo;
            this.category = category;
            this.zone = zone;
            this.description = description;
        }

        public String getCitizenName() {
            return citizenName;
        }

        public String getContactInfo() {
            return contactInfo;
        }

        public String getCategory() {
            return category;
        }

        public String getZone() {
            return zone;
        }

        public String getDescription() {
            return description;
        }

    }

    public static abstract class ComplaintHandler{
        private ComplaintHandler handler;
        
        public ComplaintHandler getHandler(){
            return handler;
        }

        public void setHandler(ComplaintHandler handler) {
            this.handler = handler;
        }
        
        public abstract int handle(ComplaintRequest request, SmartTerminal terminal);
    }
    
    public static class LocalFieldStaffHandler extends ComplaintHandler{

        @Override
        public int handle(ComplaintRequest request, SmartTerminal terminal) {
            String desc = request.getDescription().toLowerCase();
            String cat = request.getCategory().toLowerCase();
            
            boolean needsEscalation = cat.contains("multiple") || cat.contains("critical")
                    || cat.contains("overload") || cat.contains("damage")
                    || cat.contains("emergency") || cat.contains("blackout")
                    || desc.contains("multiple") || desc.contains("overload")
                    || desc.contains("main grid") || desc.contains("blackout")
                    || desc.contains("city-wide");
            
            if(!needsEscalation){
                if(terminal != null){
                    terminal.appendLog("SUCCESS", "Level 1 (Local Field Staff) handling complaint for "
                            + request.getCitizenName() + " in " + request.getZone() + " [ETA: 15 mins]");
                }
                return LEVEL_LOCAL_STAF;
            }else{
                if(terminal != null){
                    terminal.appendLog("ROUTING", "Level 1 cannot handle high severity. Escalating to Level 2 Supervisor...");
                }
                if(this.getHandler() != null){
                    return this.getHandler().handle(request, terminal);
                }
                return LEVEL_LOCAL_STAF;
            }
        }
 
    }
    
    public static class MaintenanceSupervisorHandler extends ComplaintHandler{

        @Override
        public int handle(ComplaintRequest request, SmartTerminal terminal) {
            String desc = request.getDescription().toLowerCase();
            String cat = request.getCategory().toLowerCase();
            
            boolean isCriticalEmergency = cat.contains("emergency") 
                    || cat.contains("blackout")
                    || desc.contains("main grid") || desc.contains("blackout")
                    || desc.contains("city-wide");
            
            if(!isCriticalEmergency){
                if(terminal != null){
                    terminal.appendLog("WARNING", "Level 2 (Maintenance Supervisor) handling infrastructure issue for "
                            + request.getCitizenName() + " [ETA: 30 mins]");
                }
                return LEVEL_SUPERVISOR;
            }else{
                if(terminal != null){
                    terminal.appendLog("ROUTING", "Level 2 escalating critical grid emergency to Level 3 City Manager...");
                }
                if(this.getHandler() != null){
                    return this.getHandler().handle(request, terminal);
                }
                return LEVEL_SUPERVISOR;
            }
                    
        }
        
    }
    
    public static class CityManagerHandler extends ComplaintHandler{

        @Override
        public int handle(ComplaintRequest request, SmartTerminal terminal) {
            if(terminal != null){
                terminal.appendLog("CRITICAL", "Level 3 (City Manager) took command of critical complaint from "
                        + request.getCitizenName() + " in " + request.getZone() + " [Immediate Dispatch]");
            }
            return LEVEL_CITY_MANAGER;
        }

    }
    
    public static ComplaintHandler createEscalationChain(){
        LocalFieldStaffHandler level1 = new LocalFieldStaffHandler();
        MaintenanceSupervisorHandler level2 = new MaintenanceSupervisorHandler();
        CityManagerHandler level3 = new CityManagerHandler();
        
        level1.setHandler(level2);
        level2.setHandler(level3);
        
        return level1;
    }

}
