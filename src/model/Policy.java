package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Policy {
    
    public static class EvaluationReport{
        
        public final boolean zonePassed;
        public final boolean loadPassed;
        public final boolean finalExpressionTrue;
        public final String parsedZone;
        public final int parsedThresholdKW;
        public final String parsedAction;

        public EvaluationReport(boolean zonePassed, boolean loadPassed, boolean finalExpressionTrue, String parsedZone, int parsedThresholdKW, String parsedAction) {
            this.zonePassed = zonePassed;
            this.loadPassed = loadPassed;
            this.finalExpressionTrue = finalExpressionTrue;
            this.parsedZone = parsedZone;
            this.parsedThresholdKW = parsedThresholdKW;
            this.parsedAction = parsedAction;
        }
        
    }
    
    public static EvaluationReport evaluateDsl(String dslRule,String targetZone,String currentLoadStr){
        String expectedZone = "Central District";
        int thresholdKW = 300;
        String actionText = "Dim Street Lights 40%";
        boolean isOrLogic = false;
        
        if(dslRule != null && !dslRule.isEmpty()){
            Matcher zm = Pattern.compile("ZONE\\s*==\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE).matcher(dslRule);
            if(zm.find()){
                expectedZone = zm.group(1).trim();
            }
            
            Matcher lm = Pattern.compile("(?:USAGE|LOAD)\\s*>\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(dslRule);
            if(lm.find()){
                thresholdKW = Integer.parseInt(lm.group(1).trim());
            }
            
            if(dslRule.toUpperCase().contains(" OR ")){
                isOrLogic = true;
            }
            
            Matcher tm = Pattern.compile("THEN\\s+(.+)", Pattern.CASE_INSENSITIVE).matcher(dslRule);
            if(tm.find()){
                actionText = tm.group(1).trim();
            }
        }
        
        int currentLoadKW = 345;
        if(currentLoadStr != null){
            try {
                currentLoadKW = Integer.parseInt(currentLoadStr.replace("[^0-9]", ""));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        
        boolean zonePass = targetZone != null && targetZone.toLowerCase().contains(expectedZone.toLowerCase());
        boolean loadPass = currentLoadKW > thresholdKW;
        boolean finalResult = isOrLogic ? (zonePass || loadPass) : (zonePass && loadPass);
        
        return new EvaluationReport(zonePass, loadPass, finalResult, expectedZone, thresholdKW, actionText);
    }
    
}
