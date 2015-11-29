package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import painter.Controller;


public class GUI extends javax.swing.JFrame {
    Controller controller = Controller.getInstance();
    MyPanel paintPanel = new MyPanel();
    class MyPanel extends JPanel{
        BufferedImage backgraund;
        BufferedImage buffer;
        
        public void setSize(int width, int height){
            super.setSize(width, height);
            backgraund = new BufferedImage(width, height,BufferedImage.TYPE_4BYTE_ABGR);
            buffer = new BufferedImage(width, height,BufferedImage.TYPE_4BYTE_ABGR);
            Graphics graphics = backgraund.getGraphics();
            int kubSize = 10;
            graphics.setColor(Color.GRAY);
            boolean b = true;
            for (int i = 0; i < width; i += kubSize) {
                b = !b;
                for (int j = 0; j < height; j += kubSize*2) {
                    graphics.fillRect(i, j + (b ? kubSize : 0), kubSize, kubSize);
                }
            }
            graphics.setColor(Color.LIGHT_GRAY);
            b = false;
            for (int i = 0; i < width; i += kubSize) {
                b = !b;
                for (int j = 0; j < height; j += kubSize * 2) {
                    graphics.fillRect(i, j + (b ? kubSize : 0), kubSize, kubSize);
                }
            }
            
        }
                
        @Override
        public void paint(Graphics g) {
            Image image = controller.getImage();
            if(image.getWidth(null) != backgraund.getWidth()
                    || image.getHeight(null) != backgraund.getHeight()){
                setSize(image.getWidth(null), image.getHeight(null));
            }
            Graphics graphics = buffer.getGraphics();
            graphics.drawImage(backgraund, 0, 0, null);
            graphics.drawImage(image, 0, 0, null);
            g.drawImage(buffer, 0, 0, null);
        }
        
    }
    
