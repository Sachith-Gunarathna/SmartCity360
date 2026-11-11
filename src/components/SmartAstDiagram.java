package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class SmartAstDiagram extends JPanel {

    public interface Expression {

        boolean interpret(PolicyContext context);
    }

    public static class PolicyContext {

        public final String targetZone;
        public final int currentLoadKW;

        public PolicyContext(String targetZone, int currentLoadKW) {
            this.targetZone = targetZone != null ? targetZone : "";
            this.currentLoadKW = currentLoadKW;
        }
    }

    public static class ZoneTerminalExpression implements Expression {

        private final String expectedZone;

        public ZoneTerminalExpression(String expectedZone) {
            this.expectedZone = expectedZone;
        }

        @Override
        public boolean interpret(PolicyContext context) {
            return context.targetZone.toLowerCase().contains(expectedZone.toLowerCase());
        }
    }

    public static class LoadTerminalExpression implements Expression {

        private final String operator;
        private final int thresholdKW;

        public LoadTerminalExpression(String operator, int thresholdKW) {
            this.operator = operator;
            this.thresholdKW = thresholdKW;
        }

        @Override
        public boolean interpret(PolicyContext context) {
            switch (operator) {
                case ">":
                    return context.currentLoadKW > thresholdKW;
                case ">=":
                    return context.currentLoadKW >= thresholdKW;
                case "<":
                    return context.currentLoadKW < thresholdKW;
                case "<=":
                    return context.currentLoadKW <= thresholdKW;
                case "==":
                    return context.currentLoadKW == thresholdKW;
                default:
                    return context.currentLoadKW > thresholdKW;
            }
        }
    }

    public static class LogicalNonTerminalExpression implements Expression {

        private final String logicOp;
        private final Expression left;
        private final Expression right;

        public LogicalNonTerminalExpression(String logicOp, Expression left, Expression right) {
            this.logicOp = logicOp;
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean interpret(PolicyContext context) {
            if ("OR".equalsIgnoreCase(logicOp)) {
                return left.interpret(context) || right.interpret(context);
            }
            return left.interpret(context) && right.interpret(context);
        }
    }

    private String rootTitle = "AND Expression";
    private boolean rootStatus = true;

    private String leftTitle = "ZoneCheck: Central District";
    private boolean leftStatus = true;

    private String rightTitle = "LoadCheck: > 300 kW";
    private boolean rightStatus = true;

    private String actionTitle = "Action: Dim Street Lights 40%";
    private boolean actionStatus = true;

    private int hoveredNode = -1;
    private final Rectangle[] nodeBounds = new Rectangle[4];
    private SmartTerminal boundTerminal;

    public SmartAstDiagram() {
        setBackground(new Color(18, 25, 38));
        setPreferredSize(new Dimension(520, 340));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);

        for (int i = 0; i < 4; i++) {
            nodeBounds[i] = new Rectangle();
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < 4; i++) {
                    if (nodeBounds[i].contains(e.getPoint())) {
                        logNodeInspection(i);
                        break;
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int newHover = -1;
                for (int i = 0; i < 4; i++) {
                    if (nodeBounds[i].contains(e.getPoint())) {
                        newHover = i;
                        break;
                    }
                }
                if (newHover != hoveredNode) {
                    hoveredNode = newHover;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredNode != -1) {
                    hoveredNode = -1;
                    repaint();
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void bindTerminal(SmartTerminal terminal) {
        this.boundTerminal = terminal;
    }

    private void logNodeInspection(int nodeIndex) {
        if (boundTerminal == null) {
            return;
        }
        switch (nodeIndex) {
            case 0:
                boundTerminal.appendLog("INTERPRETER",
                        "Root NonTerminalExpression [" + rootTitle + "] evaluated to: " + (rootStatus ? "TRUE" : "FALSE"));
                break;
            case 1:
                boundTerminal.appendLog("INTERPRETER",
                        "Left TerminalExpression [" + leftTitle + "] -> " + (leftStatus ? "PASS" : "FAIL"));
                break;
            case 2:
                boundTerminal.appendLog("INTERPRETER",
                        "Right TerminalExpression [" + rightTitle + "] -> " + (rightStatus ? "PASS" : "FAIL"));
                break;
            case 3:
                boundTerminal.appendLog(actionStatus ? "SUCCESS" : "WARNING",
                        "Action Node [" + actionTitle + "] -> " + (actionStatus ? "EXECUTED" : "SKIPPED"));
                break;
        }
    }

    public void evaluatePolicy(String dslText, String currentZoneInput, String currentLoadInput) {
        String expectedZone = "Central District";
        String loadOp = ">";
        int loadLimit = 300;
        String logicOp = "AND";
        String parsedAction = "Dim Street Lights 40%";

        if (dslText != null && !dslText.trim().isEmpty()) {

            Matcher zoneMatcher = Pattern.compile("ZONE\\s*==\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE).matcher(dslText);
            if (zoneMatcher.find()) {
                expectedZone = zoneMatcher.group(1).trim();
            }

            Matcher loadMatcher = Pattern.compile("(?:USAGE|LOAD)\\s*([><=]+)\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(dslText);
            if (loadMatcher.find()) {
                loadOp = loadMatcher.group(1).trim();
                loadLimit = Integer.parseInt(loadMatcher.group(2).trim());
            }

            if (model.Policy.containsOr(dslText)) {
                logicOp = "OR";
            }

            Matcher thenMatcher = Pattern.compile("THEN\\s+(.+)", Pattern.CASE_INSENSITIVE).matcher(dslText);
            if (thenMatcher.find()) {
                String rawAction = thenMatcher.group(1).trim();
                if (rawAction.toUpperCase().contains("DIM_LIGHTS")) {
                    Matcher pct = Pattern.compile("(\\d+%)").matcher(rawAction);
                    String pctVal = pct.find() ? pct.group(1) : "40%";
                    parsedAction = "Dim Street Lights " + pctVal;
                } else {
                    parsedAction = rawAction;
                }
            }
        }

        int actualLoad = 345;
        if (currentLoadInput != null) {
            try {
                actualLoad = Integer.parseInt(currentLoadInput.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException ignored) {
            }
        }

        PolicyContext context = new PolicyContext(currentZoneInput, actualLoad);
        ZoneTerminalExpression zoneExpr = new ZoneTerminalExpression(expectedZone);
        LoadTerminalExpression loadExpr = new LoadTerminalExpression(loadOp, loadLimit);
        LogicalNonTerminalExpression rootExpr = new LogicalNonTerminalExpression(logicOp, zoneExpr, loadExpr);

        this.leftTitle = "ZoneCheck: " + expectedZone;
        this.leftStatus = zoneExpr.interpret(context);

        this.rightTitle = "LoadCheck: " + loadOp + " " + loadLimit + " kW";
        this.rightStatus = loadExpr.interpret(context);

        this.rootTitle = logicOp + " Expression";
        this.rootStatus = rootExpr.interpret(context);

        this.actionTitle = "Action: " + parsedAction;
        this.actionStatus = this.rootStatus;

        if (boundTerminal != null) {
            boundTerminal.appendLog("INTERPRETER", "Building AST for policy rule...");
            boundTerminal.appendLog("INTERPRETER", "Evaluating Context -> Zone: " + currentZoneInput + " (" + (leftStatus ? "PASS" : "FAIL")
                    + "), Load: " + actualLoad + " kW (" + (rightStatus ? "PASS" : "FAIL") + ")");
            if (rootStatus) {
                boundTerminal.appendLog("SUCCESS", "Policy evaluation [TRUE]. Executing -> " + parsedAction);
            } else {
                boundTerminal.appendLog("WARNING", "Policy conditions not met [FALSE]. Action skipped.");
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(getBackground());
        g2.fillRect(0, 0, w, h);

        int topW = Math.min(200, (int) (w * 0.40));
        int midW = Math.min(220, (int) (w * 0.44));
        int botW = Math.min(245, (int) (w * 0.50));
        int boxH = 60;

        int topX = (w - topW) / 2;
        int topY = 20;

        int midGap = Math.max(20, (w - (midW * 2)) / 3);
        int leftX = midGap;
        int rightX = w - midGap - midW;
        int midY = (h / 2) - (boxH / 2) - 4;

        int botX = (w - botW) / 2;
        int botY = h - boxH - 22;

        nodeBounds[0].setBounds(topX, topY, topW, boxH);
        nodeBounds[1].setBounds(leftX, midY, midW, boxH);
        nodeBounds[2].setBounds(rightX, midY, midW, boxH);
        nodeBounds[3].setBounds(botX, botY, botW, boxH);

        int rootBottomX = topX + topW / 2;
        int rootBottomY = topY + boxH;
        int leftTopX = leftX + midW / 2;
        int rightTopX = rightX + midW / 2;
        int midTopY = midY;

        drawBranchedConnector(g2, rootBottomX, rootBottomY, leftTopX, rightTopX, midTopY);

        int rightBottomX = rightX + midW / 2;
        int rightBottomY = midY + boxH;
        int botTopX = botX + botW / 2;
        int botTopY = botY;
        drawSingleStepConnector(g2, rightBottomX, rightBottomY, botTopX, botTopY);

        drawAstNode(g2, nodeBounds[0], rootTitle, rootStatus ? "[TRUE]" : "[FALSE]", rootStatus, hoveredNode == 0);
        drawAstNode(g2, nodeBounds[1], leftTitle, leftStatus ? "[PASS]" : "[FAIL]", leftStatus, hoveredNode == 1);
        drawAstNode(g2, nodeBounds[2], rightTitle, rightStatus ? "[PASS]" : "[FAIL]", rightStatus, hoveredNode == 2);
        drawAstNode(g2, nodeBounds[3], actionTitle, actionStatus ? "[EXECUTED]" : "[SKIPPED]", actionStatus, hoveredNode == 3);

        g2.dispose();
    }

    private void drawAstNode(Graphics2D g2, Rectangle r, String title, String badgeText, boolean isPositive, boolean isHovered) {
        int arc = 14;
        Color glowBase = isPositive ? new Color(34, 211, 238) : new Color(248, 113, 113);
        Color borderColor = isPositive ? new Color(45, 212, 225) : new Color(239, 68, 68);
        Color statusColor = isPositive ? new Color(74, 222, 128) : new Color(248, 113, 113);

        int maxGlow = isHovered ? 10 : 8;
        for (int i = maxGlow; i >= 2; i -= 2) {
            int alpha = isHovered ? 24 : 16;
            g2.setColor(new Color(glowBase.getRed(), glowBase.getGreen(), glowBase.getBlue(), alpha));
            g2.fill(new RoundRectangle2D.Float(r.x - i, r.y - i, r.width + (i * 2), r.height + (i * 2), arc + i, arc + i));
        }

        GradientPaint bgPaint = new GradientPaint(
                r.x, r.y, isHovered ? new Color(28, 42, 60) : new Color(22, 33, 48),
                r.x, r.y + r.height, new Color(18, 27, 40)
        );
        g2.setPaint(bgPaint);
        g2.fill(new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, arc, arc));

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, arc, arc));

        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 14));
        FontMetrics fm1 = g2.getFontMetrics();
        g2.setColor(new Color(241, 245, 249));
        int tx1 = r.x + (r.width - fm1.stringWidth(title)) / 2;
        int ty1 = r.y + 24;
        g2.drawString(title, tx1, ty1);

        g2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 14));
        FontMetrics fm2 = g2.getFontMetrics();
        g2.setColor(statusColor);
        int tx2 = r.x + (r.width - fm2.stringWidth(badgeText)) / 2;
        int ty2 = r.y + 47;
        g2.drawString(badgeText, tx2, ty2);
    }

    private void drawBranchedConnector(Graphics2D g2, int startX, int startY, int leftX, int rightX, int endY) {
        int midY = startY + (endY - startY) / 2;
        int radius = 8;

        Path2D leftPath = new Path2D.Float();
        leftPath.moveTo(startX, startY);
        leftPath.lineTo(startX, midY - radius);
        leftPath.quadTo(startX, midY, startX - radius, midY);
        leftPath.lineTo(leftX + radius, midY);
        leftPath.quadTo(leftX, midY, leftX, midY + radius);
        leftPath.lineTo(leftX, endY - 3);

        Path2D rightPath = new Path2D.Float();
        rightPath.moveTo(startX, startY);
        rightPath.lineTo(startX, midY - radius);
        rightPath.quadTo(startX, midY, startX + radius, midY);
        rightPath.lineTo(rightX - radius, midY);
        rightPath.quadTo(rightX, midY, rightX, midY + radius);
        rightPath.lineTo(rightX, endY - 3);

        drawGlowingPath(g2, leftPath);
        drawGlowingPath(g2, rightPath);

        drawArrowHead(g2, leftX, endY);
        drawArrowHead(g2, rightX, endY);
    }

    private void drawSingleStepConnector(Graphics2D g2, int startX, int startY, int endX, int endY) {
        int midY = startY + (endY - startY) / 2;
        int radius = 8;

        Path2D path = new Path2D.Float();
        path.moveTo(startX, startY);
        path.lineTo(startX, midY - radius);
        if (endX < startX) {
            path.quadTo(startX, midY, startX - radius, midY);
            path.lineTo(endX + radius, midY);
            path.quadTo(endX, midY, endX, midY + radius);
        } else {
            path.quadTo(startX, midY, startX + radius, midY);
            path.lineTo(endX - radius, midY);
            path.quadTo(endX, midY, endX, midY + radius);
        }
        path.lineTo(endX, endY - 3);

        drawGlowingPath(g2, path);
        drawArrowHead(g2, endX, endY);
    }

    private void drawGlowingPath(Graphics2D g2, Path2D path) {

        g2.setColor(new Color(56, 189, 248, 35));
        g2.setStroke(new BasicStroke(4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(path);

        g2.setColor(new Color(125, 211, 252));
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(path);
    }

    private void drawArrowHead(Graphics2D g2, int x, int y) {
        Path2D arrow = new Path2D.Float();
        arrow.moveTo(x - 5, y - 7);
        arrow.lineTo(x, y - 1);
        arrow.lineTo(x + 5, y - 7);
        arrow.closePath();

        g2.setColor(new Color(125, 211, 252));
        g2.fill(arrow);
    }
}
