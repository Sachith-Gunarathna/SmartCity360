package gui.citizenComplaint;

import controller.SmartCityController;
import gui.dataStreamSecurity.*;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class citizenComplaint extends javax.swing.JInternalFrame {

    public citizenComplaint() {
        initComponents();
        
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        
        jButton1.putClientProperty("FlatLaf.style", "arc:30");
        
        JTextField[] textField = {jTextField1,jTextField2};
        for(JTextField tf : textField){
            if(tf != null){
                tf.putClientProperty("FlatLaf.style", "arc:15");
            }
        }
        
        smartEscalationChain1.bindTerminal(smartTerminal1);
        roundTextAreaPanel1.setText("Routine maintenance required for reported street light fault.");
        
        jComboBox1.addActionListener(e -> {
            
            if(jComboBox1.getSelectedItem() == null){
                return;
            }
            String selectedCat = jComboBox1.getSelectedItem().toString();
            if(selectedCat.contains("Emergency") || selectedCat.contains("Blackout")){
                roundTextAreaPanel1.setText("Main grid power failure and blackout affecting street lights and hospital block.");
            }else if (selectedCat.contains("Multiple") || selectedCat.contains("Critical")) {
                roundTextAreaPanel1.setText("Multiple street distribution nodes reporting sector overload.");
            } else {
                roundTextAreaPanel1.setText("Routine maintenance required for reported " + selectedCat.toLowerCase() + ".");
            }
        });
        
        smartTerminal1.appendLog("INFO", "(Local Staff -> Supervisor -> City Manager) active.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new components.PanelRound();
        jLabel19 = new javax.swing.JLabel();
        panelRound2 = new components.PanelRound();
        jLabel20 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        roundTextAreaPanel1 = new components.RoundTextAreaPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        panelRound3 = new components.PanelRound();
        smartEscalationChain1 = new components.SmartEscalationChain();
        smartTerminal1 = new components.SmartTerminal();
        jLabel22 = new javax.swing.JLabel();

        panelRound1.setBackground(new java.awt.Color(11, 15, 25));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("System Activity Log");
        panelRound1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 530, 40));

        panelRound2.setBackground(new java.awt.Color(30, 41, 59));
        panelRound2.setRoundBottomLeft(15);
        panelRound2.setRoundBottomRight(15);
        panelRound2.setRoundTopLeft(15);
        panelRound2.setRoundTopRight(15);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Submit New Citizen Complaint");
        panelRound2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 470, 40));

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Citizen Name");
        panelRound2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 131, 30));

        jTextField1.setBackground(new java.awt.Color(30, 41, 59));
        jTextField1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        panelRound2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 470, 40));

        jTextField2.setBackground(new java.awt.Color(30, 41, 59));
        jTextField2.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        panelRound2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 470, 40));

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Category");
        panelRound2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 470, 30));

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Contact Info");
        panelRound2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 131, 30));

        jLabel17.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Complaint Description");
        panelRound2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 470, 30));

        jLabel18.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Zone");
        panelRound2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 470, 30));

        jButton1.setBackground(new java.awt.Color(56, 189, 248));
        jButton1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(12, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-git-24.png"))); // NOI18N
        jButton1.setText("Submit & Route Complaint");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        panelRound2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 590, 370, 40));

        roundTextAreaPanel1.setForeground(new java.awt.Color(255, 255, 255));
        panelRound2.add(roundTextAreaPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 470, 140));

        jComboBox1.setBackground(new java.awt.Color(30, 41, 59));
        jComboBox1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Street Light Fault", "Waste Management", "Water Supply Issue", "Noise / Parking Violation", "Multiple Infrastructure Damage", "Critical Sector Overload", "Emergency Grid Failure", "City-Wide Blackout (Emergency)" }));
        panelRound2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 470, 30));

        jComboBox2.setBackground(new java.awt.Color(30, 41, 59));
        jComboBox2.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Central District", "Narammala Street", "Alawwa City", "Industrial Sector", "Hospital Block A", "Tech Park Building", "Apartment C (Block 1)", "Apartment D", "Apartment E" }));
        panelRound2.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 470, 30));

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 490, 640));

        panelRound3.setBackground(new java.awt.Color(30, 41, 59));
        panelRound3.setRoundBottomLeft(15);
        panelRound3.setRoundBottomRight(15);
        panelRound3.setRoundTopLeft(15);
        panelRound3.setRoundTopRight(15);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout smartEscalationChain1Layout = new javax.swing.GroupLayout(smartEscalationChain1);
        smartEscalationChain1.setLayout(smartEscalationChain1Layout);
        smartEscalationChain1Layout.setHorizontalGroup(
            smartEscalationChain1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        smartEscalationChain1Layout.setVerticalGroup(
            smartEscalationChain1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        panelRound3.add(smartEscalationChain1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 580, 390));

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 600, 400));
        panelRound1.add(smartTerminal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(509, 522, 600, 190));

        jLabel22.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Escalation Chain Routing Path");
        panelRound1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 470, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String citizenName = jTextField1.getText().trim().isEmpty() ? "Sachith Gunarathna" : jTextField1.getText().trim();
        String contactNumber = jTextField2.getText().trim().isEmpty() ? "+94763145020" : jTextField2.getText().trim();
        String Category = jComboBox1.getSelectedItem() != null ? jComboBox1.getSelectedItem().toString() : "Power Outage";
        String zone = jComboBox2.getSelectedItem() != null ? jComboBox2.getSelectedItem().toString() : "Central District";
        String description = roundTextAreaPanel1.getText().trim();
        
        if(description.isEmpty()){
            smartTerminal1.appendLog("WARNING", "Please enter a complaint description to route through the chain.");
            return;
        }
        
        int handledLevel = SmartCityController.getInstance().submitCitizenComplaint(
                citizenName, 
                contactNumber, 
                Category, 
                zone, 
                description, 
                smartTerminal1
        );
        smartEscalationChain1.escalateToLevel(handledLevel);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private components.PanelRound panelRound1;
    private components.PanelRound panelRound2;
    private components.PanelRound panelRound3;
    private components.RoundTextAreaPanel roundTextAreaPanel1;
    private components.SmartEscalationChain smartEscalationChain1;
    private components.SmartTerminal smartTerminal1;
    // End of variables declaration//GEN-END:variables
}
