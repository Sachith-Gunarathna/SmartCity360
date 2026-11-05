package gui.home;

import com.formdev.flatlaf.FlatDarkLaf;
import gui.citizenComplaint.citizenComplaint;
import gui.dataStreamSecurity.dataStreamSecurity;
import gui.policy.policy;
import gui.smartStreetLights.smartStreetLights;
import gui.smartZone.smartZone;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import module.AppIcon;


public class dashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(dashboard.class.getName());
    private JButton[]  menuButton;
    
    
    public dashboard() {
        initComponents();
        getRootPane().putClientProperty("JRootPane.roundRect", true);
        AppIcon.setIcon(this);
         menuButton = new JButton[]{jButton1,jButton2,jButton3,jButton4,jButton5,jButton6};
        for(JButton btn : menuButton){
            if(btn != null){
                btn.putClientProperty("FlatLaf.style", "arc:25");
            }
        }
        
        jDesktopPane1.removeAll();
        jDesktopPane1.add(new Home()).setVisible(true);
        
        jButton1.setBackground(new Color(30, 52, 82));
        jButton1.setForeground(new Color(56, 189, 248));
        jButton1.putClientProperty("JComponent.outline", "cyan");
        
        LocalTime currentTime = LocalTime.now();
        
        int hour = currentTime.getHour();
        
        if(hour >= 5 && hour < 12){
            jLabel7.setText("Good Morning !");
            pictureBox1.setImage(new ImageIcon(getClass().getResource("/resources/img/icons8-sun-48.png")));
        }else if(hour >=12 && hour < 17){
            jLabel7.setText("Good Afternoon !");
            pictureBox1.setImage(new ImageIcon(getClass().getResource("/resources/img/icons8-afternoon-48.png")));
        }else if(hour >= 17 && hour < 21){
            jLabel7.setText("Good Evening !");
            pictureBox1.setImage(new ImageIcon(getClass().getResource("/resources/img/icons8-evening-48.png")));
        }else {
            jLabel7.setText("Good Night !");
            pictureBox1.setImage(new ImageIcon(getClass().getResource("/resources/img/icons8-night-48.png")));
        }
        
        Clock();
        
    }

    private void activeButton(JButton clickButton){
    
        for(JButton btn : menuButton){
            if(btn == clickButton){
                btn.setBackground(new Color(30, 52, 82));
                btn.setForeground(new Color(56, 189, 248));
                btn.putClientProperty("JComponent.outline", "cyan");
            }else{
                btn.setBackground(new Color(30,41,59));
                btn.setForeground(new Color(255,255,255));
                btn.putClientProperty("JComponent.outline", null);
            }
        }

    }
    
    private void Clock(){
        Thread clock = new Thread(){
            public void run(){
                try{
                    while (true) {
                        Calendar cal = new GregorianCalendar();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);
                        
                        DateFormat date = new SimpleDateFormat("hh : mm : ss aa");
                        String time = date.format(new Date()).toString();
                        
                        timeLabel.setText(time);
                        dateLabel.setText(year + "/" + month + "/" + day);
                        
                        sleep(1000);
                    }
                }catch(Exception e){
                    JOptionPane.showMessageDialog(rootPane, e);
                }
            }
        };
        clock.start();
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound2 = new components.PanelRound();
        sideNavBar = new components.PanelRound();
        logo = new components.PictureBox();
        line01 = new components.PanelRound();
        companyLogo = new components.PictureBox();
        jLabel1 = new javax.swing.JLabel();
        line02 = new components.PanelRound();
        shutdownButton = new components.PictureBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        pictureBox1 = new components.PictureBox();
        jDesktopPane1 = new javax.swing.JDesktopPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelRound2.setBackground(new java.awt.Color(255, 255, 255));
        panelRound2.setRoundBottomLeft(30);
        panelRound2.setRoundBottomRight(30);
        panelRound2.setRoundTopLeft(30);
        panelRound2.setRoundTopRight(30);

        sideNavBar.setBackground(new java.awt.Color(11, 15, 25));
        sideNavBar.setRoundBottomLeft(30);
        sideNavBar.setRoundBottomRight(30);
        sideNavBar.setRoundTopLeft(30);
        sideNavBar.setRoundTopRight(30);
        sideNavBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/logo/smartcity360 logo white version.png"))); // NOI18N
        sideNavBar.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 88, 69));

        line01.setBackground(new java.awt.Color(255, 255, 255));
        line01.setRoundBottomLeft(10);
        line01.setRoundBottomRight(10);
        line01.setRoundTopLeft(10);
        line01.setRoundTopRight(10);

        javax.swing.GroupLayout line01Layout = new javax.swing.GroupLayout(line01);
        line01.setLayout(line01Layout);
        line01Layout.setHorizontalGroup(
            line01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        line01Layout.setVerticalGroup(
            line01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        sideNavBar.add(line01, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 171, 213, -1));

        companyLogo.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/logo/logo2.png"))); // NOI18N
        sideNavBar.add(companyLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 675, 88, 25));

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Powered by");
        sideNavBar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 683, -1, -1));

        line02.setBackground(new java.awt.Color(255, 255, 255));
        line02.setRoundBottomLeft(10);
        line02.setRoundBottomRight(10);
        line02.setRoundTopLeft(10);
        line02.setRoundTopRight(10);

        javax.swing.GroupLayout line02Layout = new javax.swing.GroupLayout(line02);
        line02.setLayout(line02Layout);
        line02Layout.setHorizontalGroup(
            line02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        line02Layout.setVerticalGroup(
            line02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        sideNavBar.add(line02, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 654, 213, -1));

        shutdownButton.setToolTipText("Close");
        shutdownButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        shutdownButton.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-shutdown-52.png"))); // NOI18N
        shutdownButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                shutdownButtonMouseClicked(evt);
            }
        });
        sideNavBar.add(shutdownButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, 50, 50));

        jButton1.setBackground(new java.awt.Color(30, 41, 59));
        jButton1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-dashboard-30.png"))); // NOI18N
        jButton1.setText("Dashboard");
        jButton1.setToolTipText("Dashboard");
        jButton1.setBorder(null);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton1.setIconTextGap(5);
        jButton1.addActionListener(this::jButton1ActionPerformed);
        sideNavBar.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 198, 210, 45));

        jButton2.setBackground(new java.awt.Color(30, 41, 59));
        jButton2.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-shield-30.png"))); // NOI18N
        jButton2.setText("Data Stream Security ");
        jButton2.setToolTipText("Dashboard");
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton2.setIconTextGap(5);
        jButton2.addActionListener(this::jButton2ActionPerformed);
        sideNavBar.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 261, 210, 45));

        jButton3.setBackground(new java.awt.Color(30, 41, 59));
        jButton3.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-smart-30.png"))); // NOI18N
        jButton3.setText("Smart Zone");
        jButton3.setToolTipText("Dashboard");
        jButton3.setBorder(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton3.setIconTextGap(5);
        jButton3.addActionListener(this::jButton3ActionPerformed);
        sideNavBar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 324, 210, 45));

        jButton4.setBackground(new java.awt.Color(30, 41, 59));
        jButton4.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-complaint-30.png"))); // NOI18N
        jButton4.setText("Citizen Complaint");
        jButton4.setToolTipText("Dashboard");
        jButton4.setBorder(null);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton4.setIconTextGap(5);
        jButton4.addActionListener(this::jButton4ActionPerformed);
        sideNavBar.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 387, 210, 45));

        jButton5.setBackground(new java.awt.Color(30, 41, 59));
        jButton5.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-light-30.png"))); // NOI18N
        jButton5.setText("Smart Street Lights");
        jButton5.setToolTipText("Dashboard");
        jButton5.setBorder(null);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton5.setIconTextGap(5);
        jButton5.addActionListener(this::jButton5ActionPerformed);
        sideNavBar.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 450, 210, 45));

        jButton6.setBackground(new java.awt.Color(30, 41, 59));
        jButton6.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-policy-30.png"))); // NOI18N
        jButton6.setText("Policy");
        jButton6.setToolTipText("Dashboard");
        jButton6.setBorder(null);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton6.setIconTextGap(5);
        jButton6.addActionListener(this::jButton6ActionPerformed);
        sideNavBar.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 513, 210, 45));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SmartCity360");
        sideNavBar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, -1, 35));

        dateLabel.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        dateLabel.setForeground(new java.awt.Color(255, 255, 255));
        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dateLabel.setText("04/11/2026");
        sideNavBar.add(dateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 130, 20));

        jLabel6.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tagline Goes Here");
        sideNavBar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 120, 35));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Good Morning !");
        sideNavBar.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));

        timeLabel.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timeLabel.setText("15:30");
        sideNavBar.add(timeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 130, 20));

        pictureBox1.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/img/icons8-sun-48.png"))); // NOI18N
        sideNavBar.add(pictureBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 40, 40));

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1124, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sideNavBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDesktopPane1)
                    .addComponent(sideNavBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1366, 737));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void shutdownButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_shutdownButtonMouseClicked
       System.exit(0);
    }//GEN-LAST:event_shutdownButtonMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jDesktopPane1.removeAll();
        jDesktopPane1.add(new dataStreamSecurity()).setVisible(true);
        
        activeButton(jButton2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jDesktopPane1.removeAll();
        jDesktopPane1.add(new Home()).setVisible(true);
        
        activeButton(jButton1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       jDesktopPane1.removeAll();
       jDesktopPane1.add(new smartZone()).setVisible(true);
       
       activeButton(jButton3);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jDesktopPane1.removeAll();
        jDesktopPane1.add(new citizenComplaint()).setVisible(true);
        
        activeButton(jButton4);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jDesktopPane1.removeAll();
        jDesktopPane1.add(new smartStreetLights()).setVisible(true);
        
        activeButton(jButton5);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       jDesktopPane1.removeAll();
       jDesktopPane1.add(new policy()).setVisible(true);
       
       activeButton(jButton6);
    }//GEN-LAST:event_jButton6ActionPerformed

    public static void main(String args[]) {
        FlatDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new dashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private components.PictureBox companyLogo;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private components.PanelRound line01;
    private components.PanelRound line02;
    private components.PictureBox logo;
    private components.PanelRound panelRound2;
    private components.PictureBox pictureBox1;
    private components.PictureBox shutdownButton;
    private components.PanelRound sideNavBar;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
}
