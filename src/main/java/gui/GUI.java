package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.colorchooser.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import painter.*;
import painter.instruments.Pencil;


public class GUI extends javax.swing.JFrame {
    Controller controller = Controller.getInstance();
    MyPanel paintPanel = new MyPanel();
    boolean saveSelection;
            
    class MyPanel extends JPanel{
        BufferedImage backgraund;
        BufferedImage buffer;
        
        public void setSize(int width, int height){
            super.setSize(width, height);
            mainPanel.setPreferredSize(new Dimension(width, height));
            buffer = new BufferedImage(width, height,BufferedImage.TYPE_4BYTE_ABGR);
            backgraund = new BufferedImage(width, height,BufferedImage.TYPE_4BYTE_ABGR);
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
        //controller.create(500, 500);
        initComponents();
        mainPanel.add(paintPanel);
        paintPanel.setVisible(true);
        paintPanel.setSize(1, 1);
        layersPanel.setLayout(new BoxLayout(layersPanel, BoxLayout.Y_AXIS));
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));
        newLayerButton.setIcon(new ImageIcon(getClass().getResource("new.png")));
        deleteLayerButton.setIcon(new ImageIcon(getClass().getResource("del.png")));
        upButton.setIcon(new ImageIcon(getClass().getResource("up.png")));
        downButton.setIcon(new ImageIcon(getClass().getResource("down.png")));
        unionButton.setIcon(new ImageIcon(getClass().getResource("union.png")));
        iniInstrumentPanel();
        iniInstrumentPropertyPanel();
        //refreshLayerPanel();
        try {
            BufferedImage read = ImageIO.read(getClass().getResource("icon.png"));
            setIconImage(read);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    void iniInstrumentPropertyPanel(){
        jPanel3.removeAll();
        Instrument currentInstrument = controller.getCurrentInstrument();
        if(currentInstrument == null)
            return;
        propertyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Настройки инструмента (" + currentInstrument.getName() + ")" ));
        Properties properties = currentInstrument.getProperties();
        for (Property property : properties.getProperties()) {
            if(property.type == PropertyType.SLIDER)
                addSlider(property);
            if(property.type == PropertyType.COLOR)
                addColor(property);
        }
        jPanel3.doLayout();
        jPanel3.revalidate();
        propertyPanel.doLayout();
        propertyPanel.revalidate();
        propertyPanel.repaint();
    }
    
    void addSlider(final Property property){
        JLabel label = new JLabel(property.name);
        final JSlider slider = new JSlider(1, (int)property.otherSettings, (int)property.value);
        jPanel3.add(label); 
        jPanel3.add(slider);
        slider.addChangeListener(new ChangeListener() {
 
            @Override
            public void stateChanged(ChangeEvent e) {
                property.value = slider.getValue();
            }
        });
    }
    
    void addColor(final Property property){
        JLabel label = new JLabel(property.name);
        final JPanel colorPanel = new JPanel();
        JPanel panelchik = new JPanel();
        panelchik.setBackground(Color.WHITE);
        panelchik.setLayout(new BoxLayout(panelchik, BoxLayout.X_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        colorPanel.setBackground((Color) property.value);
        panel.setBackground(new Color(253,172,149));
        panel.setMaximumSize(new Dimension(350, 20));
        panel.add(label);
        panelchik.add(colorPanel);
        panel.add(panelchik);
        jPanel3.add(panel);
        colorPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                final JDialog dialog = new JDialog();
                dialog.setTitle("Выбор цвета");
                final JColorChooser colorChooser = new JColorChooser();
                AbstractColorChooserPanel[] chooserPanels = colorChooser.getChooserPanels();
                colorChooser.removeChooserPanel(chooserPanels[0]);
                colorChooser.removeChooserPanel(chooserPanels[1]);
                colorChooser.removeChooserPanel(chooserPanels[2]);
                colorChooser.removeChooserPanel(chooserPanels[4]);
                colorChooser.setColor((Color) property.value);
                JButton button = new JButton("Выбрать");
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
                panel.add(colorChooser);
                panel.add(button);
                dialog.add(panel);
                dialog.pack(); 
                dialog.setVisible(true);
                button.addActionListener(new ActionListener() {

                    @Override  
                    public void actionPerformed(ActionEvent e) {
                        Color color = colorChooser.getColor();
                        property.value = color;
                        colorPanel.setBackground(color);
                        dialog.setVisible(false);
                        jPanel3.repaint();
                    }
                });
                
            }
            
        });
    }
    
