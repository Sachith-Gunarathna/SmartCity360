package model;

public class CitizenComplaint {

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

    public static class RoutingResult {

        private final int assignedLevel;
        private final String handlerTitle;
        private final String eta;
        private final String status;
        private final String logMessage;

        public RoutingResult(int assignedLevel, String handlerTitle, String eta, String status, String logMessage) {
            this.assignedLevel = assignedLevel;
            this.handlerTitle = handlerTitle;
            this.eta = eta;
            this.status = status;
            this.logMessage = logMessage;
        }

        public int getAssignedLevel() {
            return assignedLevel;
        }

        public String getHandlerTitle() {
            return handlerTitle;
        }

        public String getEta() {
            return eta;
        }

        public String getStatus() {
            return status;
        }

        public String getLogMessage() {
            return logMessage;
        }

    }

    public static abstract class ComplaintHandler {

        protected ComplaintHandler nextHandler;

        public void setNextHandler(ComplaintHandler nextHandler) {
            this.nextHandler = nextHandler;
        }

        public abstract RoutingResult handleComplaint(ComplaintRequest request);
    }

    public static class LocalFieldStaffHandler extends ComplaintHandler {

        @Override
        public RoutingResult handleComplaint(ComplaintRequest request) {
            String desc = request.getDescription().toLowerCase();
            String cat = request.getCategory().toLowerCase();

            boolean isMajorGridFailure = desc.contains("main grid") || desc.contains("blackout") || cat.contains("emergency");
            boolean isSupervisorLevel = cat.contains("infrastructure") || desc.contains("multiple");

            if (!isMajorGridFailure && !isSupervisorLevel) {
                return new RoutingResult(1,
                        "Level 1: Local Field Staff",
                        "15 mins",
                        "Dispatched",
                        "[SUCCESS] Level 1 dispatch sent to available field staff: EM_2941 for Zone '" + request.getZone() + "'"
                );
            }

            if (nextHandler != null) {
                return nextHandler.handleComplaint(request);
            }
            return null;
        }
    }

    public static class MaintenanceSupervisorHandler extends ComplaintHandler {

        @Override
        public RoutingResult handleComplaint(ComplaintRequest request) {
            String desc = request.getDescription().toLowerCase();
            boolean isCityWideEmergency = desc.contains("city-wide") || desc.contains("critical");

            if (!isCityWideEmergency) {
                return new RoutingResult(
                        2,
                        "Level 2: Maintenance Supervisor",
                        "30 mins",
                        "Pending / Escalation point",
                        "[ROUTING] Escalated to Level 2 Maintenance Supervisor for '" + request.getCategory() + "' in " + request.getZone()
                );
            }

            if (nextHandler != null) {
                return nextHandler.handleComplaint(request);
            }
            return null;
        }

    }

    public static class CityManagerHandler extends ComplaintHandler {

        @Override
        public RoutingResult handleComplaint(ComplaintRequest request) {
            return new RoutingResult(
                    3,
                    "Level 3: City Manager",
                    "45 mins",
                    "Awaiting Level 2 Escalation",
                    "[CRITICAL] High-priority escalation routed to Level 3 City Manager for Citizen: " + request.getCitizenName()
            );
        }

    }

    public static ComplaintHandler buildDefaultChain() {
        ComplaintHandler level1 = new LocalFieldStaffHandler();
        ComplaintHandler level2 = new MaintenanceSupervisorHandler();
        ComplaintHandler level3 = new CityManagerHandler();

        level1.setNextHandler(level2);
        level2.setNextHandler(level3);
        return level1;
    }

}
