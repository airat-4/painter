/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painter.instruments;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import painter.Action;
import painter.Instrument;
import painter.LayerManager;
import painter.Property;

/**
 *
 * @author airat
 */
public class Pencil extends Instrument{
    Point prev = new Point();
    public Pencil(String name, boolean icon, Property... property) {
        super(name, icon, property);
    }

    @Override
    public Image getDevImage() {
        return  realImage;
    }

    @Override
    public Instrument action(LayerManager layerManager, Action action, int x, int y) {
        if(action == Action.PRESSED){
            if(realImage != null){
                return this;
            }
            prev.x = x;
            prev.y = y;
        }
        if(action == Action.DRAGGET || action == Action.RELEASED){
            if(realImage == null){
                realImage = new BufferedImage(layerManager.getWidth(),  layerManager.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            }
            Graphics graphics = realImage.getGraphics();
            ((Graphics2D)graphics).setStroke(new BasicStroke((int) properties.getProperties()[0].value,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.setColor((Color) properties.getProperties()[1].value);
            graphics.drawLine(prev.x, prev.y, x, y);
            prev.x = x;
            prev.y = y;
            if(((Color) properties.getProperties()[1].value).getAlpha() < 255){
                for (int i = 0; i < realImage.getWidth(); i++) {
                    for (int j = 0; j < realImage.getHeight(); j++) {
                        if(realImage.getRGB(i, j) != 0){
                            realImage.setRGB(i, j, ((Color) properties.getProperties()[1].value).getRGB());
                        }
                    }
                    
                }
            }
        }
        return null;
    }
    
}
