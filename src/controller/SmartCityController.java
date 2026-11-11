package controller;

import components.SmartTerminal;
import model.CitizenComplaint;
import model.DataSecurity;
import model.Policy;
import model.SmartZone;
import model.StreetLightFlyweight;

public class SmartCityController {
 
    private static final SmartCityController SMART_CITY_CONTROLLER = new SmartCityController();
    
    public static SmartCityController getInstance(){
        return SMART_CITY_CONTROLLER;
    }
    
    private final StreetLightFlyweight streetLightFlyweight = new StreetLightFlyweight();
    private final SmartZone smartZone = new SmartZone();
    private final CitizenComplaint.ComplaintHandler complaintChain = CitizenComplaint.createEscalationChain();
    
    private int currentPowerKW = 435;
    private int peakLoadKW = 345;
    private int activeAlerts = 0;
    private String securityPercent = "100%";
    private String securityLabel = "Encrypted";
    private int lastEscalatedLevel = 1;
    
    private SmartCityController(){
        this.currentPowerKW = smartZone.getRootCity().getPowerUsageKW();
    }
    
    public String secureAndSendStream(String rawPayload, boolean aes, boolean hmac, boolean gzip, SmartTerminal terminal){
    
        DataSecurity.DataStream stream = new DataSecurity.RawSensorStream();
        if(gzip){
            stream = new DataSecurity.GzipCompressionDecorder(stream);
            if(terminal != null){
                terminal.appendLog("WRAPPER", "Applied CompressionDecorator (GZIP)");
            }
        }
        
        if(hmac){
            stream = new DataSecurity.HmacAuthDecorator(stream);
            if(terminal != null){
                terminal.appendLog("WRAPPER", "Applied AuthenticationDecorator (HMAC-SHA256)");
            }
        }
        
        if(aes){
            stream = new DataSecurity.Aes256EncryptionDecorator(stream);
            if(terminal != null){
                terminal.appendLog("WRAPPER", "Applied EncryptionDecorator (AES-256)");
            }
        }
        
        updateSecurityState(aes, hmac, gzip);
        String packet = stream.writeData(rawPayload);
        if(terminal != null){
            terminal.appendLog("SECURITY", "Stream Configuration: " + stream.getSecurityStatus());
            terminal.appendLog("SUCCESS", "Transmitted Packet: " + packet);
        }
        return packet;
    }
    
    public int submitCitizenComplaint(String name, String contact, String category, String zone, String desc, SmartTerminal terminal){
        CitizenComplaint.ComplaintRequest request = new CitizenComplaint.ComplaintRequest(name, contact, category, zone, desc);
        this.lastEscalatedLevel = complaintChain.handle(request, terminal);
        if(this.lastEscalatedLevel > CitizenComplaint.LEVEL_LOCAL_STAF){
            this.activeAlerts++;
        }
        return this.lastEscalatedLevel;
    }
    
    public Policy.EvaluationReport evaluateCityPolicy(String dslRule, String targetZone, String currentLoadStr){
        Policy.EvaluationReport report = Policy.evaluatePolicyRule(dslRule, targetZone, currentLoadStr);
        this.peakLoadKW = Policy.parseSafeInt(currentLoadStr, this.peakLoadKW);
        return report;
    }

    public int getCurrentPowerKW() {
        return currentPowerKW;
    }

    public void setCurrentPowerKW(int currentPowerKW) {
        this.currentPowerKW = currentPowerKW;
    }

    public int getPeakLoadKW() {
        return peakLoadKW;
    }

    public void setPeakLoadKW(int peakLoadKW) {
        this.peakLoadKW = peakLoadKW;
    }
    
    public int getTotalLights(){
        return streetLightFlyweight.getTotalSimulatedLights();
    }
    
    public int getActiveAlerts(){
        return activeAlerts;
    }

    public void setActiveAlerts(int activeAlerts) {
        this.activeAlerts = activeAlerts;
    }

    public void setSecurityPercent(String securityPercent) {
        this.securityPercent = securityPercent;
    }

    public String getSecurityPercent() {
        return securityPercent;
    }

    public String getSecurityLabel() {
        return securityLabel;
    }
    
   public void updateSecurityState(boolean aes, boolean hmac, boolean gzip){
       int count = (aes ? 1 : 0) + (hmac ? 1 : 0) + (gzip ? 1 : 0);
       if(count == 3){
           securityPercent = "100%";
           securityLabel = "Encrypted";
       }else if(count == 2){
           securityPercent = "85%";
           securityLabel = "Protected";
       }else if(count == 1){
           securityPercent = "60%";
           securityLabel = "Partial";
       }else{
           securityPercent = "0%";
           securityLabel = "Unsecured";
       }
   }

    public int getLastEscalatedLevel() {
        return lastEscalatedLevel;
    }

    public void setLastEscalatedLevel(int lastEscalatedLevel) {
        this.lastEscalatedLevel = lastEscalatedLevel;
    }

    public StreetLightFlyweight getStreetLightFlyweight() {
        return streetLightFlyweight;
    }

    public SmartZone getSmartZone(){
        return smartZone;
    }
    
    public SmartZone.CityZoneComposite getCityZoneComposite() {
        return smartZone.getRootCity();
    } 

}
