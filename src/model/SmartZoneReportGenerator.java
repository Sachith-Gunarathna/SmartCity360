package model;

import components.SmartTerminal;
import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class SmartZoneReportGenerator {

    public static void generateAndShowPdfReport(
            JTable zoneTable,
            String selectedZoneName,
            String activeZoneCount,
            String totalNodsCount,
            int totalPowerKw,
            SmartTerminal terminal
    ) {
        try {

            if (terminal != null) {
                terminal.appendLog("INFO", "Generating JasperReport for Zone: " + selectedZoneName + "...");
            }

            List tableRows = new ArrayList<>();
            int colCount = zoneTable.getColumnCount();

            for (int r = 0; r < zoneTable.getRowCount(); r++) {
                Map<String, Object> row = new HashMap<>();

                if (colCount >= 6 && zoneTable.getValueAt(r, 2) instanceof Boolean) {
                    boolean isPowerOn = Boolean.TRUE.equals(zoneTable.getValueAt(r, 2));
                    boolean hasAlert = Boolean.TRUE.equals(zoneTable.getValueAt(r, 5));

                    row.put("Field-1", getCellText(zoneTable, r, 0));
                    row.put("Field-2", getCellText(zoneTable, r, 1));
                    row.put("Field-3", isPowerOn ? "ONLINE" : "SHUTDOWN");
                    row.put("Field-4", getCellText(zoneTable, r, 3).replaceAll("[^0-9]", "") + " kW");
                    row.put("Field-5", getCellText(zoneTable, r, 4).replaceAll("[^0-9]", "") + " kWh");
                    row.put("Field-6", hasAlert ? "WARNING" : "Normal");
                } else {
                    row.put("Field-1", getCellText(zoneTable, r, 0));
                    row.put("Field-2", getCellText(zoneTable, r, 1));
                    row.put("Field-3", getCellText(zoneTable, r, 2));
                    row.put("Field-4", getCellText(zoneTable, r, 3));
                    row.put("Field-5", getCellText(zoneTable, r, 4));
                    row.put("Field-6", getCellText(zoneTable, r, 5));
                }
                tableRows.add(row);
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("targetScore", selectedZoneName);
            parameters.put("activeZone", activeZoneCount);
            parameters.put("activeIoTNodes", totalNodsCount);
            parameters.put("totalPowerLoad", totalPowerKw + "kW");

            InputStream reportStream = SmartZoneReportGenerator.class.getResourceAsStream("/resources/reports/SmartCity360PowerUsageReport.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("SmartCity360PowerUsageReport.jrxml not found in /resources/reports/ or root src!");
            }

            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(tableRows);
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            File pdfFile = new File("SmartCity360_Power_Usage_Report.pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile.getAbsolutePath());

            if (terminal != null) {
                terminal.appendLog("SUCCESS", "PDF Report exported: " + pdfFile.getAbsolutePath());
            }

            JasperViewer viwer = new JasperViewer(jasperPrint, false);
            viwer.setTitle("SmartCity360 - Power Consumption Audit Report");
            viwer.setVisible(true);

            if (Desktop.isDesktopSupported() && pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            if (terminal != null) {
                terminal.appendLog("CRITICAL", "Report Error: " + e.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Report Generation Failed:\n" + e.getMessage(), "JasperReports Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private static String getCellText(JTable table, int r, int c) {
        if (c >= table.getColumnCount()) {
            return "";
        }
        Object v = table.getValueAt(r, c);
        return v != null ? v.toString() : "";
    }

}
