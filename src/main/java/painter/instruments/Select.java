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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import painter.Action;
import painter.Instrument;
import painter.InstrumentCollection;
import painter.LayerManager;
import painter.Property;

/**
 *
 * @author airat
 */
public class Select  extends Instrument{
    Rectangle area = new Rectangle();
    public Select (String name, boolean icon, Property... property) {
        super(name, icon, property);
    }

    @Override
    public Image getDevImage() {
        int width = area.width + area.x;
        int height = area.height + area.y;
        if(width > 0 && height > 0){
            Image image = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            g.drawRect(area.x, area.y, area.width, area.height);
            return image;
        }
        return  null;
    }

    @Override
    public Instrument action(LayerManager layerManager, Action action, int x, int y) {
        if(action == Action.PRESSED){
            if (layerManager.getArea() != null && (x >= layerManager.getArea().x && x <= layerManager.getArea().width + layerManager.getArea().x
                    && y >= layerManager.getArea().y && y <= layerManager.getArea().height +layerManager.getArea().y)) {
                InstrumentCollection.getInstrument("Вырезать").action(layerManager, Action.NONE, 0, 0);
                InstrumentCollection.getInstrument("Вставить").action(layerManager, Action.NONE, layerManager.getArea().x, layerManager.getArea().y);
                return InstrumentCollection.getInstrument("Вставить");
            }
            area.x = x;
            area.y = y;
            area.width = 0;
            area.height = 0;
            layerManager.setArea(area);
        }
        if(action == Action.DRAGGET || action == Action.RELEASED){
            area.width = x - area.x;
            area.height = y - area.y;
            if(area.width < 0){
                area.x = area.x + area.width;
                area.width = -area.width;
            }
            if(area.height < 0){
                area.y = area.y + area.height;
                area.height = - area.height;
            }
            area.x = (area.x < 0)? 0 : area.x;
            area.y = (area.y < 0)? 0 : area.y;
            area.width = (area.x + area.width > layerManager.getWidth())?layerManager.getWidth() -area.x : area.width;
            area.height = (area.y + area.height > layerManager.getHeight())?layerManager.getHeight() -area.y : area.height;
            layerManager.setArea(area);
        }
        return null;
    }
    
}