    void iniInstrumentPanel(){
        instrumentPanel.setLayout(new BoxLayout(instrumentPanel, BoxLayout.Y_AXIS));
        Collection<Instrument> instrumentCollection = InstrumentCollection.getInstrumentCollection();
        Iterator<Instrument> iterator = instrumentCollection.iterator();
        mark:
        for(;;){
            JPanel panel = new JPanel();
            
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            instrumentPanel.add(panel);
            for(int i = 0; i < 5; ++i){
                if(!iterator.hasNext())
                    break mark;
                Instrument instrument = null;
                while(iterator.hasNext() && !(instrument = iterator.next()).isIcon());
                if(!iterator.hasNext())
                    break;
                JButton jButton = new JButton(instrument.getIcon());
                jButton.setToolTipText(instrument.getName());
                panel.add(jButton);
                jButton.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JButton jButton = (JButton) e.getSource();
                        controller.setInstrument(jButton.getToolTipText());
                        iniInstrumentPropertyPanel();
                        paintPanel.repaint();
                        
                    }
                    
                });
             }
        }
        
    }
    
    void refreshLayerPanel(){
        layersPanel.removeAll();
        ArrayList<Layer> layers = controller.getLayers();
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JCheckBox checkBox = (JCheckBox)e.getSource();
                boolean selected = checkBox.isSelected();
                if(e.getX() < 20){
                    controller.setVisible(checkBox.getText().substring(2), selected);
                    paintPanel.repaint();
                }else{
              
                    checkBox.setSelected(!selected);
                    String layerName = checkBox.getText().substring(2);
                    if(!controller.getCurrentLayer().getName().endsWith(layerName)){
                        controller.setCurrentLayer(layerName);
                        refreshLayerPanel();
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()== 2){
                    jTextField1.setText(controller.getCurrentLayer().getName());
                    jDialog2.setVisible(true);
                }
            }
            
            
        };
        for(int i = layers.size() - 1; i >= 0; --i){
            JCheckBox checkBox = new JCheckBox((layers.get(i) == controller.getCurrentLayer() ? "* ": "  ") 
                    + layers.get(i).getName(), layers.get(i).isVisible());
            layersPanel.add(checkBox);
            checkBox.addMouseListener(adapter);
        }
        layersPanel.doLayout();
        layersPanel.revalidate();
        jPanel2.revalidate();
        jPanel2.repaint();
    }
    
    void newLayer(){
        controller.addLayer();
        refreshLayerPanel();
    }
    
    void deleteLayer(){
        controller.removeLayer();
        refreshLayerPanel();
        paintPanel.repaint();
    }
    
    void upLayer(){
        controller.upLayer();
        refreshLayerPanel();
        paintPanel.repaint();
    }
    
    void downLayer(){
        controller.downLayer();
        refreshLayerPanel();
        paintPanel.repaint();
    }
    
    void union(){
        controller.mergeVisibleLayer();
        refreshLayerPanel();
        paintPanel.repaint();
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
        jDialog1 = new javax.swing.JDialog();
        jFileChooser1 = new javax.swing.JFileChooser();
        jDialog2 = new javax.swing.JDialog();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        x = new javax.swing.JLabel();
        y = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        scale = new javax.swing.JSlider();
        persent = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        instrumentPanel = new javax.swing.JPanel();
        propertyPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        layersPanel = new javax.swing.JPanel();
        newLayerButton = new javax.swing.JButton();
        deleteLayerButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        unionButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        create = new javax.swing.JMenuItem();
        open = new javax.swing.JMenuItem();
        pastOfBuffer = new javax.swing.JMenuItem();
        save = new javax.swing.JMenuItem();
        saveAs = new javax.swing.JMenuItem();
        edit = new javax.swing.JMenu();
        undo = new javax.swing.JMenuItem();
        redo = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        layer = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        upMenuItem = new javax.swing.JMenuItem();
        downMenuItem = new javax.swing.JMenuItem();
        unionMenuItem = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
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

        jDialog1.setTitle("Выбор файла");
        jDialog1.setMinimumSize(new java.awt.Dimension(700, 400));

        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialog2.setMinimumSize(new java.awt.Dimension(275, 135));

        jTextField1.setText("jTextField1");

        jButton1.setText("Переименовать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1)
                .addContainerGap())
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jButton1)
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Рисовальщик - Painter");

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
        propertyPanel.setMinimumSize(new java.awt.Dimension(297, 100));
        propertyPanel.setRequestFocusEnabled(false);

        jPanel3.setBackground(new java.awt.Color(253, 172, 149));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 283, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel3);

        javax.swing.GroupLayout propertyPanelLayout = new javax.swing.GroupLayout(propertyPanel);
        propertyPanel.setLayout(propertyPanelLayout);
        propertyPanelLayout.setHorizontalGroup(
            propertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        propertyPanelLayout.setVerticalGroup(
            propertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jPanel2.setBackground(new java.awt.Color(228, 150, 188));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Слои"));

        layersPanel.setBackground(new java.awt.Color(30, 213, 254));

        javax.swing.GroupLayout layersPanelLayout = new javax.swing.GroupLayout(layersPanel);
        layersPanel.setLayout(layersPanelLayout);
        layersPanelLayout.setHorizontalGroup(
            layersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 283, Short.MAX_VALUE)
        );
        layersPanelLayout.setVerticalGroup(
            layersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(layersPanel);

        newLayerButton.setToolTipText("Новый слой");
        newLayerButton.setName(""); // NOI18N
        newLayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newLayerButtonActionPerformed(evt);
            }
        });

        deleteLayerButton.setToolTipText("Удалить слой");
        deleteLayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteLayerButtonActionPerformed(evt);
            }
        });

        upButton.setToolTipText("Вверх");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setToolTipText("Вниз");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        unionButton.setToolTipText("Объединить видимые слои");
        unionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(newLayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteLayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(unionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unionButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteLayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newLayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(propertyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(instrumentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(instrumentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(propertyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.setBackground(new java.awt.Color(255, 230, 168));
        mainPanel.setPreferredSize(new java.awt.Dimension(810, 610));
        mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mainPanelMouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mainPanelMouseDragged(evt);
            }
        });
        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mainPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mainPanelMouseReleased(evt);
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
            .addGap(0, 610, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(mainPanel);

        jMenuBar1.setBackground(new java.awt.Color(82, 65, 78));
        jMenuBar1.setBorder(new javax.swing.border.MatteBorder(null));

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
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        file.add(open);

        pastOfBuffer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pastOfBuffer.setText("Вставить из буфера");
        pastOfBuffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pastOfBufferActionPerformed(evt);
            }
        });
        file.add(pastOfBuffer);

        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        save.setText("Сохранить");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        file.add(save);

        saveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAs.setText("Сохранить как");
        saveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsActionPerformed(evt);
            }
        });
        file.add(saveAs);

        jMenuBar1.add(file);

        edit.setText("Правка");

        undo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undo.setText("Отменить");
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });
        edit.add(undo);

        redo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redo.setText("Вернуть");
        redo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoActionPerformed(evt);
            }
        });
        edit.add(redo);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Вырезать");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        edit.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Копировать");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        edit.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Вставить");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        edit.add(jMenuItem5);

        jMenuBar1.add(edit);

        layer.setText("Слой");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("Создать");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        layer.add(newMenuItem);

        deleteMenuItem.setText("Удалить");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        layer.add(deleteMenuItem);

        upMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.CTRL_MASK));
        upMenuItem.setText("Вверх");
        upMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upMenuItemActionPerformed(evt);
            }
        });
        layer.add(upMenuItem);

        downMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, java.awt.event.InputEvent.CTRL_MASK));
        downMenuItem.setText("Вниз");
        downMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downMenuItemActionPerformed(evt);
            }
        });
        layer.add(downMenuItem);

        unionMenuItem.setText("Объединить видимые");
        unionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unionMenuItemActionPerformed(evt);
            }
        });
        layer.add(unionMenuItem);

        jMenuItem13.setText("На новый слой");
        layer.add(jMenuItem13);

        jMenuItem10.setText("Переименовать");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        layer.add(jMenuItem10);

        jMenuItem11.setText("Изменить размер слоёв");
        layer.add(jMenuItem11);

        jMenuItem12.setText("Масштабировать слои");
        layer.add(jMenuItem12);

        jMenuBar1.add(layer);

        help.setText("Справка");
        jMenuBar1.add(help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(y, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scale, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(persent))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
        refreshLayerPanel();
        iniInstrumentPropertyPanel();
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

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        if(evt.getActionCommand().equals("CancelSelection")){
            jDialog1.setVisible(false);
        }else{
            File file = jFileChooser1.getSelectedFile();
            if(file == null || file.isDirectory()){
                return;
            }
            if(saveSelection){
                controller.saveAs(file);
            }else{
                controller.open(file);
                refreshLayerPanel();
                iniInstrumentPropertyPanel();
            }
            repaint();
            jDialog1.setVisible(false);
        }
    }//GEN-LAST:event_jFileChooser1ActionPerformed

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        jDialog1.setVisible(true);
        saveSelection = false;
        jFileChooser1.setApproveButtonText("Открыть");
    }//GEN-LAST:event_openActionPerformed

    private void saveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsActionPerformed
       jDialog1.setVisible(true);
        saveSelection = true;
        jFileChooser1.setApproveButtonText("Сохранить");
    }//GEN-LAST:event_saveAsActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if(!controller.save()){
            saveAsActionPerformed(null);
        }
    }//GEN-LAST:event_saveActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        controller.cut();
        repaint();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void undoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoActionPerformed
        controller.undo();
        refreshLayerPanel();
        repaint();
    }//GEN-LAST:event_undoActionPerformed

    private void redoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoActionPerformed
        controller.redo();
        repaint();
    }//GEN-LAST:event_redoActionPerformed

    private void pastOfBufferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pastOfBufferActionPerformed
        controller.pastOfBuffer();
        refreshLayerPanel();
        iniInstrumentPropertyPanel();
        repaint();
    }//GEN-LAST:event_pastOfBufferActionPerformed

    private void newLayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newLayerButtonActionPerformed
        newLayer();
    }//GEN-LAST:event_newLayerButtonActionPerformed

    private void deleteLayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteLayerButtonActionPerformed
        deleteLayer();
    }//GEN-LAST:event_deleteLayerButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        upLayer();
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        downLayer();
    }//GEN-LAST:event_downButtonActionPerformed

    private void unionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unionButtonActionPerformed
        union();
    }//GEN-LAST:event_unionButtonActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        newLayer();
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        deleteLayer();
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void upMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upMenuItemActionPerformed
        upLayer();
    }//GEN-LAST:event_upMenuItemActionPerformed

    private void downMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downMenuItemActionPerformed
        downLayer();
    }//GEN-LAST:event_downMenuItemActionPerformed

    private void unionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unionMenuItemActionPerformed
        union();
    }//GEN-LAST:event_unionMenuItemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(controller.renameLayer(jTextField1.getText())){
            jDialog2.setVisible(false);
            refreshLayerPanel();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        jTextField1.setText(controller.getCurrentLayer().getName());
        jDialog2.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void mainPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMousePressed
        controller.actionPressed(evt.getX(), evt.getY());
        paintPanel.repaint();
    }//GEN-LAST:event_mainPanelMousePressed

    private void mainPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseReleased
        controller.actionReleased(evt.getX(), evt.getY());
        paintPanel.repaint();
    }//GEN-LAST:event_mainPanelMouseReleased

    private void mainPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseDragged
        controller.actionDragget(evt.getX(), evt.getY());
        paintPanel.repaint();
    }//GEN-LAST:event_mainPanelMouseDragged

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        controller.copy();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        controller.past();
        paintPanel.repaint();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

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
    private javax.swing.JButton deleteLayerButton;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JButton downButton;
    private javax.swing.JMenuItem downMenuItem;
    private javax.swing.JMenu edit;
    private javax.swing.JMenu file;
    private javax.swing.JTextField height;
    private javax.swing.JMenu help;
    private javax.swing.JPanel instrumentPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JMenu layer;
    private javax.swing.JPanel layersPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton newLayerButton;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem open;
    private javax.swing.JMenuItem pastOfBuffer;
    private javax.swing.JLabel persent;
    private javax.swing.JPanel propertyPanel;
    private javax.swing.JMenuItem redo;
    private javax.swing.JMenuItem save;
    private javax.swing.JMenuItem saveAs;
    private javax.swing.JSlider scale;
    private javax.swing.JMenuItem undo;
    private javax.swing.JButton unionButton;
    private javax.swing.JMenuItem unionMenuItem;
    private javax.swing.JButton upButton;
    private javax.swing.JMenuItem upMenuItem;
    private javax.swing.JTextField width;
    private javax.swing.JLabel x;
    private javax.swing.JLabel y;
    // End of variables declaration//GEN-END:variables
}
