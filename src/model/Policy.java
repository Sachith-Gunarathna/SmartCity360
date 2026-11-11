package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Policy {
    
    private static final int DEFAULT_THRESHOLD = 300;
    private static final int DEFAULT_ACTUAL_LOAD = 345;
    
    public interface Expression{
        int interpret();
    }
    
    public static class TerminalExpression implements Expression{
        private final int value;
        
        public TerminalExpression(int value){
            this.value = value;
        }
        
        public TerminalExpression(boolean conditionPassed){
            this.value = conditionPassed ? 1 : 0;
        }

        @Override
        public int interpret() {
            return this.value;
        }
    }
    
    public static class NonTerminalExpression implements Expression{
        private final Expression e1;
        private final Expression e2;
        private final String operator;
        
        public NonTerminalExpression(Expression e1, Expression e2, String operator){
            this.e1 = e1;
            this.e2 = e2;
            this.operator = operator;
        }

        @Override
        public int interpret() {
            if("OR".equalsIgnoreCase(operator)){
                return (e1.interpret() == 1 || e2.interpret() == 1) ? 1 : 0;
            }
            return (e1.interpret() == 1 && e2.interpret() == 1) ? 1 : 0;
        }
    }
    
    public static class EvaluationReport{
        
        public final boolean zonePassed;
        public final boolean loadPassed;
        public final boolean policyExecuted;
        public final String parsedZone;
        public final int parsedThreshold;
        public final String parsedAction;

        public EvaluationReport(boolean zonePassed, boolean loadPassed, boolean policyExecuted, String parsedZone, int parsedThreshold, String parsedAction) {
            this.zonePassed = zonePassed;
            this.loadPassed = loadPassed;
            this.policyExecuted = policyExecuted;
            this.parsedZone = parsedZone;
            this.parsedThreshold = parsedThreshold;
            this.parsedAction = parsedAction;
        }
        
    }
    
    public static EvaluationReport evaluatePolicyRule(String dslRule,String targetZone, String currentLoadInput){
        String expectedZone = "Central District";
        int threshold = DEFAULT_THRESHOLD;
        String logicOp = "AND";
        String action = "DIM_LIGHTS 40% AND ACTIVATE_BACKUP";
        
        if(dslRule != null && !dslRule.trim().isEmpty()){
            Matcher zoneMatcher = Pattern.compile("ZONE\\s*==\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE).matcher(dslRule);
            if(zoneMatcher.find()){
                expectedZone = zoneMatcher.group(1).trim();
            }
            
            Matcher metricMatcher = Pattern.compile("(?:USAGE|LOAD|NOISE|PARKING)\\s*>\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(dslRule);
            if(metricMatcher.find()){
                threshold = parseSafeInt(metricMatcher.group(1), DEFAULT_THRESHOLD);
            }
            if(dslRule.toUpperCase().contains(" OR ")){
                logicOp = "OR";
            }
            
            Matcher thenMatcher = Pattern.compile("THEN\\s+(.+)", Pattern.CASE_INSENSITIVE).matcher(dslRule);
            if(thenMatcher.find()){
                action = thenMatcher.group(1).trim();
            }
        }
        
       int actualValue = parseSafeInt(currentLoadInput, DEFAULT_ACTUAL_LOAD);
       boolean zoneMatch = targetZone != null && targetZone.toLowerCase().contains(expectedZone.toLowerCase());
       boolean metricMatch = actualValue > threshold;
       
       Expression leftTerminal = new TerminalExpression(zoneMatch);
       Expression rightTerminal = new TerminalExpression(metricMatch);
       Expression rootNonTerminal = new NonTerminalExpression(leftTerminal, rightTerminal, logicOp);
       
       boolean finalDecision = (rootNonTerminal.interpret() == 1);
       return new EvaluationReport(zoneMatch, metricMatch, finalDecision, expectedZone, threshold, action);
    }
    
    public static int parseSafeInt(String input, int fallbackValue){
        if(input == null){
            return fallbackValue;
        }
        
        String digits = input.replaceAll("[^0-9]", "");
        if(digits.isEmpty()){
            return fallbackValue;
        }
        return Integer.parseInt(digits);
    }
    
}
