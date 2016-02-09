/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painter.instruments;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import painter.Action;
import painter.Cache;
import painter.Instrument;
import painter.InstrumentCollection;
import painter.Layer;
import painter.LayerManager;
import static painter.LayerManager.clipboard;
import painter.Property;

/**
 *
 * @author airat
 */
public class Past extends Instrument {

    private Rectangle area = new Rectangle();
    private int ofsetX;
    private int ofsetY;
    private int oldX;
    private int oldY;
    public Past(String name, boolean icon, Property... property) {
        super(name, icon, property);
    }

    @Override
    public Image flush(LayerManager layerManager) {
        Image image = new BufferedImage(area.width + area.x + ofsetX, area.height + area.y + ofsetY, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics graphics = image.getGraphics();
        graphics.drawImage(realImage, ofsetX, ofsetY, null);
        realImage = null;
        layerManager.setArea(null);
        return image;

    }

    @Override
    public Image getDevImage() {
        int width = area.width + area.x;
        int height = area.height + area.y;
        if (width > 0 && height > 0) {
            Image image = new BufferedImage(width + 1 + ofsetX, height + 1 + ofsetY, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = image.getGraphics();
            g.drawImage(realImage, ofsetX, ofsetY, null);
            g.setColor(Color.BLACK);
            g.drawRect(area.x + ofsetX, area.y + ofsetY, area.width, area.height);
            return image;
        }
        return null;
    }

    @Override
    public Instrument action(LayerManager layerManager, Action action, int x, int y) {
        if (action == Action.NONE) {
            try {
                Image image = null;
                Transferable t = clipboard.getContents(null);
                if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    image = (Image) t.getTransferData(DataFlavor.imageFlavor);
                }
                realImage = (BufferedImage) image;
                area.width = realImage.getWidth();
                area.height = realImage.getHeight();
                ofsetX = x;
                ofsetY = y;
            } catch (Exception ex) {
            }
        }
        if (action == Action.PRESSED) {
            oldX = x;
            oldY = y;
            if (!(x >= ofsetX && x <= ofsetX + area.width && y >= ofsetY && y <= ofsetY + area.height)) {
                return InstrumentCollection.getInstrument("Выделить");
            }
        }
        if(action == Action.DRAGGET){
            ofsetX += x - oldX;
            ofsetY += y - oldY;
            oldX = x;
            oldY = y;
        }

        return null;
    }

}
