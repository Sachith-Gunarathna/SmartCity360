package gui.policy;

import controller.SmartCityController;
import gui.dataStreamSecurity.*;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class policy extends javax.swing.JInternalFrame {

    private final String defaultDslRule = "IF ZONE == \"Central District\" AND USAGE > 300kW\nTHEN DIM_LIGHTS 40% AND ACTIVATE_BACKUP";
    
    public policy() {
        initComponents();
        
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        
        jButton1.putClientProperty("FlatLaf.style", "arc:30");
        
        JTextField[] textfield = {jTextField1,jTextField2,jTextField3};
        for(JTextField textF : textfield){
            if(textF != null){
                textF.putClientProperty("FlatLaf.style", "arc:15");
            }
        }
        
        jLabel21.setText("Policy DSL Rule & Context");
        jTextField1.setText("Alawwa City - Central District");
        jTextField2.setText("345 kW");
        jTextField3.setText("Peak Load");

        smartTerminal2.appendLog("DSL", "IF ZONE == \"Central District\" AND USAGE > 300kW");
        smartTerminal2.appendLog("DSL", "THEN DIM_LIGHTS 40% AND ACTIVATE_BACKUP");

        smartAstDiagram1.bindTerminal(smartTerminal1);
        smartTerminal1.appendLog("INFO", "AST Engine initialized.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new components.PanelRound();
        jLabel22 = new javax.swing.JLabel();
        panelRound2 = new components.PanelRound();
        jLabel20 = new javax.swing.JLabel();
        smartAstDiagram1 = new components.SmartAstDiagram();
        panelRound3 = new components.PanelRound();
        jLabel21 = new javax.swing.JLabel();
        smartTerminal2 = new components.SmartTerminal();
        jLabel14 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        smartTerminal1 = new components.SmartTerminal();

        panelRound1.setBackground(new java.awt.Color(11, 15, 25));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Smart City Policy Engine ");
        panelRound1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 470, 40));

        panelRound2.setBackground(new java.awt.Color(30, 41, 59));
        panelRound2.setRoundBottomLeft(15);
        panelRound2.setRoundBottomRight(15);
        panelRound2.setRoundTopLeft(15);
        panelRound2.setRoundTopRight(15);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Abstract Syntax Tree (AST) Evaluation");
        panelRound2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 540, 50));

        smartAstDiagram1.setBackground(new java.awt.Color(30, 41, 59));

        javax.swing.GroupLayout smartAstDiagram1Layout = new javax.swing.GroupLayout(smartAstDiagram1);
        smartAstDiagram1.setLayout(smartAstDiagram1Layout);
        smartAstDiagram1Layout.setHorizontalGroup(
            smartAstDiagram1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        smartAstDiagram1Layout.setVerticalGroup(
            smartAstDiagram1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );

        panelRound2.add(smartAstDiagram1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 570, 310));

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, 590, 380));

        panelRound3.setBackground(new java.awt.Color(30, 41, 59));
        panelRound3.setRoundBottomLeft(15);
        panelRound3.setRoundBottomRight(15);
        panelRound3.setRoundTopLeft(15);
        panelRound3.setRoundTopRight(15);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Abstract Syntax Tree (AST) Evaluation");
        panelRound3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 470, 50));
        panelRound3.add(smartTerminal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 470, 320));

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Target Zone :");
        panelRound3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 100, 30));

        jTextField1.setBackground(new java.awt.Color(30, 41, 59));
        jTextField1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        panelRound3.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 400, 360, 30));

        jTextField2.setBackground(new java.awt.Color(30, 41, 59));
        jTextField2.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        panelRound3.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 450, 360, 30));

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Current Load :");
        panelRound3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 100, 30));

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Grid Status :");
        panelRound3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 100, 30));

        jTextField3.setBackground(new java.awt.Color(30, 41, 59));
        jTextField3.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        panelRound3.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 500, 360, 30));

        jButton1.setBackground(new java.awt.Color(56, 189, 248));
        jButton1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(12, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-git-24.png"))); // NOI18N
        jButton1.setText("Execute Policy");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        panelRound3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 570, 370, 40));

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 490, 640));

        jLabel19.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("System Activity Log");
        panelRound1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 450, 540, 40));
        panelRound1.add(smartTerminal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 500, 590, 200));

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
        
        String targetZone = jTextField1.getText().trim();
        String currentLoad = jTextField2.getText().trim();
        String gridStatus = jTextField3.getText().trim();
        
        smartTerminal2.appendLog("CONTEXT", "Evaluating -> Zone: " + targetZone + " | Load: " + currentLoad + " | Grid: " + gridStatus);
        SmartCityController.getInstance().evaluateCityPolicy(
                defaultDslRule, 
                targetZone, 
                currentLoad);
        smartAstDiagram1.evaluatePolicy(
                defaultDslRule, targetZone, currentLoad);
        
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private components.PanelRound panelRound1;
    private components.PanelRound panelRound2;
    private components.PanelRound panelRound3;
    private components.SmartAstDiagram smartAstDiagram1;
    private components.SmartTerminal smartTerminal1;
    private components.SmartTerminal smartTerminal2;
    // End of variables declaration//GEN-END:variables
}
