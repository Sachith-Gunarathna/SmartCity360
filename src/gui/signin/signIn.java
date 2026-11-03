package gui.signin;

import com.formdev.flatlaf.FlatLightLaf;
import components.SMSlide1;
import components.SMSlide2;
import components.SMSlide3;
import gui.home.dashboard;


public class signIn extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(signIn.class.getName());

    public signIn() {
        initComponents();
        jButton1.putClientProperty("FlatLaf.style", "arc:30");
        slideShow1.initSlideShow(new SMSlide1(), new SMSlide2(), new SMSlide3());
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new components.PanelRound();
        slideShow1 = new components.slideShow();
        panelRound2 = new components.PanelRound();
        pictureBox1 = new components.PictureBox();
        pictureBox4 = new components.PictureBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        companyLogo = new components.PictureBox();
        pictureBox2 = new components.PictureBox();
        pictureBox3 = new components.PictureBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelRound1.setBackground(new java.awt.Color(15, 23, 42));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);

        panelRound2.setBackground(new java.awt.Color(15, 23, 42));
        panelRound2.setRoundBottomLeft(20);
        panelRound2.setRoundBottomRight(20);
        panelRound2.setRoundTopLeft(20);
        panelRound2.setRoundTopRight(20);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pictureBox1.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/logo/smartcity360 logo white version.png"))); // NOI18N
        panelRound2.add(pictureBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 6, 168, 138));

        pictureBox4.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/img/icons8-security-lock-100.png"))); // NOI18N
        panelRound2.add(pictureBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, 40, 40));

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html> <p> <center>WELCOME BACK,</center> </br><center>OPPRATOR</center></p></html>");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelRound2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 370, 140));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Username or Email ");
        panelRound2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 346, -1));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        panelRound2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 350, 10));

        jTextField1.setBackground(new java.awt.Color(15, 23, 42));
        jTextField1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setToolTipText("Enter your username or email");
        jTextField1.setBorder(null);
        jTextField1.setCaretColor(new java.awt.Color(255, 255, 255));
        jTextField1.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        jTextField1.setFocusCycleRoot(true);
        jTextField1.setOpaque(true);
        jTextField1.setSelectionColor(new java.awt.Color(56, 189, 248));
        panelRound2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 346, 37));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Password");
        panelRound2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 346, -1));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        panelRound2.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 350, 10));

        jPasswordField1.setBackground(new java.awt.Color(15, 23, 42));
        jPasswordField1.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField1.setToolTipText("Enter your password");
        jPasswordField1.setBorder(null);
        jPasswordField1.setCaretColor(new java.awt.Color(255, 255, 255));
        jPasswordField1.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        jPasswordField1.setOpaque(true);
        jPasswordField1.setSelectionColor(new java.awt.Color(56, 189, 248));
        panelRound2.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 350, 30));

        jButton1.setBackground(new java.awt.Color(56, 189, 248));
        jButton1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("LOGING");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        panelRound2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 270, 40));

        jLabel4.setBackground(new java.awt.Color(15, 23, 42));
        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Powered by");
        panelRound2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 500, 70, 30));

        companyLogo.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/logo/logo2.png"))); // NOI18N
        panelRound2.add(companyLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 500, 110, 30));

        pictureBox2.setToolTipText("Close");
        pictureBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pictureBox2.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/icons8-shutdown-52.png"))); // NOI18N
        pictureBox2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pictureBox2MouseClicked(evt);
            }
        });
        panelRound2.add(pictureBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 30, 30));

        pictureBox3.setImage(new javax.swing.ImageIcon(getClass().getResource("/resources/img/icons8-bot-100.png"))); // NOI18N
        panelRound2.add(pictureBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 60, 50));

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(slideShow1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRound2, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(slideShow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRound2, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        new dashboard().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void pictureBox2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pictureBox2MouseClicked
        System.exit(0);
    }//GEN-LAST:event_pictureBox2MouseClicked

 
    public static void main(String args[]) {
        
        FlatLightLaf.setup();
        
        java.awt.EventQueue.invokeLater(() -> new signIn().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private components.PictureBox companyLogo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private components.PanelRound panelRound1;
    private components.PanelRound panelRound2;
    private components.PictureBox pictureBox1;
    private components.PictureBox pictureBox2;
    private components.PictureBox pictureBox3;
    private components.PictureBox pictureBox4;
    private components.slideShow slideShow1;
    // End of variables declaration//GEN-END:variables
}
