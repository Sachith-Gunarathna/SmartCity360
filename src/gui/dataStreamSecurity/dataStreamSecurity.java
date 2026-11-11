package gui.dataStreamSecurity;

import controller.SmartCityController;
import javax.swing.BorderFactory;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import model.DataSecurity;

public class dataStreamSecurity extends javax.swing.JInternalFrame {

    public dataStreamSecurity() {
        initComponents();
        
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        
        jButton1.putClientProperty("FlatLaf.style", "arc:30");
        
        roundTextAreaPanel2.setText("{\n  \"zone\": \"Alawwa City - Central District\",\n  \"sensorId\": \"NODE_1540\",\n  \"powerLoadKW\": 345,\n  \"voltage\": \"230V\",\n  \"status\": \"ONLINE\"\n}");
        
        smartTerminal1.clear();
        smartTerminal1.appendLog("INFO", "Part A: Decorator Pattern Security Engine initialized.");
        smartTerminal1.appendLog("SECURITY", "Select security wrappers and click 'Secure & Send'.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new components.PanelRound();
        jLabel19 = new javax.swing.JLabel();
        panelRound2 = new components.PanelRound();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        compressionSwitch = new components.SmartSwitch();
        encryptionSwitch = new components.SmartSwitch();
        authenticationSwitch = new components.SmartSwitch();
        jLabel20 = new javax.swing.JLabel();
        panelRound3 = new components.PanelRound();
        jLabel21 = new javax.swing.JLabel();
        smartTerminal1 = new components.SmartTerminal();
        panelRound4 = new components.PanelRound();
        jLabel22 = new javax.swing.JLabel();
        roundTextAreaPanel2 = new components.RoundTextAreaPanel();

        panelRound1.setBackground(new java.awt.Color(11, 15, 25));
        panelRound1.setPreferredSize(new java.awt.Dimension(1124, 725));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Data Stream Security");
        panelRound1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 530, 40));

        panelRound2.setBackground(new java.awt.Color(30, 41, 59));
        panelRound2.setRoundBottomLeft(20);
        panelRound2.setRoundBottomRight(20);
        panelRound2.setRoundTopLeft(20);
        panelRound2.setRoundTopRight(20);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Compression (GZIP Data)");
        panelRound2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 230, 30));

        jButton1.setBackground(new java.awt.Color(56, 189, 248));
        jButton1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(12, 0, 0));
        jButton1.setText("Secure & Send");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        panelRound2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 370, 60));

        jLabel23.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 20)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Security Layer Settings");
        panelRound2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, 20));

        jLabel24.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Encryption (AES-256)");
        panelRound2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 230, 30));

        jLabel25.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Authentication (HMAC Sign)");
        panelRound2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 230, 30));

        compressionSwitch.setText("smartSwitch1");
        panelRound2.add(compressionSwitch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 70, -1));

        encryptionSwitch.setText("smartSwitch1");
        panelRound2.add(encryptionSwitch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 70, -1));

        authenticationSwitch.setText("smartSwitch1");
        panelRound2.add(authenticationSwitch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 70, -1));

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 90, 390, 320));

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Dynamic Security Wrappers for Data Streams (Decorator Pattern)");
        panelRound1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 420, 20));

        panelRound3.setBackground(new java.awt.Color(30, 41, 59));
        panelRound3.setRoundBottomLeft(20);
        panelRound3.setRoundBottomRight(20);
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("System Wrapper Output");
        panelRound3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 420, 20));
        panelRound3.add(smartTerminal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 1080, 230));

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 1100, 280));

        panelRound4.setBackground(new java.awt.Color(30, 41, 59));
        panelRound4.setRoundBottomLeft(20);
        panelRound4.setRoundBottomRight(20);
        panelRound4.setRoundTopLeft(20);
        panelRound4.setRoundTopRight(20);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Raw Data Stream");
        panelRound4.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 420, 20));

        roundTextAreaPanel2.setForeground(new java.awt.Color(255, 255, 255));
        panelRound4.add(roundTextAreaPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 670, 270));

        panelRound1.add(panelRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 690, 320));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        String rawPayload = roundTextAreaPanel2.getText().trim();
        if(rawPayload.isEmpty()){
            smartTerminal1.appendLog("WARNING", "Raw Data Stream is empty! Please enter telemetry data.");
            return;
        }
        
        SmartCityController.getInstance().secureAndSendStream(
                rawPayload, 
                encryptionSwitch.isSelected(), 
                authenticationSwitch.isSelected(), 
                compressionSwitch.isSelected(), 
                smartTerminal1
        );
        
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private components.SmartSwitch authenticationSwitch;
    private components.SmartSwitch compressionSwitch;
    private components.SmartSwitch encryptionSwitch;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private components.PanelRound panelRound1;
    private components.PanelRound panelRound2;
    private components.PanelRound panelRound3;
    private components.PanelRound panelRound4;
    private components.RoundTextAreaPanel roundTextAreaPanel2;
    private components.SmartTerminal smartTerminal1;
    // End of variables declaration//GEN-END:variables
}