    public GUI() {
        initComponents();
        mainPanel.add(paintPanel);
        paintPanel.setVisible(true);
        paintPanel.setSize(1, 1);
        try {
            BufferedImage read = ImageIO.read(getClass().getResource("icon.png"));
            setIconImage(read);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        createDiolog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        width = new javax.swing.JTextField();
        height = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        createButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        x = new javax.swing.JLabel();
        y = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        scale = new javax.swing.JSlider();
        persent = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        instrumentPanel = new javax.swing.JPanel();
        propertyPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        create = new javax.swing.JMenuItem();
        open = new javax.swing.JMenuItem();
        pastOfBuffer = new javax.swing.JMenuItem();
        save = new javax.swing.JMenuItem();
        saveAs = new javax.swing.JMenuItem();
        edit = new javax.swing.JMenu();
        layer = new javax.swing.JMenu();
        instrument = new javax.swing.JMenu();
        help = new javax.swing.JMenu();

        createDiolog.setTitle("Создать");
        createDiolog.setMinimumSize(new java.awt.Dimension(350, 150));

        jLabel1.setText("Ширина");

        jLabel2.setText("Высота");

        height.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightActionPerformed(evt);
            }
        });

        jLabel4.setText("px");

        jLabel5.setText("px");

        createButton.setText("Создать");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout createDiologLayout = new javax.swing.GroupLayout(createDiolog.getContentPane());
        createDiolog.getContentPane().setLayout(createDiologLayout);
        createDiologLayout.setHorizontalGroup(
            createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createDiologLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(createDiologLayout.createSequentialGroup()
                        .addGroup(createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(width)
                            .addComponent(height, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)))
                    .addGroup(createDiologLayout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(createButton)))
                .addGap(3, 3, 3)
                .addGroup(createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        createDiologLayout.setVerticalGroup(
            createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createDiologLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(width, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(createDiologLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createButton)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Рисовальщик - Painter");

        mainPanel.setBackground(new java.awt.Color(255, 230, 168));
        mainPanel.setPreferredSize(new java.awt.Dimension(810, 610));
        mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mainPanelMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        x.setText("X : ");

        y.setText("Y : ");

        jLabel3.setText("Масштаб");

        scale.setMaximum(400);
        scale.setMinimum(25);
        scale.setSnapToTicks(true);
        scale.setValue(100);
        scale.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scaleStateChanged(evt);
            }
        });

        persent.setText("100%");

        jPanel1.setBackground(new java.awt.Color(186, 210, 175));

        instrumentPanel.setBackground(new java.awt.Color(182, 204, 204));
        instrumentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Инструменты"));

        javax.swing.GroupLayout instrumentPanelLayout = new javax.swing.GroupLayout(instrumentPanel);
        instrumentPanel.setLayout(instrumentPanelLayout);
        instrumentPanelLayout.setHorizontalGroup(
            instrumentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        instrumentPanelLayout.setVerticalGroup(
            instrumentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );

        propertyPanel.setBackground(new java.awt.Color(175, 153, 204));
        propertyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Настройки инструмента"));

        javax.swing.GroupLayout propertyPanelLayout = new javax.swing.GroupLayout(propertyPanel);
        propertyPanel.setLayout(propertyPanelLayout);
        propertyPanelLayout.setHorizontalGroup(
            propertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );
        propertyPanelLayout.setVerticalGroup(
            propertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(228, 150, 188));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Слои"));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(propertyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(instrumentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(instrumentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(propertyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenuBar1.setBackground(new java.awt.Color(82, 65, 78));
        jMenuBar1.setBorder(new javax.swing.border.MatteBorder(null));

        file.setBorder(null);
        file.setText("Файл");

        create.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        create.setText("Создать");
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });
        file.add(create);

        open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        open.setText("Открыть");
        file.add(open);

        pastOfBuffer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pastOfBuffer.setText("Вставить из буфера");
        file.add(pastOfBuffer);

        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        save.setText("Сохранить");
        file.add(save);

        saveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAs.setText("Сохранить как");
        file.add(saveAs);

        jMenuBar1.add(file);

        edit.setText("Правка");
        jMenuBar1.add(edit);

        layer.setText("Слой");
        jMenuBar1.add(layer);

        instrument.setText("Инструменты");
        jMenuBar1.add(instrument);

        help.setText("Справка");
        jMenuBar1.add(help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(y, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scale, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(persent))
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(scale, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(x)
                        .addComponent(y))
                    .addComponent(persent)))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
        createDiolog.setVisible(true);
    }//GEN-LAST:event_createActionPerformed

    private void heightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_heightActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        int w = Integer.parseInt(width.getText());
        int h = Integer.parseInt(height.getText());
        controller.create(w, h);
        paintPanel.repaint();
        createDiolog.setVisible(false);
    }//GEN-LAST:event_createButtonActionPerformed

    private void mainPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseMoved
        x.setText("X : " + controller.coordinateScale(evt.getX()));
        y.setText("Y : " + controller.coordinateScale(evt.getY()));
    }//GEN-LAST:event_mainPanelMouseMoved

    private void scaleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_scaleStateChanged
        persent.setText(scale.getValue() + "%");
        controller.setScale(scale.getValue());
        paintPanel.repaint();
    }//GEN-LAST:event_scaleStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem create;
    private javax.swing.JButton createButton;
    private javax.swing.JDialog createDiolog;
    private javax.swing.JMenu edit;
    private javax.swing.JMenu file;
    private javax.swing.JTextField height;
    private javax.swing.JMenu help;
    private javax.swing.JMenu instrument;
    private javax.swing.JPanel instrumentPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JMenu layer;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem open;
    private javax.swing.JMenuItem pastOfBuffer;
    private javax.swing.JLabel persent;
    private javax.swing.JPanel propertyPanel;
    private javax.swing.JMenuItem save;
    private javax.swing.JMenuItem saveAs;
    private javax.swing.JSlider scale;
    private javax.swing.JTextField width;
    private javax.swing.JLabel x;
    private javax.swing.JLabel y;
    // End of variables declaration//GEN-END:variables
}
