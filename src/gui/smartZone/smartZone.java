package gui.smartZone;

import gui.dataStreamSecurity.*;
import javax.swing.BorderFactory;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class smartZone extends javax.swing.JInternalFrame {

    public smartZone() {
        initComponents();
        
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        
        jButton1.putClientProperty("FlatLaf.style", "arc:30");
        jButton2.putClientProperty("FlatLaf.style", "arc:30");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new components.PanelRound();
        panelRound2 = new components.PanelRound();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        smartZoneTree2 = new components.SmartZoneTree();
        panelRound3 = new components.PanelRound();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        panelRound4 = new components.PanelRound();
        panelRound7 = new components.PanelRound();
        jLabel26 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        smartTerminal1 = new components.SmartTerminal();
        jLabel21 = new javax.swing.JLabel();
        panelRound5 = new components.PanelRound();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        panelRound6 = new components.PanelRound();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        smartZoneTable1 = new components.SmartZoneTable();
        panelRound8 = new components.PanelRound();
        panelRound9 = new components.PanelRound();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();

        panelRound1.setBackground(new java.awt.Color(11, 15, 25));
        panelRound1.setPreferredSize(new java.awt.Dimension(1124, 725));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound2.setBackground(new java.awt.Color(30, 41, 59));
        panelRound2.setRoundBottomLeft(25);
        panelRound2.setRoundBottomRight(25);
        panelRound2.setRoundTopLeft(25);
        panelRound2.setRoundTopRight(25);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Zone Hierarchy");
        panelRound2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 40));

        jScrollPane4.setBorder(null);
        jScrollPane4.setViewportView(smartZoneTree2);

        panelRound2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, 60, 380, 610));

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 370, 680));

        panelRound3.setBackground(new java.awt.Color(30, 41, 59));
        panelRound3.setRoundBottomLeft(20);
        panelRound3.setRoundBottomRight(20);
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Total Nodes :");
        panelRound3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 20));

        jLabel24.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 204, 255));
        jLabel24.setText("128");
        panelRound3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 50, 20));

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, 150, 40));

        panelRound4.setBackground(new java.awt.Color(30, 41, 59));
        panelRound4.setRoundBottomLeft(25);
        panelRound4.setRoundBottomRight(25);
        panelRound4.setRoundTopLeft(25);
        panelRound4.setRoundTopRight(25);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound7.setBackground(new java.awt.Color(30, 41, 59));
        panelRound7.setRoundBottomLeft(25);
        panelRound7.setRoundBottomRight(25);
        panelRound7.setRoundTopLeft(25);
        panelRound7.setRoundTopRight(25);
        panelRound7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelRound4.add(panelRound7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 390, 720, 110));

        jLabel26.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("System Activity Log");
        panelRound4.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 690, 40));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        panelRound4.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 690, 10));
        panelRound4.add(smartTerminal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 690, 130));

        panelRound1.add(panelRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 510, 710, 190));

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Smart Zone Hierarchy Management");
        panelRound1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 390, 40));

        panelRound5.setBackground(new java.awt.Color(30, 41, 59));
        panelRound5.setRoundBottomLeft(20);
        panelRound5.setRoundBottomRight(20);
        panelRound5.setRoundTopLeft(20);
        panelRound5.setRoundTopRight(20);
        panelRound5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Active Zones : ");
        panelRound5.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 20));

        jLabel30.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(102, 204, 255));
        jLabel30.setText("14");
        panelRound5.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 50, 20));

        panelRound1.add(panelRound5, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, 150, 40));

        panelRound6.setBackground(new java.awt.Color(30, 41, 59));
        panelRound6.setRoundBottomLeft(25);
        panelRound6.setRoundBottomRight(25);
        panelRound6.setRoundTopLeft(25);
        panelRound6.setRoundTopRight(25);
        panelRound6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Zone Power Usage");
        panelRound6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 710, 40));

        jScrollPane1.setViewportView(smartZoneTable1);

        panelRound6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 690, 260));

        panelRound1.add(panelRound6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 710, 310));

        panelRound8.setBackground(new java.awt.Color(30, 41, 59));
        panelRound8.setRoundBottomLeft(25);
        panelRound8.setRoundBottomRight(25);
        panelRound8.setRoundTopLeft(25);
        panelRound8.setRoundTopRight(25);
        panelRound8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound9.setBackground(new java.awt.Color(30, 41, 59));
        panelRound9.setRoundBottomLeft(25);
        panelRound9.setRoundBottomRight(25);
        panelRound9.setRoundTopLeft(25);
        panelRound9.setRoundTopRight(25);
        panelRound9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelRound8.add(panelRound9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 390, 720, 110));

        jButton1.setBackground(new java.awt.Color(204, 0, 0));
        jButton1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Shutdown Power (Emergency)");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        panelRound8.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 340, 40));

        jButton2.setBackground(new java.awt.Color(56, 189, 248));
        jButton2.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jButton2.setForeground(new java.awt.Color(12, 0, 0));
        jButton2.setText("Report Usage");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(this::jButton2ActionPerformed);
        panelRound8.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 330, 40));

        jLabel27.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Control Action Section");
        panelRound8.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 710, 40));

        panelRound1.add(panelRound8, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, 710, 110));

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

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    private components.PanelRound panelRound1;
    private components.PanelRound panelRound2;
    private components.PanelRound panelRound3;
    private components.PanelRound panelRound4;
    private components.PanelRound panelRound5;
    private components.PanelRound panelRound6;
    private components.PanelRound panelRound7;
    private components.PanelRound panelRound8;
    private components.PanelRound panelRound9;
    private components.SmartTerminal smartTerminal1;
    private components.SmartZoneTable smartZoneTable1;
    private components.SmartZoneTree smartZoneTree2;
    // End of variables declaration//GEN-END:variables
}
