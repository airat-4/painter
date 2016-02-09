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
import java.awt.image.BufferedImage;
import painter.Action;
import painter.Instrument;
import painter.LayerManager;
import painter.Property;

/**
 *
 * @author airat
 */
public class Line extends Instrument {
    Point point =new Point();
    Point point1 =new Point();
    public Line(String name, boolean icon, Property... property) {
        super(name, icon, property);
    }

    @Override
    public Image getDevImage() {
        
        return realImage;
    }

    @Override
    public Instrument action(LayerManager layerManager, Action action, int x, int y) {
         if(action == Action.PRESSED){
            if(realImage != null){
                return this;
            }
            point.x = x;
            point.y = y;
        }
        if(action == Action.DRAGGET || action == Action.RELEASED){
            realImage = new BufferedImage(layerManager.getWidth(),  layerManager.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            
            Graphics graphics = realImage.getGraphics();
            ((Graphics2D)graphics).setStroke(new BasicStroke((int) properties.getProperties()[0].value));
            graphics.setColor((Color) properties.getProperties()[1].value);
            point1.x = x;
            point1.y = y;
            graphics.drawLine(point.x, point.y, x, y);
            
        }
        return null;

    }
    
}
