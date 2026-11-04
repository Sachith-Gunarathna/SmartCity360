package gui.smartStreetLights;

import gui.dataStreamSecurity.*;
import javax.swing.BorderFactory;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class smartStreetLights extends javax.swing.JInternalFrame {

    public smartStreetLights() {
        initComponents();
        
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new components.PanelRound();
        jLabel22 = new javax.swing.JLabel();
        panelRound2 = new components.PanelRound();
        jLabel20 = new javax.swing.JLabel();
        smartStreetMap1 = new components.SmartStreetMap();
        jButton1 = new javax.swing.JButton();
        panelRound3 = new components.PanelRound();
        jLabel21 = new javax.swing.JLabel();
        smartMemoryMetrics1 = new components.SmartMemoryMetrics();
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
        jLabel22.setText("Smart Street Lights ( Flyweight Pattern)");
        panelRound1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 470, 40));

        panelRound2.setBackground(new java.awt.Color(30, 41, 59));
        panelRound2.setRoundBottomLeft(15);
        panelRound2.setRoundBottomRight(15);
        panelRound2.setRoundTopLeft(15);
        panelRound2.setRoundTopRight(15);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Flyweight Street Light Deployment");
        panelRound2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 30));

        javax.swing.GroupLayout smartStreetMap1Layout = new javax.swing.GroupLayout(smartStreetMap1);
        smartStreetMap1.setLayout(smartStreetMap1Layout);
        smartStreetMap1Layout.setHorizontalGroup(
            smartStreetMap1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );
        smartStreetMap1Layout.setVerticalGroup(
            smartStreetMap1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        panelRound2.add(smartStreetMap1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 470, 510));

        jButton1.setBackground(new java.awt.Color(56, 189, 248));
        jButton1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(12, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-git-24.png"))); // NOI18N
        jButton1.setText("Deploy New Node");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        panelRound2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 580, 370, 40));

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 490, 640));

        panelRound3.setBackground(new java.awt.Color(30, 41, 59));
        panelRound3.setRoundBottomLeft(15);
        panelRound3.setRoundBottomRight(15);
        panelRound3.setRoundTopLeft(15);
        panelRound3.setRoundTopRight(15);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Memory Optimization Metrics");
        panelRound3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 30));

        smartMemoryMetrics1.setBackground(new java.awt.Color(30, 41, 59));

        javax.swing.GroupLayout smartMemoryMetrics1Layout = new javax.swing.GroupLayout(smartMemoryMetrics1);
        smartMemoryMetrics1.setLayout(smartMemoryMetrics1Layout);
        smartMemoryMetrics1Layout.setHorizontalGroup(
            smartMemoryMetrics1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        smartMemoryMetrics1Layout.setVerticalGroup(
            smartMemoryMetrics1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        panelRound3.add(smartMemoryMetrics1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 570, 340));

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, 590, 380));

        jLabel19.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("System Activity Log");
        panelRound1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 450, 530, 40));
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

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private components.PanelRound panelRound1;
    private components.PanelRound panelRound2;
    private components.PanelRound panelRound3;
    private components.SmartMemoryMetrics smartMemoryMetrics1;
    private components.SmartStreetMap smartStreetMap1;
    private components.SmartTerminal smartTerminal1;
    // End of variables declaration//GEN-END:variables
}
